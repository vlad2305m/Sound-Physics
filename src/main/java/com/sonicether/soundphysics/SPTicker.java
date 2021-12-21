package com.sonicether.soundphysics;

import java.util.ArrayList;
import java.util.List;

public class SPTicker {
    private final List<Runnable> tasks;

    public SPTicker() {tasks = new ArrayList<>();}
    public void addTask(Runnable onNewFrame) {tasks.add(onNewFrame);}
    public void onTick() {tasks.forEach(Runnable::run);}
}
