package org.test.exceptions;

public class FileOperationsException extends RuntimeException {

    public FileOperationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileOperationsException(String message) {
        super(message);
    }
}
