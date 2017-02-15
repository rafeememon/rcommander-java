package com.rbase.rcommander;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import com.rbase.rcommander.impl.RCommanderServiceImpl;

/**
 * A builder of {@link RCommanderService} instances.
 */
public class RCommanderServiceBuilder {

    private ExecutorService executorService;
    private String rCommanderPath;
    private String connectionString;

    /**
     * Construct a new {@link RCommanderServiceBuilder} instance.
     *
     * @return the builder
     */
    public static RCommanderServiceBuilder newBuilder() {
        return new RCommanderServiceBuilder();
    }

    /**
     * Set the executor service to use when executing commands.
     *
     * @param executorService
     *            the executor service
     * @return the builder
     */
    public RCommanderServiceBuilder executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    /**
     * Set the path to the R:Commander executable.
     *
     * @param rCommanderPath
     *            the executable path
     * @return the builder
     */
    public RCommanderServiceBuilder rCommanderPath(String rCommanderPath) {
        this.rCommanderPath = rCommanderPath;
        return this;
    }

    /**
     * Set the path to the R:Commander executable for the specified version.
     *
     * @param rCommanderVersion
     *            the version
     * @return the builder
     */
    public RCommanderServiceBuilder rCommanderVersion(String rCommanderVersion) {
        this.rCommanderPath = RCommanderPaths.getPathForVersion(rCommanderVersion);
        return this;
    }

    /**
     * Set the connection string to use when executing commands.
     *
     * @param connectionString
     *            the connection string
     * @return the builder
     */
    public RCommanderServiceBuilder connectionString(String connectionString) {
        this.connectionString = connectionString;
        return this;
    }

    /**
     * Set the DSN to use when executing commands.
     *
     * @param dsn
     *            the DSN
     * @return the builder
     */
    public RCommanderServiceBuilder dsn(String dsn) {
        this.connectionString = "DSN=" + dsn;
        return this;
    }

    /**
     * Build the {@link RCommanderService} instance.
     *
     * @return the service
     */
    public RCommanderService build() {
        return new RCommanderServiceImpl(
                Objects.requireNonNull(executorService, "must specify executor service"),
                Objects.requireNonNull(rCommanderPath, "must specify R:Commander path"),
                Objects.requireNonNull(connectionString, "must specify connection string"));
    }

}
