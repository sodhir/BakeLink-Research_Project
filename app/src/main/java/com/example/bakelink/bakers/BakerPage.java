package com.example.bakelink.bakers;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
import com.example.bakelink.customers.adapters.CakeServiceAdapter;
import com.example.bakelink.customers.adapters.CustomerReviewAdapter;

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

    public TextView bakerRatingString;

    private CakeServiceAdapter adapter;

    private RecyclerView recyclerView;

    private TextView aboutBaker;

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

        Intent intent = getIntent();
        String strBaker = intent.getStringExtra("bakerName");
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

        // Checking if the imageUrl starts with "drawable" or is a URL
        if (imageUrl != null && imageUrl.startsWith("drawable/")) {
            // Get the drawable resource name from imageUrl
            String drawableName = imageUrl.replace("drawable/", "");

            // Get the resource ID from the drawable name
            int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());

            // Use Glide to load from the drawable resource
            Glide.with(context)
                    .load(resourceId) // Load from drawable
                    .into(bakerImage);
        } else {
            // Load from URL using Glide
            Glide.with(context)
                    .load(imageUrl) // Load from URL
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

       List<String> cakeServiceList = populateCakeServiceList();
        adapter = new CakeServiceAdapter(cakeServiceList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SetCustomerRecyclerView();
        SetCakeRecyclerView();

        aboutBaker = findViewById(R.id.txtAboutBaker);
        aboutBaker.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");

    }

    private void SetCakeRecyclerView() {

        RecyclerView cakeRecycler = findViewById(R.id.cakeRecycler);
        List<Cake> cakeList = GetCakeList();
        BakerCakeAdapter adapter = new BakerCakeAdapter(cakeList);
        cakeRecycler.setAdapter(adapter);
        cakeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }

    private List<Cake> GetCakeList() {
        List<Cake> cakeList = new ArrayList<>();
        cakeList.add(new Cake(12.99, "Birthday Cake", "drawable/cakesample1"));
        cakeList.add(new Cake(12.99, "Birthday Cake", "drawable/cakesample2"));
        cakeList.add(new Cake(12.99, "Birthday Cake", "drawable/cakesample3"));
        cakeList.add(new Cake(12.99, "Birthday Cake", "drawable/cakesample4"));
        return cakeList;
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

    private List<String> populateCakeServiceList() {
        List<String> cakeServiceList = List.of("Custom Wedding Cakes", "Gluten free", "Vegan");
        return cakeServiceList;
    }
}