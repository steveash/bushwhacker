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

    @Override
    public String toString() {
      return "ExceptionRule{" +
             "matches=" + matches +
             ", action=" + action +
             '}';
    }
  }

  public static class ExceptionAction {

    private final String addHint;
    private final String replaceMessage;
    private final Boolean writeToLog;

    public ExceptionAction() {
      this(null, null, false);
    }

    public ExceptionAction(String addHint, String replaceMessage, boolean writeToLog) {
      this.addHint = addHint;
      this.replaceMessage = replaceMessage;
      this.writeToLog = writeToLog;
    }

    public String getAddHint() {
      return addHint;
    }

    public String getReplaceMessage() {
      return replaceMessage;
    }

    public Boolean isWriteToLog() {
      return writeToLog;
    }

    @Override
    public String toString() {
      return "ExceptionAction{" +
             "addHint='" + addHint + '\'' +
             ", replaceMessage='" + replaceMessage + '\'' +
             ", writeToLog=" + writeToLog +
             '}';
    }
  }

  public static class ExceptionMatch {

    private final String exceptionClass;
    private final String calledFrom;
    private final String thrownFrom;
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
      this.calledFrom = calledFrom;
      this.messageMatches = messageMatches;
      this.custom = custom;
      this.thrownFrom = thrownFrom;
    }

    public String getExceptionClass() {
      return exceptionClass;
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

    public String getThrownFrom() {
      return thrownFrom;
    }

    @Override
    public String toString() {
      return "ExceptionMatch{" +
             "exceptionClass='" + exceptionClass + '\'' +
             ", calledFrom='" + calledFrom + '\'' +
             ", thrownFrom='" + thrownFrom + '\'' +
             ", messageMatches='" + messageMatches + '\'' +
             ", custom='" + custom + '\'' +
             '}';
    }
  }

  @Override
  public String toString() {
    return "XmlRules{" +
           "exceptionRules=" + exceptionRules +
           '}';
  }
}
