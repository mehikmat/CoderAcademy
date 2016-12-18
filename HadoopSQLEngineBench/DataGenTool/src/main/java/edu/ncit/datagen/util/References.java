package edu.ncit.datagen.util;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class References implements Writable {

    int len;
    long[] refs;

    References() {
    }

    References(int len, long[] refs) {
        this.len = len;
        this.refs = refs;
    }

    @Override
    public void readFields(DataInput in) throws IOException {

        len = in.readInt();
        int abslen = Math.abs(len);
        refs = new long[abslen];
        for (int i = 0; i < abslen; i++) {
            refs[i] = in.readLong();
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {

        out.writeInt(len);
        int abslen = Math.abs(len);
        for (int i = 0; i < abslen; i++) {
            out.writeLong(refs[i]);
        }
    }

}
