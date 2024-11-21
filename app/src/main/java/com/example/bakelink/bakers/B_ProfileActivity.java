package com.example.bakelink.bakers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.customers.modal.Baker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class B_ProfileActivity extends AppCompatActivity {

    private TextView bakerName, bakerDescription, bakerSpecialities;
    private ImageView profilePicture;
    private Button editProfileButton, logoutButton;
    private DatabaseReference databaseReference;
    String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bprofile);
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

        // Initialize Views
        bakerName = findViewById(R.id.baker_name);
        bakerDescription = findViewById(R.id.baker_description);
        bakerSpecialities = findViewById(R.id.baker_specialities);
        profilePicture = findViewById(R.id.profile_picture);
        editProfileButton = findViewById(R.id.edit_profile_button);
        logoutButton = findViewById(R.id.logout_button);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Firebase Database Reference (assuming you're using Firebase Realtime Database)
        databaseReference = FirebaseDatabase.getInstance().getReference("bakers").child(currentUserId);

        // Fetch baker data
        fetchBakerData();


        // Set button click listeners
        editProfileButton.setOnClickListener(v -> {
            BakerProfileBottomSheetFragment bottomSheetFragment = new BakerProfileBottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        logoutButton.setOnClickListener(v -> {
            // Handle logout logic here
        });

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_ProfileActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_ProfileActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_ProfileActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_ProfileActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
    private void fetchBakerData() {
        // Fetch the baker's data from Firebase Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("profile", "DataSnapshot: " + dataSnapshot.getValue());
                    // Get the Baker object from the snapshot
                    Baker baker = dataSnapshot.getValue(Baker.class);
                    Log.d("profile",baker.toString());

                    if (baker != null) {
                        // Set the fetched data into the views
                        bakerName.setText(baker.getBakeryTitle());
                        bakerDescription.setText(baker.getDescription());
                        List<String> specialities = baker.getSpecialtiesAndServices();
                        String specialitiesText = TextUtils.join("\n", specialities);
                        bakerSpecialities.setText(specialitiesText);

                        // Load the profile picture (if you have the URL)
                        if (!baker.getProfilePictureUrl().isEmpty()) {
                            // Load the profile image using Glide
                            Glide.with(B_ProfileActivity.this)
                                    .load(baker.getProfilePictureUrl())
                                    .circleCrop()
                                    .into(profilePicture);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error (e.g., show a toast or log the error)
            }
        });
    }
}