package com.example.bakelink.bakers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.example.bakelink.bakers.adapters.QuoteRequestAdapter;
import com.example.bakelink.bakers.adapters.TrackSubmittedQuotesAdapter;
import com.example.bakelink.bakers.models.Cake;
import com.example.bakelink.bakers.models.Quote;
import com.example.bakelink.bakers.models.QuoteRequest;
import com.example.bakelink.bakers.models.QuoteResponse;
import com.example.bakelink.customers.modal.CustomCakeRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class B_HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuoteRequestAdapter adapter;
    private List<CustomCakeRequest> quoteRequestList;
    private RecyclerView recyclerTrackSubmittedQuotes;
    private TrackSubmittedQuotesAdapter submittedQuotesAdapter;
    List<QuoteResponse> quotesSentList;

    String email;

    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String bakeryName = sharedPreferences.getString("bakery_name", null);
        welcomeText.setText("Welcome back, " + bakeryName + "!");

        recyclerView = findViewById(R.id.recyclerViewNewQuoteRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        loadAllCustomQuotes();

        //track submitted quote section
        recyclerTrackSubmittedQuotes = findViewById(R.id.recyclerViewTrackSubmittedQuote);
        loadSubmittedQuotes();

        //weekly and monthly sales section
        TextView chipWeekly = findViewById(R.id.chip_weekly);
        TextView chipMonthly = findViewById(R.id.chip_monthly);

        fetchWeeklySales(currentUserId);
        calculateTotalSalesAndOrders(currentUserId);

        // Set "Weekly" chip as selected by default
        chipWeekly.setBackgroundColor(Color.parseColor("#721124"));
        chipWeekly.setTextColor(Color.parseColor("#FFFFFF"));

        chipWeekly.setOnClickListener(view -> {
            chipWeekly.setBackgroundColor(Color.parseColor("#721124"));
            chipWeekly.setTextColor(Color.parseColor("#FFFFFF"));

            chipMonthly.setBackgroundColor(Color.parseColor("#F5F5F5"));
            chipMonthly.setTextColor(Color.parseColor("#80000000"));
            fetchWeeklySales(currentUserId);
        });

        chipMonthly.setOnClickListener(view -> {
            chipMonthly.setBackgroundColor(Color.parseColor("#721124"));
            chipMonthly.setTextColor(Color.parseColor("#FFFFFF"));

            chipWeekly.setBackgroundColor(Color.parseColor("#F5F5F5"));
            chipWeekly.setTextColor(Color.parseColor("#80000000"));
            fetchMonthlySales(currentUserId);
        });

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
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

    private void loadSubmittedQuotes() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("bakers")
                .child(currentUserId)
                .child("quoteResponses");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quotesSentList = new ArrayList<>();
                int quoteCount = (int) snapshot.getChildrenCount();
                AtomicInteger loadedQuotes = new AtomicInteger(0);


                TextView noQuotesMessage = findViewById(R.id.noQuotesMessage);
                RecyclerView quotesRecyclerView = findViewById(R.id.recyclerViewTrackSubmittedQuote);

                // If no quotes found, show the "no quotes" message and hide the RecyclerView
                if (quoteCount == 0) {
                    noQuotesMessage.setVisibility(View.VISIBLE);
                    quotesRecyclerView.setVisibility(View.GONE);
                } else {
                    noQuotesMessage.setVisibility(View.GONE);
                    quotesRecyclerView.setVisibility(View.VISIBLE);
                }

                for (DataSnapshot responseSnapshot : snapshot.getChildren()) {
                    String responseId = responseSnapshot.getKey();
                    String customCakeRequestId = responseSnapshot.child("customCakeRequestId").getValue(String.class);
                    String userId = responseSnapshot.child("userId").getValue(String.class);
                    Double quotedPrice = responseSnapshot.child("quotedPrice").getValue(Double.class);
                    String responseMessage = responseSnapshot.child("responseMessage").getValue(String.class);
                    String status = responseSnapshot.child("status").getValue(String.class);
                    String imageUrl = responseSnapshot.child("imageUrl").getValue(String.class);
                    String cakeType = responseSnapshot.child("cakeType").getValue(String.class);

                    QuoteResponse cakeResponse = new QuoteResponse();
                    cakeResponse.setQuoteResponseId(responseId);
                    cakeResponse.setCustomCakeRequestId(customCakeRequestId);
                    cakeResponse.setBakerId(userId);
                    cakeResponse.setQuotedPrice(quotedPrice);
                    cakeResponse.setResponseMessage(responseMessage);
                    cakeResponse.setStatus(status);
                    cakeResponse.setImageUrl(imageUrl);
                    cakeResponse.setCakeType(cakeType);


                    DatabaseReference customCakeRef = FirebaseDatabase.getInstance()
                            .getReference("customCakeRequests")
                            .child(customCakeRequestId);

                    customCakeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot customCakeSnapshot) {
                            String customerId = customCakeSnapshot.child("userId").getValue(String.class);
                            String imageUrl = customCakeSnapshot.child("imageUrl").getValue(String.class);

                            cakeResponse.setCustomerId(customerId);
                           cakeResponse.setImageUrl(imageUrl);

                            quotesSentList.add(cakeResponse);

                            // Increment loadedQuotes and update adapter when all quotes are loaded
                            if (loadedQuotes.incrementAndGet() == quoteCount) {
                                updateAdapter();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("CustomCakeFetch", "Failed to fetch custom cake details: " + error.getMessage());
                        }
                    });
                }

                // If no quotes found, update adapter to show empty list
                if (quoteCount == 0) {
                    updateAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LoadQuotes", "Failed to fetch quotes: " + error.getMessage());
            }
        });
    }

    // Helper method to update adapter and setup RecyclerView
    private void updateAdapter() {
        Log.d("Responses", "submitted quotes fetched: " + quotesSentList.size());
        if (submittedQuotesAdapter == null) {
            submittedQuotesAdapter = new TrackSubmittedQuotesAdapter(B_HomeActivity.this, quotesSentList);
            recyclerTrackSubmittedQuotes.setLayoutManager(new LinearLayoutManager(B_HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerTrackSubmittedQuotes.setAdapter(submittedQuotesAdapter);
        } else {
            submittedQuotesAdapter.notifyDataSetChanged();
        }
    }


    private void loadAllCustomQuotes() {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               quoteRequestList = new ArrayList<>();
                for (DataSnapshot customCakeSnapshot : snapshot.getChildren()) {
                    String customCakeRequestId = customCakeSnapshot.getKey();
                    String deliveryDate = customCakeSnapshot.child("deliveryDate").getValue(String.class);
                    String deliveryTime = customCakeSnapshot.child("deliveryTime").getValue(String.class);
                    String notes = customCakeSnapshot.child("notes").getValue(String.class);
                    String imageUrl = customCakeSnapshot.child("imageUrl").getValue(String.class);
                    String userId = customCakeSnapshot.child("userId").getValue(String.class);
                    String cakeType = customCakeSnapshot.child("cakeType").getValue(String.class);
                    String cakeSize = customCakeSnapshot.child("cakeSize").getValue(String.class);
                    String flavor = customCakeSnapshot.child("flavor").getValue(String.class);
                    String filling = customCakeSnapshot.child("filling").getValue(String.class);
                    String cakeRequestStatus = customCakeSnapshot.child("cakeRequestStatus").exists() ? customCakeSnapshot.child("cakeRequestStatus").getValue(String.class) : "";


                    CustomCakeRequest cakeRequest = new CustomCakeRequest();
                    cakeRequest.setCustomCakeRequestId(customCakeRequestId);
                    cakeRequest.setDeliveryDate(deliveryDate);
                    cakeRequest.setDeliveryTime(deliveryTime);
                    cakeRequest.setNotes(notes);
                    cakeRequest.setImageUrl(imageUrl);
                    cakeRequest.setUserId(userId);
                    cakeRequest.setCakeType(cakeType);
                    cakeRequest.setCakeSize(cakeSize);
                    cakeRequest.setFlavor(flavor);
                    cakeRequest.setFilling(filling);
                    if(userId != null){
                        cakeRequest.setUserEmail(getUserEmail(userId));
                    }
                    if(cakeRequestStatus.equals("Pending")){
                        quoteRequestList.add(cakeRequest);
                    }

                }

                Log.d("Cakes", "Cakes fetched: " + quoteRequestList.size());
                adapter = new QuoteRequestAdapter(B_HomeActivity.this, quoteRequestList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private String getUserEmail(String userId){
        DatabaseReference userdatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        userdatabaseReference.child(userId).child("email").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                email = task.getResult().getValue(String.class);
            } else {
                Log.e("Firebase", "Failed to retrieve email: " + task.getException().getMessage());
            }
        });
        return email;
    }

    private void fetchWeeklySales(String bakerId) {
        DatabaseReference salesRef = FirebaseDatabase.getInstance()
                .getReference("bakers")
                .child(bakerId)
                .child("calendar");

        // Get the last 7 days
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<String> last7Days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            last7Days.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        salesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Float> weeklySales = new LinkedHashMap<>();

                for (String date : last7Days) {
                    float dailyTotal = 0;
                    String[] dateParts = date.split("-");
                    String year = dateParts[0];
                    String month = dateParts[1];
                    String day = dateParts[2];

                    // Check if data for this day exists
                    if (snapshot.child(year).child(month).child(day).hasChild("orders")) {
                        for (DataSnapshot orderSnapshot : snapshot.child(year).child(month).child(day).child("orders").getChildren()) {
                            Float orderTotal = orderSnapshot.child("orderTotal").getValue(Float.class);
                            if (orderTotal != null) {
                                dailyTotal += orderTotal;
                            }
                        }
                    }

                    weeklySales.put(date, dailyTotal);
                }
                displayLineChart(weeklySales, "Weekly Sales");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load weekly sales", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMonthlySales(String bakerId) {
        DatabaseReference salesRef = FirebaseDatabase.getInstance()
                .getReference("bakers")
                .child(bakerId)
                .child("calendar");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

        String currentMonthYear = monthYearFormat.format(calendar.getTime());
        String[] parts = currentMonthYear.split("-");
        String currentYear = parts[0];
        String currentMonth = parts[1];

        salesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Float> monthlySales = new LinkedHashMap<>();

                for (int day = 1; day <= 31; day++) {
                    String dayString = day < 10 ? "0" + day : String.valueOf(day);

                    if (snapshot.child(currentYear).child(currentMonth).child(dayString).hasChild("orders")) {
                        float dailyTotal = 0;

                        for (DataSnapshot orderSnapshot : snapshot.child(currentYear).child(currentMonth).child(dayString).child("orders").getChildren()) {
                            Float orderTotal = orderSnapshot.child("orderTotal").getValue(Float.class);
                            if (orderTotal != null) {
                                dailyTotal += orderTotal;
                            }
                        }

                        monthlySales.put(currentYear + "-" + currentMonth + "-" + dayString, dailyTotal);
                    }
                }
                displayLineChart(monthlySales, "Monthly Sales");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load monthly sales", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayLineChart(Map<String, Float> salesData, String label) {
        LineChart lineChart = findViewById(R.id.lineChart);

        List<Entry> entries = new ArrayList<>();
        int index = 0;
        List<String> labels = new ArrayList<>(); // For custom labels

        for (Map.Entry<String, Float> entry : salesData.entrySet()) {
            entries.add(new Entry(index, entry.getValue()));
            labels.add(entry.getKey()); // Add date labels
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(Color.parseColor("#721124"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setCircleColor(Color.parseColor("#88A84F"));
        dataSet.setLineWidth(2f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        lineChart.getDescription().setText(label);
        lineChart.animateX(1000);
        lineChart.invalidate();
    }

    private void calculateTotalSalesAndOrders(String bakerId) {
        DatabaseReference salesRef = FirebaseDatabase.getInstance()
                .getReference("bakers")
                .child(bakerId)
                .child("calendar");

        salesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalSales = 0;
                int totalOrders = 0;


                for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                        for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                            if (daySnapshot.hasChild("orders")) {
                                for (DataSnapshot orderSnapshot : daySnapshot.child("orders").getChildren()) {

                                    Float orderTotal = orderSnapshot.child("orderTotal").getValue(Float.class);
                                    if (orderTotal != null) {
                                        totalSales += orderTotal;
                                    }

                                    totalOrders++;
                                }
                            }
                        }
                    }
                }


                TextView txtTotalSales = findViewById(R.id.txtTotalSales);
                TextView txtTotalOrders = findViewById(R.id.txtTotalOrders);
                txtTotalSales.setText("Total Sales: $" + totalSales);
                txtTotalOrders.setText("Total Orders: " + totalOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to calculate sales and orders", Toast.LENGTH_SHORT).show();
            }
        });
    }



}