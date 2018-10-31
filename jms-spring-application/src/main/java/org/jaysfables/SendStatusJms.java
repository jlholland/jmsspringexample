package org.jaysfables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

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
