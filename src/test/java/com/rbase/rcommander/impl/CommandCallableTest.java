package com.rbase.rcommander.impl;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.rbase.rcommander.RCommanderResult;

public class CommandCallableTest extends BaseTest {

    @After
    public void destroy() {
        Thread.interrupted();
    }

    @Test
    public void testCall() throws Exception {
        RCommanderResult result = commandCallable(CONNECTION_STRING, TEST_COMMAND, RCOMMANDER_PATH).call();
        Assert.assertEquals(TEST_EXPECTED_RESULT, result);
    }

    @Test
    public void testCallWithTimeout() throws Exception {
        RCommanderResult result =
                commandCallable(CONNECTION_STRING, TEST_COMMAND, RCOMMANDER_PATH, TEST_TIMEOUT).call();
        Assert.assertEquals(TEST_EXPECTED_RESULT, result);
    }

    @Test
    public void testCallWithTimeoutTimedOut() throws Exception {
        expectedException.expect(TimeoutException.class);
        commandCallable(CONNECTION_STRING, TEST_COMMAND, RCOMMANDER_PATH, TEST_TIMEOUT_SHORT).call();
    }

    @Test
    public void testCallInterrupted() throws Exception {
        expectedException.expect(InterruptedException.class);
        Thread.currentThread().interrupt();
        commandCallable(CONNECTION_STRING, TEST_COMMAND, RCOMMANDER_PATH).call();
    }

    @Test
    public void testCallInvalidConnectionString() throws Exception {
        expectRCommanderException("No database connected");
        commandCallable(INVALID_CONNECTION_STRING, TEST_COMMAND, RCOMMANDER_PATH).call();
    }

    @Test
    public void testCallInvalidRCommanderPath() throws Exception {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Cannot run program");
        commandCallable(CONNECTION_STRING, TEST_COMMAND, INVALID_RCOMMANDER_PATH).call();
    }

    private static CommandCallable commandCallable(
            String connectionString,
            String command,
            String rCommanderPath) {
        return new CommandCallable(connectionString, command, rCommanderPath, Optional.empty());
    }

    private static CommandCallable commandCallable(
            String connectionString,
            String command,
            String rCommanderPath,
            long timeout) {
        return new CommandCallable(connectionString, command, rCommanderPath, Optional.of(timeout));
    }

}
