package com.harrykay.smartgolems.common.entity.ai.pathfind;

import java.util.ArrayList;

import static com.harrykay.smartgolems.common.entity.ai.pathfind.StateHelpers.*;

public class Action {
    public Position move;
    public boolean placedScaffold;

    public Action(Position position, boolean placedScaffold) {
        move = new Position(position);
        this.placedScaffold = placedScaffold;
    }

    public static ArrayList<Action> getActions(State state) {
        ArrayList<Action> traversals = new ArrayList<>();

        for (Move.Directions direction : Move.Directions.values()) {
            Position newPosition = state.entityPosition.add(Move.offsets.get(direction));
            boolean placedScaffold = false;
            if (!isWithinBounds(state, newPosition)) {
                continue;
            }
            if (state.world[newPosition.x][newPosition.y][newPosition.z] == MCObjects.BLOCK) {
                continue;
            }
            if (isOnBlock(state, newPosition) && !isEnoughRoom(state, newPosition)) {
                continue;
            }
            if (canPlaceBlockBelow(state, newPosition)) {
                if (!isEnoughRoom(state, newPosition)) {
                    continue;
                }
                placedScaffold = true;
            }

            traversals.add(new Action(newPosition, placedScaffold));
        }

        return traversals;
    }
}
