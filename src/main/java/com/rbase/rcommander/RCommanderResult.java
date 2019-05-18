package com.rbase.rcommander;

import org.immutables.value.Value;

/**
 * A result of an R:Commander task.
 */
@Value.Immutable
public interface RCommanderResult {

    /**
     * The standard output of the task.
     *
     * @return the standard output
     */
    String getOutput();

    /**
     * The error output of the task.
     *
     * @return the error output
     */
    String getError();

    /**
     * Returns <code>true</code> if there is an error.
     *
     * @return true if there is an error
     */
    default boolean hasError() {
        return !getError().isEmpty();
    }

}
