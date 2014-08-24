package com.github.steveash.bushwhacker;

import com.github.steveash.bushwhacker.junit.BushwhackerRule;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Don't have Test at front or end of class name so surefire doesn't execute it directly
 * @author Steve Ash
 */
public class BushwhackerRuleCase {

  private static final Logger log = LoggerFactory.getLogger(BushwhackerRuleCase.class);

  @Rule
  public BushwhackerRule rule = Bushwhacker.testRuleForDefault();

  @Test
  public void shouldReplaceMessage() throws Exception {
    log.info("running the test that will throw and be enriched");
    throw new JunitTestException("original message");
  }
}
