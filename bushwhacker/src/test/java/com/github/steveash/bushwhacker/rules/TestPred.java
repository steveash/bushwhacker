package com.github.steveash.bushwhacker.rules;

import com.google.common.base.Predicate;

/**
* @author Steve Ash
*/
public class TestPred implements Predicate<Throwable> {

  public static final RuntimeException sampleException = new RuntimeException();

  public int count = 0;
  @Override
  public boolean apply(Throwable input) {
    count += 1;
    return true;
  }
}
