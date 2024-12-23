package com.example.bakelink.bakers;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.bakers.adapters.BakerCakeAdapter;
import com.example.bakelink.bakers.models.Cake;
import com.example.bakelink.customers.C_AllBakersActivity;
import com.example.bakelink.customers.C_CakeRequestsActivity;
import com.example.bakelink.customers.C_CartActivity;
import com.example.bakelink.customers.C_HomeActivity;
import com.example.bakelink.customers.C_ProfileActivity;
import com.example.bakelink.customers.adapters.CakeServiceAdapter;
import com.example.bakelink.customers.adapters.CustomerReviewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BakerPage extends AppCompatActivity {

    private TextView bakerName;
    private ImageView bakerImage;
    public ImageView star1;
    public ImageView star2;
    public ImageView star3;
    public ImageView star4;
    public ImageView star5;

    String currentBakerId;

    public TextView bakerRatingString;

    private CakeServiceAdapter adapter;

    private RecyclerView recyclerView, cakeRecycler;

    private TextView aboutBaker;
    private ImageButton cartIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_baker_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartIcon = findViewById(R.id.cart_icon);


        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BakerPage.this, C_CartActivity.class);
                startActivity(intent);
            }
        });

        setupBottomNavigation();

        Intent intent = getIntent();

        String strBaker = intent.getStringExtra("bakerName");
        currentBakerId = intent.getStringExtra("bakerId");
        if (currentBakerId != null) {
            fetchBakerDetails();
            fetchBakerServices();
            fetchBakerCakes();
        }
        bakerName = findViewById(R.id.txtBakerTitle);
        bakerName.setText(strBaker);
        bakerImage = findViewById(R.id.bakerImage);
        bakerRatingString = findViewById(R.id.txtBakerRating1);

        star1 = findViewById(R.id.star_1);
        star2 = findViewById(R.id.star_2);
        star3 = findViewById(R.id.star_3);
        star4 = findViewById(R.id.star_4);
        star5 = findViewById(R.id.star_5);


        Context context = getBaseContext();

        String imageUrl = intent.getStringExtra("bakerImage");


        if (imageUrl != null && imageUrl.startsWith("drawable/")) {

            String drawableName = imageUrl.replace("drawable/", "");


            int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());


            Glide.with(context)
                    .load(resourceId)
                    .into(bakerImage);
        } else {

            Glide.with(context)
                    .load(imageUrl)
                    .fitCenter()
                    .into(bakerImage);
        }


        int rating = (int) Math.round(intent.getFloatExtra("bakerRating",0));
        star1.setImageResource(rating >= 1 ? R.drawable.ic_star_filled : R.drawable.ic_star);
        star2.setImageResource(rating >= 2 ? R.drawable.ic_star_filled : R.drawable.ic_star);
        star3.setImageResource(rating >= 3 ? R.drawable.ic_star_filled : R.drawable.ic_star);
        star4.setImageResource(rating >= 4 ? R.drawable.ic_star_filled : R.drawable.ic_star);
        star5.setImageResource(rating >= 5 ? R.drawable.ic_star_filled : R.drawable.ic_star);

        Float bakerRating = intent.getFloatExtra("bakerRating", 0);
        bakerRatingString.setText(bakerRating.toString());

        recyclerView = findViewById(R.id.servicesRecycler);
        cakeRecycler = findViewById(R.id.cakeRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SetCustomerRecyclerView();

        cakeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        aboutBaker = findViewById(R.id.textView5);


    }



    private void fetchBakerDetails() {
        DatabaseReference bakerRef = FirebaseDatabase.getInstance().getReference("bakers").child(currentBakerId);
        Log.d("bakerdetails","bakerid"+currentBakerId);
        bakerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bakeryTitle= snapshot.child("bakeryTitle").getValue(String.class);
                Log.d("bakerdetails","bakertitle"+bakeryTitle);
                //String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                String description = snapshot.child("description").getValue(String.class);
                Log.d("bakerdetails","bakerdesc"+description);

                if (bakeryTitle != null) bakerName.setText(bakeryTitle);
                if (description != null) aboutBaker.setText(description);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BakerPage", "Failed to fetch baker details: " + error.getMessage());
            }
        });
    }

    private void fetchBakerServices() {
        DatabaseReference servicesRef = FirebaseDatabase.getInstance().getReference("bakers")
                .child(currentBakerId).child("specialtiesAndServices");

        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> services = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    String service = serviceSnapshot.getValue(String.class);
                    if (service != null) services.add(service);
                }

                CakeServiceAdapter adapter = new CakeServiceAdapter(services);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BakerPage", "Failed to fetch services: " + error.getMessage());
            }
        });
    }

    private void fetchBakerCakes() {
        DatabaseReference cakesRef = FirebaseDatabase.getInstance().getReference("bakers")
                .child(currentBakerId).child("cakes");

        cakesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Cake> cakes = new ArrayList<>();
                for (DataSnapshot cakeSnapshot : snapshot.getChildren()) {
                    String cakeId = cakeSnapshot.getKey();
                    String name = cakeSnapshot.child("cakeName").getValue(String.class);
                    String description = cakeSnapshot.child("description").getValue(String.class);
                    Double price = cakeSnapshot.child("price").getValue(Double.class);
                    String imageUrl = cakeSnapshot.child("cakeImgUrl").getValue(String.class);

                    Cake cake = new Cake();
                    cake.setCakeId(cakeId);
                    cake.setDescription(description);
                    cake.setCakeName(name);
                    cake.setCakeImgUrl(imageUrl);
                    cake.setPrice(price);

                    if (cakeSnapshot.child("weights").exists()) {
                        List<String> weights = new ArrayList<>();
                        for (DataSnapshot weightSnapshot : cakeSnapshot.child("weights").getChildren()) {
                            String weight = weightSnapshot.getValue(String.class);
                            if (weight != null) {
                                weights.add(weight);
                            }
                        }
                        cake.setWeights(weights);
                    }

                    if (cakeSnapshot.child("flavors").exists()) {
                        List<String> flavors = new ArrayList<>();
                        for (DataSnapshot flavorSnapshot : cakeSnapshot.child("flavors").getChildren()) {
                            String flavor = flavorSnapshot.getValue(String.class);
                            if (flavor != null) {
                                flavors.add(flavor);
                            }
                        }
                        cake.setFlavors(flavors);
                    }

                    if (cakeSnapshot.child("fillings").exists()) {
                        List<String> fillings = new ArrayList<>();
                        for (DataSnapshot fillingSnapshot : cakeSnapshot.child("fillings").getChildren()) {
                            String filling = fillingSnapshot.getValue(String.class);
                            if (filling != null) {
                                fillings.add(filling);
                            }
                        }
                        cake.setFillings(fillings);
                    }

                    cakes.add(cake);
                }

                BakerCakeAdapter adapter = new BakerCakeAdapter(cakes);
                cakeRecycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BakerPage", "Failed to fetch cakes: " + error.getMessage());
            }
        });
    }


    private void SetCustomerRecyclerView() {
        RecyclerView customerReviewRecycler = findViewById(R.id.customerReviewRecycler);
        List<String> customerReviewList = GetCustomerReview();
        CustomerReviewAdapter adapter = new CustomerReviewAdapter(customerReviewList);
        customerReviewRecycler.setAdapter(adapter);
        customerReviewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private List<String> GetCustomerReview() {

       List<String> customerReviewList = Arrays.asList("\"Ordered a custom cake for my husband’s \n" +
               "birthday, and the design was gorgeous. \n" +
               "The cake tasted good, though it was a bit \n" +
               "on the sweeter side for my taste. Overall, \n" +
               "very happy with the experience.\"", "\"Ordered a custom cake for my husband’s \n" +
               "birthday, and the design was gorgeous. \n" +
               "The cake tasted good, though it was a bit \n" +
               "on the sweeter side for my taste. Overall, \n" +
               "very happy with the experience.\"", "\"Ordered a custom cake for my husband’s \n" +
               "birthday, and the design was gorgeous. \n" +
               "The cake tasted good, though it was a bit \n" +
               "on the sweeter side for my taste. Overall, \n" +
               "very happy with the experience.\"");
        return customerReviewList;

    }





    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.none);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
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
        });
    }
}