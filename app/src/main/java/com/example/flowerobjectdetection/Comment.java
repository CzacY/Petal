package com.example.flowerobjectdetection;

public class Comment {
    private String username;
    private String content;
    private long timestamp;

    // Default constructor required for Firestore
    public Comment() {}

    public Comment(String username, String content, long timestamp) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
