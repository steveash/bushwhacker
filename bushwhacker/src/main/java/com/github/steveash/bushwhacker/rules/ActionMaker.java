package com.github.steveash.bushwhacker.rules;

import com.google.common.base.Function;

import com.github.steveash.bushwhacker.Bushwhacker;
import com.github.steveash.bushwhacker.MessageEnricher;

/**
 * @author Steve Ash
 */
public class ActionMaker {

  public static Function<Throwable,Throwable> makeAddHint(final String hint) {
    return new Function<Throwable, Throwable>() {
      @Override
      public Throwable apply(Throwable input) {
        MessageEnricher.enrich(input, hint);
        return input;
      }
    };
  }

  public static Function<Throwable, Throwable> makeReplace(final String newMessage) {
    return new Function<Throwable, Throwable>() {
      @Override
      public Throwable apply(Throwable input) {
        MessageEnricher.replace(input, newMessage);
        return input;
      }
    };
  }

  public static Function<Throwable,Throwable> makeLog() {
    return new Function<Throwable, Throwable>() {
      @Override
      public Throwable apply(Throwable input) {
        Bushwhacker.log.warn("Bushwhacker detected " + input.getClass().getSimpleName() + " and "
                             + "is just logging it here and then letting the program handle it. "
                             + input.getMessage(), input);
        return input;
      }
    };
  }

}
