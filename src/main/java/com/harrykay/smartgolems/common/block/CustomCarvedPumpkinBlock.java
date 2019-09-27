package com.harrykay.smartgolems.common.block;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockMaterialMatcher;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.function.Predicate;

import static com.harrykay.smartgolems.SmartGolems.assignGolemToPlayer;
import static com.harrykay.smartgolems.common.init.ModEntities.SMART_GOLEM;

public class CustomCarvedPumpkinBlock extends HorizontalBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private static final Predicate<BlockState> IS_PUMPKIN = (blockState) -> {
        return blockState != null && blockState.getBlock().getRegistryName().toString().equals("smartgolems:custom_carved_pumpkin");
    };
    private BlockPattern smartGolemBlockPattern;

    public CustomCarvedPumpkinBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            this.trySpawnGolem(worldIn, pos);
        }
    }

    private void trySpawnGolem(World world, BlockPos blockPos) {

        BlockPattern.PatternHelper blockpattern$patternhelper = this.getSmartGolemBlockPattern().match(world, blockPos);
        if (blockpattern$patternhelper != null) {
            for (int j = 0; j < this.getSmartGolemBlockPattern().getPalmLength(); ++j) {
                for (int k = 0; k < this.getSmartGolemBlockPattern().getThumbLength(); ++k) {
                    CachedBlockInfo cachedblockinfo2 = blockpattern$patternhelper.translateOffset(j, k, 0);
                    world.setBlockState(cachedblockinfo2.getPos(), Blocks.AIR.getDefaultState(), 2);
                    world.playEvent(2001, cachedblockinfo2.getPos(), Block.getStateId(cachedblockinfo2.getBlockState()));
                }
            }

            BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
            SmartGolemEntity smartGolemEntity = new SmartGolemEntity(SMART_GOLEM, world);
            smartGolemEntity.setPlayerCreated(true);
            smartGolemEntity.setLocationAndAngles((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.05D, (double) blockpos.getZ() + 0.5D, 0.0F, 0.0F);
            smartGolemEntity.setCustomName(new StringTextComponent(SmartGolems.golems.size() + ""));
            smartGolemEntity.setCustomNameVisible(true);
            //TODO: check that the golem can also be assigned.
            assignGolemToPlayer(smartGolemEntity, world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            world.addEntity(smartGolemEntity);

            for (ServerPlayerEntity serverplayerentity1 : world.getEntitiesWithinAABB(ServerPlayerEntity.class, smartGolemEntity.getBoundingBox().grow(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity1, smartGolemEntity);
            }

            for (int i1 = 0; i1 < this.getSmartGolemBlockPattern().getPalmLength(); ++i1) {
                for (int j1 = 0; j1 < this.getSmartGolemBlockPattern().getThumbLength(); ++j1) {
                    CachedBlockInfo cachedblockinfo1 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
                    world.notifyNeighbors(cachedblockinfo1.getPos(), Blocks.AIR);
                }
            }
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    private BlockPattern getSmartGolemBlockPattern() {
        if (this.smartGolemBlockPattern == null) {
            this.smartGolemBlockPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~").where('^', CachedBlockInfo.hasState(IS_PUMPKIN)).where('#', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.smartGolemBlockPattern;
    }
}