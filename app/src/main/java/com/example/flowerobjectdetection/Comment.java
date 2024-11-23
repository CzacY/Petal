public class Comment {
    private String username;
    private String commentContent;
    private String timestamp;

    public Comment(String username, String commentContent, String timestamp) {
        this.username = username;
        this.commentContent = commentContent;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
