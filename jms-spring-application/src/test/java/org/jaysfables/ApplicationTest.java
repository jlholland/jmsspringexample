package org.jaysfables;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;

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
