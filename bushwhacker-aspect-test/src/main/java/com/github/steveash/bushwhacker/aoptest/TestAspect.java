package com.github.steveash.bushwhacker.aoptest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steve Ash
 */
@org.aspectj.lang.annotation.Aspect
public class TestAspect {

  private static final Logger log = LoggerFactory.getLogger(TestAspect.class);

  @org.aspectj.lang.annotation.Before("execution(* com.github.steveash.bushwhacker.AspectSanityTest.*(..))")
  public void beforeLog() {
    GlobalCounter.count += 1;
    log.info("Logging before the method!");
  }
}
