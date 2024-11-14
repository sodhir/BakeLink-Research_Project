package com.example.bakelink.bakers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class B_MyCakesActivity extends AppCompatActivity {

    ImageView mcCakePicture;
    EditText mcCakeName;
    EditText mcDescription;
    EditText mcPrice;
    Button btnSaveAndContinue;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mcCakeImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmy_cakes);

        mcCakeName = findViewById(R.id.mcCakeName);
        mcDescription = findViewById(R.id.mcDescription);
        mcPrice = findViewById(R.id.mcPrice);
        btnSaveAndContinue = findViewById(R.id.btnSaveAndContinue);
        mcCakePicture = findViewById(R.id.mcCakePicture);

        mcCakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });


        btnSaveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCakeData(mcCakeImageUri);
            }
        });

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.nav_my_cakes);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_MyCakesActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_MyCakesActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_MyCakesActivity.this, B_MyCakesSetupActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_MyCakesActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // This is the URI of the selected image
            mcCakePicture.setImageURI(imageUri); // Display image in ImageView (if needed)
            // Save this URI to use it for uploading later
            mcCakeImageUri = imageUri; // Assuming you have declared ivProfilePictureUri as a global variable
        }
    }



    // Method to save cake data
    private void saveCakeData(Uri imageUri) {
        // Get user inputs
        String cakeName = mcCakeName.getText().toString().trim();
        String description = mcDescription.getText().toString().trim();
        Double price = Double.parseDouble(mcPrice.getText().toString());

        // Validation (simple example)
        if (cakeName.isEmpty() || description.isEmpty() || price == null || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and upload an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show a loading indicator if necessary
        // progressBar.setVisibility(View.VISIBLE);

        // Firebase Storage reference to store the image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("cake_images/" + UUID.randomUUID().toString());

        // Upload the image
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the image URL after a successful upload
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Create a data model for the cake
                        Map<String, Object> cakeData = new HashMap<>();
                        cakeData.put("cakeName", cakeName);
                        cakeData.put("description", description);
                        cakeData.put("price", price);
                        cakeData.put("cakeImgUrl", imageUrl);

                        // Get the baker's UID (assuming you have the user's ID)
                        String bakerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        // Save the data under "bakers" collection -> bakerId -> "cakes"
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("bakers").child(bakerId).child("cakes");
                        databaseRef.push().setValue(cakeData)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Cake data saved successfully!", Toast.LENGTH_SHORT).show();
                                       startActivity(new Intent(B_MyCakesActivity.this, B_MyCakesSetupActivity.class));
                                    } else {
                                        Toast.makeText(this, "Failed to save cake data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    // progressBar.setVisibility(View.GONE); // Hide loading indicator
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // progressBar.setVisibility(View.GONE); // Hide loading indicator
                });
    }

}