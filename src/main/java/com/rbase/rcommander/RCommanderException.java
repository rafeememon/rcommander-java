package com.rbase.rcommander;

/**
 * A runtime exception representing an R:Commander task that has exited
 * abnormally.
 */
public class RCommanderException extends RuntimeException {

    private static final long serialVersionUID = -3689684906120859816L;

    private final int exitValue;
    private final RCommanderResult result;

    /**
     * Construct a new R:Commander exception.
     *
     * @param exitValue
     *            the process exit value
     * @param result
     *            the process result
     */
    public RCommanderException(int exitValue, RCommanderResult result) {
        super("Exit value was " + exitValue);
        this.exitValue = exitValue;
        this.result = result;
    }

    /**
     * Get the R:Commander exit value.
     *
     * @return the exit value
     */
    public int getExitValue() {
        return exitValue;
    }

    /**
     * Get the R:Commander result.
     *
     * @return the result
     */
    public RCommanderResult getResult() {
        return result;
    }

}
