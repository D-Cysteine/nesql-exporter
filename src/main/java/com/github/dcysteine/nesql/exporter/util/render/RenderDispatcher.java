package com.github.dcysteine.nesql.exporter.util.render;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Singleton class which handles inter-thread communication and dispatching of render jobs.
 *
 * <p>Unfortunately, we cannot render in our own thread because it does not have an OpenGL context.
 * So we must do all of our rendering in the client thread.
 *
 * <p>This class's singleton instance will also be used as a lock. The contract for this is:
 * <ul>
 *     <li>The export thread will call {@link Object#wait()} when it wants to wait for render
 *         jobs to complete
 *     <li>The client thread will call {@link Object#notifyAll()} when all render jobs have
 *         completed
 * </ul>
 */
public enum RenderDispatcher {
    /** Singleton class, enforced by enum. */
    INSTANCE;

    public enum RendererState {
        /** Renderer is newly constructed or destroyed. */
        UNINITIALIZED,

        /** Exporter thread has requested renderer initialization. */
        INITIALIZING,

        /** Renderer has initialized itself. */
        INITIALIZED,

        /** Exporter thread has requested renderer destruction. */
        DESTROYING,

        /** We transition to this state if something went wrong during rendering. */
        ERROR,
    }

    /** Map of valid state transitions. Used for validation. */
    private static final ImmutableSetMultimap<RendererState, RendererState> STATE_TRANSITIONS =
            ImmutableSetMultimap.<RendererState, RendererState>builder()
                    .putAll(RendererState.UNINITIALIZED,
                            RendererState.INITIALIZING, RendererState.ERROR)
                    .putAll(RendererState.INITIALIZING,
                            RendererState.INITIALIZED, RendererState.ERROR)
                    .putAll(RendererState.INITIALIZED,
                            RendererState.DESTROYING, RendererState.ERROR)
                    .putAll(RendererState.DESTROYING,
                            RendererState.UNINITIALIZED, RendererState.ERROR)
                    .putAll(RendererState.ERROR,
                            RendererState.UNINITIALIZED, RendererState.INITIALIZING)
                    .build();

    private static final ImmutableSet<RendererState> ACTIVE_STATES =
            ImmutableSet.of(RendererState.INITIALIZING, RendererState.INITIALIZED);

    /**
     * Used to keep track of current {@link Renderer} state, as well as ask it to initialize or
     * destroy itself.
     *
     * <p>The renderer can only be initialized or destroyed on the client thread, so we need this
     * dispatcher to handle sending state change requests from the exporter thread to the client
     * thread.
     *
     * <p>In order to avoid concurrency issues, it must be the case that each state transition can
     * only be performed by either the exporter thread or the client thread, and not both!
     */
    private RendererState rendererState = RendererState.UNINITIALIZED;

    private final ConcurrentLinkedQueue<RenderJob> jobQueue = new ConcurrentLinkedQueue<>();

    public RendererState getRendererState() {
        return rendererState;
    }

    public synchronized void setRendererState(RendererState newState) {
        if (!STATE_TRANSITIONS.get(rendererState).contains(newState)) {
            throw new RuntimeException(
                    String.format(
                            "Attempted invalid state transition: %s to %s."
                                    + "\nDid you start up another export"
                                    + " while one was still in progress?",
                            rendererState, newState));
        }

        if ((newState == RendererState.UNINITIALIZED || newState == RendererState.INITIALIZING)
                && !jobQueue.isEmpty()) {
            Logger.chatMessage(String.format(
                    EnumChatFormatting.RED + "Render dispatcher has %s leftover jobs!"
                            + "\nClearing and transitioning to state %s.",
                    jobQueue.size(), newState.name()));
            jobQueue.clear();
        }

        rendererState = newState;
    }

    public synchronized boolean isActive() {
        return ACTIVE_STATES.contains(rendererState);
    }

    public synchronized void waitUntilJobsComplete() throws InterruptedException {
        while (!jobQueue.isEmpty() && isActive()) {
            wait();
        }
    }

    /**
     * This method is kept unsynchronized since it runs on the render thread, and we want it to be
     * fast. This should be safe.
     */
    public synchronized void notifyJobsCompleted() {
        notifyAll();
    }

    public void addJob(RenderJob job) {
        jobQueue.add(job);
    }

    public void addAllJob(Collection<RenderJob> jobs) {
        jobQueue.addAll(jobs);
    }

    /**
     * This method is kept unsynchronized since it runs on the render thread, and we want it to be
     * fast. This should be safe.
     */
    public boolean noJobsRemaining() {
        return jobQueue.isEmpty();
    }

    /**
     * This method is kept unsynchronized since it runs on the render thread, and we want it to be
     * fast. This should be safe.
     */
    public Optional<RenderJob> getJob() {
        return Optional.ofNullable(jobQueue.poll());
    }
}
