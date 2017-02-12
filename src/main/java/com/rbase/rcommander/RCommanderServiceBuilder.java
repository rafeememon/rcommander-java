package com.rbase.rcommander;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import com.rbase.rcommander.impl.RCommanderServiceImpl;

public class RCommanderServiceBuilder {

    private ExecutorService executorService;
    private String rCommanderPath;
    private String connectionString;

    public static RCommanderServiceBuilder newBuilder() {
        return new RCommanderServiceBuilder();
    }

    public RCommanderServiceBuilder executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public RCommanderServiceBuilder rCommanderPath(String rCommanderPath) {
        this.rCommanderPath = rCommanderPath;
        return this;
    }

    public RCommanderServiceBuilder rCommanderVersion(String rCommanderVersion) {
        this.rCommanderPath = RCommanderPaths.getPathForVersion(rCommanderVersion);
        return this;
    }

    public RCommanderServiceBuilder connectionString(String connectionString) {
        this.connectionString = connectionString;
        return this;
    }

    public RCommanderServiceBuilder dsn(String dsn) {
        this.connectionString = "DSN=" + dsn;
        return this;
    }

    public RCommanderService build() {
        return new RCommanderServiceImpl(
                Objects.requireNonNull(executorService, "must specify executor service"),
                Objects.requireNonNull(rCommanderPath, "must specify R:Commander path"),
                Objects.requireNonNull(connectionString, "must specify connection string"));
    }

}
