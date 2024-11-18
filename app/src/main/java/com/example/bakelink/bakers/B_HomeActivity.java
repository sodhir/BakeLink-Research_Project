package com.example.bakelink.bakers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.example.bakelink.bakers.models.Order;
import com.example.bakelink.bakers.models.OrderItem;
import com.example.bakelink.bakers.models.Quote;
import com.example.bakelink.bakers.models.QuoteRequest;
import com.example.bakelink.bakers.models.QuoteResponse;
import com.example.bakelink.customers.modal.CustomCakeRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class B_HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuoteRequestAdapter adapter;
    private List<CustomCakeRequest> quoteRequestList;
    private RecyclerView recyclerTrackSubmittedQuotes;
    private TrackSubmittedQuotesAdapter submittedQuotesAdapter;
    List<QuoteResponse> quotesSentList;
    TextView welcomeText;

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
         welcomeText = findViewById(R.id.welcomeText);
        String username = getUserEmail(FirebaseAuth.getInstance().getCurrentUser().getUid());  // Retrieve actual username from intent or shared preferences


        recyclerView = findViewById(R.id.recyclerViewNewQuoteRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize data
        loadAllCustomQuotes();
//        quoteRequestList = new ArrayList<>();
//        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.cakesample2));
//        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.themed_cake_image));
//        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.cakesample3));
        // Add more items as needed

        // Set adapter


        //track submitted quote section
        recyclerTrackSubmittedQuotes = findViewById(R.id.recyclerViewTrackSubmittedQuote);
        loadSubmittedQuotes();
//        quotesSentList = new ArrayList<>();
//        quotesSentList.add(new Quote("[Customer name]", 250.00, "Awaiting Approval",R.drawable.cakesample2));
//        quotesSentList.add(new Quote("[Customer name]", 180.00, "Approved",R.drawable.themed_cake_image));
//        quotesSentList.add(new Quote("[Customer name]", 180.00, "Approved",R.drawable.cakesample3));
//        submittedQuotesAdapter = new TrackSubmittedQuotesAdapter(this,quotesSentList);
//        recyclerTrackSubmittedQuotes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerTrackSubmittedQuotes.setAdapter(submittedQuotesAdapter);

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
                startActivity(new Intent(B_HomeActivity.this, B_MyQuoteSetupActivity.class));
                return true;
            }
            return false;
        });

        calculateWeeklySales();
        calculateMonthlySales();

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
                AtomicInteger loadedQuotes = new AtomicInteger(0); // Track how many quotes are fully loaded

                for (DataSnapshot responseSnapshot : snapshot.getChildren()) {
                    String responseId = responseSnapshot.getKey();
                    String customCakeRequestId = responseSnapshot.child("customCakeRequestId").getValue(String.class);
                    String userId = responseSnapshot.child("userId").getValue(String.class);
                    Double quotedPrice = responseSnapshot.child("quotedPrice").getValue(Double.class);
                    String responseMessage = responseSnapshot.child("responseMessage").getValue(String.class);
                    String status = responseSnapshot.child("status").getValue(String.class);

                    QuoteResponse cakeResponse = new QuoteResponse();
                    cakeResponse.setQuoteResponseId(responseId);
                    cakeResponse.setCustomCakeRequestId(customCakeRequestId);
                    cakeResponse.setUserID(userId);
                    cakeResponse.setQuotedPrice(quotedPrice);
                    cakeResponse.setResponseMessage(responseMessage);
                    cakeResponse.setStatus(status);

                    // Fetch additional details from customCakeRequests
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

// Reference to the baker's cakes node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests");


// Attach a listener to fetch data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               quoteRequestList = new ArrayList<>();
                for (DataSnapshot customCakeSnapshot : snapshot.getChildren()) {
                    // Assuming you have a Cake model class
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
                    quoteRequestList.add(cakeRequest);
                }

                Log.d("Cakes", "Cakes fetched: " + quoteRequestList.size());
                // Use the fetched list of cakes (e.g., update your UI)
                // Implement this method to handle the list of cakes
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
        welcomeText.setText("Welcome back, " + email + "!");
        return email;
    }

//    private void getSalesData() {
//        String year = "2024";
//        String month = "11";
//        String day = "17";
//
//        DatabaseReference ordersRef = FirebaseDatabase.getInstance()
//                .getReference("bakers")
//                .child(currentUserId) // Replace with the actual baker ID
//                .child("calendar")
//                .child(year)
//                .child(month)
//                .child(day)
//                .child("orders");
//
//        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Order> ordersList = new ArrayList<>();
//
//                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
//                    String orderId = orderSnapshot.child("orderId").getValue(String.class);
//                    String cakeId = orderSnapshot.child("cakeId").getValue(String.class);
//                    Double orderTotal = orderSnapshot.child("orderTotal").getValue(Double.class);
//
//                    // Create and populate an Order object (assuming you have an Order class)
//                    Order order = new Order();
//                    order.setOrderId(orderId);
//                   // order.setCakeId(cakeId);
//                    order.setOrderTotal(orderTotal != null ? orderTotal : 0.0);
//
//                    ordersList.add(order);
//                }
//
//                // Handle the fetched orders (e.g., display them in a RecyclerView)
//                Log.d("OrdersFetch", "Fetched " + ordersList.size() + " orders for date: " + day + "/" + month + "/" + year);
//                for (Order order : ordersList) {
//                    Log.d("OrdersFetch", "Order ID: " + order.getOrderId() + ", Total: " + order.getOrderTotal());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("OrdersFetch", "Failed to fetch orders: " + error.getMessage());
//            }
//        });
//    }

    double weeklySales;

    private void calculateWeeklySales() {
        String year = "2024";
        String month = "11";
        String day = "17"; // Current day, can be dynamically set

        // Calculate the dates for the past week (7 days)
        List<String> pastWeekDates = getPastWeekDates(year, month, day);

         weeklySales = 0.0;

        for (String date : pastWeekDates) {
            DatabaseReference ordersRef = FirebaseDatabase.getInstance()
                    .getReference("bakers")
                    .child(currentUserId)
                    .child("calendar")
                    .child(year)
                    .child(month)
                    .child(date)
                    .child("orders");

            ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        Double orderTotal = orderSnapshot.child("orderTotal").getValue(Double.class);
                        if (orderTotal != null) {
                            weeklySales += orderTotal;
                        }
                    }
                    // Log the weekly sales after all orders are fetched
                    Log.d("SalesFetch", "Weekly Sales: " + weeklySales);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("SalesFetch", "Failed to fetch orders: " + error.getMessage());
                }
            });
        }
    }

    private List<String> getPastWeekDates(String year, String month, String day) {
        List<String> pastWeekDates = new ArrayList<>();
        // You can use a date manipulation library like Java's LocalDate to calculate the last 7 days
        LocalDate currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        }

        for (int i = 0; i < 7; i++) {
            LocalDate pastDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pastDate = currentDate.minusDays(i);
            }
            String pastDay = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pastDay = String.format("%02d", pastDate.getDayOfMonth());
            }
            pastWeekDates.add(pastDay);
        }
        return pastWeekDates;
    }
    double monthlySales;
    private void calculateMonthlySales() {
        String year = "2024";
        String month = "11"; // Current month, can be dynamically set

         monthlySales = 0.0;

        // Assuming the month has 30 days (this may vary, use the actual number of days for the month)
        for (int day = 1; day <= 30; day++) {
            String dayString = String.format("%02d", day);

            DatabaseReference ordersRef = FirebaseDatabase.getInstance()
                    .getReference("bakers")
                    .child(currentUserId)
                    .child("calendar")
                    .child(year)
                    .child(month)
                    .child(dayString)
                    .child("orders");

            ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        Double orderTotal = orderSnapshot.child("orderTotal").getValue(Double.class);
                        if (orderTotal != null) {
                            monthlySales += orderTotal;
                        }
                    }
                    // Log the monthly sales after all orders are fetched
                    Log.d("SalesFetch", "Monthly Sales: " + monthlySales);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("SalesFetch", "Failed to fetch orders: " + error.getMessage());
                }
            });
        }
    }



}