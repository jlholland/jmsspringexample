package org.jaysfables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveCmdJms {
    @Autowired
    SendStatusJms ssj;

    @JmsListener(destination = "${queue.command}")
    void getMsg(String s) {
        ssj.sendMsg("Hello World, " + s + '!');
    }
}
