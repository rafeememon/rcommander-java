package com.rbase.rcommander.impl;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;

import com.rbase.rcommander.RCommanderResult;

public class CommandCallable implements Callable<RCommanderResult> {

    private final String connectionString;
    private final String command;
    private final String rCommanderPath;
    private final Optional<Long> timeoutSeconds;

    public CommandCallable(
            String connectionString,
            String command,
            String rCommanderPath,
            Optional<Long> timeoutSeconds) {
        this.connectionString = connectionString;
        this.command = command;
        this.rCommanderPath = rCommanderPath;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public RCommanderResult call() throws IOException, InterruptedException, TimeoutException {
        try (TemporaryFile temporaryFile = TemporaryFile.create("rcommander-", ".rmd")) {
            File commandFile = temporaryFile.getFile();
            FileUtils.writeStringToFile(commandFile, command);
            return new CommandPathCallable(
                    connectionString,
                    commandFile.getAbsolutePath(),
                    rCommanderPath,
                    timeoutSeconds).call();
        }
    }

}
