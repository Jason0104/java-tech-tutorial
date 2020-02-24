package com.java.tech.prototype;

/**
 * created by Jason on 2020/2/23
 */
public abstract class Shape implements Cloneable {

    private String id;
    protected String type;

    abstract void draw();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object clone(){
        Object obj = null;
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
