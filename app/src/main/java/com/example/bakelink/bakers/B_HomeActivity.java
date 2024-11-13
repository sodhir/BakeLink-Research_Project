package com.example.bakelink.bakers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.adapters.QuoteRequestAdapter;
import com.example.bakelink.bakers.adapters.TrackSubmittedQuotesAdapter;
import com.example.bakelink.bakers.models.Quote;
import com.example.bakelink.bakers.models.QuoteRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class B_HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuoteRequestAdapter adapter;
    private List<QuoteRequest> quoteRequestList;
    private RecyclerView recyclerTrackSubmittedQuotes;
    private TrackSubmittedQuotesAdapter submittedQuotesAdapter;
    List<Quote> quotesSentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bhome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set welcome text
        TextView welcomeText = findViewById(R.id.welcomeText);
        String username = "username";  // Retrieve actual username from intent or shared preferences
        welcomeText.setText("Welcome back, " + username + "!");

        recyclerView = findViewById(R.id.recyclerViewNewQuoteRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize data
        quoteRequestList = new ArrayList<>();
        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.cakesample2));
        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.themed_cake_image));
        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.cakesample3));
        // Add more items as needed

        // Set adapter
        adapter = new QuoteRequestAdapter(this, quoteRequestList);
        recyclerView.setAdapter(adapter);

        //track submitted quote section
        recyclerTrackSubmittedQuotes = findViewById(R.id.recyclerViewTrackSubmittedQuote);
        quotesSentList = new ArrayList<>();
        quotesSentList.add(new Quote("[Customer name]", 250.00, "Awaiting Approval",R.drawable.cakesample2));
        quotesSentList.add(new Quote("[Customer name]", 180.00, "Approved",R.drawable.themed_cake_image));
        quotesSentList.add(new Quote("[Customer name]", 180.00, "Approved",R.drawable.cakesample3));
        submittedQuotesAdapter = new TrackSubmittedQuotesAdapter(this,quotesSentList);
        recyclerTrackSubmittedQuotes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerTrackSubmittedQuotes.setAdapter(submittedQuotesAdapter);

        //weekly and monthly sales section
        TextView chipWeekly = findViewById(R.id.chip_weekly);
        TextView chipMonthly = findViewById(R.id.chip_monthly);

        // Set "Weekly" chip as selected by default
        chipWeekly.setBackgroundColor(Color.parseColor("#721124"));
        chipWeekly.setTextColor(Color.parseColor("#FFFFFF"));

        chipWeekly.setOnClickListener(view -> {
            // Set selected colors for Weekly chip
            chipWeekly.setBackgroundColor(Color.parseColor("#721124"));
            chipWeekly.setTextColor(Color.parseColor("#FFFFFF"));

            // Reset Monthly chip to default
            chipMonthly.setBackgroundColor(Color.parseColor("#F5F5F5"));
            chipMonthly.setTextColor(Color.parseColor("#80000000"));
        });

        chipMonthly.setOnClickListener(view -> {
            // Set selected colors for Monthly chip
            chipMonthly.setBackgroundColor(Color.parseColor("#721124"));
            chipMonthly.setTextColor(Color.parseColor("#FFFFFF"));

            // Reset Weekly chip to default
            chipWeekly.setBackgroundColor(Color.parseColor("#F5F5F5"));
            chipWeekly.setTextColor(Color.parseColor("#80000000"));
        });


        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                //no action
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_HomeActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_HomeActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_HomeActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });

    }

}