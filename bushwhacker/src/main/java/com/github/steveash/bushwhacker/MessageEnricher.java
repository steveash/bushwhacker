package com.github.steveash.bushwhacker;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;

import com.github.steveash.bushwhacker.util.SerializationUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * Knows how to construct the "enriched" messages and update the existing detail Messages
 *
 * @author Steve Ash
 */
public class MessageEnricher {

  private static final CharMatcher wsMatcher = CharMatcher.WHITESPACE;

  private static final SerializationUtil.FieldSetter<Throwable> detailMessageSetter =
      SerializationUtil.getFieldSetter(Throwable.class, "detailMessage");

  private static final int bannerSize = 75;

  public static String createMessage(String original, String updateMessage,
                                     String exceptionMatched) {

    updateMessage = StringUtils.replace(updateMessage, "\n", " ");
    updateMessage = wsMatcher.collapseFrom(updateMessage, ' ');

    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    sb.append(Strings.repeat("*", bannerSize));
    sb.append('\n');
    sb.append("  Bushwhacker matched an exception ").append(exceptionMatched);
    sb.append(" and wants to help you try\n  and diagnose your problem").append('\n');
    sb.append('\n');
    if (original != null) {
      sb.append("  The original (matching) problem was: " + original).append('\n');
      sb.append('\n');
    }
    sb.append("  The following hint is provided to help you:").append('\n');
    sb.append("    ").append(updateMessage).append('\n');
    sb.append('\n');
    sb.append(Strings.repeat("*", bannerSize));

    return sb.toString();
  }

  /**
   * Updates the detail message in the throwable to be the enriched message; note that there is an
   * assumption that this will be caught and enriched and propogated by the same thread and thus we
   * have no thread/visibility concerns.
   */
  public static void enrich(Throwable t, String updateMessage) {
    String newMessage = createMessage(t.getMessage(), updateMessage, t.getClass().getSimpleName());
    replace(t, newMessage);
  }

  public static void replace(Throwable t, String newMessage) {
    detailMessageSetter.set(t, newMessage);
  }
}
