package com.github.steveash.bushwhacker.rules;

import com.google.common.base.Predicate;
import com.google.common.reflect.TypeToken;

import com.github.steveash.bushwhacker.exception.IllegalBushwhackerRulesException;
import com.github.steveash.bushwhacker.util.WildcardMatcher;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Steve Ash
 */
public class PredicateMaker {

  public static Predicate<Throwable> makeCalledFrom(String calledFrom) {
    final Pattern pattern = WildcardMatcher.compileWildcardPattern(calledFrom);
    return new Predicate<Throwable>() {
      @Override
      public boolean apply(Throwable input) {
        StackTraceElement[] frames = input.getStackTrace();
        for (int i = 0; i < frames.length; i++) {
          StackTraceElement frame = frames[i];
          if (pattern.matcher(frame.getClassName()).matches()) {
            return true;
          }
        }
        return false;
      }
    };
  }

  public static Predicate<Throwable> makeExceptionClass(String exceptionClass) {
    final Pattern pattern = WildcardMatcher.compileWildcardPattern(exceptionClass);
    return new Predicate<Throwable>() {
      @Override
      public boolean apply(Throwable input) {
        TypeToken token = TypeToken.of(input.getClass());
        TypeToken.TypeSet types = token.getTypes();
        Set<Class> allTypes = types.rawTypes();
        for (Class clazz : allTypes) {
          if (pattern.matcher(clazz.getName()).matches()) {
            return true;
          }
        }
        return false;
      }
    };
  }

  public static Predicate<Throwable> makeMessageMatches(String messagePattern) {
    final Pattern p = Pattern.compile(messagePattern, Pattern.CASE_INSENSITIVE);
    return new Predicate<Throwable>() {
      @Override
      public boolean apply(Throwable input) {
        if (p.matcher(input.getMessage()).matches()) {
          return true;
        }
        return false;
      }
    };
  }

  public static Predicate<Throwable> makeCustom(String custom) {
    try {
      Class<?> customClass = Class.forName(custom);
      return (Predicate<Throwable>) customClass.newInstance();
    } catch (Exception e) {
      throw new IllegalBushwhackerRulesException("Custom bushwhacker matcher is configured to use "
                                                 + custom + " but is failing trying to create that "
                                                 + "with the following error " + e.getMessage(), e);
    }
  }

  public static Predicate<Throwable> makeThrownFrom(String thrownFromClass) {
    final Pattern pattern = WildcardMatcher.compileWildcardPattern(thrownFromClass);
    return new Predicate<Throwable>() {
      @Override
      public boolean apply(Throwable input) {
        StackTraceElement[] stackTrace = input.getStackTrace();
        if (stackTrace.length == 0) return false;

        String className = stackTrace[0].getClassName();
        if (pattern.matcher(className).matches()) {
          return true;
        }
        return false;
      }
    };
  }
}
