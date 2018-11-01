# jmsspringexample
In this example, you will learn the basics of creating a spring boot jms application that interacts with an activemq server. It will also show how to use maven and spring profiles to setup different operating environments using yaml properties.  Finally we will create a spring test to test our standalone application.

Steps used to create the example
1. Create a Maven project and then create a module called jms-spring-application
2. Add Spring Dependencies to pom file
3. Add Snake yaml dependency to pom file
4. Create Application File (Application.java)
5. Create Resources
    <br>a. Create application.yml
    <br>b. Create application.properties
    <br>c. Update pom to use profiles
    <br>d. Update pom to pick up resources
6. Create JMS Receiver (ReceiveCmdJms.java)
7. Create JMS Sender (SendStatusJms.java)
8. Add Test Dependencies
    <br>a. Add Junit Test
    <br>b. Add ActiveMQ Junit Test
    <br>c. Add Spring-Test
9. Update pom to package all dependencies in a single runnable jar

#LET'S GO THROUGH THE DETAILS

1. Create a Maven project and then create a module called jms-spring-application
<p>You could go to https://spring.io and have it build a maven package for you, it is convenient, 
but in this case I used Intellj IDE to create my project.  It was pretty straightforward.</p>
2. Add Spring Dependencies to pom file 
<p>Spring makes it easy by having starter dependecies that will get what you mostly need to create a 
spring application. Just add the following. </p>

       <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-activemq</artifactId>
              <version>2.0.5.RELEASE</version>
          </dependency>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-oxm</artifactId>
              <version>5.1.0.RELEASE</version>
          </dependency>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-core</artifactId>
              <version>5.1.0.RELEASE</version>
          </dependency>
         <dependency>
     
3. Add Snake yaml dependency to pom file.
<p>Snake yaml will be used to parse the resources files later</p>

       <dependency>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
            <version>1.23</version>
        </dependency>
        
4. Create Application File (Application.java)
<p>The Application class is what spring will use to start the application, make sure you have
the proper annotations.</p>

    @EnableJms
    @ComponentScan(basePackages = "org.jaysfables")
    @Configuration
    @SpringBootApplication
    public class Application {

        public static void main(String[] args) {
            // the WebApplicationType.NONE ensures a webserver does not start
            new SpringApplicationBuilder(Application.class).web(WebApplicationType.NONE)
                    .run(args);
        }
    }
    
5. Create Resources
<br>a. Create application.yml
<p> here you will see several spring profiles, you can set your profile setting in the 
application.properties file, which we will see next.  As you can see, we setup 
profiles for different environments.</p>
    
        spring:
          profiles: test
          # activemq:
            # If you want to test with external broker uncomment the line below
            #    broker-url: tcp://localhost:61616
            #    user: admin
            #    password: admin
          # The following is to show how you would activate a vm by spring, but if you omit it it will start it for you
          # automatically
          # activemq:
            # broker-url: vm://localhost:61616
        name: test-YAML
        queue:
          command: Command_Queue_test
          status: Status_Queue_test
        topic:
          status: Status_Topic_test
        ---
        spring:
          profiles: integration
          activemq:
            broker-url: tcp://localhost:61616
            user: admin
            password: admin
        name: integ-YAML
        queue:
          command: Command_Queue_intergration
          status: Status_Queue_intergration
        topic:
          status: Status_Topic_test
        ---
        spring:
          profiles: release
          activemq:
            broker-url: tcp://localhost:61616
            user: admin
            password: admin
        name: prod-YAML
        queue:
          command: Command_Queue
          status: Status_Queue
        topic:
          status: Status_Topic
        ---   
        
<br>b. Create application.properties

In the application.properties file we simply set the spring profile to a variable to be picked up by Maven

        # The activatedProperties is set by maven
        spring.profiles.active=@activatedProperties@

<br>c. Update pom to use profiles

Here is the magic of Maven, by using profiles we can set our application for different environments,  
I also added notes on how to set the profile if you just want to run the jar file

    <profiles>
        <!-- profile will set spring to use the yml information based upon the spring profile with the same
             name as the <activatedProperties> value -->
        <!-- to run a profile via command line you can use for example test
                  mvn spring-boot:run -Dspring-boot.run.profiles=test
             otherwise it will use the default profile
             or you can run in as a java -jar for example test replace DASH with '-'
                  java -jar target/jms-spring-application-1.0-SNAPSHOT.jar DASHDASHspring.profiles.active=test
           -->
        <profile>
            <id>test</id>
            <properties>
                <activatedProperties>test</activatedProperties>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <profile>
            <id>integration</id>
            <properties>
                <activatedProperties>integration</activatedProperties>
            </properties>
        </profile>
        <profile>
            <id>release</id>
            <properties>
                <activatedProperties>release</activatedProperties>
            </properties>
             <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
    </profiles>


<br>d. Update pom to pick up resources

Also you want to set your resource...

    <build>
        <resources>
            <!-- the resources is so you can insert properties defined in the profile -->
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
 

6. Create JMS Receiver (ReceiveCmdJms.java)

As you review the code there are several things I want to point out. The @Component 
annotation will notify to spring that it is to be scanned.  The @Autowired is dependency
injection feature, where you don't have to define you beans.  The @JmsListener sets a
queue destination, in this case it was set based on what profile is ran in the 
application.yml file. 

        @Component
        public class ReceiveCmdJms {
            @Autowired
            SendStatusJms ssj;
        
            @JmsListener(destination = "${queue.command}")
            void getMsg(String s) {
                ssj.sendMsg("Hello World, " + s + '!');
            }
        }
7. Create JMS Sender (SendStatusJms.java)

Here you can see that there is another spring annotation @Value.  This will inject the value at runtime,
again based on the profile selected in the application.yml file.

        @Component
        public class SendStatusJms {
        
            @Value("${queue.status}")
            String destination;
        
            @Autowired
            JmsTemplate jt;
        
            void sendMsg(String s) {
                jt.convertAndSend(destination, s);
            }
        }

8. Add Test Dependencies to pom
    <br>a. Add Junit Test
    
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    
    <br>b. Add ActiveMQ Junit Test
    
         <dependency>
            <groupId>org.apache.activemq.tooling</groupId>
            <artifactId>activemq-junit</artifactId>
            <version>5.15.6</version>
            <scope>test</scope>
        </dependency>
        
    <br>c. Add Spring-Test
    
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-test</artifactId>
                  <version>2.0.5.RELEASE</version>
                  <scope>test</scope>
              </dependency>
9. Create Unit Test (ApplicationTest.java)

Ok After all  of this you want to test your code.  What is nice about this Unit Test example is it will 
start an internal ActiveMQ server to test based on your profile annotation set to test.  No need to startup
an external activemq server to test, but will still connect to the external when running the release profile
in production.

A couple of new annotations, the @RunWith annotation tell spring to run this test win the spring application. 
The @SpringBootTest tells spring it is a test.  @ActiveProfiles is setting the spring profile. The @Rule is 
a Junit annotation that start an embedded activemq broker.  At want to note, look at how I set my queues to 
point to the same one the application is pointing to, whether you are in test, integration, or release.

        @RunWith(SpringRunner.class)
        @SpringBootTest
        @EnableJms
        @ActiveProfiles(profiles = "test")
        public class ApplicationTest {
            @Rule
            public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();
        
            @Autowired
            JmsTemplate jt;
        
            @Value("${queue.command}")
            String destinationCmd;
        
            @Value("${queue.status}")
            String destinationStatus;
        
            @Test(timeout = 5000)
            public void sendName() {
                jt.convertAndSend(destinationCmd, "Jay");
                jt.setReceiveTimeout(5000);
                String msg = (String) jt.receiveAndConvert(destinationStatus);
                System.out.println(msg);
                Assert.assertEquals("Hello World, Jay!", msg);
            }
        }

10. Update pom to package all dependencies in a single runnable jar

The following build instructions will make sure the application contains all the required dependencies 
contained in the jar file. Note: start-class is set to Application.class in pom properties.

            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.0.5.RELEASE</version>
                <configuration>
                    <fork>true</fork>
                    <mainClass>${start-class}</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
 

