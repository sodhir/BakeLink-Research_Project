package com.example.bakelink.bakers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class B_ProfileSetupActivity extends AppCompatActivity {

    private EditText etBakeryTitle, etDescription;
    private ImageView ivProfilePicture;
    private Button btnSaveAndContinue;
    //add for specialities and services section

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bprofile_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        etBakeryTitle = findViewById(R.id.etBakeryTitle);
        etDescription = findViewById(R.id.etDescription);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnSaveAndContinue = findViewById(R.id.btnSaveAndContinue);
        //add for specialities and services section

        // Set listener for profile picture upload
        ivProfilePicture.setOnClickListener(v -> {
            //functionality for upload picture
            Toast.makeText(this, "Upload Profile Picture", Toast.LENGTH_SHORT).show();
        });


        // Save and Continue button click listener
        btnSaveAndContinue.setOnClickListener(v -> saveProfileAndContinue());


        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        // Deselect the item (no item should be highlighted)
        bottomNavigationView.setSelectedItemId(-1);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.setSelectedItemId(-1);
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_ProfileSetupActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_ProfileSetupActivity.this, B_ScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_ProfileSetupActivity.this, B_MyCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_ProfileSetupActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void saveProfileAndContinue() {
        String bakeryTitle = etBakeryTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        /*if (bakeryTitle.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }*/


        Intent intent = new Intent(B_ProfileSetupActivity.this, B_MyCakesSetupActivity.class);
        startActivity(intent);
        finish();
    }
}