package com.harrykay.smartgolems.common.entity.ai.pathfind;

import java.util.ArrayList;
import java.util.HashMap;


// Traversing up means that a block will be placed beneath.
// Similarly, going directly down means destroy the block underneath.
public class Move {

    public static ArrayList<Directions> adjacentOffsets = new ArrayList<Directions>() {{
        adjacentOffsets = new ArrayList<>();
        //adjacentOffsets.add(Directions.UP);
        adjacentOffsets.add(Directions.DOWN);
        adjacentOffsets.add(Directions.NORTH);
        adjacentOffsets.add(Directions.EAST);
        adjacentOffsets.add(Directions.SOUTH);
        adjacentOffsets.add(Directions.WEST);
    }};
    public static HashMap<Directions, Position> offsets = new HashMap<Directions, Position>() {{
        offsets = new HashMap<>();
        put(Directions.NORTH, new Position(0, 0, -1));
        put(Directions.SOUTH, new Position(0, 0, 1));
        put(Directions.WEST, new Position(-1, 0, 0));
        put(Directions.EAST, new Position(1, 0, 0));
        //put(Directions.UP, new Position(0,1,0));
        put(Directions.DOWN, new Position(0, -1, 0));

        put(Directions.UP_NORTH, new Position(0, 1, -1));
        put(Directions.UP_SOUTH, new Position(0, 1, 1));
        put(Directions.UP_WEST, new Position(-1, 1, 0));
        put(Directions.UP_EAST, new Position(1, 1, 0));

        put(Directions.DOWN_NORTH, new Position(0, -1, -1));
        put(Directions.DOWN_SOUTH, new Position(0, -1, 1));
        put(Directions.DOWN_WEST, new Position(-1, -1, 0));
        put(Directions.DOWN_EAST, new Position(1, -1, 0));
    }};

    public enum Directions {
        /*UP,*/ DOWN, UP_NORTH, UP_EAST, UP_SOUTH, UP_WEST, DOWN_NORTH, DOWN_SOUTH, DOWN_WEST, DOWN_EAST, NORTH, EAST, SOUTH, WEST
    }
}
