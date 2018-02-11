# testingexercise

Some notes on this exercise:
All tests are located in src/test/java
* /unit - I did not write any mocks or stubs or other types of unit/component tests due to source availability but normally would    evaluate this.
* /integration - Where the bulk of my tests run.  Data driven as well as simple gets and post calls were automated.
* /Smoke - Simple deploy validation
* /performance - Used a simple script for Locust.io written in python to put some load on the api.  If you would like more information     on my approach toward performance testing api's, let's talk.
    
Resource files are located in src/test/resources
  * This folder also contains TestNG suite files.  My suite files pass parameters to the my base test class and are the primary means for     abstraction of the tests so that they can run in multiple environments.

Tests can be run in multiple ways:
  * In IDE be sure to setup a testng runtime configuration specifying a suite file such as /src/test/resources/stageintegration.xml
  * From Maven Command Line using mvn test -P "runstage"
  
  To Run Smoke:
  * In IDE be sure to setup a testng runtime configuration specifying a suite file such as /src/test/resources/stagesmoke.xml
  * From Maven Command Line using mvn test -P "runstagesmoke"
  
# Please see my comments in the source files for more information.
