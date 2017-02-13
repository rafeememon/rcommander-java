package com.rbase.rcommander.impl;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.rbase.rcommander.RCommanderException;
import com.rbase.rcommander.RCommanderResult;

public class CommandPathCallableTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @After
    public void destroy() {
        Thread.interrupted();
    }

    @Test
    public void testCall() throws Exception {
        testCallInternal(Optional.empty());
    }

    @Test
    public void testCallWithTimeout() throws Exception {
        testCallInternal(Optional.of(RCommanderTests.TIMEOUT));
    }

    @Test
    public void testCallWithTimeoutTimedOut() throws Exception {
        expectedException.expect(TimeoutException.class);
        testCallInternal(Optional.of(RCommanderTests.TIMEOUT_SHORT));
    }

    @Test
    public void testCallInterrupted() throws Exception {
        expectedException.expect(InterruptedException.class);
        Thread.currentThread().interrupt();
        testCallInternal(Optional.empty());
    }

    private static void testCallInternal(Optional<Long> timeoutSeconds) throws Exception {
        try (TemporaryFile commandFile = RCommanderTests.getTestCommandFile()) {
            RCommanderResult result = new CommandPathCallable(
                    RCommanderTests.CONNECTION_STRING,
                    commandFile.getFile().getAbsolutePath(),
                    RCommanderTests.RCOMMANDER_PATH,
                    timeoutSeconds).call();
            RCommanderTests.assertCorrectResult(result);
        }
    }

    @Test
    public void testCallInvalidConnectionString() throws Exception {
        expectedException.expect(RCommanderException.class);
        try (TemporaryFile commandFile = RCommanderTests.getTestCommandFile()) {
            new CommandPathCallable(
                    RCommanderTests.CONNECTION_STRING_INVALID,
                    commandFile.getFile().getAbsolutePath(),
                    RCommanderTests.RCOMMANDER_PATH,
                    Optional.empty()).call();
        }
    }

    @Test
    public void testCallInvalidCommandPath() throws Exception {
        expectedException.expect(RCommanderException.class);
        new CommandPathCallable(
                RCommanderTests.CONNECTION_STRING,
                RCommanderTests.COMMAND_PATH_INVALID,
                RCommanderTests.RCOMMANDER_PATH,
                Optional.empty()).call();
    }

    @Test
    public void testCallInvalidRCommanderPath() throws Exception {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Cannot run program");
        try (TemporaryFile commandFile = RCommanderTests.getTestCommandFile()) {
            new CommandPathCallable(
                    RCommanderTests.CONNECTION_STRING,
                    commandFile.getFile().getAbsolutePath(),
                    RCommanderTests.RCOMMANDER_PATH_INVALID,
                    Optional.empty()).call();
        }
    }

}
