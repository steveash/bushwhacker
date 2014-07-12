package com.github.steveash.bushwhacker;

/**
 * Main service class that attempts to match an exception and enrich it.  This is the main
 * @author Steve Ash
 */
public interface ExceptionHandler {

  /**
   * @param candidate
   * @return true if this exception (not any of its causes) matches any rule
   */
  boolean handle(Throwable candidate);
}
