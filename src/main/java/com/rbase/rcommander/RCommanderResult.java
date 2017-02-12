package com.rbase.rcommander;

public class RCommanderResult {

    private final String output;
    private final String error;

    public RCommanderResult(String output, String error) {
        this.output = output;
        this.error = error;
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return !error.isEmpty();
    }

    @Override
    public String toString() {
        return "RCommanderResult [output=" + output + ", error=" + error + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((error == null) ? 0 : error.hashCode());
        result = prime * result + ((output == null) ? 0 : output.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RCommanderResult other = (RCommanderResult) obj;
        if (error == null) {
            if (other.error != null)
                return false;
        } else if (!error.equals(other.error))
            return false;
        if (output == null) {
            if (other.output != null)
                return false;
        } else if (!output.equals(other.output))
            return false;
        return true;
    }

}
