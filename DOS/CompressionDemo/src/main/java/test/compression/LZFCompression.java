package test.compression;

import com.ning.compress.lzf.LZFInputStream;
import com.ning.compress.lzf.LZFOutputStream;

import java.io.*;

/**
 * Created by hdhamee on 6/2/16.
 */
public class LZFCompression {
    public static void main(String[] args) throws IOException {
       //compression
        OutputStream out = new LZFOutputStream(new FileOutputStream("compressed.lzf"));
        out.write("Hello world \n".getBytes());
        out.write("Hello world \n".getBytes());
        out.write("Hello world \n".getBytes());
        out.close();

        //decompression
        InputStream in = new LZFInputStream(new FileInputStream("compressed.lzf"));
        byte[] data = new byte[35];
        in.read(data);
        in.close();
        System.out.println("Uncompressed: " + new String(data));
    }
}
