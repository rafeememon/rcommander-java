package com.rbase.rcommander.impl;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.rbase.rcommander.RCommanderException;
import com.rbase.rcommander.RCommanderResult;

public class CommandCallableTest {

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
        RCommanderResult result = new CommandCallable(
                RCommanderTests.CONNECTION_STRING,
                RCommanderTests.COMMAND,
                RCommanderTests.RCOMMANDER_PATH,
                timeoutSeconds).call();
        RCommanderTests.assertCorrectResult(result);
    }

    @Test
    @Ignore("exit code not set properly")
    public void testCallInvalidConnectionString() throws Exception {
        expectedException.expect(RCommanderException.class);
        new CommandCallable(
                RCommanderTests.CONNECTION_STRING_INVALID,
                RCommanderTests.COMMAND,
                RCommanderTests.RCOMMANDER_PATH,
                Optional.empty()).call();
    }

    @Test
    public void testCallInvalidRCommanderPath() throws Exception {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Cannot run program");
        new CommandCallable(
                RCommanderTests.CONNECTION_STRING,
                RCommanderTests.COMMAND,
                RCommanderTests.RCOMMANDER_PATH_INVALID,
                Optional.empty()).call();
    }

}
