package com.github.steveash.bushwhacker;

import com.google.common.base.Predicate;

import com.github.steveash.bushwhacker.rules.XmlRules;

/**
 * Tries the specific exception against a particular rule
 * @author Steve Ash
 */
public class RuleExceptionHandler implements ExceptionHandler {

  private final Predicate<Throwable> predicate;
  private final XmlRules.ExceptionAction action;

  public RuleExceptionHandler(Predicate<Throwable> predicate, XmlRules.ExceptionAction action) {
    this.predicate = predicate;
    this.action = action;
  }

  @Override
  public boolean handle(Throwable candidate) {
    if (predicate.apply(candidate)) {
      onMatch(candidate);
      return true;
    }
    return false;
  }

  private void onMatch(Throwable candidate) {
    // only handle one kind of update right now; enriching the error message
    MessageEnricher.enrich(candidate, action.getUpdateMessage());
  }
}
