package com.example.bakelink.customers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.customers.adapters.CakeRequestAdapter;
import com.example.bakelink.customers.modal.CustomCakeRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class C_CakeRequestsActivity extends AppCompatActivity {

    RecyclerView requestsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ccake_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getAllCakeRequests();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cake_request);

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

    private void getAllCakeRequests() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests");
            databaseReference.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<CustomCakeRequest> cakeRequests = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CustomCakeRequest cakeRequest = snapshot.getValue(CustomCakeRequest.class);
                        if (cakeRequest != null) {
                            cakeRequests.add(cakeRequest);
                        }
                    }
                    // update the RecyclerView
                    updateRecyclerView(cakeRequests);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("FirebaseError", "Failed to retrieve requests", databaseError.toException());
                }
            });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateRecyclerView(List<CustomCakeRequest> cakeRequests) {

            RecyclerView recyclerView = findViewById(R.id.requestsrecyclerview);
            CakeRequestAdapter adapter = new CakeRequestAdapter(cakeRequests);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

    }
}