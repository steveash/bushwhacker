package com.github.steveash.bushwhacker.rules;

import com.google.common.base.Predicate;

import org.junit.Test;

import static com.github.steveash.bushwhacker.rules.PredicateMaker.makeCalledFrom;
import static com.github.steveash.bushwhacker.rules.PredicateMaker.makeCustom;
import static com.github.steveash.bushwhacker.rules.PredicateMaker.makeExceptionClass;
import static org.junit.Assert.*;

public class PredicateMakerTest {

  public static class AException extends RuntimeException { }
  public static class BException extends AException { }
  public static class CException extends BException { }

  @Test
  public void shouldMatchTypes() throws Exception {
    Predicate<Throwable> predA = makeExceptionClass("com.github.steveash.*AException");

    assertTrue(predA.apply(new AException()));
    assertTrue(predA.apply(new BException()));
    assertTrue(predA.apply(new CException()));
    assertFalse(predA.apply(new IllegalArgumentException()));
  }

  @Test
  public void shouldMatchStackFrames() throws Exception {
    Predicate<Throwable> predA = makeCalledFrom("com.github.steveash.*PredicateMakerTest");

    assertTrue(predA.apply(new RuntimeException()));

    Predicate<Throwable> predB = makeCalledFrom("com.somewhereelse.*.SomeClass");
    assertFalse(predB.apply(new RuntimeException()));
  }

  @Test
  public void shouldMatchMessage() throws Exception {
    Predicate<Throwable> p = PredicateMaker.makeMessageMatches("steve was here");
    assertTrue(p.apply(new RuntimeException("steve was here")));
    assertFalse(p.apply(new RuntimeException("steve was there")));
    assertFalse(p.apply(new RuntimeException("steve was here and here")));

    Predicate<Throwable> p2 = PredicateMaker.makeMessageMatches("steve was [0-9]+");
    assertTrue(p2.apply(new RuntimeException("steve was 123")));
    assertFalse(p2.apply(new RuntimeException("steve was ABC")));
  }

  @Test
  public void shouldMakeCustom() throws Exception {
    TestPred pred = (TestPred) makeCustom("com.github.steveash.bushwhacker.rules.TestPred");

    assertEquals(0, pred.count);
    assertTrue(pred.apply(new RuntimeException()));
    assertEquals(1, pred.count);
  }

  @Test
  public void shouldThrownFrom() throws Exception {
    Predicate<Throwable> pred = PredicateMaker.makeThrownFrom("com.*PredicateMakerTest");
    assertTrue(pred.apply(new RuntimeException()));
    assertFalse(pred.apply(TestPred.sampleException));
  }
}