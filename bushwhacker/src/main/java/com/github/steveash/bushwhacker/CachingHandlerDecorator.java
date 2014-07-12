package com.github.steveash.bushwhacker;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Ensures that we don't enrich the same exception more than once as its being thrown and rethrown
 *
 * @author Steve Ash
 */
public class CachingHandlerDecorator implements ExceptionHandler {

  public CachingHandlerDecorator(ExceptionHandler delegate) {
    this.delegate = delegate;
  }

  private static class LocalCache {

    final Set<Throwable> seen = Sets.newIdentityHashSet();
  }

  private final ThreadLocal<LocalCache> locals = new ThreadLocal<LocalCache>() {
    @Override
    protected LocalCache initialValue() {
      return new LocalCache();
    }
  };

  private final ExceptionHandler delegate;

  @Override
  public boolean handle(Throwable candidate) {
    LocalCache local = locals.get();

    clearIfNewThrowChain(candidate, local);
    if (hasAlreadyHandled(candidate, local)) {
      return false;
    }

    local.seen.add(candidate);
    return delegate.handle(candidate);
  }

  private boolean hasAlreadyHandled(Throwable candidate, LocalCache local) {
    return local.seen.contains(candidate);
  }

  private void clearIfNewThrowChain(Throwable candidate, LocalCache local) {
    // if we haven't seen anything from this causal chain assume its a new throw chain and clear
    Throwable e = candidate;
    while (e != null) {
      if (hasAlreadyHandled(e, local)) {
        return; // not new since we've seen one of em before
      }
    }
    local.seen.clear();
  }
}
