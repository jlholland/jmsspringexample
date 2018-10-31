package org.jaysfables;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

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
