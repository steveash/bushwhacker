package com.github.steveash.bushwhacker.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steve Ash
 */
@Aspect
public abstract class ExceptionAspect {

  private static final Logger log = LoggerFactory.getLogger(ExceptionAspect.class);

  @Pointcut("")
  public abstract void myTypes();

  @Pointcut("cflow(adviceexecution() && within(ExceptionAspect))")
  public void thisAdvice() {
  }

  @Pointcut("cflow(within(junit..*))")
  public void frameworkCode() {
  }

  @Pointcut("myTypes() && within(java.lang.Object+) && !handler(*)")
  public void eligibleTypes() {
  }

  @AfterThrowing(pointcut = "eligibleTypes() && !frameworkCode() && !thisAdvice()", throwing = "e")
  public void afterThrown(Exception e) {
    log.error("Catching thrown ", e);
  }
}
