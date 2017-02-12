package com.rbase.rcommander;

import java.util.concurrent.CompletableFuture;

public interface RCommanderService {

    CompletableFuture<RCommanderResult> submit(String command);

    CompletableFuture<RCommanderResult> submit(String command, long timeoutSeconds);

    CompletableFuture<RCommanderResult> submitPath(String commandPath);

    CompletableFuture<RCommanderResult> submitPath(String commandPath, long timeoutSeconds);

}
