package com.harrykay.smartgolems.common.entity.ai.pathfind;

public class Position {

    public int x;
    public int y;
    public int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Position(Position position) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
    }

    public Position add(Position position) {
        return new Position(x + position.x, y + position.y, z + position.z);
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj)
            return true;
        // null check
        if (obj == null)
            return false;
        // type check and cast
        if (getClass() != obj.getClass())
            return false;
        Position position = (Position) obj;
        // field comparison
        return position.x == x && position.y == y && position.z == z;
    }
}
