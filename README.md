# Simple FIX Client
## Description
Simple FIX Client (simplefixclient.jar) is a Spring Boot Java application that can be used to send predefined FIX messages to any FIX server. 

Messages are controlled by scenarios using Domain Specific Language (based on Groovy).

FIX protocol communication is based on QuickFIX/J open source library.


## Requirements and dependencies
Simple FIX Client requires Java 8. It is a Maven project (see pom.xml file) and depends on QuickFIX/J library ver. 2.0.0.

## Building
To build Simple FIX Client run <code>mvn clean install</code> or use <code>package.bat</code>. 

Resulting simplefixclient-&lt;version&gt;.jar JAR file will be created in <code>target</code> sub-folder.

This JAR file is a Spring Boot jumbo-JAR that contains all dependencies required to run the application.

## Running
Start the application using:

<code>
jar -jar target/simplefixclient-&lt;version&gt;.jar simplefixclient.cfg 
</code>

or use <code>start.bat</code> or <code>start.sh</code> script. 

## Testing
For testing you can use quickfixj-examples-executor application that is distributed as a part of QuickFIX/J distribution package.

Simple FIX Client will use FIX 4.2 protocol and will try to access port 9878 at localhost (see. simplefixclient.cfg).

Sample Groovy DSL scenario "Scenario1.groovy" is located in scenarios sub-folder. 

You can pass any scenario as a startup parameter (without .groovy extension).