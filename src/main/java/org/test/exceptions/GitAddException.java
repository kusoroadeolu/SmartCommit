package org.test.exceptions;

public class GitAddException extends RuntimeException {
    public GitAddException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitAddException(String message) {
        super(message);
    }
}
