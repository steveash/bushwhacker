package com.github.steveash.bushwhacker.rules;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import com.github.steveash.bushwhacker.CompositeExceptionHandler;
import com.github.steveash.bushwhacker.RuleExceptionHandler;

import org.apache.commons.lang3.BooleanUtils;

import static com.github.steveash.bushwhacker.rules.ActionMaker.makeAddHint;
import static com.github.steveash.bushwhacker.rules.ActionMaker.makeLog;
import static com.github.steveash.bushwhacker.rules.ActionMaker.makeReplace;
import static com.google.common.base.Functions.compose;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Steve Ash
 */
public class HandlerMaker {

  // translate the xml rules in to specific predicates to return RuleExceHandler
  public CompositeExceptionHandler buildHandlersFor(XmlRules rules) {
    ImmutableList.Builder<RuleExceptionHandler> builder = ImmutableList.builder();
    for (XmlRules.ExceptionRule rule : rules.getExceptionRules()) {
      builder.add(buildHandlerFor(rule));
    }
    return new CompositeExceptionHandler(builder.build());
  }

  public RuleExceptionHandler buildHandlerFor(XmlRules.ExceptionRule rule) {
    return new RuleExceptionHandler(makePredicate(rule), makeAction(rule));
  }

  private Predicate<Throwable> makePredicate(XmlRules.ExceptionRule rule) {
    XmlRules.ExceptionMatch matches = rule.getMatches();
    ImmutableList.Builder<Predicate<Throwable>> builder = ImmutableList.builder();

    if (isNotBlank(matches.getCalledFrom())) {
      builder.add(PredicateMaker.makeCalledFrom(matches.getCalledFrom()));
    }
    if (isNotBlank(matches.getExceptionClass())) {
      builder.add(PredicateMaker.makeExceptionClass(matches.getExceptionClass()));
    }
    if (isNotBlank(matches.getMessageMatches())) {
      builder.add(PredicateMaker.makeMessageMatches(matches.getMessageMatches()));
    }
    if (isNotBlank(matches.getCustom())) {
      builder.add(PredicateMaker.makeCustom(matches.getCustom()));
    }
    if (isNotBlank(matches.getThrownFrom())) {
      builder.add(PredicateMaker.makeThrownFrom(matches.getCustom()));
    }
    return Predicates.and(builder.build());
  }

  private Function<Throwable, ?> makeAction(XmlRules.ExceptionRule rule) {
    Function<Throwable,Throwable> result = Functions.identity();

    XmlRules.ExceptionAction action = rule.getAction();
    if (isNotBlank(action.getReplaceMessage())) {
      result = compose(makeReplace(action.getReplaceMessage()), result);
    }
    if (isNotBlank(action.getAddHint())) {
      result = compose(makeAddHint(action.getAddHint()), result);
    }
    if (BooleanUtils.isTrue(action.isWriteToLog())) {
      result = compose(makeLog(), result);
    }

    return result;
  }
}
