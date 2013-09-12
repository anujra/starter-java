package com.twilio.config;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class JavaStarterConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String template;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @NotEmpty
    @JsonProperty
    private String defaultName = "StarterJava";

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }


    @Valid
    @NotNull
    @JsonProperty
    private AsyncHttpClientConfiguration asyncHttpClientConfiguration = new AsyncHttpClientConfiguration();

    public AsyncHttpClientConfiguration getAsyncHttpClientConfiguration() {
        return asyncHttpClientConfiguration;
    }

    public void setAsyncHttpClientConfiguration(AsyncHttpClientConfiguration asyncHttpClientConfiguration) {
        this.asyncHttpClientConfiguration = asyncHttpClientConfiguration;
    }


    @Valid
    @NotNull
    @JsonProperty
    private TwilioConfiguration twilioConfiguration = new TwilioConfiguration();

    public TwilioConfiguration getTwilioConfiguration() {
        return twilioConfiguration;
    }

    public void setTwilioConfiguration(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

}
