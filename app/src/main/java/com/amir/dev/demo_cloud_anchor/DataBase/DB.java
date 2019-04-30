package com.amir.dev.demo_cloud_anchor.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amir.dev.demo_cloud_anchor.model.Adjacent;
import com.amir.dev.demo_cloud_anchor.model.MyAnchor;

import java.util.ArrayList;

public class DB {

    private SQLiteDatabase sql;


    public DB(SQLiteDatabase sql) {
        this.sql = sql;

    }

    // دي داله بتنشئ جدول لل MyAnchor
    public void createTable(){
        String str= "create table if not exists MyAnchor"+"(a_id integer primary key autoincrement,anchor_name text,anchor_desc text,anchor_length float,anchor_id float)" ;
        sql.execSQL(str);
    }

    //دي داله بتنشئ جدول لل parent
    public void createTableParent(){
        String str= "create table if not exists parent"+"(p_id integer primary key autoincrement,parent_name text,a_id integer,length float)" ;
        sql.execSQL(str);
    }


    // دي داله يتضيف داتا جوا ال parent
    public void insertParent(String name,int id,float l) {
        String str = "insert into parent(parent_name,a_id,length) values ('"+ name+"',"+id+","+l+")";
        sql.execSQL(str);
    }

    // دي داله بتضيف انكور جوا ال myanchor  وبترجع الاي دي بتاع الاتكور اللي اتضاف
    public int insertAnchor(MyAnchor anchor) {
        String str = "insert into MyAnchor(anchor_name,anchor_desc,anchor_length,anchor_id) values ('" + anchor.getAnchor_name() + "','" + anchor.getAnchor_desc() + "'," + anchor.getLenght() + ",'" + anchor.getAnchor_id() + "')";
        sql.execSQL(str);
        String getSQLid= "select a_id from MyAnchor where anchor_name='"+anchor.getAnchor_name()+"'";
        Cursor cursor = sql.rawQuery(getSQLid, null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex("a_id"));
        cursor.close();
        return id;
    }

    // دي داله بترجعلي كل الانكورز اللي ف الداتا بيز
     public ArrayList<MyAnchor> getAll(){
        ArrayList<MyAnchor> anchors=new ArrayList<>();
            String str="select * from MyAnchor";
            Cursor cursor= sql.rawQuery(str,null);
            if (cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String name =cursor.getString(cursor.getColumnIndex("anchor_name"));
                    String desc =cursor.getString(cursor.getColumnIndex("anchor_desc"));
                    float length =cursor.getFloat(cursor.getColumnIndex("anchor_length"));
                    String anchorId =cursor.getString(cursor.getColumnIndex("anchor_id"));
                    int id =cursor.getInt(cursor.getColumnIndex("a_id"));
                    MyAnchor anchor =new MyAnchor();
                    anchor.setAnchor_name(name);
                    anchor.setId(id);
                    anchor.setAnchor_desc(desc);
                    anchor.setLenght(length);
                    anchor.setAnchor_id(anchorId);
                    anchors.add(anchor);

                }
                cursor.close();
            }
            return anchors;
    }

    // دي داله بتجيبلي الانكور اللي ليه اسم معين
    public MyAnchor getAnchorByName(String name1){
        String str="select * from MyAnchor where anchor_name='"+name1+"'";
        Cursor cursor= sql.rawQuery(str,null);
        MyAnchor anchor =new MyAnchor();
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            String name =cursor.getString(cursor.getColumnIndex("anchor_name"));
            String desc =cursor.getString(cursor.getColumnIndex("anchor_desc"));
            float length =cursor.getFloat(cursor.getColumnIndex("anchor_length"));
            String anchorId =cursor.getString(cursor.getColumnIndex("anchor_id"));
            int id =cursor.getInt(cursor.getColumnIndex("a_id"));
            anchor.setAnchor_name(name);
            anchor.setAnchor_desc(desc);
            anchor.setLenght(length);
            anchor.setAnchor_id(anchorId);
            anchor.setId(id);
            cursor.close();
        }
        return anchor;
    }


    // دي بترجعلي الانكور اللي ليه اي دي معين
    public MyAnchor getAnchorById(int id){
        String str="select * from MyAnchor where a_id="+id;
        Cursor cursor= sql.rawQuery(str,null);
        MyAnchor anchor =new MyAnchor();
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            String name =cursor.getString(cursor.getColumnIndex("anchor_name"));
            String desc =cursor.getString(cursor.getColumnIndex("anchor_desc"));
            float length =cursor.getFloat(cursor.getColumnIndex("anchor_length"));
            String anchorId =cursor.getString(cursor.getColumnIndex("anchor_id"));
            anchor.setAnchor_name(name);
            anchor.setAnchor_desc(desc);
            anchor.setLenght(length);
            anchor.setAnchor_id(anchorId);
            anchor.setId(id);
            cursor.close();
        }
        return anchor;
    }


    // دي داله بتاخد مني اي دي انكور معين وبترجعلي الجيران بتوعه

    public ArrayList<Adjacent> getAnchorAdjacents(int id){
        String str="select * from parent where a_id="+id;
        ArrayList<Adjacent> adjacents=new ArrayList<>();
        Cursor cursor= sql.rawQuery(str,null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                String name =cursor.getString(cursor.getColumnIndex("parent_name"));
                int a_id =cursor.getInt(cursor.getColumnIndex("a_id"));
                int p_id =cursor.getInt(cursor.getColumnIndex("p_id"));
                float length  =cursor.getFloat(cursor.getColumnIndex("length"));
                Adjacent adjacent = new Adjacent();
                adjacent.setA_id(a_id);
                adjacent.setId(p_id);
                adjacent.setName(name);
                adjacent.setLength(length);
                adjacents.add(adjacent);
            }
            cursor.close();
        }
        return adjacents;
    }

// دي داله بتمسح الجدولين بتوع الداتا بيز
    public void DeleteAllData(){
        String str = "Delete from MyAnchor";
        String str1 = "Delete from parent";
        sql.execSQL(str);
        sql.execSQL(str1);
    }

}
