package com.rbase.rcommander.impl;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import com.rbase.rcommander.RCommanderResult;
import com.rbase.rcommander.RCommanderService;

public class RCommanderServiceImpl implements RCommanderService {

    private final ExecutorService executorService;
    private final String rCommanderPath;
    private final String connectionString;

    public RCommanderServiceImpl(
            ExecutorService executorService,
            String rCommanderPath,
            String connectionString) {
        this.executorService = executorService;
        this.rCommanderPath = rCommanderPath;
        this.connectionString = connectionString;
    }

    @Override
    public CompletableFuture<RCommanderResult> submit(String command) {
        return submitCancelable(
                new CommandCallable(connectionString, command, rCommanderPath, Optional.empty()));
    }

    @Override
    public CompletableFuture<RCommanderResult> submit(String command, long timeoutSeconds) {
        return submitCancelable(
                new CommandCallable(connectionString, command, rCommanderPath, Optional.of(timeoutSeconds)));
    }

    @Override
    public CompletableFuture<RCommanderResult> submitPath(String commandPath) {
        return submitCancelable(
                new CommandPathCallable(connectionString, commandPath, rCommanderPath, Optional.empty()));
    }

    @Override
    public CompletableFuture<RCommanderResult> submitPath(String commandPath, long timeoutSeconds) {
        return submitCancelable(
                new CommandPathCallable(connectionString, commandPath, rCommanderPath, Optional.of(timeoutSeconds)));
    }

    private <T> CompletableFuture<T> submitCancelable(Callable<T> callable) {
        return RCommanderFutures.submitCancelable(callable, executorService);
    }

}
