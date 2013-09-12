package com.twilio.config;

/** Configuration class for Netty Async Http Client
 * @author: arjuna
 * Date: 4/9/13
 * Time: 10:55 AM
 */
public class AsyncHttpClientConfiguration {

    private int requestTimeoutInMs = 15000;

    private int maximumConnectionsPerHost = 10;

    private int maximumConnectionsTotal = 100;

    private int maxRequestRetry = 1;

    private boolean allowPoolingConnection = true;

    private boolean compressionEnabled = true;

    private boolean useProxyServer = false;

    private String proxyServer = "";

    private int proxyPort = 13128;

    public int getRequestTimeoutInMs() {
        return requestTimeoutInMs;
    }

    public void setRequestTimeoutInMs(int requestTimeoutInMs) {
        this.requestTimeoutInMs = requestTimeoutInMs;
    }

    public int getMaximumConnectionsPerHost() {
        return maximumConnectionsPerHost;
    }

    public void setMaximumConnectionsPerHost(int maximumConnectionsPerHost) {
        this.maximumConnectionsPerHost = maximumConnectionsPerHost;
    }

    public int getMaximumConnectionsTotal() {
        return maximumConnectionsTotal;
    }

    public void setMaximumConnectionsTotal(int maximumConnectionsTotal) {
        this.maximumConnectionsTotal = maximumConnectionsTotal;
    }

    public boolean getUseProxyServer() {
        return useProxyServer;
    }

    public void setUseProxyServer(boolean useProxyServer) {
        this.useProxyServer = useProxyServer;
    }

    public String getProxyServer() {
        return proxyServer;
    }

    public void setProxyServer(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getMaxRequestRetry() {
        return maxRequestRetry;
    }

    public void setMaxRequestRetry(int maxRequestRetry) {
        this.maxRequestRetry = maxRequestRetry;
    }

    public boolean isAllowPoolingConnection() {
        return allowPoolingConnection;
    }

    public void setAllowPoolingConnection(boolean allowPoolingConnection) {
        this.allowPoolingConnection = allowPoolingConnection;
    }

    public boolean isCompressionEnabled() {
        return compressionEnabled;
    }

    public void setCompressionEnabled(boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }
}
