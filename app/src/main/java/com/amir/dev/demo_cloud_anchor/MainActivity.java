package com.amir.dev.demo_cloud_anchor;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amir.dev.demo_cloud_anchor.DataBase.DB;
import com.amir.dev.demo_cloud_anchor.model.MyAnchor;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // ده تعريف الكاميرا
    CustomArFragment arFragment;

    // ده اوبجكت من الداتا بيز بتاعتنا
    DB db;

    // ده اوبجكت من sqlite بتاع الاندرويد
    SQLiteDatabase sql;

    //  ده عامله علشان لو هو ب 0 ميرسمش الخط لاني هكون عند اول انكور لسا لكن لو اكبر من 0 هيرسم الحط
    int counter =0;

    // دي array فيها كل الانكورز اللي انا بضيفها
    ArrayList<AnchorNode> currentAnchorsLine=new ArrayList<>();





    // دي مسئوله عن فتح صفحة اليوزر
    public void OpenMapActivity_Click(View view) {
        Intent intent =new Intent(MainActivity.this,UserMapActivity.class);
        startActivity(intent);
    }

    // دي اللي بتفتحلك صفحة الليست بتاعت كل الانكورز اللي ضفتها
    public void saveAnchorParents_Click(View view) {
        Intent intent = new Intent(MainActivity.this,AllAnchorsActivity.class);
        startActivity(intent);
    }

    // دي بتمسحلك كل الجداول اللي عندك ف الداتا بيز
    public void deleteData_Click(View view) {
        db.DeleteAllData();
    }


    // ده enum متخزن جواه 3 قيم بتعبرلي عن حالات الانكور بتاعي اللي انا لسا ضاغط ع الشاشه ومخليه يترفع ع الكلاود
    private enum AppAnchorState{
        NONE,
        HOSTING,
        HOSTED
    }

    // ده متغير بتحكم بيه في اني اخلي الادمن يقدر يضغط ع الشاشه ولا لا
    boolean isPlaced =false;
    // دي الحاله ال default  للانكور
    AppAnchorState appAnchorState=AppAnchorState.NONE;

    Anchor anchor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ده متغير من نوع SQLiteDatabase علشان اعرفه بستدعي الداله دي بتاخد مني اسم الداتا بيز والمود بتاعها 0 يعني private
        sql=openOrCreateDatabase("test10",0,null);

        //ده متغير من نوع DB وده اللي هو الكلاس بتاعنا اللي فيه كل الدوال اللي بنتعامل بيها ف الداتا بيز
        db=new DB(sql);
        // دي داله بتخليني انشئ جدول للانكورز
        db.createTable();
        // دي داله بتخليني انشئ جدول لل adjacent  اللي هو جدول ال parent
        db.createTableParent();

        // ده كدا ال كاميرا بتاعت الل ar اللي من خلالها بقدر اعمل العمليات بتاعت ال ar اللي انا عايزها
        arFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        // ده الحدث بتاع لما اضغط ع الشاشه
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            // لو اقدر اني اضيف انكور
           if (!isPlaced) {

               // هنا هينشئ انكور مكان ضغطتي ع الكاميرا ويبدا يرفعه ع الكلاود
               anchor = arFragment.getArSceneView().getSession().hostCloudAnchor(hitResult.createAnchor());
              // هنا بقوله ان حاله الانكور ده دلوقتي انه بيترفع
               appAnchorState = AppAnchorState.HOSTING;
               // دي داله بتخليني ارسم ال 3d مودل بتاعي مكان الانكور
               createModel(anchor);
               // دي داله بتظهرلي رساله توست
               showToast("Hosting...");

               // هنا بمنعه انه يضيف اي انكور تاني ع الشاشه لحد م الانكور الحالي يترفع ع الكلاود
               isPlaced=true;
           }
        });



        // ده الحدث اللي بيتنفذ معايا كل ما يحصل update  ف الكاميرا
        arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

           // هنا بقوله لو الانكور الحالي حالته انه مش بيترفع ع السيرفر خرج برا الحدص ده
            if (appAnchorState !=AppAnchorState.HOSTING)
                return;

            // لو هو بقا بيترقع ع الكلاود هتجيبلي الحاله بتاعته من الكلاود متنساش ان الكلام ده بيتنغذ كل شويه اللي كل ما الكاميرا يحصلها update يعني كل شويه
            Anchor.CloudAnchorState cloudAnchorState = anchor.getCloudAnchorState();

            // لو لقدر الله حصل حاجه عمليه رفع الانكور ع الكلاود يعني حالته فيها error
            if (cloudAnchorState.isError()){
                // هتظهر رساله توست فيها ال error ده
                showToast(cloudAnchorState.toString());
            }
            //لو الانكور اترفع بنجاح الحمد لله
            else if (cloudAnchorState== Anchor.CloudAnchorState.SUCCESS){

               // هتحلي حالته اللي هنا بانه اترفع
                appAnchorState=AppAnchorState.HOSTED;

                // هنا هنجيب ال id بتاعه اللي موجود ع الكلاود علشان ده اللي هنجيبه بيه من ع الكلاود بعدين
                String anchorId = anchor.getCloudAnchorId();


                // save anchor to data base
                //بعرف هنا التكست بتاعت الاسم
                EditText nameView=findViewById(R.id.anchor_name);
                //بعرف هنا التكست بتاعت الوصف
                EditText descView=findViewById(R.id.anchor_desc);

                //بجيب القيمه اللي ف التكست بتاع الاسم
                String name =nameView.getText().toString();
                //بجيب القيمه اللي ف التكست بتاع الوصف
                String  desc =descView.getText().toString();


                // بعرف هنا متغير من الانكور اللي انا عامله علشان هحفظ فيه الاسم والوصف و ال id وهخزنه ف sqlite
                MyAnchor mAnchor=new MyAnchor();
                mAnchor.setAnchor_name(name);
                mAnchor.setAnchor_desc(desc);
                mAnchor.setAnchor_id(anchorId);

                // دي داله بتحفظ الانكور ف الداتا بيز
                db.insertAnchor(mAnchor);

                // بظهرله رساله انه اتحفظ بنجاح
                showToast("Anchor Hosted Successfully . Anchor ID : "+anchorId);
                // بخلي الادمن يقدر انه يضغط ع الشاشه علشان يضيف انكور جديد
                isPlaced=false;
            }
        });
    }

    private void showToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }


    // دي داله الرسم كنت شرحتهالكم قبل كدا
    private void createModel(Anchor anchor) {
        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("1.sfb"))
                .build()
                .thenAccept(modelRenderable ->placeModel(anchor,modelRenderable));
    }

    private void placeModel(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode =new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        currentAnchorsLine.add(anchorNode);
        if (counter>0){
           lineBetweenPoints(currentAnchorsLine.get(counter).getLocalPosition(),currentAnchorsLine.get(counter-1).getLocalPosition(),anchorNode);
        }
        counter++;
    }


    public void lineBetweenPoints(Vector3 point1, Vector3 point2, AnchorNode anchorNode) {
        // دي النود اللي هرسم عليها الحط بتاعي
        Node lineNode = new Node();


        // هنا بجيب نقطه المنتصف بين اتنين انكور علشان احط فيها الخط
        float x =(point1.x+point2.x)/2;
        float y =point1.y;
        float z =(point1.z+point2.z)/2;
        Vector3 v =new Vector3(x,y,z);

        Log.i("middle",String.valueOf(x)+"||"+String.valueOf(y)+"{"+String.valueOf(point2.x)+"||"+ String.valueOf(point2.z)+"}");

   /* First, find the vector extending between the two points and define a look rotation in terms of this
        Vector. */

        // ده فيكتور ثلاثي الابعاد وهو ناتج طرح الاحداثيين اللي برسم بينهم وبكدا هو اصبح بيعبر عن اتجاه من نقطة للتانيه
        final Vector3 difference = Vector3.subtract(point1, point2);

        // بعمله للمتجه اللي فات نورماليز علشان اجيب الاتجاه
        final Vector3 directionFromTopToBottom = difference.normalized();

        // هنا بحسب انا المفروض اعمل rotate  بمقدار كام علشان ابص ف اتجاه المتجه ده
        final Quaternion rotationFromAToB =
                Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());

   /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
         to extend to the necessary length.  */



        // هنا بنشئ الرسمه بتاعت الخط
        MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new Color(0, 255, 244))
                .thenAccept(
                        material -> {
                            /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
                                   to extend to the necessary length.  */
                            ModelRenderable lineRenderable = ShapeFactory.makeCube(new Vector3(.8f, .3f, difference.length()),
                                    Vector3.zero(), material);

   /* Last, set the local rotation of the node to the rotation calculated earlier and set the local position to
       the midpoint between the given points . */

                            //هنا بقوله الاحداثيات بتاعتي حط عليها الخط اللي رسمته وخليه يعمل rotate بنفس المقدار اللي حسبته قبل كدا
                            lineNode.setParent(anchorNode);
                            lineNode.setRenderable(lineRenderable);
                            lineNode.setWorldPosition(v);
                            lineNode.setWorldRotation(rotationFromAToB);
                            Toast.makeText(this,"finish",Toast.LENGTH_LONG).show();
                        });
    }
}
