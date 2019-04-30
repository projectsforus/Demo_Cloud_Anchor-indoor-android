package com.amir.dev.demo_cloud_anchor.model;





// ده كلاس بيوصف شكل الداتا بتاعت الانكورز الجيران للانكور الاصلي
public class Adjacent {

    String name;
    int id;
    int a_id;
    float length;

    public Adjacent() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getA_id() {
        return a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
