package com.github.steveash.bushwhacker.exception;

/**
 * @author Steve Ash
 */
public class BushwhackerStartupException extends RuntimeException {

  public BushwhackerStartupException() {
  }

  public BushwhackerStartupException(String message) {
    super(message);
  }

  public BushwhackerStartupException(String message, Throwable cause) {
    super(message, cause);
  }

  public BushwhackerStartupException(Throwable cause) {
    super(cause);
  }
}
