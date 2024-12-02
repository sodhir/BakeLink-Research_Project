package com.example.bakelink.customers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.bakelink.bakers.models.Order;
import com.example.bakelink.bakers.models.OrderItem;
import com.example.bakelink.customers.adapters.OrderItemsAdapter;
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
import java.util.Date;
import java.util.List;

public class C_CartActivity extends AppCompatActivity {

    String currentUserId;

    List<OrderItem> orderItems = new ArrayList<>();

    RecyclerView recyclerView;

    OrderItemsAdapter adapter;

    TextView total;

    EditText deliveryDate;
    EditText deliveryAdd;

    Button checkoutbtn;

    String bakerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ccart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        total = findViewById(R.id.total_value);
        deliveryDate = findViewById(R.id.deliveryDate);
        deliveryAdd = findViewById(R.id.deliveryAdd);
        checkoutbtn = findViewById(R.id.checkout_button);

        String deliveryDateValue = getIntent().getStringExtra("deliveryDate");
        String deliveryAddressValue = getIntent().getStringExtra("deliveryAddress");

        if (deliveryDateValue != null) {
            deliveryDate.setText(deliveryDateValue);
        }

        if (deliveryAddressValue != null) {
            deliveryAdd.setText(deliveryAddressValue);
        }

        deliveryDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                deliveryDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }, year, month, day);
            datePickerDialog.show();
        });

        // Get the current user ID
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadOrderItems();


        recyclerView = findViewById(R.id.recycler_view_order_items);
        adapter = new OrderItemsAdapter(orderItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //fab
        ImageButton fab = findViewById(R.id.fab_request_quote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), C_RequestQuoteActivity.class));
        });




                //Bottom navigation
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.none);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), C_HomeActivity.class));
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


        checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrderData();
            }
        });
    }

    private void saveOrderData() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String dateInput = deliveryDate.getText().toString().trim();
        String formattedDate;

        try {
            if (!dateInput.isEmpty()) {
                // Parse the input date string and format it
                Date parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateInput);
                formattedDate = df.format(parsedDate);
            } else {
                // Use the current date as fallback
                formattedDate = df.format(new Date());
            }
        } catch (Exception e) {
            Log.e("DateError", "Invalid date format: " + dateInput, e);
            formattedDate = df.format(new Date());
        }

        String deliveryAdd = this.deliveryAdd.getText().toString();
        String currentUserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderType = orderItems.get(0).getOrderType();
        Double orderTotal = Double.parseDouble(total.getText().toString());

           String bakerID = orderItems.get(0).getBakerId().trim();


        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("bakers")
                .child(bakerID)
                .child("calendar")
                .child(formattedDate)
                .child("orders");

        // Create order object
        Order order = new Order();
        order.setOrderId(orderRef.push().getKey());
        order.setDeliveryAddress(deliveryAdd);
        order.setOrderDate(formattedDate);
        order.setOrderType(orderType);
        order.setOrderTotal(orderTotal);
        order.setCustomerName(currentUserid);
        order.setOrderItems(orderItems);

        // Save order to Firebase
        orderRef.child(order.getOrderId()).setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firebase", "Order added successfully");
                Toast.makeText(this, "Order has been placed!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), C_HomeActivity.class));
            } else {
                Log.e("Firebase", "Failed to add order", task.getException());
            }
        });

        clearCartItems();

    }

    private void clearCartItems() {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("userCarts").child(currentUserId).child("temporaryCartItems");
        cartRef.removeValue();
    }

    public void loadOrderItems(){
      orderItems.clear();
       DatabaseReference orderItemRef = FirebaseDatabase.getInstance().getReference("userCarts").child(currentUserId).child("temporaryCartItems");
       orderItemRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               Log.d("order", "onDataChange:" + snapshot.toString());
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    OrderItem orderItem = itemSnapshot.getValue(OrderItem.class);
                    Log.d("order", "OrderItem: " + orderItem.getOrderItemId());
                    if(orderItem.getStatus().equals("Pending")){
                        orderItem.setOrderItemId(itemSnapshot.getKey());
                        orderItems.add(orderItem);
                    }
                }
                adapter.notifyDataSetChanged();
                calculateTotal();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Log.d("order", "Failed to load order items");
           }
       });

    }

    public void calculateTotal(){
        Double myCartTotal = 0.0;
        for(OrderItem item : orderItems){
            myCartTotal = myCartTotal + item.getPrice();
        }
        total.setText("$" + myCartTotal.toString());
    }
}