package com.example.bakelink.bakers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.adapters.BakerAllCakeAdapter;
import com.example.bakelink.bakers.models.Cake;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class B_MyAllCakesActivity extends AppCompatActivity {

    RecyclerView allCakeRecycler;

    List<Cake> allCakeList;

    Button addnewCake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmy_all_cakes);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        allCakeRecycler = findViewById(R.id.allmyCakesRecycler);

        fetchAllCakes();

        addnewCake = findViewById(R.id.allckbtnAddNewCake);
        addnewCake.setOnClickListener(v -> {
            startActivity(new Intent(B_MyAllCakesActivity.this, B_AddNewCakeActivity.class));
        });

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.nav_my_cakes);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_MyAllCakesActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_MyAllCakesActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_MyAllCakesActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_MyAllCakesActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });

    }

    public void fetchAllCakes(){
// Get the current user (baker) UID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentBakerId = mAuth.getCurrentUser().getUid();

// Reference to the baker's cakes node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bakers")
                .child(currentBakerId).child("cakes");

// Attach a listener to fetch data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Cake> cakesList = new ArrayList<>();
                for (DataSnapshot cakeSnapshot : snapshot.getChildren()) {
                    // Assuming you have a Cake model class
                    String cakeId = cakeSnapshot.getKey();
                    String name = cakeSnapshot.child("cakeName").getValue(String.class);
                    String description = cakeSnapshot.child("description").getValue(String.class);
                    Double price = cakeSnapshot.child("price").getValue(Double.class);
                    String imageUrl = cakeSnapshot.child("cakeImgUrl").getValue(String.class);
                    //double price = 0;
//                    try {
//                        price = Double.parseDouble(priceString);
//                    } catch (NumberFormatException e) {
//                        Log.e("ParsingError", "Failed to parse price to double: " + e.getMessage());
//                    }

                    Cake cake = new Cake();
                    cake.setCakeId(cakeId);
                    cake.setDescription(description);
                    cake.setCakeName(name);
                    cake.setCakeImgUrl(imageUrl);
                    cake.setPrice(price); // Assuming `price` is of type double
                    cakesList.add(cake);
                }

                Log.d("Cakes", "Cakes fetched: " + cakesList.size());
                // Use the fetched list of cakes (e.g., update your UI)
                displayCakes(cakesList); // Implement this method to handle the list of cakes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void displayCakes(List<Cake> cakesList) {
        // Create an adapter and set it to the RecyclerView
        BakerAllCakeAdapter adapter = new BakerAllCakeAdapter(cakesList);
        allCakeRecycler.setAdapter(adapter);
        allCakeRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}