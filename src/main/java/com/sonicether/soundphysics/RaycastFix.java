package com.sonicether.soundphysics;

import com.google.common.collect.Maps;
import com.sonicether.soundphysics.config.ConfigManager;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
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
import java.util.function.Supplier;

public class RaycastFix {

    public static long lastUpd = 0;
    public static Map<Long, ImmutableTriple<BlockState,VoxelShape, VoxelShape>> shapeCache = new Long2ObjectOpenHashMap<>(2048, 0.75f);
    // reset every tick, usually up to 2200
        // {pos, (block state, (block, fluid)) }

    // ===copied and modified===

    public static BlockHitResult fixedRaycast(RaycastContext context, BlockView world, @Nullable BlockPos ignore) {

        final Vec3d start = context.getStart();
        final Vec3d end = context.getEnd();
        return raycast(context.getStart(), context.getEnd(), context, (pos) -> {
            //SoundPhysics.t1();
            //===============================================
            if (new BlockPos(pos).equals(ignore)) return null;
            //===============================================

            BlockState bs = world.getBlockState(pos);

            if (bs.isAir() || bs.getBlock().equals(Blocks.MOVING_PISTON)) return null;
            long posl = pos.asLong();
            ImmutableTriple<BlockState,VoxelShape, VoxelShape> shapes = shapeCache.computeIfAbsent(posl, (key) -> {
                //SoundPhysics.t2();
                //SoundPhysics.t1();
                if (ConfigManager.getConfig().Misc.raytraceParticles)
                    ((World)world).addParticle(ParticleTypes.END_ROD, false, pos.getX() + 0.5d, pos.getY()+1d, pos.getZ()+0.5d, 0,0,0);
                if(bs.getBlock().equals(Blocks.MOVING_PISTON)) return null;
                return new ImmutableTriple<>(bs, bs.getCollisionShape(world, pos), context.getFluidShape(world.getFluidState(pos), world, pos));
            });
            //SoundPhysics.t2();
            //SoundPhysics.t1();
            if (shapes == null) return null;


            VoxelShape voxelShape = shapes.getMiddle();//BlockShape
            BlockHitResult blockHitResult = world.raycastBlock(start, end, pos, voxelShape, bs);
            VoxelShape voxelShape2 = shapes.getRight();//FluidShape
            BlockHitResult blockHitResult2 = voxelShape2.raycast(start, end, pos);

            //SoundPhysics.t2();
            if (blockHitResult2 == null) return blockHitResult;
            if (blockHitResult == null) return blockHitResult2;
            double d = start.squaredDistanceTo(blockHitResult.getPos());
            double e = start.squaredDistanceTo(blockHitResult2.getPos());
            return d <= e ? blockHitResult : blockHitResult2;
        }, () -> BlockHitResult.createMissed(context.getEnd(), null, new BlockPos(context.getEnd())));

    }

    static BlockHitResult raycast(Vec3d start, Vec3d end, RaycastContext context, Function<BlockPos, BlockHitResult> blockHitFactory, Supplier<BlockHitResult> missFactory) {
        if (start.equals(end)) {
            return missFactory.get();
        } else {
            double d = MathHelper.lerp(-1.0E-7D, end.x, start.x);
            double e = MathHelper.lerp(-1.0E-7D, end.y, start.y);
            double f = MathHelper.lerp(-1.0E-7D, end.z, start.z);
            double g = MathHelper.lerp(-1.0E-7D, start.x, end.x);
            double h = MathHelper.lerp(-1.0E-7D, start.y, end.y);
            double i = MathHelper.lerp(-1.0E-7D, start.z, end.z);
            int j = MathHelper.floor(g);
            int k = MathHelper.floor(h);
            int l = MathHelper.floor(i);
            BlockPos.Mutable mutable = new BlockPos.Mutable(j, k, l);
            BlockHitResult object = blockHitFactory.apply(mutable);
            if (object != null) {
                return object;
            } else {
                double m = d - g;
                double n = e - h;
                double o = f - i;
                int p = MathHelper.sign(m);
                int q = MathHelper.sign(n);
                int r = MathHelper.sign(o);
                double s = p == 0 ? 1.7976931348623157E308D : (double)p / m;
                double t = q == 0 ? 1.7976931348623157E308D : (double)q / n;
                double u = r == 0 ? 1.7976931348623157E308D : (double)r / o;
                double v = s * (p > 0 ? 1.0D - MathHelper.fractionalPart(g) : MathHelper.fractionalPart(g));
                double w = t * (q > 0 ? 1.0D - MathHelper.fractionalPart(h) : MathHelper.fractionalPart(h));
                double x = u * (r > 0 ? 1.0D - MathHelper.fractionalPart(i) : MathHelper.fractionalPart(i));

                BlockHitResult object2;
                do {
                    if (!(v <= 1.0D) && !(w <= 1.0D) && !(x <= 1.0D)) {
                        return missFactory.get();
                    }

                    if (v < w) {
                        if (v < x) {
                            j += p;
                            v += s;
                        } else {
                            l += r;
                            x += u;
                        }
                    } else if (w < x) {
                        k += q;
                        w += t;
                    } else {
                        l += r;
                        x += u;
                    }

                    object2 = blockHitFactory.apply(mutable.set(j, k, l));
                } while(object2 == null);

                return object2;
            }
        }
    }

}
