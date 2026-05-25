package com.kuldeep.webhook.model;

public class GenerateWebhookResponse {

    private String webhook;
    private String accessToken;

    public String getWebhook() { return webhook; }
    public String getAccessToken() { return accessToken; }

    public void setWebhook(String webhook) { this.webhook = webhook; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}
