package com.github.dcysteine.nesql.exporter.util.render;

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
    }

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

    /**
     * This probably doesn't actually need to be concurrent, as currently it will not be read until
     * all results have been written to it.
     */
    private final ConcurrentLinkedQueue<RenderResult> resultQueue = new ConcurrentLinkedQueue<>();

    public RendererState getRendererState() {
        return rendererState;
    }

    public void setRendererState(RendererState state) {
        rendererState = state;
    }

    public synchronized void waitUntilJobsComplete() throws InterruptedException {
        wait();
    }

    public synchronized void notifyJobsCompleted() {
        notifyAll();
    }

    public void addJob(RenderJob job) {
        jobQueue.add(job);
    }

    public void addAllJob(Collection<RenderJob> jobs) {
        jobQueue.addAll(jobs);
    }

    public boolean noJobsRemaining() {
        return jobQueue.peek() == null;
    }

    public Optional<RenderJob> getJob() {
        return Optional.ofNullable(jobQueue.poll());
    }

    public void addResult(RenderResult result) {
        resultQueue.add(result);
    }

    public boolean noResultsRemaining() {
        return resultQueue.peek() == null;
    }

    public Optional<RenderResult> getResult() {
        return Optional.ofNullable(resultQueue.poll());
    }
}
