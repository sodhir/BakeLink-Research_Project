package com.example.bakelink.customers;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.customers.adapters.DealsAdapter;
import com.example.bakelink.customers.adapters.TopCakeCategoryAdapter;
import com.example.bakelink.customers.adapters.TrendingBakerAdapter;
import com.example.bakelink.customers.modal.Baker;
import com.example.bakelink.customers.modal.CakeCategory;
import com.example.bakelink.customers.modal.Deal;

import java.util.ArrayList;
import java.util.List;

public class C_HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTrendingBakers;
    private TrendingBakerAdapter trendingBakerAdapter;
    private List<Baker> trendingBakersList;

    private RecyclerView recyclerViewCategories;
    private List<CakeCategory> cakeCategoriesList;
    private TopCakeCategoryAdapter topCakeCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the top toolbar
        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);

        //Trending baker section
        // Initialize RecyclerView
        recyclerViewTrendingBakers = findViewById(R.id.recycler_view_trending_bakers);
        trendingBakersList = new ArrayList<>(); // Populate this with your data
        trendingBakersList.add(new Baker("Sweet Crumbs Bakery", "drawable/baker_a_image", 4.8f));
        trendingBakersList.add(new Baker("Frosted Perfection", "drawable/baker_b_image", 4.7f));
        trendingBakersList.add(new Baker("Bake Me Happy", "drawable/baker_c_image", 4.7f));

        // Setup the adapter
        trendingBakerAdapter = new TrendingBakerAdapter(trendingBakersList);
        recyclerViewTrendingBakers.setAdapter(trendingBakerAdapter);
        recyclerViewTrendingBakers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Top cake categories section
        //recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        //recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 3));

        // Sample data for cake categories
        cakeCategoriesList = new ArrayList<>();
        cakeCategoriesList.add(new CakeCategory("Wedding Cakes", "drawable/wedding_cake_image"));
        cakeCategoriesList.add(new CakeCategory("Seasonal Cakes", "drawable/seasonal_cake_image"));
        cakeCategoriesList.add(new CakeCategory("Themed Cakes", "drawable/themed_cake_image"));

        topCakeCategoryAdapter = new TopCakeCategoryAdapter(cakeCategoriesList);
        recyclerViewCategories.setAdapter(topCakeCategoryAdapter);

        //deals section
        RecyclerView dealsRecyclerView = findViewById(R.id.deals_recycler_view);

        List<Deal> dealsList = new ArrayList<>();
        dealsList.add(new Deal("Get 20% Off All Vegan Cakes!", "Use Code: VEGAN20", "Hurry! Limited Offer", "drawable/deal_image_1"));
        dealsList.add(new Deal("$10 Off Custom Cake Orders", "For orders above $100", "Valid till Dec 31, 2024", "drawable/deal_image_2"));
        dealsList.add(new Deal("Order a wedding cake\n" + "and enjoy free delivery", "", "Offer ends Nov 15, 2024", "drawable/deal_image_3"));

        DealsAdapter dealsAdapter = new DealsAdapter(dealsList);
        dealsRecyclerView.setAdapter(dealsAdapter);
        dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



    }
}