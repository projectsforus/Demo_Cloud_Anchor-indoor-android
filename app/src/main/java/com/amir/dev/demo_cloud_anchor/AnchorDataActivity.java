package com.amir.dev.demo_cloud_anchor;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amir.dev.demo_cloud_anchor.DataBase.DB;
import com.amir.dev.demo_cloud_anchor.model.MyAnchor;

import java.util.ArrayList;

public class AnchorDataActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner allAnchors;
    TextView nameView,descView;
    EditText lengthView;
    DB db;
    int id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anchor_data);
        SQLiteDatabase sql=openOrCreateDatabase("test10",0,null);
        db =new DB(sql);
        db.createTableParent();
        allAnchors=findViewById(R.id.allAnchorNames);
        nameView=findViewById(R.id.tvName);
        descView=findViewById(R.id.tvDesc);
        lengthView=findViewById(R.id.etLength);


        // هنا بستقبل الاي دي اللي جايلي من صفحة allanchors
        id = getIntent().getIntExtra("id",1);

        // عن طريق الاي دي ده بجيب الانكور بتاعه من الداتا بيز
        MyAnchor anchor1 = db.getAnchorById(id);

        // هنا بعرض الداتا بتاعت الانكور ده
        nameView.setText(anchor1.getAnchor_name());
        descView.setText(anchor1.getAnchor_desc());

        // هنا زي الصفحه بتاعت allanchors  بجيب كل الانكورز اللي ف الداتا بيز وباخد اسم كل انكور اضيفه ف ارراي
        ArrayList<MyAnchor> all = db.getAll();
        ArrayList<String> allNames =new ArrayList<>();
        for (MyAnchor anchor : all){
            allNames.add(anchor.getAnchor_name());
        }
        // هنا بعرض كل الاسماء ف ليست
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,allNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allAnchors.setAdapter(adapter);

    }

    public void saveAnchorParents_Click(View view) {
        // هنا لو سايب اللينث فاضي هيظهرلك ايرور
        if (lengthView.getText().equals("")){
            lengthView.setError("please enter the length");
            return;
        }
        else {
            // هنا بجيب الاسم اللي اختارته من الليست
            String adjacent = allAnchors.getSelectedItem().toString();
            //هنا بجيب القيمة اللي دخلتها ف اللينث واحولها ل float
            float length = Float.parseFloat(lengthView.getText().toString());

            // باخد الاسم واللينث والاي دي واخزنهم ف جدول ال parent
            db.insertParent(adjacent,id,length);
        }
    }
}
