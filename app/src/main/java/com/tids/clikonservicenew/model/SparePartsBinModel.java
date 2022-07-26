package com.tids.clikonservicenew.model;

public class SparePartsBinModel {

    private String valueField;
    private String textField;

    public SparePartsBinModel(String valueField, String textField) {
        this.valueField = valueField;
        this.textField = textField;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }
}
