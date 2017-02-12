package com.rbase.rcommander.impl;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.rbase.rcommander.RCommanderException;
import com.rbase.rcommander.RCommanderResult;

public class CommandPathCallable implements Callable<RCommanderResult> {

    private final String connectionString;
    private final String commandPath;
    private final String rCommanderPath;
    private final Optional<Long> timeoutSeconds;

    public CommandPathCallable(
            String connectionString,
            String commandPath,
            String rCommanderPath,
            Optional<Long> timeoutSeconds) {
        this.connectionString = connectionString;
        this.commandPath = commandPath;
        this.rCommanderPath = rCommanderPath;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public RCommanderResult call() throws IOException, InterruptedException, TimeoutException {
        Process process = null;
        try {
            process = new ProcessBuilder(rCommanderPath, commandPath, "DB " + connectionString).start();
            waitFor(process);
            checkExitValue(process);
            return getResult(process);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private void waitFor(Process process) throws InterruptedException, TimeoutException {
        if (timeoutSeconds.isPresent()) {
            boolean exited = process.waitFor(timeoutSeconds.get(), TimeUnit.SECONDS);
            if (!exited) {
                throw new TimeoutException();
            }
        } else {
            process.waitFor();
        }
    }

    private static void checkExitValue(Process process) throws IOException {
        int exitValue = process.exitValue();
        if (exitValue != 0) {
            throw new RCommanderException(exitValue, getResult(process));
        }
    }

    private static RCommanderResult getResult(Process process) throws IOException {
        return new RCommanderResult(
                IOUtils.toString(process.getInputStream(), Charsets.UTF_8).trim(),
                IOUtils.toString(process.getErrorStream(), Charsets.UTF_8).trim());
    }

}
