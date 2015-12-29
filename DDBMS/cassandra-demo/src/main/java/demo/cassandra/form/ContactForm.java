package demo.cassandra.form;

import demo.cassandra.dao.Contact;
import demo.cassandra.dao.ContactDAO;
import demo.cassandra.net.Connection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContactForm extends JFrame implements ActionListener,KeyListener {
    private static JLabel       lblEmpHead,lblLine, lblContactNo, lblContactName, lblContactId,lblDate;
    private static JTextField txtContactNo, txtContactName, textContactId,txtDate;
    private static JButton     cmdSave, cmdList,cmdUpdate,cmdClear,cmdDelete;
    private static JPanel     mainPanel,lblPanel,btnPanel;
    private static Contact contact;
    private static Connection connection;
    private static String  currentDate;
    private static SimpleDateFormat formatter;
    private static Date  dt;
    private static  JTable jTable;
    private static JScrollPane tableContainer;
    private static  DefaultTableModel contactTableModel;

    public ContactForm() {
        connection = new Connection();

        currentDate=new String();
        formatter = new SimpleDateFormat ("MMM - dd - yyyy",Locale.getDefault());
        dt=new Date();
        currentDate = formatter.format(dt);

        mainPanel=new JPanel();
        mainPanel.setLayout(null);
        //Label Panel

        lblPanel=new JPanel();
        lblPanel.setLayout(null);

        lblEmpHead = new JLabel(" Contact Book ");
        lblEmpHead.setFont(new Font("TimesRoman", Font.BOLD, 22));
        lblEmpHead.setBounds(210, 10, 300, 20);
        lblLine =new JLabel(" =================");
        lblLine.setBounds(210, 23, 300, 20);
        lblLine.setForeground(Color.red);

        lblContactId =new JLabel("Contact Id :");
        lblContactId.setFont(new Font("TimesRoman", Font.BOLD, 12));
        lblContactId.setBounds(25, 60, 200, 20);
        textContactId =new JTextField("contact id ... ");
        textContactId.setFont(new Font("TimesRoman", Font.BOLD, 12));
        textContactId.setBounds(150, 60, 150, 20);
        textContactId.addKeyListener(this);

        lblContactNo =new JLabel("Contact No :");
        lblContactNo.setFont(new Font("TimesRoman", Font.BOLD, 12));
        lblContactNo.setBounds(25, 120, 200, 20);
        txtContactNo =new JTextField("contact no... ");
        txtContactNo.setFont(new Font("TimesRoman", Font.BOLD, 12));
        txtContactNo.setBounds(150, 120, 150, 20);
        txtContactNo.addKeyListener(this);

        lblContactName =new JLabel("Contact Name :");
        lblContactName.setFont(new Font("TimesRoman", Font.BOLD, 12));
        lblContactName.setBounds(25, 180, 200, 20);
        txtContactName =new JTextField("contact name ... ");
        txtContactName.setFont(new Font("TimesRoman", Font.BOLD, 12));
        txtContactName.setBounds(150, 180, 150, 20);
        txtContactName.addKeyListener(this);


        jTable = getJTable();
        jTable.setFont(new Font("TimesRoman", Font.BOLD, 12));
        jTable.setShowGrid(true);
        tableContainer = new JScrollPane(jTable);
        tableContainer.setBounds(325,50,225,250);



        lblDate  =new JLabel("Date :");
        lblDate.setBounds(400, 25, 80, 20);
        lblDate.setFont(new Font("TimesRoman", Font.BOLD, 14));
        txtDate=new JTextField();
        txtDate.setFont(new Font("TimesRoman", Font.BOLD, 14));
        txtDate.setBounds(450, 25, 125, 20);
        txtDate.setEnabled(false);
        txtDate.setText(currentDate);

        lblPanel.add(lblEmpHead);
        lblPanel.add(lblLine);
        lblPanel.add(lblContactId);
        lblPanel.add(textContactId);
        lblPanel.add(lblContactNo);
        lblPanel.add(txtContactNo) ;
        lblPanel.add(lblContactName);
        lblPanel.add(txtContactName) ;
        lblPanel.add(lblDate);
        lblPanel.add(txtDate) ;
        lblPanel.add(tableContainer);

        //Button Panel
        btnPanel  =new JPanel();
        btnPanel.setLayout(null);

        cmdSave=new JButton("Save");
        cmdSave.setMnemonic('S');
        cmdSave.setFont(new Font("TimesRoman",Font.BOLD,12));
        cmdSave.setBounds(25,10,80,25);
        cmdSave.addActionListener(this);

        cmdList =new JButton("List");
        cmdList.setMnemonic('F');
        cmdList.setFont(new Font("TimesRoman", Font.BOLD, 12));
        cmdList.setBounds(125, 10, 80, 25);
        cmdList.addActionListener(this);

        cmdUpdate=new JButton("Update");
        cmdUpdate.setMnemonic('u');
        cmdUpdate.setFont(new Font("TimesRoman",Font.BOLD,12));
        cmdUpdate.setBounds(225,10,80,25);
        cmdUpdate.addActionListener(this);

        cmdDelete=new JButton("Delete");
        cmdDelete.setMnemonic('D');
        cmdDelete.setFont(new Font("TimesRoman",Font.BOLD,12));
        cmdDelete.setBounds(325,10,80,25);
        cmdDelete.addActionListener(this);

        cmdClear=new JButton("Clear");
        cmdClear.setMnemonic('C');
        cmdClear.setFont(new Font("TimesRoman",Font.BOLD,12));
        cmdClear.setBounds(425,10,80,25);
        cmdClear.addActionListener(this);

        btnPanel.add(cmdUpdate);
        btnPanel.add(cmdClear);
        btnPanel.add(cmdSave);
        btnPanel.add(cmdList);
        btnPanel.add(cmdDelete);

        mainPanel.add(lblPanel);
        lblPanel.setBounds(0,0,600,300);
        mainPanel.add(btnPanel);
        btnPanel.setBounds(0,300,600,400);

        getContentPane().add(mainPanel);
        //cmdUpdate.setEnabled(false);
        //cmdDelete.setEnabled(false);
    }

    public void keyReleased(KeyEvent evt){}

    public void keyTyped(KeyEvent evt){}

    public void keyPressed(KeyEvent evt) {
        Object obj = evt.getSource();
        int key = evt.getKeyCode();
        if(key == KeyEvent.VK_ENTER){
            if(obj == txtContactNo) {
                txtContactName.requestFocus();
                BlankCheck(txtContactNo);
            }
            if(obj == txtContactName){
                txtContactName.requestFocus();
                BlankCheck(txtContactName);
            }
            if(obj == textContactId){
                cmdSave.requestFocus();
                BlankCheck(textContactId);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();
        if(obj == cmdClear) {
            Clear();
        }
        if(obj == cmdSave) {
            setContact();
            ContactDAO.save(connection.getConnection(), contact);
            Clear();
        }
        if(obj == cmdList) {
            getJTable();
            resetTable(contactTableModel);
            ContactDAO.list(connection.getConnection(), jTable);
            /*cmdSave.setEnabled(false);
            cmdUpdate.setEnabled(true);
            cmdDelete.setEnabled(true);*/
        }
        if(obj == cmdDelete) {
            setContact();
            BlankCheck(txtContactNo);
            ContactDAO.delete(connection.getConnection(), contact);
            Clear();
            /*cmdSave.setEnabled(true);
            cmdUpdate.setEnabled(false);
            cmdDelete.setEnabled(false);*/
        }
        if(obj == cmdUpdate) {
            setContact();
            BlankCheck(txtContactNo);
            ContactDAO.update(connection.getConnection(), contact);
            Clear();
            /*cmdSave.setEnabled(true);
            cmdUpdate.setEnabled(false);
            cmdDelete.setEnabled(false);*/
        }
    }

    public void  Clear() {
        txtContactNo.setText("");
        txtContactName.setText("");
        textContactId.setText("");
    }

    public void BlankCheck(JTextField txt) {
        if (txt.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Primary Key cannot be null");
            txt.requestFocus();
        }
    }

    //Set Contact Information
    public void setContact() {
        contact = new Contact();
        contact.setContactId(textContactId.getText().trim());
        contact.setContactNo(txtContactNo.getText().trim());
        contact.setContactName(txtContactName.getText().trim());
    }

    private JTable getJTable() {
        String[] colName = { "Contact Id", "Contact No", "Contact Name"};
        if (jTable == null) {
            jTable = new JTable() {
                public boolean isCellEditable(int nRow, int nCol) {
                    return false;
                }
            };
        }
        contactTableModel = (DefaultTableModel) jTable.getModel();
        contactTableModel.setColumnIdentifiers(colName);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jTable;
    }

    private void resetTable(DefaultTableModel dataModel) {
        dataModel.setRowCount(0);
    }
}