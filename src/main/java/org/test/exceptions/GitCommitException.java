package org.test.exceptions;

public class GitCommitException extends RuntimeException {
    public GitCommitException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitCommitException(String message) {
        super(message);
    }
}
