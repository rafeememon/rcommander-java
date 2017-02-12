package com.rbase.rcommander.impl;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import com.rbase.rcommander.RCommanderPaths;
import com.rbase.rcommander.RCommanderResult;

public class RCommanderTests {

    private RCommanderTests() {
        // utility class
    }

    static final String RCOMMANDER_PATH = RCommanderPaths.getPathForVersion("XE");
    static final String CONNECTION_STRING = "DSN=RRBYW18";

    static final String RCOMMANDER_PATH_INVALID = "foo.exe";
    static final String CONNECTION_STRING_INVALID = "DSN=foo";

    static final long TIMEOUT = 10L;
    static final long TIMEOUT_SHORT = 0L;

    static final String COMMAND = "SELECT Company INTO vCompany FROM Customer WHERE CustID = 127\n"
            + "PAUSE 2 USING .vCompany\n";
    static final String COMMAND_PATH_INVALID = "foo.rmd";

    private static final String EXPECTED_OUTPUT = "[RAM Data Systems, Inc.]";
    private static final String EXPECTED_ERROR = "";

    static TemporaryFile getTestCommandFile() {
        try {
            TemporaryFile temporaryFile = TemporaryFile.create("rcommander-test-", ".rmd");
            FileUtils.writeStringToFile(temporaryFile.getFile(), COMMAND);
            return temporaryFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void assertCorrectResult(RCommanderResult result) {
        Assert.assertEquals("output is correct", EXPECTED_OUTPUT, result.getOutput());
        Assert.assertEquals("error is correct", EXPECTED_ERROR, result.getError());
    }

}
