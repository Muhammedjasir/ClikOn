package com.tids.clikonservice.model;

public class LocationModel {

    private String productDocId;
    private String productRefId;
    private String location;
    private String address;
    private String productName;
    private String productQrCode;

    public LocationModel(String productDocId, String productRefId, String location, String address, String productName, String productQrCode) {
        this.productDocId = productDocId;
        this.productRefId = productRefId;
        this.location = location;
        this.address = address;
        this.productName = productName;
        this.productQrCode = productQrCode;
    }

    public String getProductQrCode() {
        return productQrCode;
    }

    public void setProductQrCode(String productQrCode) {
        this.productQrCode = productQrCode;
    }

    public String getProductDocId() {
        return productDocId;
    }

    public void setProductDocId(String productDocId) {
        this.productDocId = productDocId;
    }

    public String getProductRefId() {
        return productRefId;
    }

    public void setProductRefId(String productRefId) {
        this.productRefId = productRefId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
