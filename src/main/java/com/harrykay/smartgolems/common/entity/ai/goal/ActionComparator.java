package com.harrykay.smartgolems.common.entity.ai.goal;

import java.util.Comparator;

public class ActionComparator implements Comparator<Action> {

    // ascending order.
    public int compare(Action s1, Action s2) {
        if (s1.priority > s2.priority)
            return 1;
        else if (s1.priority < s2.priority)
            return -1;
        return 0;
    }
}
