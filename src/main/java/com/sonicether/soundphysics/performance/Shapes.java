package com.sonicether.soundphysics.performance;

import net.minecraft.util.shape.VoxelShape;

public class Shapes {
    public final VoxelShape solid;
    public final VoxelShape liquid;

    public Shapes(VoxelShape solid, VoxelShape liquid) {
        this.solid = solid;
        this.liquid = liquid;
    }

    public VoxelShape getSolid() {
        return this.solid;
    }

    public VoxelShape getLiquid() {
        return this.liquid;
    }
}
