package com.github.steveash.bushwhacker;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Tries the specific exception against a particular rule
 * @author Steve Ash
 */
public class RuleExceptionHandler implements ExceptionHandler {

  private final Predicate<Throwable> predicate;
  private final Function<Throwable,?> action;

  public RuleExceptionHandler(Predicate<Throwable> predicate, Function<Throwable,?> action) {
    this.predicate = predicate;
    this.action = action;
  }

  @Override
  public boolean handle(Throwable candidate) {
    if (predicate.apply(candidate)) {
      action.apply(candidate);
      return true;
    }
    return false;
  }
}
