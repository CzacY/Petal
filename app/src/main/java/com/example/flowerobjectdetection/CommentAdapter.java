package com.example.flowerobjectdetection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;

    // Constructor to initialize the comment list
    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each comment item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        // Get the current comment
        Comment comment = commentList.get(position);

        // Set the data in the views
        holder.usernameTextView.setText(comment.getUsername());
        holder.commentTextView.setText(comment.getCommentContent());
        holder.timestampTextView.setText(comment.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return commentList.size();  // Return the total number of comments
    }

    // ViewHolder class to represent each comment item
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, commentTextView, timestampTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            // Initialize the views for the comment item
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }
}