package com.sonicether.soundphysics;

public class SPMath {
    //<editor-fold desc="IFD clamp();">
    public static int clamp(int a, int max, int min) {return Math.min(max, Math.max(min, a)); }
    public static float clamp(float a, float max, float min) {return Math.min(max, Math.max(min, a)); }
    public static double clamp(double a, double max, double min) {return Math.min(max, Math.max(min, a)); }
    //</editor-fold>
    //<editor-fold desc="FD lerp();">
    public static float lerp(float a, float b, float f) {return a + clamp(f, 1.0f, 0.0f) * (b - a);}
    public static double lerp(double a, double b, double f) {return a + clamp(f, 1.0d, 0.0d) * (b - a);}
    //</editor-fold>
}
