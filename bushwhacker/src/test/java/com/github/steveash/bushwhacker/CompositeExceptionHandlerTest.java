package com.github.steveash.bushwhacker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.junit.Test;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CompositeExceptionHandlerTest {

  @Test
  public void shouldExecuteMultiple() throws Exception {

    ExceptionHandler e1 = mock(ExceptionHandler.class);
    ExceptionHandler e2 = mock(ExceptionHandler.class);
    ExceptionHandler e3 = mock(ExceptionHandler.class);
    CompositeExceptionHandler handler = new CompositeExceptionHandler(of(e1, e2, e3));

    verify(e1, times(0)).handle(any(Throwable.class));
    verify(e2, times(0)).handle(any(Throwable.class));
    verify(e3, times(0)).handle(any(Throwable.class));

    handler.handle(new RuntimeException());

    verify(e1, times(1)).handle(any(Throwable.class));
    verify(e2, times(1)).handle(any(Throwable.class));
    verify(e3, times(1)).handle(any(Throwable.class));
  }
}