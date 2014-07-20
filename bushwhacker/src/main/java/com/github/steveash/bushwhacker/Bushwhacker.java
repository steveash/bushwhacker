package com.github.steveash.bushwhacker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main entry point in to the bushwhacker exception enrichment library.  Use one of the static
 * factories to get an intance of bushwhacker
 *
 * @author Steve Ash
 */
public class Bushwhacker {

  public static final Logger log = LoggerFactory.getLogger(Bushwhacker.class);
  private static final String defaultRulesFileName = "bushwhacker.xml";

  /**
   * Create an instance of bushwhacker for the given rule filename.  Rules files are xml and must be
   * on the classpath.
   *
   * @return This method will always return a working instance or throw an exception
   */
  public static Bushwhacker forRules(String rulesFileNameOnClasspath) throws IOException {
    return Holder.builder.buildFromClasspath(rulesFileNameOnClasspath);
  }

  /**
   * Convenient wrapper of #forRules that swallows any exception (logging it to warn first) then
   * returns a NoOp implementation of Bushwhacker.  This is convenient when you are using
   * Bushwhacker in its typical usage scenario and want it to silently fail
   * @param rulesFileNameOnClasspath
   * @return
   */
  public static Bushwhacker tryForRules(String rulesFileNameOnClasspath) {
    try {
      return forRules(rulesFileNameOnClasspath);
    } catch (IOException e) {
      return logAndMakeNoOp(rulesFileNameOnClasspath, e);
    }
  }

  public static Bushwhacker tryForDefault() {
    return Holder.defaultInstance;
  }

  private final AtomicLong totalHandledCount = new AtomicLong(0);
  private final ExceptionChainHandler chainHandler;

  Bushwhacker(ExceptionChainHandler chainHandler) {
    this.chainHandler = chainHandler;
  }

  public void handle(Throwable t) {
    boolean handled = this.chainHandler.handle(t);
    if (handled) {
      totalHandledCount.incrementAndGet();
    }
  }

  public long totalHandledCount() {
    return totalHandledCount.get();
  }

  private static final class Holder {

    private static final BushwhackerBuilder builder = new BushwhackerBuilder();
    private static final Bushwhacker defaultInstance;

    static {
      Bushwhacker result;
      try {
        result = builder.buildFromClasspath(defaultRulesFileName);
      } catch (Exception e) {
        result = logAndMakeNoOp(defaultRulesFileName, e);
      }
      defaultInstance = result;
    }
  }

  private static Bushwhacker logAndMakeNoOp(String filename, Exception e) {
    Bushwhacker result;
    log.warn("Cannot load the bushwhacker rules [" + filename + "], continuing without "
             + "bushwhacker " + e.getMessage());
    log.debug("Complete problem while loading bushwhacker rules: ", e);
    result = new Bushwhacker(new NullExceptionChainHandler());
    return result;
  }
}
