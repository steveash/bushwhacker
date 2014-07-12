bushwhacker
===========

A simple aspectJ library that can be used to add debugging hints for common pitfalls experienced during java development

## The Problem
Have you ever been pinged by a new or junior developer that says: "hey take a look at this stack-trace-- do you have any hints?"  Obviously, we try to build systems that fail in descriptive and helpful ways -- but sometimes things fail in ways that aren't easy to make more descriptive or are exceptions out of libraries that we can't easily edit or catch/re-throw.  In a second after glancing at the stack trace, the experienced developer knows what the problem is.  Would be nice if we could automate this...

## The Solution
Bushwhacker is a small library that allows more experienced devs to provide additional debugging information and hints to junior devs at development time when certain situations occur.  The experienced devs pinpoint (using simple rules) which exceptions should be "enriched" and what info to add.  Bushwhacker (through aspectJ or through JUnit Rules) will intercept these exceptions and re-write their messages to add the additional informatoin.  Bushwhacker is also extensible allowing you to provide custom detection and enrichment logic.  

Bushwhacker allows you to
* Describe rules to pinpoint which exceptions should be caught and what to do with them
* Rules can be fairly specific using conjunctive constraints such as:
  * Exception of a certain type or subtype
  * Exception of a particular package
  * Exception thrown from a particular class or package
  * Exception throw from a location with a preceeding call stack frame from a particular class/method
  * Exception message matching some regular expression
  * Exception messages containing some text
  * Custom class that you implement to determine how to catch or what to do
* Once matched an exception can be
  * Logged out as is with accompanying informational messages
  * The exception message can be replaced with a new messaage containing additional information
  * Handled by a class you write

You can use Bushwhacker by:
* adding it to your aop.xml to use load-time-weaving to catch exceptions as they are thrown/re-thrown.  This is useful in situations where you want to catch exceptions that are being swallowed by parts of your system.  You could log these before they get swallowed, for example.
* Or if you don't wnat to use AspectJ you can use a JUnit @Rule to enrich the exceptions as they propogate to JUnit so the developer will get the enriched messages when JUnit prints the exception information
* Just invoke it yourself whenever is best for you

To use bushwhacker just:
* Configure aspectJ load time weaving through the javaagent command
* Include the bushwhacker dependency on your *test* classpath (not production -- you don't want to do the extra cost of testing every thrown exception at production time)
* Create an xml file in your test-resources as bushwhacker/exception-rules.xml.  You can even have bushwhacker rules in your dependencies and it'll pickup all of the exception-rules.xml it can find and add them all (in classpath order).
