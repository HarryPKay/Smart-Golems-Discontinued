package com.harrykay.smartgolems.common.entity.ai.pathfind;

import static com.harrykay.smartgolems.MathHelpers.manhattenDistance;

public class StateHelpers {

//    //world, initial position, desired position
//    public State createState()
//    {
//        // set size
//        // scan in the blocks preset
//        // set height dimensions
//        return new State();
//    }

    public static boolean isEnoughRoom(State state, Position position) {
        if (Constants.entityHeight + position.y >= state.size) {
            return false;
        }

        for (int i = 0; i < Constants.entityHeight; ++i) {
            if (state.world[position.x][position.y + i][position.z] == MCObjects.BLOCK
                    || state.world[position.x][position.y + i][position.z] == MCObjects.SCAFFOLDING_BLOCK) {
                return false;
            }
        }
        return true;
    }

    public static int heuristic(Position current, Position desired) {
        return manhattenDistance(current.x, current.y, current.z, desired.x, desired.y, desired.z);
    }

    public static void setAction(State state, Action action) {
        state.entityPosition = new Position(action.move);
        if (state.world[action.move.x][action.move.y][action.move.z] == MCObjects.SCAFFOLDING_BLOCK) {
            state.world[action.move.x][action.move.y][action.move.z] = MCObjects.AIR;
        } else {
            Position newPosition = action.move.add(Move.offsets.get(Move.Directions.DOWN));
            state.world[newPosition.x][newPosition.y][newPosition.z] = MCObjects.SCAFFOLDING_BLOCK;
        }
    }

    public static void printState(State state) {
        for (int y = 0; y < state.size; ++y) {
            for (int z = 0; z < state.size; ++z) {
                for (int x = 0; x < state.size; ++x) {

                    if (state.entityPosition.equals(new Position(x, y, z))) {
                        System.out.print("x ");
                    } else {
                        System.out.print(state.world[x][y][z].ordinal() + " ");
                    }
                }
                System.out.println();
            }
            System.out.println("y = " + y + "\n");
        }
    }

    public static void clearState(MCObjects[][][] state, int size) {
        for (int y = 0; y < size; ++y) {
            for (int z = 0; z < size; ++z) {
                for (int x = 0; x < size; ++x) {
                    state[x][y][z] = MCObjects.AIR;
                }
            }
        }
    }

    public static void makeFloor(MCObjects[][][] state, int size, int y) {
        for (int z = 0; z < size; ++z) {
            for (int x = 0; x < size; ++x) {
                state[x][y][z] = MCObjects.BLOCK;
            }
        }
    }

    public static boolean hasAdjacentBlock(State state, Position position) {
        for (Move.Directions direction : Move.adjacentOffsets) {
            Position newPosition = position.add(Move.offsets.get(direction));
            if (isWithinBounds(state, newPosition) && state.world[newPosition.x][newPosition.y][newPosition.z] != MCObjects.AIR) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnBlock(State state, Position position) {
        Position newPosition = position.add(Move.offsets.get(Move.Directions.DOWN));
        return isWithinBounds(state, newPosition) &&
                (state.world[newPosition.x][newPosition.y][newPosition.z] == MCObjects.BLOCK
                        || state.world[newPosition.x][newPosition.y][newPosition.z] == MCObjects.SCAFFOLDING_BLOCK);
    }

    public static boolean canPlaceBlockBelow(State state, Position position) {
        Position newPosition = position.add(Move.offsets.get(Move.Directions.DOWN));

        return isWithinBounds(state, newPosition) && hasAdjacentBlock(state, position) && state.world[newPosition.x][newPosition.y][newPosition.z] == MCObjects.AIR;
    }

    public static boolean isWithinBounds(State state, Position position) {
        return position.x >= 0
                && position.y >= 0
                && position.z >= 0
                && position.x < state.size
                && position.y < state.size
                && position.z < state.size;
    }
}
