import java.util.*;
import java.io.*;

public class FileLogger {

  public synchronized void log(String msg) {
    DataOutputStream dos = null;
    try {
      dos = new DataOutputStream(
              new FileOutputStream("log.txt",true));
      dos.writeBytes(msg);
      dos.close();
    } catch (FileNotFoundException ex) {
      //
    }
    catch (IOException ex) {
      //
    }
  }
}
