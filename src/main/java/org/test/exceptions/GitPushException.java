package org.test.exceptions;

public class GitPushException extends RuntimeException {
    public GitPushException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitPushException(String message) {
        super(message);
    }
}
