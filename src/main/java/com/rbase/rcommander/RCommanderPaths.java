package com.rbase.rcommander;

import java.io.File;
import java.nio.file.Paths;

public class RCommanderPaths {

    private RCommanderPaths() {
        // utility class
    }

    private static final String RBTI_DIRECTORY = "C:\\RBTI";
    private static final String RCOMMANDER_PREFIX = "RCommander";

    /**
     * Get the default path of R:Commander for the specified version.
     *
     * @param version
     *            the version
     * @return the path to R:Commander
     */
    public static String getPathForVersion(String version) {
        String fullName = RCOMMANDER_PREFIX + version;
        String path = Paths.get(RBTI_DIRECTORY, fullName, fullName + ".exe").toString();
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("R:Commander version " + version + " does not exist at " + path);
        } else if (!file.canExecute()) {
            throw new IllegalArgumentException("R:Commander version " + version + " is not executable");
        }
        return path;
    }

}
