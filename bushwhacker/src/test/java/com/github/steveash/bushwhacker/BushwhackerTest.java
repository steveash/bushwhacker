package com.github.steveash.bushwhacker;

import com.github.steveash.bushwhacker.util.ThrowUtil;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BushwhackerTest {

  private static final Logger log = LoggerFactory.getLogger(BushwhackerTest.class);
  private Bushwhacker bw;

  @Before
  public void setUp() throws Exception {
    bw = Bushwhacker.forRules("rule2.xml");
  }

  @Test
  public void shouldHandleTypeMatch() throws Exception {
    assertEquals(0, bw.totalHandledCount());

    bw.handle(new IllegalArgumentException("shouldnt match"));
    assertEquals(0, bw.totalHandledCount());

    AException e1 = new AException();
    bw.handle(e1);
    log.info("After handling " + e1.getMessage());
    assertTrue(e1.getMessage().contains("Caught the AException"));
    assertEquals(1, bw.totalHandledCount());
  }

  @Test
  public void shouldHandleReplacement() throws Exception {
    BException e2 = null;
    try {
      new ThrowUtil().throwBException();
    } catch (BException e) {
      bw.handle(e);
      e2 = e;
    }
    assertNotNull(e2);
    assertEquals(1, bw.totalHandledCount());
    assertEquals("My new message is BException", e2.getMessage());
  }

  @Test
  public void shouldHandleMessageMatch() throws Exception {
    CException e3 = new CException("This is the song that never ends");
    bw.handle(e3);
    assertEquals(1, bw.totalHandledCount());
  }
}