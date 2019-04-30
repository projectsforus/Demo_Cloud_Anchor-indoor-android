package com.amir.dev.demo_cloud_anchor.algorithm;

import java.util.HashSet;
import java.util.Set;

public class Graph {

    // ده كلاس بيوصفلي شكل الجراف اللي بيتكون من مجموعه من النودز
    private Set<MyNode> nodes = new HashSet<>();

    public Set<MyNode> getNodes() {
        return nodes;
    }

    public void setNodes(Set<MyNode> nodes) {
        this.nodes = nodes;
    }

    public void addNode(MyNode nodeA) {
        nodes.add(nodeA);

    }
}
