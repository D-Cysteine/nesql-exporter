package com.github.dcysteine.nesql.exporter.render;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RenderResult {
    public static RenderResult create(RenderJob job, int[] result) {
        return new AutoValue_RenderResult(job, result);
    }

    public abstract RenderJob job();

    /**
     * Arrays are mutable! Be sure not to mutate the value returned here!
     *
     * <p>We are providing direct access to this mutable value to avoid the overhead of making a
     * copy, and because the limited scope of this mod means that it's unlikely that we would
     * accidentally mutate this.
     */
    @SuppressWarnings("mutable")
    public abstract int[] result();
}
