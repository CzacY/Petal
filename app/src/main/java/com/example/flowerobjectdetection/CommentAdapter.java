package com.example.flowerobjectdetection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (comments == null || comments.isEmpty() || position >= comments.size()) {
            return; // Avoid any out-of-bounds errors
        }

        Comment comment = comments.get(position);

        // Safely bind data with fallback for null fields
        if (comment != null) {
            holder.usernameTextView.setText(
                    comment.getUsername() != null && !comment.getUsername().trim().isEmpty()
                            ? comment.getUsername()
                            : "Anonymous"
            );
            holder.commentTextView.setText(
                    comment.getText() != null && !comment.getText().trim().isEmpty()
                            ? comment.getText()
                            : "No comment provided"
            );
        } else {
            holder.usernameTextView.setText("Anonymous");
            holder.commentTextView.setText("No comment provided");
        }
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView commentTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }
    }
}