package test.compression;

import org.xerial.snappy.Snappy;
import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

import java.io.*;

/**
 * Snappy is a fast C++ compressor/de-compressor developed by Google.
 *
 * The snappy-java(https://github.com/xerial/snappy-java) is a Java port
 * of the snappy library(https://github.com/google/snappy)
 *
 * Snappy's main target is very high-speed compression/decompression with reasonable compression size.
 *
 *
 * Created by hdhamee on 6/2/16.
 */
public class SnappyCompression {

    public static void main(String[] args) throws IOException {
        SnappyCompression snappyCompression =  new SnappyCompression();
        snappyCompression.simpleAPI();
        snappyCompression.streamBasedAPI();
    }

    private void simpleAPI() throws IOException {
        String input = "Hello snappy-java! Hello snappy-java! Hello snappy-java! Hello snappy-java!";

        byte[] compressed = Snappy.compress(input.getBytes());
        System.out.println("Compressed: " + new String(compressed));


        byte[] uncompressed = Snappy.uncompress(compressed);
        System.out.println("Uncompressed: " + new String(uncompressed));
        System.out.println("SimpleCompression Finished \n\n\n\n\n");
    }

    private void streamBasedAPI() throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new SnappyOutputStream( new FileOutputStream(new File("input.snappy"))));
        bufferedOutputStream.write("Hello world hello world\n".getBytes());
        bufferedOutputStream.write("Hello world hello world\n".getBytes());
        bufferedOutputStream.write("Hello world hello world\n".getBytes());
        bufferedOutputStream.write("Hello world hello world\n".getBytes());
        bufferedOutputStream.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new SnappyInputStream(new FileInputStream(new File("input.snappy")))));
        String line;
        while ( (line = bufferedReader.readLine()) != null){
            System.out.println("Uncompressed: " + line);
        }
        bufferedReader.close();
    }
}
