package com.github.steveash.bushwhacker;

/**
 * @author Steve Ash
 */
public class NullExceptionChainHandler implements ExceptionChainHandler {

  @Override
  public boolean handle(Throwable t) {
    return false;
  }
}
