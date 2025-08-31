package org.test.exceptions;

public class SmartCommitException extends RuntimeException {
    public SmartCommitException(String message) {
        super(message);
    }

    public SmartCommitException(String message, Throwable cause) {
        super(message, cause);
    }
}
