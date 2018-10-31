package com.haleysoftware.popularmovies.database;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by haleysoft on 10/30/18.
 * <p>
 * Manages database changes on a single Thread to avoid race conditions.
 */
public class MovieExecutor {

    private static final Object LOCK = new Object();
    private static MovieExecutor executorInstance;
    private final Executor diskIO;

    /**
     * The executor used to make database changes.
     *
     * @param diskIO The single thread to make database changes.
     */
    private MovieExecutor(Executor diskIO) {
        this.diskIO = diskIO;
    }

    /**
     * Returns an instance of the executor
     *
     * @return the executor used to get make database changes.
     */
    public static MovieExecutor getInstance() {
        if (executorInstance == null) {
            synchronized (LOCK) {
                executorInstance = new MovieExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return executorInstance;
    }

    /**
     * Gets the thread instance.
     *
     * @return The thread to make database changes on.
     */
    public Executor diskIO() {
        return diskIO;
    }
}
