package com.harrykay.smartgolems.common.entity.ai.pathfind;

import java.util.Arrays;

public class State {
    public MCObjects[][][] world = null;
    public Position entityPosition = null;
    public int size;

    public State(MCObjects[][][] world, Position position, int size) {
        this.size = size;
        this.world = new MCObjects[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.arraycopy(world[i][j], 0, this.world[i][j], 0, size);
            }
        }
        this.entityPosition = new Position(position);
    }

    State(State state) {
        this.size = state.size;
        this.world = new MCObjects[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.arraycopy(state.world[i][j], 0, this.world[i][j], 0, size);
            }
        }
        this.entityPosition = new Position(state.entityPosition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (this.size != ((State) obj).size) {
            return false;
        }
        if (this.world == null) {
            if (other.world != null)
                return false;
        } else if (!Arrays.deepEquals(world, other.world))
            return false;

        if (this.entityPosition == null) {
            return other.entityPosition == null;
        } else return entityPosition.equals(other.entityPosition);
    }
}
