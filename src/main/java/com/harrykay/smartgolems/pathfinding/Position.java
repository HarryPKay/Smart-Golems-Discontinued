package com.harrykay.smartgolems.pathfinding;

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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position position = (Position) obj;
        return position.x == x && position.y == y && position.z == z;
    }
}
