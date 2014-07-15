package com.github.steveash.bushwhacker.util;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class WildcardMatcherTest {

  @Test
  public void shouldMatchWildcards() throws Exception {
    assertMatch(true, "com.github.steve.whatever.SomeClass", "com.github.*.whatever.*");
    assertMatch(false, "com.github.steve.nope.SomeClass", "com.github.*.whatever.*");
    assertMatch(true, "com.github.steve.nope.SomeClass", "*");
    assertMatch(true, "com.github.steve.nope.SomeClass(AMethod:102)", "*.SomeClass(*)");
  }

  private void assertMatch(boolean shouldMatch, String candidate, String pattern) {
    Pattern p = WildcardMatcher.compileWildcardPattern(pattern);
    boolean matches = p.matcher(candidate).matches();
    if (shouldMatch) {
      assertTrue(candidate + " should match " + pattern, matches);
    } else {
      assertFalse(candidate + " shouldnt match " + pattern, matches);
    }
  }
}