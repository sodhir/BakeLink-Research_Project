package com.example.bakelink.bakers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private FrameLayout ivProfileFrame;
    private Button btnSaveAndContinue;
    //add for specialities and services section
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri ivProfilePictureUri;
    private LinearLayout servicesListLayout;
    private SpecialitiesBottomSheetFragment bottomSheetFragment;
    private FrameLayout addServiceButton;

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
        ivProfileFrame = findViewById(R.id.ivProfileFrame);
        btnSaveAndContinue = findViewById(R.id.btnSaveAndContinue);
        servicesListLayout = findViewById(R.id.services_list_layout);
        addServiceButton = findViewById(R.id.add_specialities_button);

        // Set listener for profile picture upload
        ivProfileFrame.setOnClickListener(v -> {
            //functionality for upload picture
            //Toast.makeText(this, "Upload Profile Picture", Toast.LENGTH_SHORT).show();
            openImagePicker();
        });

        //Set listener for adding specialities
        addServiceButton.setOnClickListener(v -> {
            bottomSheetFragment = new SpecialitiesBottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
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
                startActivity(new Intent(B_ProfileSetupActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_ProfileSetupActivity.this, B_MyAllCakesActivity.class));
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

    public void addSpecialityToList(String speciality) {
        TextView textView = new TextView(this);
        // Set the text for the TextView
        textView.setText(speciality);
        // Set the text color
        textView.setTextColor(getResources().getColor(android.R.color.black));
        // Set the text size
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        // Set padding
        textView.setPadding(0, 8, 0, 8);
        // Set the layout parameters (margin top)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topMargin = 8; // 8dp margin top
        // Apply the layout parameters to the TextView
        textView.setLayoutParams(layoutParams);
        // Add the TextView to the layout
        servicesListLayout.addView(textView);
    }

    private void saveProfileAndContinue() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is authenticated
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if the user is not authenticated
        }

        String bakeryTitle = etBakeryTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        Uri imageUri = ivProfilePictureUri;

       if (currentUser != null) {
           Toast.makeText(this, "User authenticated.", Toast.LENGTH_SHORT).show();
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
                                    startActivity(new Intent(B_ProfileSetupActivity.this, B_MyQuoteSetupActivity.class));
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

        //startActivity(new Intent(B_ProfileSetupActivity.this, B_MyQuoteSetupActivity.class));

    }
}