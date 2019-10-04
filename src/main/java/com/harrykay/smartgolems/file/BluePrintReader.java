package com.harrykay.smartgolems.file;

import com.harrykay.smartgolems.common.entity.ai.goal.Action;
import com.harrykay.smartgolems.common.entity.ai.goal.ActionComparator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class BluePrintReader {

    public static final String path = "C:\\Users\\user\\AppData\\Roaming\\.minecraft\\assets\\";

    public static PriorityQueue<Action> read(String fileName) {

        PriorityQueue<Action> actions = new PriorityQueue<>(new ActionComparator());

        //TODO: look into json
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + fileName));
            String line = reader.readLine();
            double y = 0;
            double z = 0;
            int priority = 0;
            boolean isReadingY = false;
            boolean isMapping = false;
            Direction direction = Direction.NORTH;
            HashMap<String, Block> numberToBlock = new HashMap<>();

            Rotation rotation = Rotation.NONE;
            String modid = "minecraft";
            while (line != null) {
                if (line.startsWith("y")) {
                    isReadingY = true;
                    line = reader.readLine();
                    continue;
                }

                if (line.startsWith("m")) {
                    isMapping = !isMapping;
                    line = reader.readLine();
                    continue;
                }

                if (isMapping) {
                    StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                    String number = stringTokenizer.nextToken();
                    String blockName = stringTokenizer.nextToken();
                    for (final Block block : ForgeRegistries.BLOCKS.getValues()) {
                        if (Objects.requireNonNull(block.getRegistryName()).toString().equals(modid + ":" + blockName)) {
                            System.out.println("mapping " + number + " to " + block.getRegistryName());
                            numberToBlock.put(number, block);
                        }
                    }
                    line = reader.readLine();
                    continue;
                }

                if (isReadingY) {
                    isReadingY = false;
                    y = Integer.parseInt(line) + 4;
                    z = 0;
                    line = reader.readLine();
                    continue;
                }

                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                for (int x = 0; stringTokenizer.hasMoreTokens(); ++x) {
                    String token = stringTokenizer.nextToken();
                    switch (token) {
                        case "n":
                            --x;
                            rotation = Rotation.NONE;
                            break;
                        case "e":
                            --x;
                            rotation = Rotation.CLOCKWISE_90;
                            break;
                        case "w":
                            --x;
                            rotation = Rotation.COUNTERCLOCKWISE_90;
                            break;
                        case "s":
                            --x;
                            rotation = Rotation.CLOCKWISE_180;
                            break;
                        default:
                            if (numberToBlock.containsKey(token) && numberToBlock.get(token) != Blocks.AIR) {
                                //System.out.println("adding " + token);
                                actions.add(new Action(priority, new BlockPos(x, y, z), numberToBlock.get(token), rotation, direction));
                            } else if (numberToBlock.containsKey(token) && numberToBlock.get(token) == Blocks.AIR) {
                                // do nothing
                            } else {
                                System.out.println("Unrecognized value given: " + token);
                            }
                    }
                }
                ++z;
                ++priority;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            //System.out.println("test");
            e.printStackTrace();
        }

        return actions;
    }
}
