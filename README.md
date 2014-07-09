bushwhacker
===========

A simple aspectJ library that can be used to add debugging hints for common pitfalls experienced during java development

## The Problem
Have you ever been pinged by a new or junior developer that says: "hey take a look at this stack-trace-- do you have any hints?"  Obviously, we try to build systems that fail in descriptive and helpful ways -- but sometimes things fail in ways that aren't easy to make more descriptive or are exceptions out of libraries that we can't easily edit or catch/re-throw.  In one second the experienced developer knows what the problem is or what to recommend.  

Bushwhacker is a simple, easy to use library that allows more experienced devs to provide additional debugging information (at test time, not production time) to help point junior devs in the right direction when hitting common pitfalls.  It allows you to 
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
  * The exception can be rethrown with another exception with a new message and the original exception as the cause

To use bushwhacker just:
* Include the bushwhacker dependency on your *test* classpath (not production -- you don't want to do the extra cost of testing every thrown exception at production time)
* Create an xml file in your test-resources as bushwhacker/exception-rules.xml.  You can even have bushwhacker rules in your dependencies and it'll pickup all of the exception-rules.xml it can find and add them all (in classpath order).
