package edu.ncit.reducesidejoin.basic;

import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdhamee on 7/10/17.
 */
public class ReduceSideJoin {

    // Donations Mapper
    public static class DonationsMapper extends Mapper<Object, Text, Text, ObjectWritable> {
        private Text outputKey = new Text();
        private ObjectWritable outputValue = new ObjectWritable();

        @Override
        public void map(Object rowOffset, Text donationRow, Context context) throws IOException, InterruptedException {
            // Parse csv line to create DonationRowWritable object
            DonationRowWritable donationRowWritable = new DonationRowWritable();
            donationRowWritable.parseLine(donationRow.toString());

            outputKey.set(donationRowWritable.project_id);
            outputValue.set(donationRowWritable);

            context.write(outputKey, outputValue);
        }
    }

    // Projects Mapper
    public static class ProjectsMapper extends Mapper<Object, Text, Text, ObjectWritable> {
        private Text outputKey = new Text();
        private ObjectWritable outputValue = new ObjectWritable();

        @Override
        public void map(Object rowOffset, Text projectRow, Context context) throws IOException, InterruptedException {
            // Parse csv line to create ProjectRowWritable object
            ProjectRowWritable projectRowWritable = new ProjectRowWritable();
            projectRowWritable.parseLine(projectRow.toString());

            outputKey.set(projectRowWritable.project_id);
            outputValue.set(projectRowWritable);

            context.write(outputKey, outputValue);
        }
    }

    /**
     * Join Reducer:
     * Each invocation of the reduce() method will receive a list of ObjectWritable.
     * These ObjectWritable objects are either Donation or Project objects, and are all linked by the same "project_id".
     */
    public static class JoinReducer extends Reducer<Text, ObjectWritable, Text, Text> {
        private static final String NULL_DONATION_OUTPUT = "null|null|null|null|null";
        private static final String NULL_PROJECT_OUTPUT = "null|null|null|null";

        private Text donationOutput = new Text();
        private Text projectOutput = new Text();

        private List<String> donationsList = new ArrayList<>();
        private List<String> projectsList = new ArrayList<>();

        @Override
        protected void reduce(Text projectId, Iterable<ObjectWritable> values, Context context) throws IOException, InterruptedException {
            // Clear data lists
            donationsList.clear();
            projectsList.clear();

            // Fill up data lists with selected fields
            for (ObjectWritable value : values) {
                Object object = value.get();

                if (object instanceof DonationRowWritable) {
                    DonationRowWritable donation = (DonationRowWritable) object;
                    String donationOutput = String.format("%s|%s|%s|%s|%.2f", donation.donation_id, donation.project_id,
                            donation.donor_city, donation.ddate, donation.total);
                    donationsList.add(donationOutput);

                } else if (object instanceof ProjectRowWritable) {
                    ProjectRowWritable project = (ProjectRowWritable) object;
                    String projectOutput = String.format("%s|%s|%s|%s", project.project_id, project.school_city,
                            project.poverty_level, project.primary_focus_subject);
                    projectsList.add(projectOutput);

                } else {
                    String errorMsg = String.format("Object of class %s is neither a %s nor %s.",
                            object.getClass().getName(), ProjectRowWritable.class.getName(), DonationRowWritable.class.getName());
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
