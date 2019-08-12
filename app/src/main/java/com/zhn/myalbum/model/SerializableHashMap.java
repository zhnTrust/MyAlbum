package com.zhn.myalbum.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 封装hashmap,用于在intent中传递
 */
public class SerializableHashMap implements Serializable {
    private HashMap<String,String[]> map;

    public HashMap<String, String[]> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String[]> map) {
        this.map = map;
    }
}
