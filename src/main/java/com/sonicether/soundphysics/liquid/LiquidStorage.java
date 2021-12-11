package com.sonicether.soundphysics.liquid;

import com.sonicether.soundphysics.SPLog;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Set;

/*
            !!!WIP!!!
    Data structure to detect if a desired block is the medium for sound.

    Should attach to every chunk
 */

public class LiquidStorage {
    private boolean full = false;
    private int bottom;
    private int top;
    private boolean[][] sections;
    private static boolean[] empty() {return new boolean[16*16];}

    public enum LIQUIDS {
        WATER(Set.of(Blocks.WATER, Blocks.BUBBLE_COLUMN)),
        LAVA(Set.of(Blocks.LAVA)),
        AIR(Set.of(Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR));
        final Set<Block> allowed;
        LIQUIDS(Set<Block> a){allowed = a;}
        public boolean matches(Block b){ return allowed.contains(b); }
    }

    public boolean[] getSection(int y) {
        if (!full || y > top || y < bottom) return null;
        return sections[y-bottom];
    }

    public boolean getBlock(int x, int y, int z) {
        if (!full || y > top || y < bottom) return false;
        boolean[] section = sections[y-bottom];
        if (section == null) return false;
        if (x >= 16 || x < 0 || z >= 16 || z < 0) {SPLog.logError("Coords: "+x+", "+z+" are out of bounds"); return false;}
        return section[x+(z<<4)];
    }

    public void initSection(int y) {
        if (!full) { sections = new boolean[][]{empty()}; bottom = y; top = y; full = true; }
        else if (y < bottom) { sections = ArrayUtils.addAll(new boolean[bottom-y][], sections); sections[0]=empty(); bottom = y; }
        else if (y > top) { sections = ArrayUtils.addAll(sections, new boolean[y-top][]); sections[y-bottom]=empty(); top = y; }
        else sections[y-bottom]=empty();
    }

    public void setSection(int y, boolean[] section) {
        if (!full || y > top || y < bottom) initSection(y);
        sections[y-bottom] = section;
    }

    public void setBlock(int x, int y, int z, boolean block) {
        if (x >= 16 || x < 0 || z >= 16 || z < 0) SPLog.logError("Coords: "+x+", "+z+" are out of bounds");
    }
}
