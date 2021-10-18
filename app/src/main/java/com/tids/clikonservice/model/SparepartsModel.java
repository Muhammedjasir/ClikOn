package com.tids.clikonservice.model;

public class SparepartsModel {

    private String tb_id;
    private String productId;
    private String qty;
    private String partsId;
    private String partsName;
    private String partsCode;

    public SparepartsModel(String tb_id, String productId, String qty, String partsId, String partsName, String partsCode) {
        this.tb_id = tb_id;
        this.productId = productId;
        this.qty = qty;
        this.partsId = partsId;
        this.partsName = partsName;
        this.partsCode = partsCode;
    }

    public String getPartsCode() {
        return partsCode;
    }

    public void setPartsCode(String partsCode) {
        this.partsCode = partsCode;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public String getTb_id() {
        return tb_id;
    }

    public void setTb_id(String tb_id) {
        this.tb_id = tb_id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPartsId() {
        return partsId;
    }

    public void setPartsId(String partsId) {
        this.partsId = partsId;
    }
}
