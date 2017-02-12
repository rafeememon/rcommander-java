package com.rbase.rcommander.impl;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class TemporaryFileTest {

    @Test
    public void test() throws IOException {
        File file = null;
        try (TemporaryFile temporaryFile = TemporaryFile.create("test-", ".txt")) {
            file = temporaryFile.getFile();
            Assert.assertTrue(file.exists());
        }
        Assert.assertFalse(file.exists());
    }

}
