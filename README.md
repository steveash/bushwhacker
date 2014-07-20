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

## Getting Started
Bushwhacker is on Maven Central (well it will be shortly!).  So added a dependency:

```xml
<dependency>
    <groupId>com.github.steveash.bushwhacker</groupId>
    <artifactId>bushwhacker</artifactId>
    <version>1.0</version>
</dependency>
```

Next you need to create the Bushwhacker rules file to describe what exceptions to catch. Create
a bushwhacker.xml file in src/test/resources that looks like:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<rules>
  <exceptions>
    <exception>
      <matches>
        <exceptionClass>org.springframework.beans.factory.NoSuchBeanDefinitionException</exceptionClass>
        <calledFrom>org.springframework.transaction.aspectj.AbstractTransactionAspect*</calledFrom>
        <messageMatches>.*No qualifying bean of type.*PlatformTransactionManager.*</messageMatches>
      </matches>
      <action>
        <addHint>
        This usually happens when your unit test extends from the wrong test fixture. 
        Since you are using something that needs the database, extend from 
        DatabaseTestFixture. You might not even realize that your trying to use the 
        database, but some code is going over an @Transactional which is causing it to 
        try and use the database.
        </addHint>
      </action>
    </exception>
  </exceptions>
</rules>
```

If you already have load time weaving in your project setup, great! Skip to the next step.  If you
don't then you need to modify your Surefire configuration to add load time weaving.  Add this to 
your parent POM:

```xml
<project ...>
  ...
  <properties>
    <aspectj-version>1.8.1</aspectj-version>
    ...
  </properties>
  
  <dependencies>
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>${aspectj-version}</version>
      <scope>test</scope> <!-- we're only using it to help in tests -->
    </dependency>
    ...
  </dependencies>
  
  <build>
    <plugins>
      <plugin>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.17</version>
        <dependencies>
          <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>${aspectj-version}</version>
          </dependency>
          <dependency>
            <groupId>org.apache.maven.surefire</groupId>
            <artifactId>surefire-junit47</artifactId>
            <version>2.17</version>
          </dependency>
        </dependencies>
        <configuration>
          <argLine>
            -javaagent:${user.home}/.m2/repository/org/aspectj/aspectjweaver/${aspectj-version}/aspectjweaver-${aspectj-version}.jar
          </argLine>
          <skipTests>false</skipTests>
          <testFailureIgnore>false</testFailureIgnore>
        </configuration>
      </plugin>
      ...
```

This sets the javaagent up to do load time weaving when you run your unit tests.  Intellij is also 
smart enough to figure out to include this when running unit tests via the IDE.

Lastly, you have to enable the AspectJ advice to weave your classes.  This is done by creating
a file: `src/test/resources/META-INF/aop.xml` in your project.  This file needs to contain:

```xml
<?xml version="1.0" ?>
<aspectj>
  <aspects>
    <concrete-aspect name="com.yourproject.MyExceptionAspect"
        extends="com.github.steveash.bushwhacker.aop.ExceptionAspect">
      <pointcut name="myTypes" expression="within(com.yourproject..*)"/>
    </concrete-aspect>
  </aspects>
  <weaver options="-warn:none -Xlint:ignore" />
</aspectj>
```

That's it!  The bushwhacker.xml above catches the No Bean exception thrown when trying to
setup a database transaction and adds a helpful tip for other devs who stumble across this 
problem.  Now when you run the unit test you see an exception message like:

```
org.springframework.beans.factory.NoSuchBeanDefinitionException: 
***************************************************************************
  Bushwhacker matched an exception NoSuchBeanDefinitionException and wants to help you try
  and diagnose your problem

  The original (matching) problem was: No qualifying bean of type [org.springframework.transaction.PlatformTransactionManager] is defined

  The following hint is provided to help you:
     This usually happens when your unit test extends from the wrong test fixture. Since you are using something that needs the database, extend from DatabaseTestFixture. You might not even realize that your trying to use the database, but some code is going over an @Transactional which is causing it to try and use the database. 

***************************************************************************
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:319)
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:985)
	...
```