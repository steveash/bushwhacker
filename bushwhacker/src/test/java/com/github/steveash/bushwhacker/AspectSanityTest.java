package com.github.steveash.bushwhacker;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steve Ash
 */
public class AspectSanityTest {

  private static final Logger log = LoggerFactory.getLogger(AspectSanityTest.class);

  @Ignore // delivered turned off in aop.xml
  @Test
  public void shouldLog() throws Exception {
    log.info("Starting");

    anotherMethod();

    log.info("Ending");
  }

  public void anotherMethod() {
    log.info("In the other method");
  }
}
