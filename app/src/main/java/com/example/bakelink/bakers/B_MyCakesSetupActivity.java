package com.example.bakelink.bakers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class B_MyCakesSetupActivity extends AppCompatActivity {

    private Button ckbtnAddNewCake;
    private Button ckbtnSaveAndContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmy_cakes_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ckbtnAddNewCake = findViewById(R.id.allckbtnAddNewCake);
        ckbtnSaveAndContinue = findViewById(R.id.ckbtnSaveAndContinue);

        // Add new cake button click listener
        ckbtnAddNewCake.setOnClickListener(v -> addNewCake());

        // Save and Continue button click listener
        ckbtnSaveAndContinue.setOnClickListener(v -> saveProfileAndContinue());


        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        // Deselect the item (no item should be highlighted)
        bottomNavigationView.setSelectedItemId(-1);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_MyCakesSetupActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_MyCakesSetupActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_MyCakesSetupActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_MyCakesSetupActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });


    }

    private void addNewCake() {

        Intent intent = new Intent(B_MyCakesSetupActivity.this, B_AddNewCakeActivity.class);
        startActivity(intent);
        finish();
    }


    private void saveProfileAndContinue() {

        Intent intent = new Intent(B_MyCakesSetupActivity.this, B_MyQuoteSetupActivity.class);
        startActivity(intent);
        finish();
    }


}