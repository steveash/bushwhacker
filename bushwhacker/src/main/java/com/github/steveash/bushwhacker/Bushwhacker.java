package com.github.steveash.bushwhacker;

import com.github.steveash.bushwhacker.junit.BushwhackerRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main entry point in to the bushwhacker exception enrichment library.  Use one of the static
 * factories to get an instance of bushwhacker
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
   * Creates a JUnit Rule instance for the bushwhacker rules specified by rulesFileNameOnClasspath
   * this will fail if the rules can't be read.  You use this by adding a field in your junit
   * test class of type BushwhackerRule annotated with Junit's @Rule annotation.  E.g.:
   *
   * <pre>
   *   @Rule
   *   private BushwhackerRule rule = Bushwhacker.testRuleFor("myRules.xml");
   * </pre>
   *
   * @param rulesFileNameOnClasspath
   * @return
   */
  public static BushwhackerRule testRuleFor(String rulesFileNameOnClasspath) {
    try {
      return new BushwhackerRule(forRules(rulesFileNameOnClasspath));
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot load Bushwhacker rules named " +
                                         rulesFileNameOnClasspath, e);
    }
  }

  /**
   * Gets the default bushwhacker rule instance from reading bushwhacker.xml from the classpath
   * if these rules cant be found a warning will be logged and a no-op bushwhacker instance will
   * be returned.  See #testRuleFor for an example of how to use bushwhacker in your code
   * @return
   */
  public static BushwhackerRule testRuleForDefault() {
    return new BushwhackerRule(tryForDefault());
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
