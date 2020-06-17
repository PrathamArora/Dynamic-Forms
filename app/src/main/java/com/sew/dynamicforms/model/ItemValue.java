package com.sew.dynamicforms.model;

import android.content.Intent;

import org.json.JSONObject;

import java.io.Serializable;

public class ItemValue implements Serializable {
    private String label;
    private int _id;

    private ItemValue(int _id, String label) {
        set_id(_id);
        setLabel(label);
    }

    static ItemValue getItemValueFromJSON(JSONObject singleItemValue) {
        int _id = singleItemValue.has("_id") ? Integer.parseInt(singleItemValue.optString("_id")) : 0;
        String label = singleItemValue.optString("label");
        return new ItemValue(_id, label);
    }

    public int get_id() {
        return _id;
    }

    private void set_id(int _id) {
        this._id = _id;
    }

    public String getLabel() {
        return label;
    }

    private void setLabel(String label) {
        this.label = label;
    }
}
