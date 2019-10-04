package com.harrykay.smartgolems.common.entity.ai.goal;

import java.util.Comparator;

public class ActionComparator implements Comparator<Action> {

    // ascending order.
    public int compare(Action leftAction, Action rightAction) {
        if (leftAction.priority > rightAction.priority)
            return 1;
        else if (leftAction.priority < rightAction.priority)
            return -1;
        return 0;
    }
}
