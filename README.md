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
    <br>b. Create application.properties
    <br>c. Update pom to use profiles
    <br>d. Update pom to pick up resources
6. Create JMS Receiver (ReceiveCmdJms.java)
7. Create JMS Sender (SendStatusJms.java)
8. Add Test Dependencies
    <br>a. Add Junit Test
    <br>b. Add ActiveMQ Junit Test
    <br>c. Add Spring-Test
    
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-test</artifactId>
                  <version>2.0.5.RELEASE</version>
                  <scope>test</scope>
              </dependency>
9. Update pom to package all dependencies in a single runnable jar
