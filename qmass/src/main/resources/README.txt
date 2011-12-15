QMass, the Clustering Solution or your Enterprise

 * Project info
------------------------------------------------------------------------------------
Project homepage at ;
http://code.google.com/p/qmass/

Check the reference documentation for usage ;
http://code.google.com/p/qmass/wiki/QMass

Try out the console on cloud :
http://qmass.cloudfoundry.com

 * Zip package contains
------------------------------------------------------------------------------------
/qmass.jar contains the qmass.

/dependencies contains other dependent jars as well


 * QMass Console Application
------------------------------------------------------------------------------------
Run the qconsole.bat to start a qmass console application.

> qconsole.bat

Start up multiple console's which will get connected forming a grid.

to put first creat a map and assign it :
> m = console.getMap("m")

Then 'put' to put values:
> m.put(1,"foo")
> [QMassConsole] Start system logs;
> 20 Haz 2011 10:52:30,113 - DEBUG - key 1 matched to : LocalGridNode{targetSocket=localhost/127.0.0.1:6662}
> [QMassConsole] End system logs;
> [QMassConsole] put ok

QMassConsole informs that put is successfull. It also print outs system logs so you can watch what's going on.
Here the put is propagated to the local storage.

> m.put(2,"boo")
> [QMassConsole] Start system logs;
> 20 Haz 2011 10:52:35,254 - DEBUG - key 2 matched to : QMassGridNode{targetSocket =localhost/127.0.0.1:6661}
> 20 Haz 2011 10:52:35,254 - DEBUG - org.mca.qmass.grid.service.DefaultGridService@1fd54c4 send put for : 2, Q
> 20 Haz 2011 10:52:35,254 - DEBUG - localhost/127.0.0.1:6662, default sending PutRequest{requestNo=Id{id=1, key=localhost/127.0.0.1:6661}, key=2, value=Q} Event{id=default, handler=PutRequestEventHandler, service=localhost/127.0.0.1:6662} to localhost/127.0.0.1:6661
> 20 Haz 2011 10:52:35,269 - DEBUG - localhost/127.0.0.1:6662, default received; PutResponse{requestNo=Id{id=1, key=localhost/127.0.0.1:6661}, successfull=true} Event{id=default, handler=PutResponseEventHandler, service=localhost/127.0.0.1:6661}, service : org.mca.qmass.grid.service.DefaultGridService@1fd54c4
> 20 Haz 2011 10:52:35,269 - DEBUG - org.mca.qmass.grid.service.DefaultGridService@1fd54c4 response : PutResponse{requestNo=Id{id=1, key=localhost/127.0.0.1:6661}, successfull=true} Event{id=default, handler=PutResponseEventHandler, service=localhost/127.0.0.1:6661}
> 20 Haz 2011 10:52:35,269 - DEBUG - time spent waiting for response : 15
> [QMassConsole] End system logs;
> [QMassConsole] put ok

This time we can see from the logs that the put is propagated to the connected qmass.

You may also use 'get' to get values:
> m.get(1)
> [QMassConsole] Start system logs;
> 20 Haz 2011 10:52:37,988 - DEBUG - key 1 matched to : LocalGridNode{targetSocket=localhost/127.0.0.1:6662}
> [QMassConsole] End system logs;
> [QMassConsole] get 1 returns 'Q'

Tye 'bye' to exit