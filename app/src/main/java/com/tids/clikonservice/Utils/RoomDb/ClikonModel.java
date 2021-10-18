package com.tids.clikonservice.Utils.RoomDb;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity annotation for giving table name and declaring it
@Entity(tableName = "products")
public class ClikonModel {

    //@primary Key to set id as primary key
    // and making auto increment for each new list
    @PrimaryKey(autoGenerate = true)
    private int id;

    //@ColumnInfo for giving column name todo_title for entity title
    //and this name will be used in all database queries
    @ColumnInfo(name = "product_code")
    private String product_code;

    @ColumnInfo(name = "product_name")
    private String product_name;

    @ColumnInfo(name = "product_serialNumber")
    private String serial_no;

    @ColumnInfo(name = "product_batchNumber")
    private String batch_no;

    @ColumnInfo(name = "product_complaint")
    private String complaint;

    public ClikonModel(String product_code, String product_name, String serial_no, String batch_no, String complaint) {
        this.product_code = product_code;
        this.product_name = product_name;
        this.serial_no = serial_no;
        this.batch_no = batch_no;
        this.complaint = complaint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
}
