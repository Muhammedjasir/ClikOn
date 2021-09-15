package com.tids.clikonservice.model;

public class ScannedProductModel {

    private String productDocId;
    private String productScannedId;
    private String productName;
    private String productSerialNumber;
    private String productBatchNumber;
    private String productComplaint;
    private String pageFlag;

    public ScannedProductModel(String productDocId, String productScannedId, String productName,
                               String productSerialNumber, String productBatchNumber, String productComplaint) {
        this.productDocId = productDocId;
        this.productScannedId = productScannedId;
        this.productName = productName;
        this.productSerialNumber = productSerialNumber;
        this.productBatchNumber = productBatchNumber;
        this.productComplaint = productComplaint;
    }

    public ScannedProductModel(String productDocId, String productScannedId, String productName,
                               String productSerialNumber, String productBatchNumber, String productComplaint,
                               String pageFlag) {
        this.productDocId = productDocId;
        this.productScannedId = productScannedId;
        this.productName = productName;
        this.productSerialNumber = productSerialNumber;
        this.productBatchNumber = productBatchNumber;
        this.productComplaint = productComplaint;
        this.pageFlag = pageFlag;
    }

    public String getProductDocId() {
        return productDocId;
    }

    public void setProductDocId(String productDocId) {
        this.productDocId = productDocId;
    }

    public String getProductScannedId() {
        return productScannedId;
    }

    public void setProductScannedId(String productScannedId) {
        this.productScannedId = productScannedId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSerialNumber() {
        return productSerialNumber;
    }

    public void setProductSerialNumber(String productSerialNumber) {
        this.productSerialNumber = productSerialNumber;
    }

    public String getProductBatchNumber() {
        return productBatchNumber;
    }

    public void setProductBatchNumber(String productBatchNumber) {
        this.productBatchNumber = productBatchNumber;
    }

    public String getProductComplaint() {
        return productComplaint;
    }

    public void setProductComplaint(String productComplaint) {
        this.productComplaint = productComplaint;
    }

    public String getPageFlag() {
        return pageFlag;
    }

    public void setPageFlag(String pageFlag) {
        this.pageFlag = pageFlag;
    }
}
