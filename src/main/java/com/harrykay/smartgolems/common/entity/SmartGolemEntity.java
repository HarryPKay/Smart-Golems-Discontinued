package com.harrykay.smartgolems.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SmartGolemEntity extends IronGolemEntity {

    private int placeBlockTimer = 10;
    private int goToPathTimer = 10;

//    private ArrayList<BlockPos> positions = new ArrayList<>();
//    private ArrayList<Block> currentState = new ArrayList<>();
//    private ArrayList<Block> desiredState = new ArrayList<>();
//
//    private static class BuildAction
//    {
//        public BuildAction(BlockPos blockPos, Block block)
//        {
//            this.blockPos = blockPos;
//            this.block = block;
//        }
//
//        public BlockPos blockPos;
//        public Block block;
//    }

    //private ArrayList<BuildAction> placements = new ArrayList<>();


    //HashMap<BlockPos, Block> placements = new HashMap<>();

    private boolean isBuilding = false;
    private int followTimer;

    public SmartGolemEntity(EntityType<? extends IronGolemEntity> type, World world) {
        super(type, world);

//        for (int i = 0; i < 9; ++i)
//        {
//            desiredState.add(Blocks.AIR);
//        }
    }

    @Override
    protected void registerGoals() {

    }

    protected void registerAttributes() {
        super.registerAttributes();
        setPlayerCreated(true);
    }

    private void aStarSearch() {

    }

    private void store() {

    }

//    // Timer and check if has path?
//    public void scanEnvironment()
//    {
//        currentState = new ArrayList<>();
//        positions = new ArrayList<>();
//        for (int x =  -1; x <= 1; ++x)
//        {
//            for (int z = -1; z <= 1; ++z)
//            {
//                BlockPos blockPos = new BlockPos(posX + x, posY - 1, posZ + z);
//                positions.add(blockPos);
//                currentState.add(world.getBlockState(blockPos).getBlock());
////                positions.set((x + 1) * 3 + ((z + 1) % 3), blockPos);
////                currentState.set((x + 1) * 3 + ((z + 1) % 3), world.getBlockState(blockPos).getBlock());
//                //System.out.println(world.getBlockState(blockPos).getBlock().getRegistryName());
//            }
//        }
//
//        // Get what block is underneath that entity
//        //BlockPos blockPos = new BlockPos(event.getPlayer().posX, event.getPlayer().posY - 1, event.getPlayer().posZ);
//        //System.out.println(event.getPlayer().world.getBlockState(blockPos).getBlock().getRegistryName());
//        //event.getPlayer().world.getBlockState(blockPos).getBlock().asItem();
//
//
//        isBuilding = false;
//        if (currentState.size() < 9 || desiredState.size() < 9 || positions.size() < 9)
//        {
//            System.out.println(currentState.size() );
//            System.out.println(desiredState.size());
//            System.out.println(positions.size());
//        }
//
//        placements = new ArrayList<>();
//        //placements = new HashMap<>();
//        for (int i = 0; i < desiredState.size(); ++i)
//        {
//
//            System.out.println(currentState.get(i).getRegistryName() + " " +  desiredState.get(i).getRegistryName());
//            if (currentState.get(i).getRegistryName() != desiredState.get(i).getRegistryName() )
//            {
//                isBuilding = true;
//                placements.add(new BuildAction(positions.get(i), desiredState.get(i)));
//                //placements.put(positions.get(i), desiredState.get(i));
//                //world.destroyBlock(positions.get(i),true);
//
//            }
//        }
//    }

    private void pickUp() {

    }

    private boolean placeBlock(BlockPos blockPos, Block block) {
//        if (!isBuilding)
//        {
//            return false;
//        }
//
//        if (posX == blockPos.getX() - 1 && posY == blockPos.getY() && posZ == blockPos.getZ())
//        {
//            System.out.println("Placing block");
//            BlockState state = block.getDefaultState();
//            swingArm(getActiveHand());
//            world.destroyBlock(blockPos,true);
//            world.setBlockState(blockPos, state);
//        }
//
//        //if (posX == blockPos.getX() && posX)
//
//        // Retry count?
//        if (!getNavigator().tryMoveToXYZ(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ(), 2D))
//        {
//            System.out.println("Can't set path: placeblock()");
//        }
        return true;
    }

    @Override
    public void livingTick() {
        super.livingTick();

//        if (this > 0) {
//            --this.attackTimer;
//        }


//        if (!placements.isEmpty())
//        {
//            if (placeBlock(placements.get(0).blockPos, placements.get(0).block))
//            {
//                placements.remove(0);
//            }
//        }


//        PlayerEntity player =  world.getClosestPlayer(this.posX, this.posY, this.posZ, 100,true);
//
//        if (player != null)
//        {
//            if (this.getNavigator().tryMoveToXYZ(player.posX, player.posY, player.posZ, 2))
//            {
////                System.out.println(getCustomName() + " is chasing " + player.posX + " "  + player.posY + " " + player.posZ);
////                System.out.println(getType());
//            }
//            else
//            {
////                System.out.println(getCustomName() + " is not chasing");
//            }
//        }
    }

    // Has inventory?

    // Lookahead

    // Actions:
    // Place block
    // Retrieve block

}
