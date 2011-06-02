QMass, the Clustering Solution or your Enterprise

 * Project info

Project homepage at ;
http://code.google.com/p/qmass/

Check the reference documentation for usage ;
http://code.google.com/p/qmass/wiki/QMass

 * Zip package contains

/qmass.jar contains the qmass.
/examples contains the example applications.

 * Examples
Switch to examples directory and run the following command to start the examples;

> java -jar qmass_runner.jar

By default this will start 5 different servers running example applications.
You may specify the number of instance with;

> java -jar qmass_runner.jar 2

To access the application enter the following on your browser;
http://localhost:8080/qmassx0
http://localhost:8081/qmassx1

and so on ...

The session sharing example (chatty) will join the sessions (as long as they are started
on the same browser) and will share the text values accross the cluster and update the
chatboard.