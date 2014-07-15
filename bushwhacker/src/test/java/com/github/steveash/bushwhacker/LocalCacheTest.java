package com.github.steveash.bushwhacker;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LocalCacheTest {

  private final ExceptionHandler handler = mock(ExceptionHandler.class);
  private final CachingHandlerDecorator h = new CachingHandlerDecorator(handler);

  @Test
  public void shouldCacheWhenSeen() throws Exception {

    Exception e1 = new RuntimeException();
    Exception e2 = new RuntimeException(e1);
    Exception e4 = new RuntimeException();

    verifyCount(0);
    h.handle(e1);
    verifyCount(1);
    h.handle(e1);
    verifyCount(1);

    h.handle(e2);
    verifyCount(2);
    // shouldn't have caused a reset
    h.handle(e1);
    verifyCount(2);

    h.handle(e4);
    // should've cleared
    h.handle(e1);
    verifyCount(4);
  }

  private boolean verifyCount(int expected) {
    return verify(handler, times(expected)).handle(any(Throwable.class));
  }
}