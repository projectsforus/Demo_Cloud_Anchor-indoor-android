package com.amir.dev.demo_cloud_anchor.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyAnchor implements Parcelable{

    // ده كلاس بيوصف شكل الانكور الاصلي بتاعي
    String anchor_name;
    String anchor_desc;
    String anchor_id;
    float lenght;
    String Parent;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyAnchor() {
    }

    public String getParent() {
        return Parent;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public MyAnchor(String anchor_name, String anchor_desc, String anchor_id, float lenght) {

        this.anchor_name = anchor_name;
        this.anchor_desc = anchor_desc;
        this.anchor_id = anchor_id;
        this.lenght = lenght;
    }

    protected MyAnchor(Parcel in) {
        anchor_name = in.readString();
        anchor_desc = in.readString();
        anchor_id = in.readString();
        lenght = in.readFloat();
    }

    public static final Creator<MyAnchor> CREATOR = new Creator<MyAnchor>() {
        @Override
        public MyAnchor createFromParcel(Parcel in) {
            return new MyAnchor(in);
        }

        @Override
        public MyAnchor[] newArray(int size) {
            return new MyAnchor[size];
        }
    };

    public String getAnchor_name() {

        return anchor_name;
    }

    public void setAnchor_name(String anchor_name) {
        this.anchor_name = anchor_name;
    }

    public String getAnchor_desc() {
        return anchor_desc;
    }

    public void setAnchor_desc(String anchor_desc) {
        this.anchor_desc = anchor_desc;
    }

    public String getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(String anchor_id) {
        this.anchor_id = anchor_id;
    }

    public float getLenght() {
        return lenght;
    }

    public void setLenght(float lenght) {
        this.lenght = lenght;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(anchor_name);
        parcel.writeString(anchor_desc);
        parcel.writeString(anchor_id);
        parcel.writeFloat(lenght);
    }
}
