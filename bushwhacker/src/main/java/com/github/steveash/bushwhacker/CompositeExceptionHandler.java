package com.github.steveash.bushwhacker;

import com.google.common.collect.ImmutableList;

/**
 * Orchstrates trying all of the rules configured for the given candidate
 * @author Steve Ash
 */
public class CompositeExceptionHandler implements ExceptionHandler {

  private final ImmutableList<? extends ExceptionHandler> handlers;

  public CompositeExceptionHandler(Iterable<? extends ExceptionHandler> handlers) {
    this.handlers = ImmutableList.copyOf(handlers);
  }

  @Override
  public boolean handle(Throwable candidate) {
    for (int i = 0; i < handlers.size(); i++) {
      if (handlers.get(i).handle(candidate)) return true;
    }
    return false;
  }
}
