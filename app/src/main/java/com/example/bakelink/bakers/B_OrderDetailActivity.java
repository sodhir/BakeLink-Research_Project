package com.example.bakelink.bakers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class B_OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_border_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set welcome text
        TextView welcomeText = findViewById(R.id.welcomeText);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String bakeryName = sharedPreferences.getString("bakery_name", null); // Get bakery name
        welcomeText.setText("Welcome back, " + bakeryName + "!");

        Spinner orderStatusSpinner = findViewById(R.id.order_status_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Pending", "Completed", "In Progress"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderStatusSpinner.setAdapter(adapter);

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.none);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_OrderDetailActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_OrderDetailActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_OrderDetailActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_OrderDetailActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}
