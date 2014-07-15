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
    this.locals = new LocalCache();
    this.delegate = delegate;
  }

  public static class LocalCache {

    private final ThreadLocal<Set<Throwable>> locals = new ThreadLocal<Set<Throwable>>() {
        @Override
        protected Set<Throwable> initialValue() {
          return Sets.newIdentityHashSet();
        }
      };

    public Set<Throwable> getSeen() {
      return locals.get();
    }
  }

  private final LocalCache locals;
  private final ExceptionHandler delegate;

  @Override
  public boolean handle(Throwable candidate) {
    Set<Throwable> seen = locals.getSeen();

    clearIfNewThrowChain(candidate, seen);
    if (hasAlreadyHandled(candidate, seen)) {
      return false;
    }

    seen.add(candidate);
    return delegate.handle(candidate);
  }

  private boolean hasAlreadyHandled(Throwable candidate, Set<Throwable> seen) {
    return seen.contains(candidate);
  }

  private void clearIfNewThrowChain(Throwable candidate, Set<Throwable> seen) {
    // if we haven't seen anything from this causal chain assume its a new throw chain and clear
    Throwable e = candidate;
    while (e != null) {
      if (hasAlreadyHandled(e, seen)) {
        return; // not new since we've seen one of em before
      }
      e = e.getCause();
    }
    clear(seen);
  }

  public void reset() {
    clear(locals.getSeen());
  }

  private void clear(Set<Throwable> seen) {
    seen.clear();
  }
}
