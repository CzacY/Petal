package com.example.flowerobjectdetection;
import com.example.flowerobjectdetection.Comment;
public class Comment {
    private String username;
    private String commentText;
    private String plantId;
    private long timestamp;

    // Constructor to initialize the values
    public Comment(String username, String commentText, String plantId, long timestamp) {
        this.username = username;
        this.commentText = commentText;
        this.plantId = plantId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
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

    // Optionally, you can add a toString() method for easier logging
    @Override
    public String toString() {
        return "Comment{" +
                "username='" + username + '\'' +
                ", commentText='" + commentText + '\'' +
                ", plantId='" + plantId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}


