package com.sonicether.soundphysics.performance;

import com.sonicether.soundphysics.SoundPhysics;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

import static com.sonicether.soundphysics.SoundPhysics.pC;

public class RaycastFix {

    public static long lastUpd = 0;
    public static Map<Long, ImmutableTriple<BlockState,VoxelShape, VoxelShape>> shapeCache = new Long2ObjectOpenHashMap<>(2048, 0.75f);
    // reset every tick, usually up to 2200
        // {pos, (block state, (block, fluid)) }

    // ===copied and modified===

    public static SPHitResult fixedRaycast(RaycastContext context, BlockView world, @Nullable BlockPos ignore) {

        final Vec3d start = context.getStart();
        final Vec3d end = context.getEnd();
        return raycast(context.getStart(), context.getEnd(),
                (pos) -> {
                    //SoundPhysics.t1();
                    //final long t = System.nanoTime();// rm
                    //SoundPhysics.tt.addAndGet(System.nanoTime()-t);// rm
                    //===============================================
                    if (new BlockPos(pos).equals(ignore)) return null;
                    //===============================================

                    BlockState bs = world.getBlockState(pos);//All performance is in getting air

                    if (bs.isAir() || bs.getBlock().equals(Blocks.MOVING_PISTON)) return null;
                    long posl = pos.asLong();
                    ImmutableTriple<BlockState, VoxelShape, VoxelShape> shapes;
                    //noinspection SynchronizeOnNonFinalField
                    synchronized (shapeCache) {
                        shapes = shapeCache.computeIfAbsent(posl, (key) -> {
                            //SoundPhysics.t2();
                            //SoundPhysics.t1();
                            if (bs.getBlock().equals(Blocks.MOVING_PISTON)) return null;
                            if (pC.dRays)
                                ((World) world).addParticle(ParticleTypes.END_ROD, false, pos.getX() + 0.5d, pos.getY() + 1d, pos.getZ() + 0.5d, 0, 0, 0);
                            return new ImmutableTriple<>(bs, bs.getCollisionShape(world, pos), context.getFluidShape(world.getFluidState(pos), world, pos));
                        });
                    }

                    //SoundPhysics.t1();


                    if (shapes == null) return null;
                    VoxelShape voxelShape = shapes.getMiddle();//BlockShape
                    SPHitResult blockHitResult = SPHitResult.get(world.raycastBlock(start, end, pos, voxelShape, bs), bs);
                    VoxelShape voxelShape2 = shapes.getRight();//FluidShape
                    SPHitResult blockHitResult2 = SPHitResult.get(voxelShape2.raycast(start, end, pos), bs);

                    //SoundPhysics.t2();
                    if (blockHitResult2 == null) return blockHitResult;
                    if (blockHitResult == null) return blockHitResult2;
                    double d = start.squaredDistanceTo(blockHitResult.getPos());
                    double e = start.squaredDistanceTo(blockHitResult2.getPos());
                    return d <= e ? blockHitResult : blockHitResult2;
                });

    }

    static SPHitResult raycast(Vec3d start, Vec3d end, Function<BlockPos, SPHitResult> blockHitFactory) {
        if (start.equals(end)) {
            return SPHitResult.createMissed(end, null, new BlockPos(end));
        } else {
            double xe1 = MathHelper.lerp(-1.0E-7D, end.x, start.x); // x end v1.1
            double ye1 = MathHelper.lerp(-1.0E-7D, end.y, start.y);
            double ze1 = MathHelper.lerp(-1.0E-7D, end.z, start.z);
            double xs1 = MathHelper.lerp(-1.0E-7D, start.x, end.x); // x start v1.1
            double ys1 = MathHelper.lerp(-1.0E-7D, start.y, end.y);
            double zs1 = MathHelper.lerp(-1.0E-7D, start.z, end.z);
            int xbs1 = MathHelper.floor(xs1); // x blockPos start v1.1
            int ybs1 = MathHelper.floor(ys1);
            int zbs1 = MathHelper.floor(zs1);
            BlockPos.Mutable blockPosStart1 = new BlockPos.Mutable(xbs1, ybs1, zbs1);
            SPHitResult hitResultStart = blockHitFactory.apply(blockPosStart1);
            if (hitResultStart != null) {
                return hitResultStart;
            } else {
                double dx = xe1 - xs1;
                double dy = ye1 - ys1;
                double dz = ze1 - zs1;
                int dirx = MathHelper.sign(dx);
                int diry = MathHelper.sign(dy);
                int dirz = MathHelper.sign(dz);
                double rdx = dirx == 0 ? 1.7976931348623157E308D : (double)dirx / dx; // 1/dx
                double rdy = diry == 0 ? 1.7976931348623157E308D : (double)diry / dy;
                double rdz = dirz == 0 ? 1.7976931348623157E308D : (double)dirz / dz;
                double rxs = rdx * (dirx > 0 ? 1.0D - MathHelper.fractionalPart(xs1) : MathHelper.fractionalPart(xs1)); // relative to blockPos start
                double rys = rdy * (diry > 0 ? 1.0D - MathHelper.fractionalPart(ys1) : MathHelper.fractionalPart(ys1));
                double rzs = rdz * (dirz > 0 ? 1.0D - MathHelper.fractionalPart(zs1) : MathHelper.fractionalPart(zs1));

                SPHitResult object2;
                do {
                    if (!(rxs <= 1.0D) && !(rys <= 1.0D) && !(rzs <= 1.0D)) {
                        return SPHitResult.createMissed(end, null, new BlockPos(end));
                    }

                    if (rxs < rys) {
                        if (rxs < rzs) {
                            xbs1 += dirx;
                            rxs += rdx;
                        } else {
                            zbs1 += dirz;
                            rzs += rdz;
                        }
                    } else if (rys < rzs) {
                        ybs1 += diry;
                        rys += rdy;
                    } else {
                        zbs1 += dirz;
                        rzs += rdz;
                    }

                    object2 = blockHitFactory.apply(blockPosStart1.set(xbs1, ybs1, zbs1));
                } while(object2 == null);

                return object2;
            }
        }
    }

}
