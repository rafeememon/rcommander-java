package com.rbase.rcommander.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class RCommanderFutures {

    private RCommanderFutures() {
        // utility class
    }

    static <T> CompletableFuture<T> submitCancelable(
            Callable<T> callable,
            ExecutorService executorService) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Future<?> internalFuture = executorService.submit(() -> {
            try {
                future.complete(callable.call());
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
                Thread.currentThread().interrupt();
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        future.whenComplete((result, throwable) -> {
            if (future.isCancelled()) {
                internalFuture.cancel(true);
            }
        });
        return future;
    }

}
