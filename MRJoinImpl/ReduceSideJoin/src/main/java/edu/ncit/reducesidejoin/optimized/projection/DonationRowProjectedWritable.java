package edu.ncit.reducesidejoin.optimized.projection;

import edu.ncit.reducesidejoin.util.StringHelper;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DonationRowProjectedWritable implements WritableComparable<DonationRowProjectedWritable> {
    public String donation_id;
    public String project_id;
    public String ddate;
    public String donor_city;
    public float total;

    @Override
    public void readFields(DataInput in) throws IOException {
        donation_id = in.readUTF();
        project_id = in.readUTF();
        ddate = in.readUTF();
        donor_city = in.readUTF();
        total = in.readFloat();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(donation_id);
        out.writeUTF(project_id);
        out.writeUTF(donor_city);
        out.writeUTF(ddate);
        out.writeFloat(total);
    }

    public void parseLine(String line) throws IOException {
        // Remove all double-quotes from line
        line = StringHelper.removeDoubleQuotes(line);

        // Split on commas, unless they were escaped with a backslash
        // e.g. the "message" field can contain commas.
        // Negative limit param "-1" to keep empty values in resulting array
        String[] parts = line.split("(?<!\\\\),", -1);

        donation_id = parts[0];
        project_id = parts[1];
        donor_city = parts[4];
        ddate = parts[8];
        total = StringHelper.parseFloat(parts[11]);
    }

    @Override
    public int compareTo(DonationRowProjectedWritable o) {
        return this.donation_id.compareTo(o.donation_id);
    }

    @Override
    public String toString() {
        return String.format("%s|project=%s|city=%s|total=%.2f",
                donation_id, project_id, donor_city, total);
    }

}
