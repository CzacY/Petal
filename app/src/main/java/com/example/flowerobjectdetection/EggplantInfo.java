package com.example.flowerobjectdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class EggplantInfo extends AppCompatActivity {
private Button gcomment;
private Button eggplantcomment;
private Button commentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eggplant_info);

        commentButton = findViewById(R.id.btn_eggplantcomment);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SunflowerComment", "Comment button clicked"); // Debug Log
                try {
                    startActivity(new Intent(EggplantInfo.this, EggplantCommentF.class));
                } catch (Exception e) {
                    Log.e("ActivityError", "Error starting SunflowerComment", e);
                }
            }
        });

    }
}