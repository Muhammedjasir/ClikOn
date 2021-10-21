package com.tids.clikonservice.model;

public class LocationModel {

    private String id;
    private String shopName;
    private String address;
    private String type;



    public LocationModel(String id, String shopName, String address, String type) {
        this.id = id;
        this.shopName = shopName;
        this.address = address;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
