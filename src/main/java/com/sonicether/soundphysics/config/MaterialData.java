package com.sonicether.soundphysics.config;

@SuppressWarnings("CanBeFinal")
public class MaterialData {
    public double reflectivity;
    public double absorption;
    public String example;

    public MaterialData(double r, double a, String s){
        reflectivity = r; absorption = a; example = s;
    }

    public MaterialData(double r, double a){
        reflectivity = r; absorption = a; example = null;
    }

    @SuppressWarnings("unused")
    public MaterialData() { reflectivity = 0; absorption = 1; example = ""; }

    public double getReflectivity() {return reflectivity;}
    public double getAbsorption() {return absorption;}
    public String getExample() {return example;}
}