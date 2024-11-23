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

public class EggplantInfo extends AppCompatActivity {

    private TextView statusTextView;
    private Button saveButton, commentButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eggplant_info);

        // Play sound automatically when the activity starts
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.cuteringtone);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());

        // Bind UI components
        statusTextView = findViewById(R.id.textViewegg);
        saveButton = findViewById(R.id.btn_saveEggplant);
        commentButton = findViewById(R.id.btn_submitCommentegg);

        commentButton.setOnClickListener(v -> {
            Intent intent = new Intent(EggplantInfo.this, EggplantComment.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("AuthenticationError", "No authenticated user found!");
            statusTextView.setText("Please log in to save plants.");
            saveButton.setEnabled(false);
            return;
        }

        String userId = currentUser.getUid();

        saveButton.setOnClickListener(v -> savePlant(userId, "Eggplant"));
    }

    private void savePlant(String userId, String plantName) {
        db.collection("users").document(userId)
                .update(
                        "savedPlants", FieldValue.arrayUnion(plantName)
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