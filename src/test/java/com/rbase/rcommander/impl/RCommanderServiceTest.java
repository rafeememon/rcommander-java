package com.rbase.rcommander.impl;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.rbase.rcommander.RCommanderException;
import com.rbase.rcommander.RCommanderResult;
import com.rbase.rcommander.RCommanderService;

public class RCommanderServiceTest {

    private ExecutorService executorService;
    private RCommanderService rCommanderService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        executorService = Executors.newSingleThreadExecutor();
        rCommanderService = new RCommanderServiceImpl(
                executorService,
                RCommanderTests.RCOMMANDER_PATH,
                RCommanderTests.CONNECTION_STRING);
    }

    @After
    public void destroy() {
        Thread.interrupted();
        executorService.shutdown();
    }

    @Test
    public void testSubmit() throws InterruptedException, ExecutionException {
        RCommanderResult result = rCommanderService.submit(RCommanderTests.COMMAND).get();
        RCommanderTests.assertCorrectResult(result);
    }

    @Test
    public void testSubmitWithTimeout() throws InterruptedException, ExecutionException {
        RCommanderResult result = rCommanderService.submit(RCommanderTests.COMMAND, RCommanderTests.TIMEOUT)
                .get();
        RCommanderTests.assertCorrectResult(result);
    }

    @Test
    public void testSubmitWithTimeoutTimedOut() throws InterruptedException, ExecutionException {
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(TimeoutException.class));
        rCommanderService.submit(RCommanderTests.COMMAND, RCommanderTests.TIMEOUT_SHORT).get();
    }

    @Test
    public void testSubmitInterrupted() throws InterruptedException, ExecutionException {
        expectedException.expect(InterruptedException.class);
        Thread.currentThread().interrupt();
        rCommanderService.submit(RCommanderTests.COMMAND, RCommanderTests.TIMEOUT_SHORT).get();
    }

    @Test
    public void testSubmitPath() throws InterruptedException, ExecutionException {
        try (TemporaryFile commandFile = RCommanderTests.getTestCommandFile()) {
            RCommanderResult result = rCommanderService.submitPath(commandFile.getFile().getAbsolutePath()).get();
            RCommanderTests.assertCorrectResult(result);
        }
    }

    @Test
    public void testSubmitPathWithTimeout() throws InterruptedException, ExecutionException {
        try (TemporaryFile commandFile = RCommanderTests.getTestCommandFile()) {
            RCommanderResult result = rCommanderService
                    .submitPath(commandFile.getFile().getAbsolutePath(), RCommanderTests.TIMEOUT).get();
            RCommanderTests.assertCorrectResult(result);
        }
    }

    @Test
    public void testSubmitPathWithTimeoutTimedOut() throws InterruptedException, ExecutionException {
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(TimeoutException.class));
        try (TemporaryFile commandFile = RCommanderTests.getTestCommandFile()) {
            rCommanderService.submitPath(commandFile.getFile().getAbsolutePath(), RCommanderTests.TIMEOUT_SHORT)
                    .get();
        }
    }

    @Test
    public void testSubmitInvalidRCommanderPath() throws InterruptedException, ExecutionException {
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(IOException.class));
        expectedException.expectMessage("Cannot run program");
        rCommanderService = new RCommanderServiceImpl(
                executorService,
                RCommanderTests.RCOMMANDER_PATH_INVALID,
                RCommanderTests.CONNECTION_STRING);
        rCommanderService.submit(RCommanderTests.COMMAND).get();
    }

    @Test
    public void testSubmitInvalidConnectionString() throws InterruptedException, ExecutionException {
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(RCommanderException.class));
        rCommanderService = new RCommanderServiceImpl(
                executorService,
                RCommanderTests.RCOMMANDER_PATH,
                RCommanderTests.CONNECTION_STRING_INVALID);
        rCommanderService.submit(RCommanderTests.COMMAND).get();
    }

    @Test
    public void testSubmitPathInvalidRCommanderPath() throws InterruptedException, ExecutionException {
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(IOException.class));
        expectedException.expectMessage("Cannot run program");
        rCommanderService = new RCommanderServiceImpl(
                executorService,
                RCommanderTests.RCOMMANDER_PATH_INVALID,
                RCommanderTests.CONNECTION_STRING);
        try (TemporaryFile commandFile = RCommanderTests.getTestCommandFile()) {
            rCommanderService.submitPath(commandFile.getFile().getAbsolutePath()).get();
        }
    }

    @Test
    public void testSubmitPathInvalidCommandPath() throws InterruptedException, ExecutionException {
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(RCommanderException.class));
        rCommanderService = new RCommanderServiceImpl(
                executorService,
                RCommanderTests.RCOMMANDER_PATH,
                RCommanderTests.CONNECTION_STRING);
        rCommanderService.submitPath(RCommanderTests.COMMAND_PATH_INVALID).get();
    }

    @Test
    public void testSubmitPathInvalidConnectionString() throws InterruptedException, ExecutionException {
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(IsInstanceOf.instanceOf(RCommanderException.class));
        rCommanderService = new RCommanderServiceImpl(
                executorService,
                RCommanderTests.RCOMMANDER_PATH,
                RCommanderTests.CONNECTION_STRING_INVALID);
        try (TemporaryFile commandFile = RCommanderTests.getTestCommandFile()) {
            rCommanderService.submitPath(commandFile.getFile().getAbsolutePath()).get();
        }
    }

}
