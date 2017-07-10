package edu.ncit.reducesidejoin.optimized.projection;

import edu.ncit.reducesidejoin.util.StringHelper;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ProjectRowProjectedWritable implements WritableComparable<ProjectRowProjectedWritable> {
    public String project_id;
    public String school_city;
    public String primary_focus_subject;
    public String poverty_level;

    @Override
    public void readFields(DataInput in) throws IOException {
        project_id = in.readUTF();
        school_city = in.readUTF();
        primary_focus_subject = in.readUTF();
        poverty_level = in.readUTF();

    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(project_id);
        out.writeUTF(school_city);
        out.writeUTF(primary_focus_subject);
        out.writeUTF(poverty_level);

    }

    public void parseLine(String line) throws IOException {
        // Remove all double-quotes from line
        line = StringHelper.removeDoubleQuotes(line);

        // Split on commas, unless they were escaped with a backslash
        // Negative limit param "-1" to keep empty values in resulting array
        String[] parts = line.split("(?<!\\\\),", -1);

        project_id = parts[0];
        school_city = parts[6];
        primary_focus_subject = parts[21];
        poverty_level = parts[26];

    }

    @Override
    public int compareTo(ProjectRowProjectedWritable o) {
        return this.project_id.compareTo(o.project_id);
    }
}