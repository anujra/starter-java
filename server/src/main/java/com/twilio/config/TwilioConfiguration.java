package com.twilio.config;

public class TwilioConfiguration {

    private String accountSid = "";

    private String authToken = "";

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
