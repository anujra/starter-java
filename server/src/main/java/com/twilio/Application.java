package com.twilio;

/**
 *
 * @author: arjuna
 * Date: 3/11/13
 * Time: 12:53 PM
 *
 */


import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ProxyServer;
import com.twilio.config.AsyncHttpClientConfiguration;
import com.twilio.config.JavaStarterConfiguration;
import com.twilio.config.TwilioConfiguration;
import com.twilio.sdk.TwilioRestClient;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public enum Application {
    // Singleton
    INSTANCE;

    private final Logger LOG = LoggerFactory.getLogger(Application.class);

    /**
     * HealthCheck
     */
    private String template;

    /**
     * Name of Service
     */
    private String defaultName;

    /**
     *
     */
    private AsyncHttpClient asyncHttpClient;

    /**
     *
     */
    private TwilioRestClient twilioRestClient;

    public void initialize(JavaStarterConfiguration configuration, Environment environment) throws IOException
    {
        template = configuration.getTemplate();
        defaultName = configuration.getDefaultName();

        AsyncHttpClientConfiguration asyncConfig = configuration.getAsyncHttpClientConfiguration();

        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setRequestTimeoutInMs(asyncConfig.getRequestTimeoutInMs());
        builder.setMaximumConnectionsPerHost(asyncConfig.getMaximumConnectionsPerHost());
        builder.setMaximumConnectionsTotal(asyncConfig.getMaximumConnectionsTotal());

        if (asyncConfig.getUseProxyServer())
            builder.setProxyServer(new ProxyServer(asyncConfig.getProxyServer(), asyncConfig.getProxyPort()));

        builder.setMaxRequestRetry(asyncConfig.getMaxRequestRetry());
        builder.setAllowPoolingConnection(asyncConfig.isAllowPoolingConnection());
        builder.setCompressionEnabled(asyncConfig.isCompressionEnabled());

        asyncHttpClient = new AsyncHttpClient(builder.build());

        TwilioConfiguration twilioConfig = configuration.getTwilioConfiguration();
        twilioRestClient = new TwilioRestClient(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

    }

    public String getTemplate() {
        return template;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void init()
    {
        LOG.trace("Application::init");
        LOG.debug("");
    }

    public AsyncHttpClient getAsyncHttpClient()
    {
        return asyncHttpClient;
    }

    public TwilioRestClient getTwilioRestClient()
    {
        return twilioRestClient;
    }

}
