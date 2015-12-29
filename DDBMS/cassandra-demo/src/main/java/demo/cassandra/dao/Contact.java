package demo.cassandra.dao;

public class Contact {
    private String contactId;
    private String contactNo;
    private String contactName;
    private String contactFields = "(contactid,contactno,contactname)";

    public String getContactFields() {
        return contactFields;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String toString(){
        return "[" +getContactId() + " ," + getContactName() + ", " +getContactName() + "]";
    }
}