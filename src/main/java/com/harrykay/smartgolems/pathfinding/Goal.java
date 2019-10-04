package com.harrykay.smartgolems.pathfinding;

public class Goal {
    Position desiredPosition;
    Position initialPosition;

    public Goal(Position initialPosition, Position desiredPosition) {
        this.initialPosition = initialPosition;
        this.desiredPosition = desiredPosition;
    }

    public boolean reached(Position currentPosition) {
        return desiredPosition.equals(currentPosition);
    }
}
