package com.rbase.rcommander.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RCommanderFuturesTest {

    private ExecutorService executorService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        executorService = Executors.newFixedThreadPool(1);
    }

    @After
    public void destroy() {
        executorService.shutdown();
    }

    @Test
    public void testSubmitCancelableSuccess() {
        String string = "foo";
        CompletableFuture<String> future = RCommanderFutures.submitCancelable(() -> {
            return string;
        }, executorService);
        Assert.assertEquals(string, future.join());
    }

    @Test
    public void testSubmitCancelableFailure() {
        Exception exception = new Exception("exception");
        expectedException.expect(CompletionException.class);
        expectedException.expectCause(IsEqual.equalTo(exception));
        CompletableFuture<String> future = RCommanderFutures.submitCancelable(() -> {
            throw exception;
        }, executorService);
        future.join();
    }

    @Test
    public void testSubmitCancelableCancel() throws InterruptedException {
        AtomicBoolean endReached = new AtomicBoolean(false);
        CompletableFuture<String> future = RCommanderFutures.submitCancelable(() -> {
            Thread.sleep(10L);
            endReached.set(true);
            return null;
        }, executorService);
        future.cancel(true);
        Thread.sleep(20L);
        Assert.assertFalse(endReached.get());
    }

    @Test
    public void testSubmitCancelableShutdown() throws InterruptedException {
        AtomicBoolean endReached = new AtomicBoolean(false);
        CompletableFuture<String> future = RCommanderFutures.submitCancelable(() -> {
            Thread.sleep(10L);
            endReached.set(true);
            return null;
        }, executorService);
        executorService.shutdownNow();
        Thread.sleep(20L);
        Assert.assertFalse(endReached.get());
        Assert.assertTrue(future.isCompletedExceptionally());
    }

}
