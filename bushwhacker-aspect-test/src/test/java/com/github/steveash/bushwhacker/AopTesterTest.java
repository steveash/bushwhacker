package com.github.steveash.bushwhacker;

import com.github.steveash.bushwhacker.aoptest.AopTester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class AopTesterTest {

  private static final Logger log = LoggerFactory.getLogger(AopTesterTest.class);

  @org.junit.Test
  public void shouldFire() throws Exception {

    Exception e = new AopTester().returnThrown();
    log.info("Returned ", e);

    assertEquals("Good message", e.getMessage());
  }
}
