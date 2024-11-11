package com.example.bakelink.bakers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class B_ProfileSetupActivity extends AppCompatActivity {

    private EditText etBakeryTitle, etDescription;
    private ImageView ivProfilePicture;
    private Button btnSaveAndContinue;
    //add for specialities and services section
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri ivProfilePictureUri;

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
            //Toast.makeText(this, "Upload Profile Picture", Toast.LENGTH_SHORT).show();
            openImagePicker();
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
            ivProfilePicture.setImageURI(imageUri); // Display image in ImageView (if needed)
            // Save this URI to use it for uploading later
            ivProfilePictureUri = imageUri;
        }
    }

    private void saveProfileAndContinue() {
        String bakeryTitle = etBakeryTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        Uri imageUri = ivProfilePictureUri;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profilePictures").child(userId + ".jpg");
            storageReference.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Get the image download URL
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Create a Map to hold baker profile data
                        Map<String, Object> bakerProfile = new HashMap<>();
                        bakerProfile.put("bakeryTitle", bakeryTitle);
                        bakerProfile.put("description", description);
                        bakerProfile.put("profilePictureUrl", imageUrl); // Save image URL
                        // Add other fields as necessary

                        // Save to "bakers" collection
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bakers");
                        databaseReference.child(userId).setValue(bakerProfile)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(B_ProfileSetupActivity.this, B_MyCakesSetupActivity.class));
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to save profile.", Toast.LENGTH_SHORT).show();
                                });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to get image URL.", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }



    }
}