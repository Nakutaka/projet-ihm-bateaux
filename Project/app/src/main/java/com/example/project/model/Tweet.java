package com.example.project.model;

public class Tweet {
    private final String body;
    private final String [] hashtags;

    public Tweet(String body, String ... hashtags) {
        this.body = body;
        this.hashtags = hashtags;
    }
    public String getBody() {
        return body;
    }

    public String getHashtags(){
        return String.join(" ", hashtags);
    }
}
