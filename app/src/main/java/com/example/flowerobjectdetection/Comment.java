package com.example.flowerobjectdetection;

public class Comment {
    private String username;
    private String commentText;
    private String plantId;
    private long timestamp;

    // Default constructor (required for Firestore)
    public Comment() {}

    // Constructor
    public Comment(String username, String commentText, String plantId, long timestamp) {
        this.username = username;
        this.commentText = commentText;
        this.plantId = plantId;
        this.timestamp = timestamp;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getPlantId() {
        return plantId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return null;
    }

    public int getCommentContent() {
        return 0;
    }
}
