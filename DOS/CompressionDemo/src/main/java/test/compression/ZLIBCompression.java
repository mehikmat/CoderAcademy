package test.compression;

import java.io.*;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Example program to demonstrate how to use zlib compression with Java.
 * For compression:   DeflaterOutputStream
 * For decompression: InflaterInputStream
 *
 */
public class ZLIBCompression {

    public static void main(String[] args) throws IOException, DataFormatException {
        File input = new File("input.txt");
        File compressed = new File("input_compressed.deflate");

        compressFile(input,compressed);
        decompressFile(compressed, new File("decompressed.txt"));
    }

    public static void compressFile(File raw, File compressed) throws IOException {
        InputStream in = new FileInputStream(raw);
        OutputStream out = new DeflaterOutputStream(new FileOutputStream(compressed));
        shovelInToOut(in, out);
        in.close();
        out.close();
    }

    public static void decompressFile(File compressed, File raw) throws IOException{
        InputStream in = new InflaterInputStream(new FileInputStream(compressed));
        OutputStream out = new FileOutputStream(raw);
        shovelInToOut(in, out);
        in.close();
        out.close();
    }

    private static void shovelInToOut(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1000];
        int len;
        while((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
}
