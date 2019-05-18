package com.rbase.rcommander.impl;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.rbase.rcommander.RCommanderResult;

public class CommandPathCallableTest extends BaseTest {

    @After
    public void destroy() {
        Thread.interrupted();
    }

    @Test
    public void testCall() throws Exception {
        RCommanderResult result =
                commandPathCallable(CONNECTION_STRING, commandFile(TEST_COMMAND), RCOMMANDER_PATH).call();
        Assert.assertEquals(TEST_EXPECTED_RESULT, result);
    }

    @Test
    public void testCallWithTimeout() throws Exception {
        RCommanderResult result =
                commandPathCallable(CONNECTION_STRING, commandFile(TEST_COMMAND), RCOMMANDER_PATH, TEST_TIMEOUT).call();
        Assert.assertEquals(TEST_EXPECTED_RESULT, result);
    }

    @Test
    public void testCallWithTimeoutTimedOut() throws Exception {
        expectedException.expect(TimeoutException.class);
        commandPathCallable(CONNECTION_STRING, commandFile(TEST_COMMAND), RCOMMANDER_PATH, TEST_TIMEOUT_SHORT).call();
    }

    @Test
    public void testCallInterrupted() throws Exception {
        expectedException.expect(InterruptedException.class);
        Thread.currentThread().interrupt();
        commandPathCallable(CONNECTION_STRING, commandFile(TEST_COMMAND), RCOMMANDER_PATH).call();
    }

    @Test
    public void testCallInvalidConnectionString() throws Exception {
        expectRCommanderException("No database connected");
        commandPathCallable(INVALID_CONNECTION_STRING, commandFile(TEST_COMMAND), RCOMMANDER_PATH).call();
    }

    @Test
    public void testCallInvalidCommandPath() throws Exception {
        // FIXME: "-ERROR- File foo.rmd does not exists" in stdout
        expectRCommanderException("");
        commandPathCallable(INVALID_CONNECTION_STRING, INVALID_COMMAND_PATH, RCOMMANDER_PATH).call();
    }

    @Test
    public void testCallInvalidRCommanderPath() throws Exception {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Cannot run program");
        commandPathCallable(CONNECTION_STRING, commandFile(TEST_COMMAND), INVALID_RCOMMANDER_PATH).call();
    }

    private static CommandPathCallable commandPathCallable(
            String connectionString,
            String commandPath,
            String rCommanderPath) {
        return new CommandPathCallable(connectionString, commandPath, rCommanderPath, Optional.empty());
    }

    private static CommandPathCallable commandPathCallable(
            String connectionString,
            String commandPath,
            String rCommanderPath,
            long timeout) {
        return new CommandPathCallable(connectionString, commandPath, rCommanderPath, Optional.of(timeout));
    }

}
