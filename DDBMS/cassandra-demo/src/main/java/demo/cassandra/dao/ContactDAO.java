package demo.cassandra.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Iterator;

public class ContactDAO {

    public ContactDAO() {}

    public static void save(Session session, Contact contact) {
        String query = "insert into contact " + contact.getContactFields() + " values (" + "'" + contact.getContactId() +
                "'" + "," + "'" + contact.getContactNo() + "'" + "," + "'" + contact.getContactName() + "'" + ")";
        Statement statement = session.newSimpleStatement(query);
        System.out.println("InsertQuery: " + query);
        ResultSet resultSet= session.execute(statement);

        if(resultSet.wasApplied()) {
            JOptionPane.showMessageDialog(null, " \nSaving Successfully  \n");
        }else {
            JOptionPane.showMessageDialog(null, " \n ExecutionException \n " +resultSet.getAllExecutionInfo() + "\n");
        }
        session.close();
    }

    public static void delete(Session session, Contact contact) {
        String contactNo = contact.getContactNo();
        String query="delete from contact where contactNo = " + "'" + contactNo + "'";
        System.out.println("DeleteQuery: " + query);
        Statement stmt=session.newSimpleStatement(query);
        ResultSet resultSet = session.execute(stmt);

        if(resultSet.wasApplied()) {
            JOptionPane.showMessageDialog(null, " \nDeleted Successfully  \n");
        }else {
            JOptionPane.showMessageDialog(null, " \n ExecutionException \n " +resultSet.getAllExecutionInfo() + "\n");
        }
        session.close();
    }

    public static void update(Session session, Contact contact) {
        String contactNo = contact.getContactNo();
        String query="update contact SET contactname =" + "'" + contact.getContactName() + "'" + "," +
                " contactid = " + "'" + contact.getContactId() + "'" +
                " where contactno =" + "'" + contactNo + "'";

        System.out.println("UpdateQuery: " + query);
        Statement stmt=session.newSimpleStatement (query);
        ResultSet resultSet = session.execute(stmt);

        if(resultSet.wasApplied()) {
            JOptionPane.showMessageDialog(null, " \nUpdated Successfully  \n");
        }else {
            JOptionPane.showMessageDialog(null, " \n ExecutionException \n " +resultSet.getAllExecutionInfo() + "\n");
        }
        session.close();
    }

    public static void list(Session session, JTable jTable) {
        String query="select * from contact";
        System.out.println("ListQuery: " + query);
        ResultSet resultSet = session.execute(query);

        Iterator<Row> itr = resultSet.iterator();
        DefaultTableModel tableModel = (DefaultTableModel) jTable.getModel();

        while(itr.hasNext()) {
            String[] data = new String[3];
            Row rs = itr.next();
            data[0] = rs.getString(1);
            data[1] = rs.getString(0);
            data[2] = rs.getString(2);
            System.out.println("Data: " + rs.toString());
            tableModel.addRow(data);
        }

        jTable.setModel(tableModel);

        if(resultSet.wasApplied()) {
            JOptionPane.showMessageDialog(null, " \nFetched Successfully  \n");
        }else {
            JOptionPane.showMessageDialog(null, " \n ExecutionException \n " +resultSet.getAllExecutionInfo() + "\n");
        }
        session.close();
    }

}