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

public class B_AddNewCakeActivity extends AppCompatActivity {

    private EditText etCakeName, etDescription, etCakePrice;
    private ImageView ivCakeImage;
    private Button btnDone;
    //add for sizes, weights, flavor, fillings section

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_badd_new_cake);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        etCakeName = findViewById(R.id.nckCakeName);
        etDescription = findViewById(R.id.nckDescription);
        etCakePrice = findViewById(R.id.nckCakePrice);
        ivCakeImage = findViewById(R.id.ivCakeImage);
        btnDone = findViewById(R.id.btnDone);

        // Set listener for cake image upload
        ivCakeImage.setOnClickListener(v -> {
            //functionality for upload picture
            Toast.makeText(this, "Upload Cake Image", Toast.LENGTH_SHORT).show();
        });

        // Done button click listener
        btnDone.setOnClickListener(v -> saveCakeDetails());

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        // Deselect the item (no item should be highlighted)
        bottomNavigationView.setSelectedItemId(-1);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.setSelectedItemId(-1);
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_AddNewCakeActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_AddNewCakeActivity.this, B_ScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_AddNewCakeActivity.this, B_MyCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_AddNewCakeActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void saveCakeDetails() {
        String cakeName = etCakeName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String cakePrice = etCakePrice.getText().toString().trim();

        /*if (cakeName.isEmpty() || description.isEmpty() || cakePrice.isEmpty()) {
            Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
            return;
        }*/

        // Create a new Cake object
        // Create a Cake model and a database method to save the cake
        // Cake newCake = new Cake(cakeName, description, cakePrice, imageUri.toString());

        // code to save data to database


        Toast.makeText(this, "Cake added successfully", Toast.LENGTH_SHORT).show();


        startActivity(new Intent(B_AddNewCakeActivity.this, B_MyCakesSetupActivity.class));
        finish();
    }
}