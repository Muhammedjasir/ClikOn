package com.tids.clikonservice.model;

public class ProductModel {

    private int id;
    private String product_code;
    private String product_name;
    private String product_date;
    private String product_status;
    private String product_serial_number;
    private String product_batch_number;

    public ProductModel(int id, String product_code, String product_name, String product_date, String product_status,
                        String product_serial_number, String product_batch_number) {
        this.id = id;
        this.product_code = product_code;
        this.product_name = product_name;
        this.product_date = product_date;
        this.product_status = product_status;
        this.product_serial_number = product_serial_number;
        this.product_batch_number = product_batch_number;
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

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getProduct_serial_number() {
        return product_serial_number;
    }

    public void setProduct_serial_number(String product_serial_number) {
        this.product_serial_number = product_serial_number;
    }

    public String getProduct_batch_number() {
        return product_batch_number;
    }

    public void setProduct_batch_number(String product_batch_number) {
        this.product_batch_number = product_batch_number;
    }
}
