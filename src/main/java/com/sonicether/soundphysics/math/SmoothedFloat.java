package com.sonicether.soundphysics.math;

import com.sonicether.soundphysics.SPMath;
import com.sonicether.soundphysics.SoundPhysics;

public class SmoothedFloat implements FloatProvider{
    private static final double LN_OF_2 = Math.log(2.0);
    private final float decayConstant;
    private final FloatProvider unsmoothed;
    private float accumulator;
    private boolean hasInitialValue;

    public SmoothedFloat(float halfLife, FloatProvider unsmoothed) {
        // Half life is measured in game ticks, or 50 milliseconds @20 TPS
        // For example, a half life of value of 60.0f is 60 ticks or 3000 milliseconds (3 seconds)

        // https://en.wikipedia.org/wiki/Exponential_decay#Measuring_rates_of_decay
        // https://en.wikipedia.org/wiki/Exponential_smoothing#Time_constant
        this.decayConstant = (float) (LN_OF_2 / halfLife);
        this.unsmoothed = unsmoothed;

        SoundPhysics.ticker.addTask(this::update);
    }

    private void update() {
        if (!hasInitialValue) {
            // There is no smoothing on the first value.
            // This is not an optimal approach to choosing the initial value:
            // https://en.wikipedia.org/wiki/Exponential_smoothing#Choosing_the_initial_smoothed_value
            //
            // However, it works well enough for now.
            accumulator = unsmoothed.getAsFloat();
            hasInitialValue = true;

            return;
        }

        // Implements the basic variant of exponential smoothing
        // https://en.wikipedia.org/wiki/Exponential_smoothing#Basic_(simple)_exponential_smoothing_(Holt_linear)

        // xₜ
        float newValue = unsmoothed.getAsFloat();

        // 𝚫t
        float tickDelta = 1.0f;

        // Compute the smoothing factor based on our
        // α = 1 - e^(-𝚫t/τ) = 1 - e^(-k𝚫t)
        float smoothingFactor = 1.0f - exponentialDecayFactor(this.decayConstant, tickDelta);

        // sₜ = αxₜ + (1 - α)sₜ₋₁
        accumulator = SPMath.lerp(accumulator, newValue, smoothingFactor);
    }

    @Override
    public float getAsFloat() {
        if (!hasInitialValue) {return unsmoothed.getAsFloat();}
        return accumulator;
    }

    private static float exponentialDecayFactor(float k, float t) {
        // https://en.wikipedia.org/wiki/Exponential_decay
        // e^(-kt)
        return (float) Math.exp(-k * t);
    }
}
