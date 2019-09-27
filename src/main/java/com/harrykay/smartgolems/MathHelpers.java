package com.harrykay.smartgolems;

import net.minecraft.util.math.BlockPos;

import static java.lang.Math.pow;

public class MathHelpers {

    //TODO use sqrt of this instead.
    public static double euclideanDistanceSq(BlockPos blockPos1, BlockPos blockPos2) {
        return pow(blockPos1.getX() - blockPos2.getX(), 2) + pow(blockPos1.getY() - blockPos2.getY(), 2) + pow(blockPos1.getZ() - blockPos2.getZ(), 2);
    }

    public static boolean isNearAbove(BlockPos bottom, BlockPos top) {
        double xBottom = Math.floor(bottom.getX()) + 0.5D;
        double xTop = Math.floor(top.getX()) + 0.5D;
        double yTop = Math.floor(top.getY());
        double yBottom = Math.floor(top.getY());
        double zBottom = Math.floor(bottom.getZ()) + 0.5D;
        double zTop = Math.floor(top.getZ()) + 0.5D;


        return euclideanDistanceSq(new BlockPos(xBottom, 0, zBottom), new BlockPos(xTop, 0, zTop)) <= 1 && yBottom <= yTop;
    }

//    public static boolean isNearAbove(BlockPos bottom, BlockPos top, double range)
//    {
//
//    }
}
