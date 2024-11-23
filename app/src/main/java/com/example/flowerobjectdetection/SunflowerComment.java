package com.example.flowerobjectdetection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Comment;

import java.io.ByteArrayOutputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class SunflowerComment extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private List<Comment> commentsList;
    private CommentAdapter commentAdapter;
    private EditText editTextComment;
    private Button buttonSubmitComment;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunflower_comment);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView);
        commentsList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);

        // Set up input field and post button
        View commentInput = findViewById(R.id.editTextComment);
        View postCommentButton = findViewById(R.id.buttonSubmitComment);

        // Fetch existing comments
        fetchComments();

        // Post comment when button is clicked
        postCommentButton.setOnClickListener(v -> postComment());
    }

    private void postComment() {
        // Get current user (from FirebaseAuth)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Get comment input text from EditText
            String commentText = editTextComment.getText().toString();

            // Log comment text to check if it's retrieved correctly
            Log.d("SunflowerComment", "Comment text: " + commentText);



                // Log to confirm comment creation
            ByteArrayOutputStream newComment = new ByteArrayOutputStream();
            Log.d("SunflowerComment", "New comment created: " + newComment.toString());

                // Save comment to Firestore
                db.collection("Comments")
                        .add(newComment)
                        .addOnSuccessListener(documentReference -> {
                            Log.d("SunflowerComment", "Comment posted successfully!");
                            Toast.makeText(SunflowerComment.this, "Comment posted!", Toast.LENGTH_SHORT).show();
                            editTextComment.setText("");  // Clear the input after posting
                        })
                        .addOnFailureListener(e -> {
                            Log.w("SunflowerComment", "Error posting comment: ", e);
                            Toast.makeText(SunflowerComment.this, "Failed to post comment.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(SunflowerComment.this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
            }
        }



    private void fetchComments() {
        db.collection("Comments")
                .whereEqualTo("plantId", "sunflower") // Specific to Sunflower
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    commentsList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Comment comment = doc.toObject(Comment.class);
                        commentsList.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                });
    }
}
