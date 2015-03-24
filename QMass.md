# QMass Reference #


---


# Introduction #

QMass is a Clustering solution for Enterprise applications.


---

# Development Process #
I provide new snapshots for download as I implement new features listed on the TODO wiki. I also keep this page as current as possible and try to give clear examples on how to use this api.
I upload two artifacts, and deprecate old ones. One is the qmass.jar other is qmass distribution package which contains some examples and dependencies to run them.


---

# IR for Configuring QMass #
Apart from configuring QMass using app./framework specific methods, like Hibernate properties, you could use the IR api.

QMass could be configured using _/ir.properties_ file and a configuration class that implements _org.mca.qmass.core.ir.QMassIR_ .

An IR file on the classpath, with the line :
```
default,QMassIR=org.mca.qmass.web.ir.MyIR
```
will use the _MyIR_ as the configuration class for the QMass instance with _"default"_ id.

Here is how your configuration class _MyIR_ should look like :
```
public class MyIR extends org.mca.qmass.core.ir.DefaultQMassIR {

    @Override
    public String getMulticastAddress() {
        return "230.0.0.1";
    }

    @Override
    public EventService getDiscoveryEventService(QMass qmass, DiscoveryService discoveryService, InetSocketAddress listening) {
        return new MulticastClusterManager(qmass, discoveryService, listening);
    }
    
}
```

The default configuration is overridden and QMass is configured with the specified multicast address.


---

# Cluster Discovery #
Currently QMass comes with two different cluster discovery methods through multicast address or ip port scanning. Default multicast address is 230.0.0.1, default port scan range is localhost 6661-6670.


---

# Protocol #
QMass protocol currently uses P2P over TCP communication for messaging among cluster though different networking models are planned for future.  Cluster discovery code relies on UDP packets.


---

# As a Data Grid #
QMass could be used as a data grid or an in-memmory db. Like it's predecessors key value pairs would be distributed over your cluster so that there is no duplication.
To set up a grid each cluster member register _QMassGrid_ as a service :
```
    QMass qm = new QMass("test");
    QMassGrid grid = new QMassGrid(qm);
```
Put, get and remove as normally you would on Map :
```
    grid.put(1L, myObject);
```
## Configuring with IR ##
Apart from using the default your main QMass IR might implement the _org.mca.qmass.grid.ir.QMassGridIR_ :
```
    public interface QMassGridIR {

        // If true QMass will wait for a put confirmation
        // defaults to true
        boolean getWaitForPutResponse();

        // If true QMass will wait for remove confirmation
        // defaults to true
        boolean getWaitForRemoveResponse();

    }
```
## Console Application ##
You may start the console application where you may access your data grid. To do so if you have downloaded the distribution package simply call the qconsole.bat script or type the following with the correct path to the dependencies:
```
    java -cp qmass.jar;dependencies/mongo-java-driver-2.5.2.jar org.mca.qmass.console.ConsoleMain
```
Console application processes the commands using the JavaScript shell. Type help for the most current commands.
Here is an example session on how you may put, get and remove to the map variable named 'm' :
```
  Welcome to QMassConsole!
  Type ; 'help' for help, 'bye' or 'Ctrl + C' to exit
  @/192.168.1.241:1880
  Cluster : [/192.168.1.241:1877]

> m = console.getMap("m")
  org.mca.qmass.grid.QMassGrid@b754b2

> m.put(1,"mca")
  true

> m.get(1)
  mca

> m.remove(1)
  mca

>
```
Type bye or Ctrl+C to exit.

---

# Hibernate 2nd Level Cache Support #
An example persistence.xml definition using QMass would be like :
```
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence 
   http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd"
             version="1.0">
    <persistence-unit name="mypersistence" transaction-type="JTA">
        <provider>org.hibernate.ejb.HibernatePersistence</provider>
        <jta-data-source>yourdatasource</jta-data-source>
        <properties>
            <property name="hibernate.dialect" value="org.hibernate.dialect.Oracle10gDialect"/>
            <property name="hibernate.cache.use_query_cache" value="true"/>
            <property name="hibernate.cache.use_second_level_cache" value="true"/>
            <property name="hibernate.cache.provider_class" 
   value="org.mca.qmass.cache.hibernate.provider.QMassHibernateCacheProvider"/>
            <property name="qmass.cluster" value="localhost,6661,6670/"/>
            <property name="qmass.name" value="myproject"/>
            <property name="qmass.replicate.updates" value="false"/>
            <property name="qmass.replicate.inserts" value="false"/>
        </properties>
    </persistence-unit>
</persistence>
```
QMass cache provider :
```
<property name="hibernate.cache.provider_class" 
   value="org.mca.qmass.cache.hibernate.provider.QMassHibernateCacheProvider"/>
```
Instead of using cache provider you may also use region factory :
```
<property name="hibernate.cache.region.factory_class"
   value="org.mca.qmass.cache.hibernate.factory.QMassRegionFactory"/>
```
## Discovering Cluster with IP Port Scan ##
Define the cluster IP and port range (defaults to localhost,6661,6670/) :
```
<property name="qmass.cluster" value="10.10.10.112,6661,6670/10.10.10.113,6661,6670/"/>
```
Upon start each application will try to allocate a port and greet others in the port range.
## Multicast IP ##
Instead of using the above method you could use multicast ip for communication :
```
<property name="qmass.multicast.cluster" value="230.0.0.1"/>
```
Port which will be used to write to (defaults to 4445):
```
<property name="qmass.multicast.writeport" value="4445"/>
```
This port will be incremented until a free port is found.

Port to read from (defaults to 4444):
```
<property name="qmass.multicast.readport" value="4444"/>
```
## Serialize/Deserialize Values ##
By default QMass Cache will propagate the invalidations across cluster. To serialize and propagate the inserted values and updates you may use these properties :
```
<property name="qmass.replicate.updates" value="true"/>
<property name="qmass.replicate.inserts" value="true"/>
```
## Cached vs Grid Data ##
Note that cached data is always on the server and multiple copies exist allowing instant access unlike the data managed by grid.

---

# Session Sharing #
QMass allows you to replicate session data amongst, applications.

You have to define the _QMassGridedFilter_ on _web.xml_ :
```
<filter>
    <filter-name>QMassFilter</filter-name>
    <filter-class>org.mca.qmass.http.grid.QMassGridedFilter</filter-class>
    <init-param>
        <param-name>qmass.name</param-name>
        <param-value>fastdog</param-value>
    </init-param>
</filter>

<filter-mapping>
    <filter-name>QMassFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```
This will use the QMass instance with _fastdog_ id.
By default QMass will try to replicate all the session attributes (except those starting with com.sun.faces).
You may also choose to filter session attributes based on annotations. Your configuration should implement the QMassHttpIR apart from QMassIR :
```
public class MyIR extends DefaultQMassIR implements QMassHttpIR {

    private ClusterAttributeFilter attributeFilter = new SharedClusterAttributeFilter();

    @Override
    public ClusterAttributeFilter getClusterAttributeFilter() {
        return attributeFilter;
    }
...
}
```
_getClusterAttributeFilter_ method should return the attribute filter in this case _SharedClusterAttributeFilter_ .
_SharedClusterAttributeFilter_ only replicates the objects which are annotated with _@Shared_ annotation. So JSF managed beans that should be replicated should look like :
```
@ManagedBean
@SessionScoped
@Shared
public class Hello implements Serializable {

    private String name;
...
}
```
Before completing the response cycle _QMassGridedFilter_ will sync the session attributes to the grid. This uses the Grid Service defined above.
# Change The Log Levels Cluster Wide #
A Simple example application changing the application log levels cluster wide, works with log4j :
```
new DefaultLogService("logService",QMass.getQMass()).changeLog("org.apache.myfaces","DEBUG");
```
Constructor creates and registers it self as a service on the QMass. Services could be accessed using qmass getService method whit an id ("logService" in this case).