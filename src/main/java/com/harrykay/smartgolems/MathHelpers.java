package com.harrykay.smartgolems;

import net.minecraft.util.math.BlockPos;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MathHelpers {

    //TODO use sqrt of this instead.
    public static double euclideanDistanceSq(BlockPos blockPos1, BlockPos blockPos2) {
        return pow(blockPos1.getX() - blockPos2.getX(), 2) + pow(blockPos1.getY() - blockPos2.getY(), 2) + pow(blockPos1.getZ() - blockPos2.getZ(), 2);
    }

    public static double euclideanDistance(BlockPos blockPos1, BlockPos blockPos2) {
        return sqrt(euclideanDistanceSq(blockPos1, blockPos2));
    }

    public static int manhattenDistance(int x, int y, int z, int x2, int y2, int z2) {
        return Math.abs(x - x2) + Math.abs(y - y2) + Math.abs(z - z2);
    }

//    public static BlockPos normalizedDifference(BlockPos blockPos1, BlockPos blockPos2)
//    {
//        double x
//
//
//        blockPos1.getY() - blockPos2.getY();
//    }
}
