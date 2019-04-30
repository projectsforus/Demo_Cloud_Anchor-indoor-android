package com.amir.dev.demo_cloud_anchor.algorithm;

import android.database.sqlite.SQLiteDatabase;

import com.amir.dev.demo_cloud_anchor.DataBase.DB;
import com.amir.dev.demo_cloud_anchor.model.MyAnchor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyNode {

    // ده كلاس بيوصفلي شكل ال node اللي الالجوريزم هيشتغل عليها
    MyAnchor anchor;
    ArrayList<MyNode> parent;
    private List<MyNode> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    Map<MyNode, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(MyNode destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public MyAnchor getAnchor() {
        return anchor;
    }

    public void setAnchor(MyAnchor anchor) {
        this.anchor = anchor;
    }

    public List<MyNode> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<MyNode> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Map<MyNode, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<MyNode, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public MyNode(MyAnchor anchor) {
        this.anchor = anchor;
    }

    public ArrayList<MyNode> getParent() {
        return parent;
    }

    public void setParent(ArrayList<MyNode> parent) {
        this.parent = parent;
    }
}
