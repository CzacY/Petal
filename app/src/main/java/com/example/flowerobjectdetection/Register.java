package com.example.flowerobjectdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    private Button registerBtn, resendEmailBtn;
    private TextInputEditText editTextEmail, editTextPassword, editTextUsername;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView textView, timerTextView, viewlog;
    private FirebaseUser user;
    private CheckBox checkBox;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;


    // Timer countdown value (100 seconds)
    private static final long TIMER_DURATION = 100000; // 100 seconds

    // Regex pattern for the password
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    private static final String GMAIL_DOMAIN = "@gmail.com";


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        checkBox = findViewById(R.id.check_id);
        registerBtn = findViewById(R.id.btn_register); // Reference to register button
        registerBtn.setEnabled(false); // Initially disable the register button

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show the Terms and Conditions dialog
                    materialAlertDialogBuilder.setTitle("Terms And Conditions");
                    materialAlertDialogBuilder.setMessage("Terms and Conditions\n" +
                            "Introduction\n" +
                            "Welcome to the PETAL app, developed for the exclusive use within Las Piñas City’s \n" +
                            "Botanical Garden. This app is designed to enhance your botanical experience by \n" +
                            "providing plant detection, identification, and educational resources. By using this app, you \n" +
                            "agree to comply with and be bound by these Terms and Conditions. Please read them \n" +
                            "carefully.\n" +
                            "User Registration and Accounts\n" +
                            "Eligibility: You must be at least 12 years old to register and use this app.\n" +
                            "Account Creation: Users are required to create an account to access certain features. \n" +
                            "You agree to provide accurate and complete information during registration and to update \n" +
                            "your information to keep it current.\n" +
                            "Account Security: You are responsible for maintaining the confidentiality of your account \n" +
                            "credentials. Notify us immediately if you suspect any unauthorized use of your account.\n" +
                            "User Profiles\n" +
                            "Profile Information: Users can create profiles that include personal information and \n" +
                            "preferences. You agree to provide accurate information and to update it as necessary.\n" +
                            "Privacy: Your profile information will be used in accordance with our Privacy Policy.\n" +
                            "Plant Detection and Identification\n" +
                            "Accuracy: While we strive to provide accurate plant identification, we do not guarantee \n" +
                            "the accuracy of the information provided by the app. Always verify critical information with \n" +
                            "a professional botanist or horticulturist.\n" +
                            "Usage: The app is intended for educational and informational purposes only and should \n" +
                            "not be used as a sole resource for plant identification.\n" +
                            "Saving Favorites/Plant Log\n" +
                            "Personal Plant Log: Users can save detected plants to their personal plant log for future \n" +
                            "reference. This feature is for personal use only and should not be used for commercial \n" +
                            "purposes.\n" +
                            "Comments Section and User Interaction\n" +
                            "Conduct: Users can post comments and interact with other users. You agree to use \n" +
                            "respectful and appropriate language and to not post any content that is offensive, abusive, \n" +
                            "or violates any laws.\n" +
                            "Monitoring: We reserve the right to monitor, moderate, and remove any comments or \n" +
                            "interactions that violate these Terms and Conditions.\n" +
                            "Intellectual Property\n" +
                            "Ownership: The app, including its content, features, and functionality, is owned by the \n" +
                            "City Government of Las Piñas. Unauthorized use of any intellectual property is prohibited.\n" +
                            "License: You are granted a limited, non-exclusive, non-transferable license to use the app \n" +
                            "for personal, non-commercial purposes.\n" +
                            "Limitation of Liability\n" +
                            "Disclaimer: The app is provided \"as is\" without any warranties, express or implied. The \n" +
                            "City Government of Las Piñas is not liable for any damages resulting from the use of the \n" +
                            "app.\n" +
                            "Indemnification: You agree to indemnify and hold harmless the City Government of Las \n" +
                            "Piñas from any claims arising out of your use of the app or violation of these Terms and \n" +
                            "Conditions.\n" +
                            "Changes to Terms and Conditions\n" +
                            "Updates: We may update these Terms and Conditions from time to time. Your continued \n" +
                            "use of the app after any changes constitutes acceptance of the new terms.\n" +
                            "Governing Law\n" +
                            "Jurisdiction: These Terms and Conditions are governed by and construed in accordance \n" +
                            "with the laws of the Philippines. Any disputes arising out of or in connection with these \n" +
                            "terms shall be subject to the exclusive jurisdiction of the courts of Las Piñas City.\n" +
                            "Contact Information\n" +
                            "If you have any questions or concerns about these Terms and Conditions, please contact \n" +
                            "us at petalar2023@gmail.com");

                    // Dialog "Accept" button
                    materialAlertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            checkBox.setEnabled(true);
                            registerBtn.setEnabled(true); // Enable register button if terms are accepted
                            dialogInterface.dismiss();
                        }
                    });

                    // Dialog "Decline" button
                    materialAlertDialogBuilder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            checkBox.setChecked(false); // Uncheck the checkbox if terms are declined
                            registerBtn.setEnabled(false); // Ensure register button remains disabled
                        }
                    });

                    // Show the dialog
                    materialAlertDialogBuilder.show();
                } else {
                    registerBtn.setEnabled(false); // Disable register button if checkbox is unchecked
                }
            }
        });

        // Only redirect to the Login screen, not the dashboard
        if (currentUser != null && currentUser.isEmailVerified()) {
            // Redirects to Login if user has verified email but hasn't logged in yet
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.txtregisterEmail);
        editTextPassword = findViewById(R.id.txtregisterPassword);
        registerBtn = findViewById(R.id.btn_register);
        resendEmailBtn = findViewById(R.id.btn_resend); // Button for resending verification email
        progressBar = findViewById(R.id.progressBar);
        viewlog = findViewById(R.id.loginNow);
        timerTextView = findViewById(R.id.timerTextView); // TextView to display timer
        editTextUsername = findViewById(R.id.txtUserName);

        viewlog.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        registerBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());
            String username = String.valueOf(editTextUsername.getText());
            editTextEmail.setText("");
            editTextPassword.setText("");
            editTextUsername.setText("");



            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Validate email domain
            if (!email.endsWith(GMAIL_DOMAIN)) {
                Toast.makeText(Register.this, "Only Gmail accounts are allowed.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Validate password
            if (!Pattern.matches(PASSWORD_PATTERN, password)) {
                Toast.makeText(Register.this, "Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, and a digit.", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                return;
            }


            // Create the user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Store the user object
                            user = mAuth.getCurrentUser();

                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(emailTask -> {
                                            if (emailTask.isSuccessful()) {
                                                Toast.makeText(Register.this,
                                                        "Registration successful. Verification email sent to " + user.getEmail(),
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Register.this, Login.class);

                                                // Notify user to verify their email
                                                Toast.makeText(Register.this,
                                                        "Please verify your email before logging in.",
                                                        Toast.LENGTH_LONG).show();

                                                // Start the countdown timer
                                                startEmailVerificationTimer();

                                            } else {
                                                Toast.makeText(Register.this,
                                                        "Failed to send verification email.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            if (task.getException() != null && task.getException().getMessage().contains("email address is already in use")) {
                                Toast.makeText(Register.this, "Email already in use. Please log in.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        resendEmailBtn.setOnClickListener(v -> {
            if (user != null) {
                user.sendEmailVerification()
                        .addOnCompleteListener(emailTask -> {
                            if (emailTask.isSuccessful()) {
                                Toast.makeText(Register.this,
                                        "Verification email resent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                                startEmailVerificationTimer(); // Reset the timer




                            } else {
                                Toast.makeText(Register.this,
                                        "Failed to resend verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    private void startEmailVerificationTimer() {
        timerTextView.setVisibility(View.VISIBLE);  // Ensure the timer is visible
        resendEmailBtn.setVisibility(View.GONE);    // Hide resend button until timer finishes

        new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeLeft = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timerTextView.setText("Time left to verify email: " + timeLeft);
            }


            @Override
            public void onFinish() {
                timerTextView.setText("Time's up! Resend the verification email.");
                resendEmailBtn.setVisibility(View.VISIBLE); // Show resend button

                // Check if the email is verified
                if (user != null && !user.isEmailVerified()) {
                    // If not verified, delete the user
                    user.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Email verification failed. Please try registering again.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Register.this, "Failed to remove unverified account.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }
       };
