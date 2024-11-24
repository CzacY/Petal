package com.example.flowerobjectdetection;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class SunflowerInfo extends AppCompatActivity {

    private TextView statusTextView;
    private Button saveButton, commentButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunflower_info);

        // Play sound automatically when the activity starts
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.cuteringtone);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());

        // Bind UI components
        statusTextView = findViewById(R.id.textView6); // Status TextView
        saveButton = findViewById(R.id.btn_save); // Save Button
        commentButton = findViewById(R.id.btn_commentSunflower);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SunflowerComment", "Comment button clicked"); // Debug Log
                try {
                    startActivity(new Intent(SunflowerInfo.this, SunflowerComment.class));
                } catch (Exception e) {
                    Log.e("ActivityError", "Error starting SunflowerComment", e);
                }
            }
        });


        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("AuthenticationError", "No authenticated user found!");
            statusTextView.setText("Please log in to save plants.");
            saveButton.setEnabled(false); // Disable the save button if no user is logged in
            return;
        }

        String userId = currentUser.getUid();

        // Set the save button click listener
        saveButton.setOnClickListener(v -> savePlant(userId, "Sunflower"));
    }

    private void savePlant(String userId, String plantName) {
        // Update Firestore to add the plant to the user's savedPlants array
        db.collection("users").document(userId)
                .update(
                        "savedPlants", FieldValue.arrayUnion(plantName) // Add the plant to the array
                )
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", plantName + " saved successfully.");
                    statusTextView.setText("Plant saved!");
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error saving " + plantName, e);
                    statusTextView.setText("Failed to save the plant. Try again.");
                });
    }
}
