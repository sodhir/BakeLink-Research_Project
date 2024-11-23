package com.example.bakelink.customers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.customers.modal.CustomerProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class C_ProfileActivity extends AppCompatActivity {

    private ImageButton cartIcon;
    private TextView emailTextView, fullNameTextView, addressTextView, dobTextView;
    private ImageView profileImageView;
    private Button editProfileButton;
    private DatabaseReference customersReference;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cprofile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        cartIcon = findViewById(R.id.cart_icon);

        // Set OnClickListener for the cart icon
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to C_CartActivity
                Intent intent = new Intent(C_ProfileActivity.this, C_CartActivity.class);
                startActivity(intent);
            }
        });

        // Initialize Firebase and UI components
        auth = FirebaseAuth.getInstance();
        customersReference = FirebaseDatabase.getInstance().getReference("Customers");
        //profileImageView = findViewById(R.id.profile_image);
        emailTextView = findViewById(R.id.tv_email);
        fullNameTextView = findViewById(R.id.tv_full_name);
        addressTextView = findViewById(R.id.tv_address);
        dobTextView = findViewById(R.id.tv_dob);
        editProfileButton = findViewById(R.id.btn_edit_profile);
        storageReference = FirebaseStorage.getInstance().getReference("profile_images"); // Reference for profile images in Firebase Storage

        // Load user data
        String userId = auth.getCurrentUser().getUid();
        loadCustomerData(userId);
        Log.d("customerProfile","userid:"+userId);

        // Edit Profile button
        editProfileButton.setOnClickListener(v -> {
            BottomSheetEditProfile editProfileSheet = new BottomSheetEditProfile();
            editProfileSheet.show(getSupportFragmentManager(), "EditProfileBottomSheet");
        });

        //fab
        ImageButton fab = findViewById(R.id.fab_request_quote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), C_RequestQuoteActivity.class));
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), C_HomeActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.nav_bakers) {
                    startActivity(new Intent(getApplicationContext(), C_AllBakersActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.nav_cake_request) {
                    startActivity(new Intent(getApplicationContext(), C_CakeRequestsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), C_ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
    private void loadCustomerData(String userId) {
        Log.d("customerProfile", "useridinsidefnc:" + userId);

        // Fetch email from Users collection
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
        usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("email").getValue(String.class);
                    Log.d("customerProfile", "email:" + email);
                    emailTextView.setText(email != null ? email : "N/A");
                } else {
                    Log.d("customerProfile", "No user found with userId: " + userId);
                    emailTextView.setText("N/A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(C_ProfileActivity.this, "Failed to load email", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch other details from Customers collection
        customersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullName = snapshot.child("fullName").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String dob = snapshot.child("dob").getValue(String.class);
                    String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                    fullNameTextView.setText(fullName != null ? fullName : "N/A");
                    addressTextView.setText(address != null ? address : "N/A");
                    dobTextView.setText(dob != null ? dob : "N/A");

                    // Load profile image using a library like Glide or Picasso
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(C_ProfileActivity.this)
                                .load(profileImageUrl)// Placeholder image
                                .into(profileImageView);
                    } else {
                        profileImageView.setImageResource(R.drawable.cakesample1);
                    }
                } else {
                    Log.d("customerProfile", "Customer node not found. Creating default data...");
                    createDefaultCustomerData(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(C_ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDefaultCustomerData(String userId) {
        Log.d("customerProfile", "Creating default customer data for userId: " + userId);
        DatabaseReference defaultCustomerRef = customersReference.child(userId);

        // Example default data with placeholder profile image URL
        CustomerProfile defaultProfile = new CustomerProfile(
                "Default Name",
                "Default Address",
                "01/01/2000"
        );

        defaultCustomerRef.setValue(defaultProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("customerProfile", "Default customer data created successfully.");
                Toast.makeText(C_ProfileActivity.this, "Default profile created. Please edit your details.", Toast.LENGTH_SHORT).show();

                // Update UI with default values
                fullNameTextView.setText(defaultProfile.getFullName());
                addressTextView.setText(defaultProfile.getAddress());
                dobTextView.setText(defaultProfile.getDob());
                profileImageView.setImageResource(R.drawable.cakesample1);
            } else {
                Log.d("customerProfile", "Failed to create default customer data.");
                Toast.makeText(C_ProfileActivity.this, "Failed to create default profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadProfileImage(Uri imageUri, String userId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProfileImages").child(userId + ".jpg");

        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Save the image URL to the database
                    customersReference.child(userId).child("profileImageUrl").setValue(uri.toString())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(C_ProfileActivity.this, "Profile image updated successfully!", Toast.LENGTH_SHORT).show();

                                // Update the profile image in the UI
                                Glide.with(C_ProfileActivity.this)
                                        .load(uri)
                                        .into(profileImageView);
                            })
                            .addOnFailureListener(e -> Toast.makeText(C_ProfileActivity.this, "Failed to update profile image.", Toast.LENGTH_SHORT).show());
                })
        ).addOnFailureListener(e -> Toast.makeText(C_ProfileActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show());
    }
}