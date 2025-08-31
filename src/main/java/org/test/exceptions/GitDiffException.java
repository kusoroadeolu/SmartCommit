package org.test.exceptions;

public class GitDiffException extends RuntimeException {
  public GitDiffException(String message, Throwable cause) {
    super(message, cause);
  }

  public GitDiffException(String message) {
    super(message);
  }
}
