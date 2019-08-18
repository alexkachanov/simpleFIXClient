# Simple FIX Client
## Description
Simple FIX Client (simplefixclient.jar) is a Spring Boot Java application that can be used to send predefined FIX messages to any FIX server. 

Messages are controlled by scenarios using Domain Specific Language (based on Groovy).

FIX protocol functionality is based on <a href="https://www.quickfixj.org/">QuickFIX/J open source library</a>.

## Requirements and Dependencies
Simple FIX Client requires Java 8 (can also run on Java 11 LTS or latest Java 12). 

It is a Maven project (see pom.xml, file Apache Maven 3.6.1 is recommended) and depends on:
* Spring 5.1.9 https://spring.io/projects/spring-framework
* Spring Boot 2.1.7 https://spring.io/projects/spring-boot
* QuickFIX/J 2.1.1 https://github.com/quickfix-j/quickfixj 
* Groovy 1.8.9

All dependencies are downloaded from Internet when you run <code>mvn clean install</code>.

## Building
Since project is a Spring Boot project, <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html">Spring Boot Maven</a> build plugin is used to build the artifact.

To build Simple FIX Client run <code>mvn clean install</code> or <code>mvn package</code> or use <code>package.bat</code> in the project folder.

Resulting simplefixclient-&lt;version&gt;.jar JAR file will be created in <code>target</code> sub-folder.

This JAR file is a Spring Boot jumbo-JAR that contains all dependencies required to run the application.

## Running
Start the application using:

<code>jar -jar target/simplefixclient-&lt;version&gt;.jar simplefixclient.cfg</code>

or use <code>mvn spring-boot:run<code> command

or use <code>start.bat</code> or <code>start.sh</code> script.

## Eclipse
Before opening project in Eclipse, run <code>mvn eclipse:eclipse</code> task in the project's folder. Maven will create Eclipse specific project files. Then import the project into your workspace as a Maven project.

To run project in Eclipse, use Maven task <code>spring-boot:run</code>. 

## Testing
For testing you can use <a href="https://www.quickfixj.org/usermanual/2.0.0/usage/examples.html">quickfixj-examples-executor application</a> that is distributed as a part of QuickFIX/J distribution package.

Simple FIX Client will use FIX 4.2 protocol and will try to access port 9878 at localhost (see. simplefixclient.cfg).

Sample Groovy DSL scenario "Scenario1.groovy" is located in <code>scenarios</code> sub-folder. 

You can pass any scenario as a startup parameter (without .groovy extension).