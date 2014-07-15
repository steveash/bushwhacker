package com.github.steveash.bushwhacker;

import com.github.steveash.bushwhacker.rules.ConfigLoader;
import com.github.steveash.bushwhacker.rules.HandlerMaker;
import com.github.steveash.bushwhacker.rules.XmlRules;

import java.io.IOException;

/**
 * @author Steve Ash
 */
public class BushwhackerBuilder {

  private final ConfigLoader loader = ConfigLoader.getInstance();
  private final HandlerMaker handlerMaker = new HandlerMaker();

  public Bushwhacker buildFromClasspath(String rulesFilename) throws IOException {
    XmlRules xmlRules = loader.loadRulesFromClasspath(rulesFilename);
    ExceptionHandler handler = handlerMaker.buildHandlersFor(xmlRules);
    CachingHandlerDecorator cachingHandler = new CachingHandlerDecorator(handler);
    ExceptionChainHandler chainHandler = new DefaultExceptionChainHandler(cachingHandler);
    return new Bushwhacker(chainHandler);
  }
}
