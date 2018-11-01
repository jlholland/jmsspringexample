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

