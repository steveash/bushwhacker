package com.github.steveash.bushwhacker;

import java.util.List;

import static com.google.common.base.Throwables.getCausalChain;
import static com.google.common.collect.Lists.reverse;

/**
 * Handles a chain of exceptions by invoking the ExceptionHandler for each; this is _not_ an
 * ExceptionHandler because it handles causal chains whereas an ExceptionHandler is not supposed
 * to.  So its not substitutable and thus not in an IS A relationship.
 * @author Steve Ash
 */
public class ExceptionChainHandler {

  private final ExceptionHandler perExceptionHandler;

  public ExceptionChainHandler(ExceptionHandler perExceptionHandler) {
    this.perExceptionHandler = perExceptionHandler;
  }

  public boolean handle(Throwable t) {
    List<Throwable> rootToTop = reverse(getCausalChain(t));

    boolean anyHandled = false;
    for (int i = 0; i < rootToTop.size(); i++) {
      if (perExceptionHandler.handle(t)) {
        anyHandled = true;
      }
    }

    return anyHandled;
  }
}
