package com.hyipc.uhf_r2000.model;

import java.io.Serializable;

/**
 * Created by ting on 2017/9/28.
 */

public class KeyValue implements Serializable {
    private byte key;
    private String value;

    public KeyValue() {

    }

    public KeyValue(byte key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public byte getKey() {
        return key;
    }

    public void setKey(byte key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
   public String toString() {
     return value;
   }
}
