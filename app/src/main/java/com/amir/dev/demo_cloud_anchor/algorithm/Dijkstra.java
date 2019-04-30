package com.amir.dev.demo_cloud_anchor.algorithm;

import android.util.Log;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Dijkstra {





    // دي داله مسئوله عن تنفيذ الالجوريزم
    public static Graph calculateShortestPathFromSource(Graph graph,MyNode source) {
        source.setDistance(0);
        // دي ارراي بحط فيها النودز اللي الالجوريزم زارها
        Set<MyNode> settledNodes = new HashSet<>();
        // دي ارراي بحط فيها النودز اللي الالجوريزم مازارهاش
        Set<MyNode> unsettledNodes = new HashSet<>();

        // هنا بحط السورس ف النودز اللي ماتزارتش لسا
        unsettledNodes.add(source);

        // ده لوب هيفضل يتنفذ طول ما عندي نودز الالجوريزم مازارهاش
        while (unsettledNodes.size() != 0) {

            // هنا بجيب اصغر نود ف النودز اللي الالجوريزم مازارهاش
            MyNode currentNode = getLowestDistanceNode(unsettledNodes);

            // اول ماجبتها كدا معناها ان الالجوريزم زارها فهمسحها من الليست بتاعت اللي ماتزاروش
            unsettledNodes.remove(currentNode);

            // النود دي جواها داله بتجيبلي الجيران بتاعتها

            //فانا هنا عامل لوب يلف على كل جيرانها
            for (Map.Entry< MyNode, Integer> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                // هنا جوا اللوب بمسك جيرانها واحد واحد بجيب منه القيمة اللي ع الجار ذات نفسه
                MyNode adjacentNode = adjacencyPair.getKey();
                // هنا جوا اللوب بمسك جيرانها واحد واحد بجيب منه القيمة اللي ع ال edge اللي بين النود والجار ده
                Integer edgeWeight = adjacencyPair.getValue();

                //بعد كدا بشوف لو الجار ده الالجوريزم ما زارهوش قبل كدا
                if (!settledNodes.contains(adjacentNode)) {
                    // هنا بقوله لو القيمه اللي ع ال edge بين النود والجار ده اصغر من القيمة اللي ع الجار غيرلي قيمة النود بقيمة ال edge
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    // بعد كدا بقول ان الجار انا مازرتوش لسا
                    unsettledNodes.add(adjacentNode);
                }
            }
            // يعد ما خلصت كل جيران النود والقيم بتاعتهم هقوله هنا ان النود دي الالجوريزم زارها
            settledNodes.add(currentNode);

        }
        // هنا برجع الجراف بعد ما كل نود جواه اتحددلها اقصر طريق بينها وبين ال نود الاولى
        return graph;
    }

    private static MyNode getLowestDistanceNode(Set < MyNode > unsettledNodes) {

       // دي داله بتلف النودز كلها اللي الالجوريزم مازارهاش بتطلعلي اصغر نود فيهم

        MyNode lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (MyNode node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(MyNode evaluationNode,
                                                 Integer edgeWeigh, MyNode sourceNode) {

        // دي داله بتحسب لو القيمه اللي عال edge  اقل من القيمة اللي ف النود بتبدلهم وبعدين تقول ان الجار ده بينتمي للطريق الاقصر للنود دي ومع التكرار بيبتدي الطريق يتحط فيه جار بعد جار لحد ما يتكون كله
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<MyNode> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}
