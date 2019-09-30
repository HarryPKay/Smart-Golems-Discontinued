package com.harrykay.smartgolems.common.entity.ai.pathfind;

import org.antlr.v4.runtime.misc.NotNull;

import static com.harrykay.smartgolems.common.entity.ai.pathfind.StateHelpers.heuristic;
import static com.harrykay.smartgolems.common.entity.ai.pathfind.StateHelpers.setAction;

public class Node {

    public Node parent = null;
    public int g = 0;
    public int f = 0;
    State state;
    Action action = null;

    // Head constructor
    public Node(@NotNull State state) {
        this.state = new State(state);
    }

    // Child constructor
    public Node(@NotNull Node parent, Action action, Goal goal) {
        this.parent = parent;
        this.action = action;
        this.state = new State(parent.state);
        setAction(this.state, action);
        this.g = parent.g + 1;
        this.f = this.g + heuristic(this.state.entityPosition, goal.desiredPosition);
    }
}


