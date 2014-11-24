package com.github.steveash.bushwhacker.exception;

/**
 * @author Steve Ash
 */
public class IllegalBushwhackerRulesException extends RuntimeException {

  public IllegalBushwhackerRulesException() {
  }

  public IllegalBushwhackerRulesException(String message) {
    super(message);
  }

  public IllegalBushwhackerRulesException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalBushwhackerRulesException(Throwable cause) {
    super(cause);
  }
}
