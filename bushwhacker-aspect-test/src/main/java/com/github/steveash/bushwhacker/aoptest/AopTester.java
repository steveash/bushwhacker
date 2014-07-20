package com.github.steveash.bushwhacker.aoptest;

/**
 * @author Steve Ash
 */
public class AopTester {

  public Exception returnThrown() {
    try {
      callAnother();
      return null;
    } catch (Exception e) {
      return e;
    }
  }

  private void callAnother() {
    throw new AopTestException("Bad message");
  }
}
