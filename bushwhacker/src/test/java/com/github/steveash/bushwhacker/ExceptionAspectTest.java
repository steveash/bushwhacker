package com.github.steveash.bushwhacker;

import org.junit.Test;

/**
 * @author Steve Ash
 */
public class ExceptionAspectTest {

  public static class TestException extends RuntimeException {

  }

  @Test
  public void shouldInterceptException() throws Exception {
    try {
      callAndThrow();
    } catch (TestException e) {
      // no op
    }
  }

  private void callAndThrow() {
    throw new TestException();
  }
}
