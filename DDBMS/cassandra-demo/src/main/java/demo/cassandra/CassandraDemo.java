package demo.cassandra;

import demo.cassandra.util.Center;
import demo.cassandra.form.ContactForm;

import javax.swing.*;

/**
 * Created by hdhamee on 10/16/15.
 */
public class CassandraDemo {
    private static Center center;

    //Main  Function
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ContactForm frmForm = new ContactForm();
                center = new Center(frmForm,600,400);
                frmForm.setVisible(true);
                frmForm.setResizable(false);
                frmForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
