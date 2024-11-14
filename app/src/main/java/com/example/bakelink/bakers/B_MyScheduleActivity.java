package com.example.bakelink.bakers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.adapters.OrderAdapter;
import com.example.bakelink.bakers.models.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class B_MyScheduleActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private SwitchCompat blockDaySwitch;
    private TextView orderDetailsTitle;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private TextView noOrdersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmy_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendarView = findViewById(R.id.calendarView);
        blockDaySwitch = findViewById(R.id.blockDaySwitch);
        orderDetailsTitle = findViewById(R.id.tvOrderDetailsTitle);
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        noOrdersTextView = findViewById(R.id.noOrdersTextView);

        // Initialize RecyclerView
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize order list and adapter
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        ordersRecyclerView.setAdapter(orderAdapter);

        // 1. Get current date
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH); // Note: 0-based month
        int currentYear = calendar.get(Calendar.YEAR);

        // 2. Set initial order details with current date
        String currentDate = currentDay + "/" + (currentMonth + 1) + "/" + currentYear;
        String initialOrderDetails = getString(R.string.order_details_title, currentDate);
        orderDetailsTitle.setText(initialOrderDetails);

        // Load orders for the current date
        loadOrdersForDate(currentDate);

        /*// Add sample orders
        orderList.add(new Order("Regular", "15/11/2024", "Hi", "John Doe", "Chocolate Cake", "123 Baker St.",R.drawable.cakesample2));
        orderList.add(new Order("Custom", "15/11/2024", "Hello", "Jane Smith", "Wedding Cake", "456 Cake Ave.",R.drawable.themed_cake_image));
        orderList.add(new Order("Custom", "16/11/2024", "Hello", "Jane Smith", "Wedding Cake", "456 Cake Ave.",R.drawable.cakesample2));*/



        // Handle calendar date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Log.d("OrderList", "Size: " + orderList.size());
            Toast.makeText(B_MyScheduleActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();

            // Set order details for the selected date
            String orderDetailsText = getString(R.string.order_details_title, selectedDate);
            orderDetailsTitle.setText(orderDetailsText);

            // Load orders for the selected date
            loadOrdersForDate(selectedDate);
        });



        // Set an initial checked state
        blockDaySwitch.setChecked(false); // depending on the default state

        // Add a listener to detect switch state changes
        blockDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Code to execute when switch is turned ON
                    Toast.makeText(getApplicationContext(), "Day blocked", Toast.LENGTH_SHORT).show();
                } else {
                    // Code to execute when switch is turned OFF
                    Toast.makeText(getApplicationContext(), "Day unblocked", Toast.LENGTH_SHORT).show();
                }
            }
        });




        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.nav_schedule);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_MyScheduleActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                //no action
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_MyScheduleActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_MyScheduleActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });

    }

    // Method to load orders for the given date
    private void loadOrdersForDate(String selectedDate) {
        // Clear previous orders
        List<Order> filteredOrders = new ArrayList<>();

        // Sample orders (normally you will fetch this from a database or API)
        List<Order> allOrders = getAllSampleOrders();

        // Filter orders based on the selected date
        for (Order order : allOrders) {
            if (order.getOrderDate().equals(selectedDate)) {
                filteredOrders.add(order);
            }
        }

        // If there are no orders, show "No orders for this date"
        if (filteredOrders.isEmpty()) {
            noOrdersTextView.setVisibility(View.VISIBLE);  // Show message
            ordersRecyclerView.setVisibility(View.GONE);   // Hide RecyclerView
        } else {
            noOrdersTextView.setVisibility(View.GONE);     // Hide message
            ordersRecyclerView.setVisibility(View.VISIBLE);  // Show RecyclerView
            orderAdapter = new OrderAdapter(filteredOrders);
            ordersRecyclerView.setAdapter(orderAdapter);
            orderAdapter.notifyDataSetChanged();
        }
    }

    // Sample orders
    private List<Order> getAllSampleOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("Regular", "15/11/2024", "Hi", "John Doe", "Chocolate Cake", "123 Baker St.", R.drawable.cakesample2));
        orders.add(new Order("Custom", "15/11/2024", "Hello", "Jane Smith", "Wedding Cake", "456 Cake Ave.", R.drawable.themed_cake_image));
        orders.add(new Order("Custom", "16/11/2024", "Hello", "Jane Smith", "Wedding Cake", "456 Cake Ave.", R.drawable.cakesample2));
        return orders;
    }


}