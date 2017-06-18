package com.rbase.rcommander;

import java.util.concurrent.CompletableFuture;

/**
 * A service for executing R:Commander commands.
 */
public interface RCommanderService {

    /**
     * Submit a command for execution in R:Commander.
     *
     * @param command
     *            the command to run
     * @return a future representing pending completion of the task
     */
    CompletableFuture<RCommanderResult> submit(String command);

    /**
     * Submit a command for execution in R:Commander that will be terminated
     * after the specified timeout.
     *
     * @param command
     *            the command to run
     * @param timeoutSeconds
     *            the maximum time to wait in seconds
     * @return a future representing pending completion of the task
     */
    CompletableFuture<RCommanderResult> submit(String command, long timeoutSeconds);

    /**
     * Submit a command file for execution in R:Commander.
     *
     * @param commandPath
     *            the path of the command file to run
     * @return a future representing pending completion of the task
     */
    CompletableFuture<RCommanderResult> submitPath(String commandPath);

    /**
     * Submit a command file for execution in R:Commander that will be
     * terminated after the specified timeout.
     *
     * @param commandPath
     *            the path of the command file to run
     * @param timeoutSeconds
     *            the maximum time to wait in seconds
     * @return a future representing pending completion of the task
     */
    CompletableFuture<RCommanderResult> submitPath(String commandPath, long timeoutSeconds);

    /**
     * Shutdown the service.
     */
    void shutdown();

}
