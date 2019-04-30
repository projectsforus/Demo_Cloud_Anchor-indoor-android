package com.amir.dev.demo_cloud_anchor;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amir.dev.demo_cloud_anchor.DataBase.DB;
import com.amir.dev.demo_cloud_anchor.model.MyAnchor;

import java.util.ArrayList;

public class AllAnchorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_anchors);
        ListView allAnchors =findViewById(R.id.lvAnchors);

        SQLiteDatabase sql=openOrCreateDatabase("test10",0,null);
        DB db =new DB(sql);


        // هنا بجيب من الداتا بيز كل الانكورز الموجوده عندي
        ArrayList<MyAnchor> all = db.getAll();

        // هنا بجيب ارراي لكل الاسماء بتاعت الانكورز
        ArrayList<String> allNames =new ArrayList<>();
        for (MyAnchor anchor : all){
            allNames.add(anchor.getAnchor_name());
        }

        // هنا بعرض كل الاسماء ف ليست فيو
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,allNames);
        allAnchors.setAdapter(adapter);


        // ده الحدث اللي بيتنفذ ف كل مرة اضغط فيها ع اسم من الليست
        allAnchors.setOnItemClickListener((adapterView, view, i, l) -> {

            // بقوله افتحلي صفحه فيها الداتا بتاعت الانكور ده وببعت الاي دي بتاع الانكور للصفحه دي
            Intent intent = new Intent(AllAnchorsActivity.this,AnchorDataActivity.class);
            intent.putExtra("id",all.get(i).getId());
            startActivity(intent);
        });
    }
}
