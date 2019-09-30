package com.harrykay.smartgolems.common.entity.ai.pathfind;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    // ascending order.
    public int compare(Node leftNode, Node rightNode) {
        if (leftNode.f > rightNode.f)
            return 1;
        else if (leftNode.f < rightNode.f)
            return -1;
        return 0;
    }
}
