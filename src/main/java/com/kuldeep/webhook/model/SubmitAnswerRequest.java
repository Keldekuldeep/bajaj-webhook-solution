package com.kuldeep.webhook.model;

public class SubmitAnswerRequest {

    private String finalQuery;

    public SubmitAnswerRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }
}
