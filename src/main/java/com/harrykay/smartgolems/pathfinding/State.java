package com.harrykay.smartgolems.pathfinding;

import java.util.Arrays;

public class State {


    // Optimize, maybe also have the algorithm use blockpos instead.
//    public PriorityQueue<Action> createScaffoldPlan(BlockPos initialBlockPos, BlockPos goalBlockPos, int dimensionIncrease)
//    {
//        Position initialPosition = new Position(0, 0, 0);
//        Position goalPosition = new Position(
//                Math.abs(goalBlockPos.getX() - initialBlockPos.getX()),
//                Math.abs(goalBlockPos.getY() - initialBlockPos.getY()),
//                Math.abs(goalBlockPos.getZ() - initialBlockPos.getZ()));
//
//        int xDimension = goalPosition.x + dimensionIncrease;
//        int yDimension = goalPosition.y + dimensionIncrease + entityHeight;
//        int zDimension = goalPosition.z + dimensionIncrease;
//
//        MCObjects[][][] stateWorld = new MCObjects[xDimension][yDimension][zDimension];
//        for (int x = 0; x < xDimension; ++x)
//        {
//            for (int y = 0; y < yDimension; ++y)
//            {
//                for (int z = 0; z < zDimension; ++z)
//                {
//                    if (world.getBlockState(new BlockPos(x + initialBlockPos.getX(), y + initialBlockPos.getY(), z + initialBlockPos.getZ())).getBlock() == Blocks.AIR)
//                    {
//                        stateWorld[x][y][z] = MCObjects.AIR;
//                    }
//                    else {
//                        stateWorld[x][y][z] = MCObjects.BLOCK;
//                    }
//                }
//            }
//        }
//
//        State initialState = new State(stateWorld, initialPosition, xDimension, yDimension, zDimension);
//        Goal goal = new Goal(initialPosition, goalPosition);
//        ArrayList<com.harrykay.smartgolems.pathfinding.Action> solution = ScaffoldNavigator.findSolution(goal, initialState);
//
//        for (com.harrykay.smartgolems.pathfinding.Action action : solution)
//        {
//            if (action.placedScaffold)
//            {
//                BlockPos blockPos = new BlockPos(
//                        initialBlockPos.getX() + action.move.x,
//                        initialBlockPos.getY() + action.move.y -1,
//                        initialBlockPos.getZ() + action.move.z);
//                actions.add(new Action((int)priorityCounter++, Action.ActionType.PLACE_BLOCK, blockPos, Blocks.DIRT, null, null));
//            }
//            else
//            {
//                BlockPos blockPos = new BlockPos(
//                        initialBlockPos.getX() + action.move.x,
//                        initialBlockPos.getY() + action.move.y,
//                        initialBlockPos.getZ() + action.move.z);
//                actions.add(new Action((int)priorityCounter++, Action.ActionType.MOVE_TO, blockPos, null, null, null));
//            }
//        }
//        return null;
//    }


    public MCObjects[][][] world = null;
    public Position entityPosition = null;
    public int xSize;
    public int ySize;
    public int zSize;

    public State(MCObjects[][][] world, Position position, int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.world = new MCObjects[xSize][ySize][zSize];
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                System.arraycopy(world[x][y], 0, this.world[x][y], 0, zSize);
            }
        }

        this.entityPosition = new Position(position);
    }

    State(State state) {
        this.xSize = state.xSize;
        this.ySize = state.ySize;
        this.zSize = state.zSize;
        this.world = new MCObjects[xSize][ySize][zSize];
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                System.arraycopy(state.world[i][j], 0, this.world[i][j], 0, xSize);
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
        if (this.xSize != ((State) obj).xSize || this.ySize != ((State) obj).ySize || this.zSize != ((State) obj).zSize) {
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
