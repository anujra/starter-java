package com.twilio;

/**
 *
 * @author: arjuna
 * Date: 3/11/13
 * Time: 12:53 PM
 *
 */

import com.twilio.config.JavaStarterConfiguration;
import com.twilio.resources.SdkTestResource;
import com.twilio.resources.SmsResource;
import com.twilio.resources.SynchronousResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.twilio.core.health.TemplateHealthCheck;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StarterJava extends Service<JavaStarterConfiguration> {

    private final Logger LOG = LoggerFactory.getLogger(StarterJava.class);

    public static void main(String[] args) throws Exception {
        new StarterJava().run(args);
    }

    public StarterJava() {
        super();
    }

    @Override
    public void initialize(final Bootstrap<JavaStarterConfiguration> bootstrap) {
        bootstrap.setName("StarterJava");
    }

    @Override
    public void run(JavaStarterConfiguration configuration, Environment environment) {

        /// parse yml file - read configurations.
        try {
            Application.INSTANCE.initialize(configuration, environment);
        } catch (IOException e) {
            LOG.error("StarterJava::initialize, System exit with -1");
            System.exit(-1);
        }

        /// create SMSResouce <- Serve twiml
        environment.addResource(new SmsResource());

        /// create TestResource <- A post to this resouce will send an SMS email
        environment.addResource(new SdkTestResource((Application.INSTANCE.getTwilioRestClient())));

        /// a synchronous resouce that uses continuations. Go wild..
        environment.addResource(new SynchronousResource((Application.INSTANCE.getAsyncHttpClient())));

        final String template = configuration.getTemplate();
        environment.addHealthCheck(new TemplateHealthCheck(template));
    }
}
