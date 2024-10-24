package com.example.bakelink.customers;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.bakelink.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.customers.adapters.TrendingBakerAdapter;
import com.example.bakelink.customers.modal.Baker;

import java.util.ArrayList;
import java.util.List;

public class C_HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTrendingBakers;
    private TrendingBakerAdapter trendingBakerAdapter;
    private List<Baker> trendingBakersList;

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


    }
}