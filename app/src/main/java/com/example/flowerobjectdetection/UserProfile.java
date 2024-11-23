package com.example.flowerobjectdetection;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private LinearLayout savedPlantsContainer;
    private TextView usernameTxt, emailTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        // Bind UI components
        usernameTxt = findViewById(R.id.usernameTextView);
        emailTxt = findViewById(R.id.emailTextView);
        savedPlantsContainer = findViewById(R.id.savedPlantsContainer);

        // Check if user is signed in
        if (user == null) {
            Toast.makeText(this, "No user is signed in.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no user is signed in
            return;
        }

        loadUserProfile(); // Load user profile data
    }

    private void loadUserProfile() {
        String userId = user.getUid();
        DocumentReference userDocRef = db.collection("users").document(userId);

        // Fetch user data from Firestore
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    // Display user data
                    String username = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");
                    usernameTxt.setText("Hi " + username + "!");
                    usernameTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    emailTxt.setText(email);

                    // Fetch saved plants and display them
                    List<String> savedPlants = (List<String>) documentSnapshot.get("savedPlants");
                    displaySavedPlants(savedPlants);
                } else {
                    Log.e("UserProfile", "User document does not exist.");
                }
            } else {
                Log.e("UserProfile", "Failed to fetch user data", task.getException());
            }
        });
    }

    private void displaySavedPlants(List<String> savedPlants) {
        if (savedPlants != null && !savedPlants.isEmpty()) {
            savedPlantsContainer.removeAllViews(); // Clear existing views
            for (String plantName : savedPlants) {
                // Create a container for each plant entry
                LinearLayout plantLayout = new LinearLayout(this);
                plantLayout.setOrientation(LinearLayout.HORIZONTAL);
                plantLayout.setPadding(0, 16, 0, 16);
                plantLayout.setGravity(Gravity.CENTER_VERTICAL);

                // Image for the plant
                ImageView plantImage = new ImageView(this);
                plantImage.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
                plantImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                plantImage.setImageResource(getPlantImageResource(plantName));

                // Text for the plant name
                TextView plantLink = new TextView(this);
                plantLink.setText(plantName);
                plantLink.setTextColor(Color.BLACK);
                plantLink.setTextSize(35);
                plantLink.setTypeface(null, Typeface.BOLD);
                plantLink.setPadding(16, 0, 0, 0);

                // Set click listeners for the plant name and image
                plantLink.setOnClickListener(v -> openPlantInfoActivity(plantName));
                plantImage.setOnClickListener(v -> openPlantInfoActivity(plantName));

                // Add the plant entry to the layout
                plantLayout.addView(plantImage);
                plantLayout.addView(plantLink);
                savedPlantsContainer.addView(plantLayout);
            }
        } else {
            // Display a message if no plants are saved
            TextView noPlantsText = new TextView(this);
            noPlantsText.setText("No plants saved yet.");
            savedPlantsContainer.addView(noPlantsText);
        }
    }

    private int getPlantImageResource(String plantName) {
        // Map plant names to image resources
        switch (plantName) {
            case "Gumamela":
                return R.drawable.gumamela; // Replace with actual resource ID
            case "Sunflower":
                return R.drawable.sunflowerr; // Replace with actual resource ID
            case "Eggplant":
                return R.drawable.eggplantdata;
            case "Bougainvillea":
                return R.drawable.bougainvillea;
            case "Forgetmenot":
                return R.drawable.forgetmenot;
            case "Frangipani":
                return R.drawable.fra;
            case "Cosmos":
                return R.drawable.cosmos;
            case "Aloevera":
                return R.drawable.aloevera;
            case "Zinnia":
                return R.drawable.zinnia;
            case "Climbingrose":
                return R.drawable.rose;
            default:
                return 0; // Default plant image
        }
    }

    private void openPlantInfoActivity(String plantName) {
        // Map plant names to their corresponding info activities
        Class<?> plantActivity = getPlantInfoActivity(plantName);
        if (plantActivity != null) {
            startActivity(new Intent(UserProfile.this, plantActivity));
        } else {
            Toast.makeText(this, "No activity available for " + plantName, Toast.LENGTH_SHORT).show();
        }
    }

    private Class<?> getPlantInfoActivity(String plantName) {
        // Map plant names to their respective activities
        switch (plantName) {
            case "Gumamela":
                return GumamelaInfo.class;
            case "Sunflower":
                return SunflowerInfo.class;
            case "Eggplant":
                return EggplantInfo.class;
            case "Bougainvillea":
                return BougainvilleaInfo.class;
            case "Forgetmenot":
                return ForgetmenotInfo.class;
            case "Frangipani":
                return FrangipaniInfo.class;
            case "Cosmos":
                return CosmosInfo.class;
            case "Aloevera":
                return AloeveraInfo.class;
            case "Zinnia":
                return ZinniaInfo.class;
            case "Climbingrose":
                return ClimbingroseInfo.class;
            default:
                return null;
        }
    }
}
