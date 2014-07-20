package com.github.steveash.bushwhacker;

import com.github.steveash.bushwhacker.aoptest.GlobalCounter;
import com.github.steveash.bushwhacker.aoptest.TestAspect;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ash
 */
public class AspectSanityTest {

  private static final Logger log = LoggerFactory.getLogger(AspectSanityTest.class);

  @Test
  public void shouldLog() throws Exception {
    GlobalCounter.count = 0;

    log.info("Starting");

    anotherMethod();

    log.info("Ending");

    assertEquals(1, GlobalCounter.count);
  }

  public void anotherMethod() {
    log.info("In the other method");
  }
}
