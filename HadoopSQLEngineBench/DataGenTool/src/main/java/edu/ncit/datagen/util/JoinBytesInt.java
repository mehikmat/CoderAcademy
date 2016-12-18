package edu.ncit.datagen.util;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class JoinBytesInt implements Writable {

//	private static final Log log = LogFactory.getLog(JoinBytesInt.class.getName());

    public byte ulen;
    public int refs;
    public byte[] url;

    public JoinBytesInt() {
        clear();
    }

    @Override
    public String toString() {
        return String.valueOf(ulen) + ":" + String.valueOf(refs) + ":" + String.valueOf(url);
    }

    public void clear() {
        ulen = 0;
        url = null;
        refs = 0;
    }

    public void add(JoinBytesInt j) {
        refs = refs + j.refs;
        if (0 == ulen) {
            ulen = j.ulen;
            url = j.url;
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {

        ulen = in.readByte();
        if (0 == ulen) {
            refs = WritableUtils.readVInt(in);
//			refs = in.readInt();
            url = null;
        } else {
            if (ulen < 0) {
                refs = 0;
                ulen = (byte) (-ulen);
            } else {
                refs = WritableUtils.readVInt(in);
//				refs = in.readInt();
            }
            url = new byte[ulen];
            in.readFully(url, 0, ulen);
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        if (0 == ulen) {
            out.writeByte(ulen);
            WritableUtils.writeVInt(out, refs);
//			out.writeInt(refs);
        } else {
            if (0 == refs) {
                out.writeByte(-ulen);
            } else {
                out.writeByte(ulen);
                WritableUtils.writeVInt(out, refs);
//				out.writeInt(refs);
            }
            out.write(url, 0, ulen);
        }
    }
}
