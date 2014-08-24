package com.github.steveash.bushwhacker;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ash
 */
public class TestBushwhackerRule {

  @Test
  public void shouldBeEnrichedByBw() throws Exception {
    Result result = JUnitCore.runClasses(BushwhackerRuleCase.class);
    List<Failure> fails = result.getFailures();
    assertEquals(1, fails.size());
    Failure failure = fails.get(0);
    Throwable ex = failure.getException();
    assertEquals("Replaced", ex.getMessage());
  }
}
