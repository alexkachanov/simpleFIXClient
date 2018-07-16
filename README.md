# Simple FIX Client
## Description
Simple FIX Client (simplefixclient.jar) that can be used to send FIX messages to any FIX server. 

Messages are controlled by scenarious using Domain Specific Language (based on Groovy).

FIX protocol communication is based on QuickFIX/J open source library.


## Requirements and dependencies
Simple FIX Client requires Java 8. It is a Maven project (see pom.xml file) and depends on QuickFIX/J library ver. 2.0.0.


## Testing
For testing you can use quickfixj-examples-executor application that is distributed as a part of QuickFix/J distribution package.

Simple FIX Client will use FIX 4.2 protocol and will try to access port 9878 at localhost (see. simplefixclient.cfg).

Sample scenario "Scenario1.groovy" is located in scenarios sub-folder. 

You can pass any scenario as a startup parameter without .groovy extension.