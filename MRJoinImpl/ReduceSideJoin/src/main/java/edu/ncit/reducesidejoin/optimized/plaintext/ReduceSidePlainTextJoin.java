package edu.ncit.reducesidejoin.optimized.plaintext;

import edu.ncit.reducesidejoin.optimized.projection.DonationRowProjectedWritable;
import edu.ncit.reducesidejoin.optimized.projection.ProjectRowProjectedWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdhamee on 7/10/17.
 */
public class ReduceSidePlainTextJoin {

    // Donations Mapper
    public static class DonationsPlainTextMapper extends Mapper<Object, Text, Text, Text> {
        private Text outputKey = new Text();
        private Text outputValue = new Text();

        @Override
        public void map(Object rowOffset, Text donationRow, Context context) throws IOException, InterruptedException {
            // Parse csv line to create DonationRowProjectedWritable object
            DonationRowProjectedWritable donation = new DonationRowProjectedWritable();
            donation.parseLine(donationRow.toString());

            String donationOutput = String.format("D|%s|%s|%s|%s|%.2f", donation.donation_id,
                    donation.project_id, donation.ddate, donation.donor_city, donation.total);

            outputKey.set(donation.project_id);
            outputValue.set(donationOutput);

            context.write(outputKey, outputValue);
        }
    }

    // Projects Mapper
    public static class ProjectsPlainTextMapper extends Mapper<Object, Text, Text, Text> {
        private Text outputKey = new Text();
        private Text outputValue = new Text();

        @Override
        public void map(Object rowOffset, Text projectRow, Context context) throws IOException, InterruptedException {
            // Parse csv line to create ProjectRowProjectedWritable object
            ProjectRowProjectedWritable project = new ProjectRowProjectedWritable();
            project.parseLine(projectRow.toString());

            // Create new object with projected values
            String projectOutput = String.format("P|%s|%s|%s|%s",
                    project.project_id, project.school_city, project.poverty_level, project.primary_focus_subject);

            outputKey.set(project.project_id);
            outputValue.set(projectOutput);

            context.write(outputKey, outputValue);
        }
    }

    /**
     * Join Reducer:
     * Each invocation of the reduce() method will receive a list of ObjectWritable.
     * These ObjectWritable objects are either Donation or Project objects, and are all linked by the same "project_id".
     */
    public static class PlainTextJoinReducer extends Reducer<Text, Text, Text, Text> {
        private static final String NULL_DONATION_OUTPUT = "null|null|null|null|null";
        private static final String NULL_PROJECT_OUTPUT = "null|null|null|null";

        private Text donationOutput = new Text();
        private Text projectOutput = new Text();

        private List<String> donationsList = new ArrayList<>();
        private List<String> projectsList = new ArrayList<>();

        @Override
        protected void reduce(Text projectId, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            // Clear data lists
            donationsList.clear();
            projectsList.clear();

            // Fill up data lists with selected fields
            for (Text value : values) {
                String textVal = value.toString();

                // Get first char which determines the type of data
                char type = textVal.charAt(0);

                // Remove the type flag "P|" or "D|" from the beginning to get original data content
                textVal = textVal.substring(2);

                if (type == 'D') {
                    donationsList.add(textVal);

                } else if (type == 'P') {
                    projectsList.add(textVal);

                } else {
                    String errorMsg = String.format("Type is neither a D nor P.");
                    throw new IOException(errorMsg);
                }
            }

            // Join data lists (example with FULL OUTER JOIN)
            if (!donationsList.isEmpty()) {
                for (String dontationStr : donationsList) {
                    if (!projectsList.isEmpty()) {
                        // Case 1 : Both LEFT and RIGHT sides of the join have values
                        // Extra loop to write all combinations of (LEFT, RIGHT)
                        // These are also the outputs of an INNER JOIN
                        for (String projectStr : projectsList) {
                            donationOutput.set(dontationStr);
                            projectOutput.set(projectStr);
                            context.write(donationOutput, projectOutput);
                        }
                    } else {
                        // Case 2 : LEFT side has values but RIGHT side doesn't.
                        // Simply write (LEFT, null) to output for each value of LEFT.
                        // These are also the outputs of a LEFT OUTER JOIN
                        donationOutput.set(dontationStr);
                        projectOutput.set(NULL_PROJECT_OUTPUT);
                        context.write(donationOutput, projectOutput);
                    }
                }
            } else {
                // Case 3 : LEFT side doesn't have values, but RIGHT side has values
                // Simply write (null, RIGHT) to output for each value of LEFT.
                // These are also the outputs of a RIGHT OUTER JOIN
                for (String projectStr : projectsList) {
                    donationOutput.set(NULL_DONATION_OUTPUT);
                    projectOutput.set(projectStr);
                    context.write(donationOutput, projectOutput);
                }
            }
        }
    }
}
