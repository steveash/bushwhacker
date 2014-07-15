package com.github.steveash.bushwhacker.util;

import com.google.common.base.CharMatcher;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

import static com.google.common.base.CharMatcher.anyOf;
import static com.google.common.base.CharMatcher.inRange;
import static org.apache.commons.lang3.StringUtils.replace;

/**
 * @author Steve Ash
 */
public class WildcardMatcher {

  private static final CharMatcher validChars =
      inRange('a', 'z').or(inRange('0', '9')).or(inRange('A','Z')).or(anyOf(".?*():"));

  public static Pattern compileWildcardPattern(String pattern) {
    if (!validChars.matchesAllOf(pattern)) {
      throw new IllegalArgumentException("Cannot match the pattern \"" + pattern +
                                         "\" because some of the characters in the pattern are not "
                                         + "valid");
    }
    pattern = replace(pattern, ".", "\\.");
    pattern = replace(pattern, "(", "\\(");
    pattern = replace(pattern, ")", "\\)");

    pattern = replace(pattern, "?", ".?");
    pattern = replace(pattern, "*", ".*");

    return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
  }
}
