package com.example.flowerobjectdetection;

public class Comments {
    private String username;
    private String text;
    private String plantId;
    private long timestamp;

    // No-argument constructor (required for Firebase)
    public Comments() {
        this.username = "Anonymous";
        this.text = "No comment provided";
        this.plantId = "";
        this.timestamp = System.currentTimeMillis();
    }

    // Full constructor
    public Comments(String username, String text, String plantId, long timestamp) {
        this.username = username != null && !username.trim().isEmpty() ? username : "Anonymous";
        this.text = text != null && !text.trim().isEmpty() ? text : "No comment provided";
        this.plantId = plantId;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null && !username.trim().isEmpty() ? username : "Anonymous";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text != null && !text.trim().isEmpty() ? text : "No comment provided";
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "username='" + username + '\'' +
                ", text='" + text + '\'' +
                ", plantId='" + plantId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
