package com.example.flowerobjectdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword, editTextUsername;
    private Button registerBtn, resendEmailBtn;
    private CheckBox termsCheckBox;
    private TextView timerTextView, loginTextView;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db; // Firestore instance
    private CollectionReference usersRef; // Reference to the "users" collection

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    private static final String GMAIL_DOMAIN = "@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users"); // "users" collection in Firestore

        // Bind UI components
        editTextEmail = findViewById(R.id.txtregisterEmail);
        editTextPassword = findViewById(R.id.txtregisterPassword);
        editTextUsername = findViewById(R.id.txtUserName);
        registerBtn = findViewById(R.id.btn_register);
        resendEmailBtn = findViewById(R.id.btn_resend);
        termsCheckBox = findViewById(R.id.check_id);
        timerTextView = findViewById(R.id.timerTextView);
        loginTextView = findViewById(R.id.loginNow);
        progressBar = findViewById(R.id.progressBar);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);

        registerBtn.setEnabled(false); // Initially disable register button
        resendEmailBtn.setEnabled(false); // Initially disable resend email button
        timerTextView.setVisibility(View.GONE); // Hide the timer initially

        setupTermsAndConditions();

        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        registerBtn.setOnClickListener(v -> registerUser());

        resendEmailBtn.setOnClickListener(v -> {
            if (user != null) {
                sendVerificationEmail();
                startTimer(); // Start the 1-minute timer
            }
        });
    }

    private void setupTermsAndConditions() {
        termsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showTermsAndConditionsDialog(); // Show the Terms and Conditions dialog
            } else {
                registerBtn.setEnabled(false); // Disable the register button if unchecked
            }
        });
    }

    private void showTermsAndConditionsDialog() {
        // Create a ScrollView to contain the Terms and Conditions text
        ScrollView scrollView = new ScrollView(this);
        TextView termsTextView = new TextView(this);

        // Set the Terms and Conditions text
        String termsAndConditions = "Terms and Conditions\n\n" +
                "Introduction\n" +
                "Welcome to the PETAL app, developed for the exclusive use within Las Piñas City’s Botanical Garden. " +
                "This app is designed to enhance your botanical experience by providing plant detection, identification, " +
                "and educational resources. By using this app, you agree to comply with and be bound by these Terms " +
                "and Conditions. Please read them carefully.\n\n" +
                "User Registration and Accounts\n" +
                "Eligibility: You must be at least 12 years old to register and use this app.\n" +
                "Account Creation: Users are required to create an account to access certain features. " +
                "You agree to provide accurate and complete information during registration and to update your " +
                "information to keep it current.\n\n" +
                "Plant Detection and Identification\n" +
                "Accuracy: While we strive to provide accurate plant identification, we do not guarantee the accuracy " +
                "of the information provided by the app. Always verify critical information with a professional botanist " +
                "or horticulturist.\n\n" +
                "Saving Favorites/Plant Log\n" +
                "Personal Plant Log: Users can save detected plants to their personal plant log for future reference. " +
                "This feature is for personal use only and should not be used for commercial purposes.\n\n" +
                "Contact Information\n" +
                "If you have any questions or concerns about these Terms and Conditions, please contact us at petalar2023@gmail.com.";

        termsTextView.setText(termsAndConditions);
        termsTextView.setPadding(32, 32, 32, 32);
        termsTextView.setTextSize(16);
        scrollView.addView(termsTextView);

        // Create a MaterialAlertDialogBuilder
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setTitle("Terms and Conditions");
        dialogBuilder.setView(scrollView);

        // Initially disable the "Accept" button
        final Button[] acceptButton = new Button[1];
        dialogBuilder.setPositiveButton("Accept", (dialog, which) -> {
            termsCheckBox.setChecked(true);
            registerBtn.setEnabled(true); // Enable the register button
            dialog.dismiss();
        });

        dialogBuilder.setNegativeButton("Decline", (dialog, which) -> {
            termsCheckBox.setChecked(false);
            registerBtn.setEnabled(false); // Disable the register button
            dialog.dismiss();
        });

        // Show the dialog
        MaterialAlertDialogBuilder builtDialog = dialogBuilder;
        builtDialog.setCancelable(false); // Make it non-cancelable
        androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // Get the "Accept" button reference
        acceptButton[0] = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        acceptButton[0].setEnabled(false); // Initially disable the "Accept" button

        // Add a scroll listener to enable the "Accept" button when reaching the bottom
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                acceptButton[0].setEnabled(true); // Enable the button if at the bottom
            }
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showToast("Enter email");
            return;
        }
        if (!email.endsWith(GMAIL_DOMAIN) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Only valid Gmail accounts are allowed");
            return;
        }
        if (TextUtils.isEmpty(password) || !Pattern.matches(PASSWORD_PATTERN, password)) {
            showToast("Password must have at least 8 characters, include uppercase, lowercase, and a digit");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            showToast("Enter username");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserDataToFirestore(user.getUid(), username, email);
                            sendVerificationEmail();
                            startTimer(); // Start the 1-minute timer for the resend button
                            clearInputFields(); // Clear input fields after successful registration
                        }
                    } else {
                        String message = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                        showToast(message);
                    }
                });
    }

    private void saveUserDataToFirestore(String userId, String username, String email) {
        // User data with an empty "savedPlants" array
        User userData = new User(username, email, new ArrayList<>()); // ArrayList for savedPlants

        usersRef.document(userId).set(userData)
                .addOnSuccessListener(aVoid -> showToast("User data saved to Firestore"))
                .addOnFailureListener(e -> showToast("Failed to save user data"));
    }

    private void sendVerificationEmail() {
        user.sendEmailVerification().addOnCompleteListener(emailTask -> {
            if (emailTask.isSuccessful()) {
                showToast("Verification email sent. Please verify before logging in.");
            } else {
                showToast("Failed to send verification email.");
            }
        });
    }

    private void startTimer() {
        resendEmailBtn.setEnabled(false); // Disable the resend button
        timerTextView.setVisibility(View.VISIBLE); // Show the timer

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Resend available in: " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                if (user != null) {
                    user.reload().addOnCompleteListener(reloadTask -> {
                        if (reloadTask.isSuccessful() && !user.isEmailVerified()) {
                            // Remove user from Firebase Auth
                            user.delete().addOnCompleteListener(deleteTask -> {
                                if (deleteTask.isSuccessful()) {
                                    showToast("Registration failed: Email not verified in time.");
                                    // Optionally, delete user data from Firestore
                                    usersRef.document(user.getUid()).delete();
                                } else {
                                    showToast("Failed to delete unverified user.");
                                }
                            });
                        }
                    });
                }

                resendEmailBtn.setEnabled(true); // Re-enable the resend button
                timerTextView.setVisibility(View.GONE); // Hide the timer
            }
        }.start();
    }

    private void clearInputFields() {
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextUsername.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
    }
}

// Updated User model class
class User {
    public String username;
    public String email;
    public List<String> savedPlants;

    public User() {
        // Default constructor for Firestore
    }

    public User(String username, String email, List<String> savedPlants) {
        this.username = username;
        this.email = email;
        this.savedPlants = savedPlants;
    }
}
