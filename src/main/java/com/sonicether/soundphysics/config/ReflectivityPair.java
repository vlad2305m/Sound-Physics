package com.sonicether.soundphysics.config;

public class ReflectivityPair {
    public double reflectivity;
    public String example;

    public ReflectivityPair(double r, String s){
        reflectivity = r; example = s;
    }

    public ReflectivityPair() { reflectivity = 0; example = null; }

    public double getLeft() {return reflectivity;}
    public String getRight() {return example;}
    public void setLeft(double r) {reflectivity = r;}
    public void setRight(String s) {example = s;}
}
