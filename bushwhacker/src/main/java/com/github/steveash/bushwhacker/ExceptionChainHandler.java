package com.github.steveash.bushwhacker;

/**
 * Handles a throwable and iterates over all of it processing each exception in the chain; this
 * is fundamentally different from the ExceptionHandler which only handles a single exception
 * @author Steve Ash
 */
public interface ExceptionChainHandler {

  boolean handle(Throwable t);
}
