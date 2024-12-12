package com.example.flowerobjectdetection;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class FrangipaniComment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextComment;
    private Button buttonSubmitComment;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunflower_comment2);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind UI components
        recyclerView = findViewById(R.id.recyclerView);
        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmitComment = findViewById(R.id.btn_PostComment);

        // Initialize RecyclerView
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);

        loadComments();

        buttonSubmitComment.setOnClickListener(v -> submitComment());
    }

    private void loadComments() {
        // Reference to the comments collection
        CollectionReference commentsRef = db.collection("plants").document("Frangipani").collection("comments");

        commentsRef.orderBy("timestamp", Query.Direction.ASCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    commentList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Comment comment = doc.toObject(Comment.class);
                        if (comment != null) {
                            commentList.add(comment);
                        }
                    }
                    commentAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Error loading comments", e));
    }

    private void submitComment() {
        String content = editTextComment.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (content.isEmpty()) {
            Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser == null) {
            Toast.makeText(this, "You need to log in to post comments.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String username = documentSnapshot.getString("username");
                    if (username == null || username.isEmpty()) username = "Anonymous";

                    Comment newComment = new Comment(username, content, System.currentTimeMillis());

                    db.collection("plants").document("Frangipani").collection("comments")
                            .add(newComment)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Comment added!", Toast.LENGTH_SHORT).show();
                                editTextComment.setText("");
                                loadComments();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FirestoreError", "Error adding comment", e);
                                Toast.makeText(this, "Failed to add comment. Try again.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error fetching username", e);
                    Toast.makeText(this, "Failed to fetch username. Try again.", Toast.LENGTH_SHORT).show();
                });
    }


}
