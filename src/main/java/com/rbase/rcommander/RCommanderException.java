package com.rbase.rcommander;

public class RCommanderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int exitValue;
    private final RCommanderResult result;

    public RCommanderException(int exitValue, RCommanderResult result) {
        super("Exit value was " + exitValue);
        this.exitValue = exitValue;
        this.result = result;
    }

    public int getExitValue() {
        return exitValue;
    }

    public RCommanderResult getResult() {
        return result;
    }

}
