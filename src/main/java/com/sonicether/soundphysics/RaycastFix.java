package com.sonicether.soundphysics;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RaycastFix {

    // ===copied and modified===
    // we want to only pick up the block's sides. so I just return when I cross a side
    public static BlockHitResult fixedRaycast(RaycastContext context, BlockView world, @Nullable BlockPos ignore) {
        return (BlockHitResult) BlockView.raycast(context.getStart(), context.getEnd(), context, (contextx, pos) -> {
            BlockState blockState = world.getBlockState(pos);
            FluidState fluidState = world.getFluidState(pos);
            Vec3d vec3d = contextx.getStart();
            Vec3d vec3d2 = contextx.getEnd();
            VoxelShape voxelShape = contextx.getBlockShape(blockState, world, pos);
            BlockHitResult blockHitResult = world.raycastBlock(vec3d, vec3d2, pos, voxelShape, blockState);
            //=============================================================================================

            if (blockHitResult == null || blockHitResult.getBlockPos().equals(ignore)) blockHitResult = null;

            //=============================================================================================
            VoxelShape voxelShape2 = contextx.getFluidShape(fluidState, world, pos);
            BlockHitResult blockHitResult2 = voxelShape2.raycast(vec3d, vec3d2, pos);
            double d = blockHitResult == null ? 1.7976931348623157E308D : contextx.getStart().squaredDistanceTo(blockHitResult.getPos());
            double e = blockHitResult2 == null ? 1.7976931348623157E308D : contextx.getStart().squaredDistanceTo(blockHitResult2.getPos());
            return d <= e ? blockHitResult : blockHitResult2;
        }, (contextx) -> {
            Vec3d vec3d = contextx.getStart().subtract(contextx.getEnd());
            return BlockHitResult.createMissed(contextx.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), new BlockPos(contextx.getEnd()));
        });
    }

}
