package com.github.steveash.bushwhacker.junit;

import com.github.steveash.bushwhacker.Bushwhacker;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * JUnit rule that can be used to catch and enrich bushwhacker rules that escape Junit
 * test cases
 * @author Steve Ash
 */
public class BushwhackerRule implements TestRule {

  private final Bushwhacker bw;

  public BushwhackerRule(Bushwhacker bw) {
    this.bw = bw;
  }

  @Override
  public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        try {
          base.evaluate();
        } catch (Throwable t) {
          bw.handle(t);
          throw t;
        }
      }
    };
  }
}
