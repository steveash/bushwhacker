package com.github.steveash.bushwhacker;

/**
 * @author Steve Ash
 */
public class JunitTestException extends RuntimeException {

  public JunitTestException() {
  }

  public JunitTestException(String message) {
    super(message);
  }

  public JunitTestException(String message, Throwable cause) {
    super(message, cause);
  }

  public JunitTestException(Throwable cause) {
    super(cause);
  }
}
