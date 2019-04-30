package com.amir.dev.demo_cloud_anchor;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amir.dev.demo_cloud_anchor.DataBase.DB;
import com.amir.dev.demo_cloud_anchor.algorithm.Dijkstra;
import com.amir.dev.demo_cloud_anchor.algorithm.Graph;
import com.amir.dev.demo_cloud_anchor.algorithm.MyNode;
import com.amir.dev.demo_cloud_anchor.model.Adjacent;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UserMapActivity extends AppCompatActivity {

    CustomArFragment arFragment;

    // دي الليست اللي هتعرض البدايه والنهاية
    Spinner startAnchor,endAnchor;


    // دول علشان اعرف الداتا بيز
    DB db ;
    SQLiteDatabase sql;


    ArrayList<MyAnchor> anchors ;
    ArrayList<MyNode> nodes = new ArrayList<>();
    ArrayList<AnchorNode> ResolvedAnchors=new ArrayList<>();
    int currentAnchorIndex=0;
    ArrayList<String> anchorsName =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        arFragment= (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arfragment);

        sql=openOrCreateDatabase("test10",0,null);
        db=new DB(sql);
        db.createTable();


        // دي ليست من كل الانكورز
        anchors=db.getAll();


        // هنا بجهز ليست جديدة لنحديد اسماء كل انكور
        for (MyAnchor a:anchors){
            anchorsName.add(a.getAnchor_name());
        }


        startAnchor =findViewById(R.id.startAnchor);
        endAnchor=findViewById(R.id.endAnchor);


        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,anchorsName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        startAnchor.setAdapter(adapter);
        endAnchor.setAdapter(adapter);






    }

    public void Next_Anchor_Click(View view) {

        // ده الحدث اللي بيتنفذ مع ضغطه اليوزر

        ArrayList<MyAnchor> allAnchors = db.getAll();

        // هنا بمشي علي كل الانكورز احولهم لنودز
        for (MyAnchor a : allAnchors){
            MyNode node = new MyNode(a);
            nodes.add(node);
        }

        // هنا بجيب الجيران بتاعت كل نود

        for (MyNode node: nodes){
            ArrayList<Adjacent> adjacents = db.getAnchorAdjacents(node.getAnchor().getId());

            // هنا بمشي عليهم جار ورا جار واجيب الانكور بتاع الجار ده
            for (Adjacent adjacent : adjacents){
                MyAnchor anchorByName = db.getAnchorByName(adjacent.getName());
                for (MyNode n:nodes){
                    // بمسك الانكور بتاع الجار اللي معايا ف كل لفه واختبره هل هو هو الانكور بتاع النود دي
                    if (n.getAnchor().getAnchor_name().equals(anchorByName.getAnchor_name())){

                        // لو طلع هو هضيفه ف الجيران بتاعت النود
                        node.addDestination(n,(int)adjacent.getLength());
                    }
                }
            }
        }

        // هنا بجيب القيمة اللي اليوزر مختارها كبدايه

        String s = startAnchor.getSelectedItem().toString();

        // باخد القيمه دي اللي هي اسم الانكور واجيب بيه الانكور ذات نفسه
        MyAnchor anchorByName = db.getAnchorByName(s);
        //بعد كدا بعمل نود من الانكور
        MyNode source =new MyNode(anchorByName);

       // والف على كل النودز واشوف مين فيهم ليها نفس الانكور اللي معايا واخليها هي ال source
        for (MyNode n : nodes){
            if (n.getAnchor().getAnchor_name().equals(source.getAnchor().getAnchor_name())){
                source=n;
                break;
            }
        }
        // هنا بعرف جراف جديد
        Graph graph =new Graph();

        // هلف على كل النودز اللي عندي اضيفها كلها جوا الجراف
        for (MyNode node : nodes){
            graph.addNode(node);
        }

        // بعد كدا بستدعي الالجوريزم بتاعي واباصيله الجراف ونقطة البداية
        graph = Dijkstra.calculateShortestPathFromSource(graph,source);

        String end = endAnchor.getSelectedItem().toString();

        // دي ارراي هحط فيها الطريق اللي راجعلي ف الجراف
        // بس الجراف بيبقى راجع باقصر طريق عند كل نقطه فانا هختار اقصر طريق من البدايه وحتى النقطة اللي انا محددها كبدايه
        List<MyNode> path=new ArrayList<>();
        for (MyNode node : graph.getNodes()){
            if (node.getAnchor().getAnchor_name().equals(end))
            {
                path=node.getShortestPath();
                break;
            }
        }

        // الطريق بيبقى راجع من غير اخر انكور
        // فانا هجيب الانكور بتاع ال End وهضيفه للارراي بتاعت الطريق اللي انا لسا جايبه

        MyAnchor anchorByName1 = db.getAnchorByName(end);
        path.add(new MyNode(anchorByName1));

        // الباص بيبقى جاي ف شكل list اللي هي linked list فانا هحولها لارراي ليست عادية
        ArrayList<MyAnchor>myPath =new ArrayList<>();
        for (MyNode mNode:path){
            myPath.add(mNode.getAnchor());
        }

        // الcurrentindex ده متغير بيمثلي الاندكس بتاع الانكور اللي عليها الدور ف الطريق
        MyAnchor anchor = myPath.get(currentAnchorIndex);
        // بعد ما جبت الانكور اللي عليه الدور هبدا اجيبه من الكلاود عن طريق الانكور اي دي
        Anchor resolvedAnchor = arFragment.getArSceneView().getSession().resolveCloudAnchor(anchor.getAnchor_id());

        // وابدا ارسم الانكور ده
        createModel(resolvedAnchor,anchor.getAnchor_name());
        currentAnchorIndex++;
       // لو الاندكس نخطى حدود اللارراي بتاهت الطريق بغير قمته لاندكش بتاع اخر انكور
        if (currentAnchorIndex >= myPath.size()){
            currentAnchorIndex=myPath.size()-1;
        }

        //هنا بعرض الوصف بتاع الانكور ف text view
        TextView desc =findViewById(R.id.desc);
        desc.setText(anchor.getAnchor_desc());
    }

    public void Draw_Next_Anchor_Click(View view) {

        // دي داله بتمشي على كل الانكور تبدا تشوف لو الانكور ده مش الاول ف الطريق يعني اعرف ارسم حط هترسمة

       for (int i = 1;i<ResolvedAnchors.size();i++){
           lineBetweenPoints(ResolvedAnchors.get(i).getLocalPosition(),ResolvedAnchors.get(i-1).getLocalPosition(),ResolvedAnchors.get(i));

       }



    }

    private void createModel(Anchor anchor,String last) {

        //هنا بشوف هل الاتكور اللي بيترسم هو احر انكور ف الباص ولا لا لو هو هعرضله اشاره للنهايه ولو مش هو هعرضله الاوبجت العادي
        if (last.equals(endAnchor.getSelectedItem().toString())){
            ModelRenderable
                    .builder()
                    .setSource(this, Uri.parse("stop_sign.sfb"))
                    .build()
                    .thenAccept(modelRenderable ->placeModel(anchor,modelRenderable));
        }
        else {
            ModelRenderable
                    .builder()
                    .setSource(this, Uri.parse("1.sfb"))
                    .build()
                    .thenAccept(modelRenderable ->placeModel(anchor,modelRenderable));
        }


    }

    private void placeModel(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode =new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        ResolvedAnchors.add(anchorNode);


    }


    public void lineBetweenPoints(Vector3 point1, Vector3 point2, AnchorNode anchorNode) {
        // دي النود اللي هرسم عليها الحط بتاعي
        Node lineNode = new Node();

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
                            //lineNode.setLocalPosition(Vector3.add(point1, point2));
                            lineNode.setWorldPosition(v);
                            lineNode.setWorldRotation(rotationFromAToB);
                            Toast.makeText(this,"finish",Toast.LENGTH_LONG).show();
                        });
    }

}
