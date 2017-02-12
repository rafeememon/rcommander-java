package com.rbase.rcommander.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TemporaryFile implements AutoCloseable {

    private final File file;

    public static TemporaryFile create(String prefix, String suffix) throws IOException {
        return new TemporaryFile(File.createTempFile(prefix, suffix));
    }

    private TemporaryFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public void close() {
        FileUtils.deleteQuietly(file);
    }

}
