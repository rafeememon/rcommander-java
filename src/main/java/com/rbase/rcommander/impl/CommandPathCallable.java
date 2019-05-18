package com.rbase.rcommander.impl;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import com.rbase.rcommander.ImmutableRCommanderResult;
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
        try (TemporaryFile outFile = TemporaryFile.create("rcommander-out-", ".log");
                TemporaryFile errFile = TemporaryFile.create("rcommander-err-", ".log")) {
            process = new ProcessBuilder(rCommanderPath, commandPath, "DB " + connectionString)
                    .redirectOutput(outFile.getFile())
                    .redirectError(errFile.getFile())
                    .start();
            waitFor(process);
            RCommanderResult result = getResult(outFile.getFile(), errFile.getFile());
            int exitValue = process.exitValue();
            if (exitValue != 0) {
                throw new RCommanderException(exitValue, result);
            }
            return result;
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

    private static RCommanderResult getResult(File outFile, File errFile) throws IOException {
        return ImmutableRCommanderResult.builder()
                .output(FileUtils.readFileToString(outFile, Charsets.UTF_8).trim())
                .error(FileUtils.readFileToString(errFile, Charsets.UTF_8).trim())
                .build();
    }

}
