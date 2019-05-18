package com.rbase.rcommander.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.rbase.rcommander.RCommanderException;
import com.rbase.rcommander.RCommanderPaths;
import com.rbase.rcommander.RCommanderResult;

public class BaseTest {

    protected static final String RCOMMANDER_PATH = RCommanderPaths.getPathForVersion("XE");
    protected static final String CONNECTION_STRING = "DSN=RRBYW18";
    protected static final String TEST_COMMAND = "SELECT Company INTO vCompany FROM Customer WHERE CustID = 127\n"
            + "PAUSE 2 USING .vCompany\n";
    protected static final RCommanderResult TEST_EXPECTED_RESULT = new RCommanderResult("[RAM Data Systems, Inc.]", "");
    protected static final long TEST_TIMEOUT = 10L;
    protected static final long TEST_TIMEOUT_SHORT = 0L;

    protected static final String INVALID_RCOMMANDER_PATH = "foo.exe";
    protected static final String INVALID_CONNECTION_STRING = "DSN=foo";
    protected static final String INVALID_COMMAND_PATH = "foo.rmd";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    protected static String commandFile(String command) {
        try {
            File file = File.createTempFile("rcommander-test-", ".rmd");
            FileUtils.writeStringToFile(file, command);
            return file.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void expectRCommanderException(String error) {
        expectedException.expect(RCommanderException.class);
        expectedException.expect(RCommanderErrorMatcher.errorContains(error));
    }

}
