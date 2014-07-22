package com.github.steveash.bushwhacker.aop;

import com.github.steveash.bushwhacker.Bushwhacker;

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

  @Pointcut("myTypes() && within(java.lang.Object+) && !handler(*) && call(* *..*(..)) && !within(*..*EnhancerBy*) && !within(*..*CGLIB*)")
  public void eligibleTypes() {
  }

  @AfterThrowing(pointcut = "eligibleTypes() && !thisAdvice()", throwing = "e")
  public void afterThrown(Exception e) {
    Bushwhacker.tryForDefault().handle(e);
  }
}
