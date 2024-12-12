package com.example.flowerobjectdetection;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdminActivity extends AppCompatActivity {

    private TextView txtUserEmails;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();  // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);  // Set layout for Admin activity

        txtUserEmails = findViewById(R.id.txtUsernames);

        // Fetch all user data (emails) from Firestore
        fetchUserEmails();
    }

    // Fetch user emails from Firestore
    private void fetchUserEmails() {
        db.collection("users")  // Reference to the "users" collection in Firestore
                .get()  // Fetch all documents
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        StringBuilder emails = new StringBuilder();
                        for (DocumentSnapshot document : querySnapshot) {
                            User user = document.toObject(User.class);  // Convert each document to a User object
                            if (user != null) {
                                emails.append(user.getEmail()).append("\n");  // Append each email to the StringBuilder
                            }
                        }
                        // Set the fetched emails to the TextView
                        txtUserEmails.setText(emails.toString());
                    } else {
                        // If the fetch failed, show a toast message
                        Toast.makeText(AdminActivity.this, "Failed to fetch user emails.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

    /*private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        emailTextView = findViewById(R.id.txtUsernames);

        // Get the email passed from the Login activity
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Display the email
        if (userEmail != null) {
            emailTextView.setText("Logged in as: " + userEmail);
        } else {
            emailTextView.setText("No email received.");
        }
    }
    }
*/
