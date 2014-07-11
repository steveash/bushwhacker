package com.github.steveash.bushwhacker;

import com.github.steveash.bushwhacker.rules.Rules;
import com.github.steveash.bushwhacker.rules.Rules.ExceptionRule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigLoaderTest {

  @Test
  public void shouldReadDefaults() throws Exception {
    Rules rules = ConfigLoader.getInstance().loadRulesFromClasspath("bushwhacker.xml");
    assertEquals(1, rules.getExceptionRules().size());
  }

  @Test
  public void shouldReadPopulated() throws Exception {
    Rules rules = ConfigLoader.getInstance().loadRulesFromClasspath("rule1.xml");
    assertEquals(2, rules.getExceptionRules().size());
    ExceptionRule first = rules.getExceptionRules().get(0);
    ExceptionRule second = rules.getExceptionRules().get(1);
    assertEquals("*", first.getMatches().getCalledFrom());
    assertEquals("Another exception", second.getAction().getUpdateMessage().trim());
  }
}