package com.github.steveash.bushwhacker.rules;

import com.google.common.collect.Lists;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * @author Steve Ash
 */
@XStreamAlias("rules")
public class XmlRules {

  @XStreamAlias("exceptions")
  private List<ExceptionRule> exceptionRules = Lists.newArrayList();

  public List<ExceptionRule> getExceptionRules() {
    return exceptionRules;
  }

  @XStreamAlias("exception")
  public static class ExceptionRule {

    private ExceptionMatch matches = new ExceptionMatch();
    private ExceptionAction action = new ExceptionAction();

    public ExceptionMatch getMatches() {
      return matches;
    }

    public ExceptionAction getAction() {
      return action;
    }
  }

  public static class ExceptionAction {

    private final String updateMessage;

    public ExceptionAction(String updateMessage) {
      this.updateMessage = updateMessage;
    }

    public ExceptionAction() {
      this(null);
    }

    public String getUpdateMessage() {
      return updateMessage;
    }
  }

  public static class ExceptionMatch {

    private final String exceptionClass;
    private final String thrownFrom;
    private final String calledFrom;
    private final String messageMatches;
    private final String custom;

    public ExceptionMatch() {
      this(
          null, null, null, null, null
      );
    }

    public ExceptionMatch(String exceptionClass, String thrownFrom, String calledFrom,
                          String messageMatches, String custom) {
      this.exceptionClass = exceptionClass;
      this.thrownFrom = thrownFrom;
      this.calledFrom = calledFrom;
      this.messageMatches = messageMatches;
      this.custom = custom;
    }

    public String getExceptionClass() {
      return exceptionClass;
    }

    public String getThrownFrom() {
      return thrownFrom;
    }

    public String getCalledFrom() {
      return calledFrom;
    }

    public String getMessageMatches() {
      return messageMatches;
    }

    public String getCustom() {
      return custom;
    }
  }
}
