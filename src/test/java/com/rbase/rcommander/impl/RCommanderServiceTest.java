package com.rbase.rcommander.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rbase.rcommander.RCommanderException;
import com.rbase.rcommander.RCommanderResult;
import com.rbase.rcommander.RCommanderService;

public class RCommanderServiceTest extends BaseTest {

    private ExecutorService executorService;

    @Before
    public void setup() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @After
    public void destroy() {
        Thread.interrupted();
        executorService.shutdown();
    }

    @Test
    public void testSubmit() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        RCommanderResult result = service.submit(TEST_COMMAND).get();
        Assert.assertEquals(TEST_EXPECTED_RESULT, result);
    }

    @Test
    public void testSubmitWithTimeout() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        RCommanderResult result = service.submit(TEST_COMMAND, TEST_TIMEOUT).get();
        Assert.assertEquals(TEST_EXPECTED_RESULT, result);
    }

    @Test
    public void testSubmitWithTimeoutTimedOut() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(TimeoutException.class));
        service.submit(TEST_COMMAND, TEST_TIMEOUT_SHORT).get();
    }

    @Test
    public void testSubmitInterrupted() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        expectedException.expect(InterruptedException.class);
        Thread.currentThread().interrupt();
        service.submit(TEST_COMMAND).get();
    }

    @Test
    public void testSubmitPath() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        RCommanderResult result = service.submitPath(commandFile(TEST_COMMAND)).get();
        Assert.assertEquals(TEST_EXPECTED_RESULT, result);
    }

    @Test
    public void testSubmitPathWithTimeout() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        RCommanderResult result = service.submitPath(commandFile(TEST_COMMAND), TEST_TIMEOUT).get();
        Assert.assertEquals(TEST_EXPECTED_RESULT, result);
    }

    @Test
    public void testSubmitPathWithTimeoutTimedOut() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(TimeoutException.class));
        service.submitPath(commandFile(TEST_COMMAND), TEST_TIMEOUT_SHORT).get();
    }

    @Test
    public void testSubmitInvalidRCommanderPath() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(INVALID_RCOMMANDER_PATH, CONNECTION_STRING);
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(IOException.class));
        expectedException.expectMessage("Cannot run program");
        service.submit(TEST_COMMAND).get();
    }

    @Test
    public void testSubmitInvalidConnectionString() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, INVALID_CONNECTION_STRING);
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(RCommanderException.class));
        service.submit(TEST_COMMAND).get();
    }

    @Test
    public void testSubmitPathInvalidRCommanderPath() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(INVALID_RCOMMANDER_PATH, CONNECTION_STRING);
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(IOException.class));
        expectedException.expectMessage("Cannot run program");
        service.submitPath(commandFile(TEST_COMMAND)).get();
    }

    @Test
    public void testSubmitPathInvalidCommandPath() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(RCommanderException.class));
        service.submitPath(INVALID_COMMAND_PATH).get();
    }

    @Test
    public void testSubmitPathInvalidConnectionString() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, INVALID_CONNECTION_STRING);
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(RCommanderException.class));
        service.submitPath(commandFile(TEST_COMMAND)).get();
    }

    @Test
    public void testSubmitLargeOutput() throws InterruptedException, ExecutionException {
        RCommanderService service = rCommanderService(RCOMMANDER_PATH, CONNECTION_STRING);
        String command = repeat("PAUSE 2 USING " + repeat("R", 100) + "\n", 100);
        service.submit(command).get();
    }

    private RCommanderService rCommanderService(String rCommanderPath, String connectionString) {
        return new RCommanderServiceImpl(executorService, rCommanderPath, connectionString);
    }

    private static String repeat(String string, int copies) {
        return String.join("", Collections.nCopies(copies, string));
    }

}
