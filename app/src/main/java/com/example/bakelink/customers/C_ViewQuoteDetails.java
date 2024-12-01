package com.example.bakelink.customers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.OrderItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class C_ViewQuoteDetails extends AppCompatActivity {

    private ImageButton cartIcon;
    TextView cakeTypeText, cakeTypePriceText, cakeSizeText, cakeSizePriceText, totalPriceText, additionalNotesText, cakeLayersText, cakeLayersPriceText,
            cakeWeightText, cakeWeightPriceText, additionalNotesPriceText, cakeFlavorText, cakeFlavorPriceText, cakeFillingText, cakeFillingPriceText,
            deliveryPriceText, discountPriceText, notesFromBaker;

    Button btnAccept, btnReject;

    String customCakeRequestId;
    String responseId, bakerId, cakeImageUrl;

    Double quotedPrice;
    String deliveryDate, deliveryAdd;

    String cakeRequestStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cview_quote_details);

         cakeTypeText = findViewById(R.id.qcakeType);
         cakeTypePriceText = findViewById(R.id.qcakeTypePrice);
         cakeSizeText = findViewById(R.id.qcakeSize);
         cakeSizePriceText = findViewById(R.id.qcakeSizePrice);
         totalPriceText = findViewById(R.id.qcakeTotalPrice);
         additionalNotesText = findViewById(R.id.qcakenotes);
         notesFromBaker = findViewById(R.id.notes_details);
         cakeLayersText = findViewById(R.id.qcakeLayer);
         cakeLayersPriceText = findViewById(R.id.qcakeLayerPrice);
         cakeWeightText = findViewById(R.id.qcakeWeight);
         cakeWeightPriceText = findViewById(R.id.qcakeWeightPrice);
         cakeFlavorText = findViewById(R.id.qcakeFlavor);
         cakeFlavorPriceText = findViewById(R.id.qcakeFlavorPrice);
         cakeFillingText = findViewById(R.id.qcakeFilling);
         cakeFillingPriceText = findViewById(R.id.qcakeFillingPrice);
         additionalNotesPriceText = findViewById(R.id.qcakenotesPrice);
         deliveryPriceText = findViewById(R.id.qcakedeliveryPrice);
         discountPriceText = findViewById(R.id.qcakediscountPrice);

         btnAccept = findViewById(R.id.accept_quote);
         btnReject = findViewById(R.id.reject_quote);

         responseId = getIntent().getStringExtra("responseId");
         customCakeRequestId = getIntent().getStringExtra("customCakeRequestId");

        loadQuoteDetails(responseId, customCakeRequestId);

        cartIcon = findViewById(R.id.cart_icon);

        // Set OnClickListener for the cart icon
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to C_CartActivity
                Intent intent = new Intent(C_ViewQuoteDetails.this, C_CartActivity.class);
                startActivity(intent);
            }
        });

        //fab
        ImageButton fab = findViewById(R.id.fab_request_quote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), C_RequestQuoteActivity.class));
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrderToCart();
                cakeRequestStatus = "Accepted";
                updateStatus(customCakeRequestId, responseId, bakerId, cakeRequestStatus);
                Intent intent = new Intent(C_ViewQuoteDetails.this, C_CartActivity.class);
                intent.putExtra("deliveryDate", deliveryDate);
                intent.putExtra("deliveryAddress", deliveryAdd);
                startActivity(intent);
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cakeRequestStatus = "Rejected";
                updateStatus(customCakeRequestId, responseId, bakerId, cakeRequestStatus);
                Intent intent = new Intent(C_ViewQuoteDetails.this, C_ViewQuotesPerCakeRequestActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.none);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
    }

    private void updateStatus(String customCakeRequestId, String responseId, String bakerId , String cakeRequestStatus) {

        if(cakeRequestStatus.equals("Rejected")){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests").child(customCakeRequestId).child("responses").child(responseId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("status", "Rejected");
            databaseReference.updateChildren(updates);

            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("bakers").child(bakerId).child("quoteResponses").child(responseId);
            Map<String, Object> updates3 = new HashMap<>();
            updates3.put("status", "Rejected");
            databaseReference3.updateChildren(updates3);

        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests").child(customCakeRequestId).child("responses").child(responseId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("status", "Accepted");
            databaseReference.updateChildren(updates);

            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("customCakeRequests").child(customCakeRequestId);
            Map<String, Object> updates2 = new HashMap<>();
            updates2.put("cakeRequestStatus", "Completed");
            databaseReference2.updateChildren(updates2);

            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("bakers").child(bakerId).child("quoteResponses").child(responseId);
            Map<String, Object> updates3 = new HashMap<>();
            updates3.put("status", "Accepted");
            databaseReference3.updateChildren(updates3);

            updateRecommendationStatus(customCakeRequestId , bakerId);
        }

    }

    private void updateRecommendationStatus(String customCakeRequestId, String bakerId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recommendations");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String recCakeId = dataSnapshot.child("customCakeRequestId").exists()? dataSnapshot.child("customCakeRequestId").getValue(String.class): "no value";
                    if(recCakeId.equals(customCakeRequestId)){
                            dataSnapshot.getRef().child("bakerTitle").setValue(bakerId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addOrderToCart() {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userCarts").child(currentUserId).child("temporaryCartItems");

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(databaseReference.push().getKey());
        orderItem.setCakeId(customCakeRequestId);
        orderItem.setOrderType("Custom");
        orderItem.setStatus("Pending");
        orderItem.setItemTitle("Custom Cake Order");
        orderItem.setFlavor("");
        orderItem.setWeight("");
        orderItem.setQuantity(1);
        orderItem.setPrice(quotedPrice);
        orderItem.setImageUrl(cakeImageUrl);
        orderItem.setBakerId(bakerId);


        databaseReference.push().setValue(orderItem);
    }

    private void loadQuoteDetails(String responseId, String customCakeRequestId) {

        Log.d("quotedetails", responseId + " " + customCakeRequestId);

        // Fetch and display quote details based on quoteId
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests").child(customCakeRequestId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Extract data from snapshot
                    Log.d("quotedetails", snapshot.toString());
                    String cakeType = snapshot.child("cakeType").getValue(String.class);
                    //String cakeTypePrice = "$" + snapshot.child("cakeTypePrice").getValue(Double.class);
                    //Log.d("cakeType", cakeTypePrice);

                    String cakeSize = snapshot.child("cakeSize").getValue(String.class);
                    //String cakeSizePrice = "$" + snapshot.child("cakeSizePrice").getValue(Double.class);

                    String cakeLayers = snapshot.child("noOfLayers").getValue(String.class);
                    //String cakeLayersPrice = "$" + snapshot.child("cakeLayersPrice").getValue(Double.class);

                    String cakeWeight = snapshot.child("cakeWeight").getValue(String.class);
                    //String cakeWeightPrice = "$" + snapshot.child("cakeWeightPrice").getValue(Double.class);

                    String cakeFlavor = snapshot.child("flavor").getValue(String.class);
                    //String cakeFlavorPrice = "$" + snapshot.child("cakeFlavorPrice").getValue(Double.class);

                    String cakeFilling = snapshot.child("filling").getValue(String.class);
                    //String cakeFillingPrice = "$" + snapshot.child("cakeFillingPrice").getValue(Double.class);

                    String additionalNotes = snapshot.child("notes").getValue(String.class);
                    //String additionalNotesPrice = "$" + snapshot.child("additionalNotesPrice").getValue(Double.class);

                    //String deliveryPrice = "$" + snapshot.child("deliveryChargesPrice").getValue(Double.class);

                    //String discountPrice = "$" + snapshot.child("discountsPrice").getValue(Double.class);

                    //bakerId = snapshot.child("bakerId").getValue(String.class);
                    // Update UI components
                    //cakeImageUrl = snapshot.child("imageUrl").getValue(String.class);

                    cakeTypeText.setText("Cake type: " + cakeType);
                    //cakeTypePriceText.setText(cakeTypePrice);

                    cakeSizeText.setText("Cake size: " + cakeSize);
                    //cakeSizePriceText.setText(cakeSizePrice);

                    cakeLayersText.setText("No. of layers: " + cakeLayers);
                    //cakeLayersPriceText.setText(cakeLayersPrice);

                    cakeWeightText.setText("Cake weight: " + cakeWeight);
                    //cakeWeightPriceText.setText(cakeWeightPrice);

                    cakeFlavorText.setText("Cake flavor: " + cakeFlavor);
                    //cakeFlavorPriceText.setText(cakeFlavorPrice);

                    cakeFillingText.setText("Cake filling: " + cakeFilling);
                    //cakeFillingPriceText.setText(cakeFillingPrice);

                    additionalNotesText.setText("Additional notes: " + additionalNotes);
                    //additionalNotesPriceText.setText(additionalNotesPrice);

                    //deliveryPriceText.setText(deliveryPrice);
                    //discountPriceText.setText(discountPrice);

                    //quotedPrice =  snapshot.child("quotedPrice").getValue(Double.class);

                    //totalPriceText.setText("$" + snapshot.child("quotedPrice").getValue(Double.class));

                    //String bakerMessage = snapshot.child("responseMessage").getValue(String.class);
                    //notesFromBaker.setText(bakerMessage);

                    //deliveryDate = snapshot.child("deliveryDate").getValue(String.class);
                    //deliveryAdd = snapshot.child("deliveryAddress").getValue(String.class);

                }else{
                    Log.d("quotedetails", "snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference databaseReferenceinner = FirebaseDatabase.getInstance().getReference("customCakeRequests").child(customCakeRequestId).child("responses").child(responseId);
        databaseReferenceinner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Extract data from snapshot
                    Log.d("quotedetails", snapshot.toString());
                    //String cakeType = snapshot.child("cakeType").getValue(String.class);
                    String cakeTypePrice = "$" + snapshot.child("cakeTypePrice").getValue(Double.class);
                    Log.d("cakeType", cakeTypePrice);

                    //String cakeSize = snapshot.child("cakeSize").getValue(String.class);
                    String cakeSizePrice = "$" + snapshot.child("cakeSizePrice").getValue(Double.class);

                    //String cakeLayers = snapshot.child("cakeLayers").getValue(String.class);
                    String cakeLayersPrice = "$" + snapshot.child("cakeLayersPrice").getValue(Double.class);

                    //String cakeWeight = snapshot.child("cakeWeight").getValue(String.class);
                    String cakeWeightPrice = "$" + snapshot.child("cakeWeightPrice").getValue(Double.class);

                    //String cakeFlavor = snapshot.child("flavor").getValue(String.class);
                    String cakeFlavorPrice = "$" + snapshot.child("cakeFlavorPrice").getValue(Double.class);

                    //String cakeFilling = snapshot.child("filling").getValue(String.class);
                    String cakeFillingPrice = "$" + snapshot.child("cakeFillingPrice").getValue(Double.class);

                    //String additionalNotes = snapshot.child("additionalNotes").getValue(String.class);
                    String additionalNotesPrice = "$" + snapshot.child("additionalNotesPrice").getValue(Double.class);

                    String deliveryPrice = "$" + snapshot.child("deliveryChargesPrice").getValue(Double.class);

                    String discountPrice = "$" + snapshot.child("discountsPrice").getValue(Double.class);

                    bakerId = snapshot.child("bakerId").getValue(String.class);
                    // Update UI components
                    cakeImageUrl = snapshot.child("imageUrl").getValue(String.class);

                    //cakeTypeText.setText("Cake type: " + cakeType);
                    cakeTypePriceText.setText(cakeTypePrice);

                    //cakeSizeText.setText("Cake size: " + cakeSize);
                    cakeSizePriceText.setText(cakeSizePrice);

                    //cakeLayersText.setText("No. of layers: " + cakeLayers);
                    cakeLayersPriceText.setText(cakeLayersPrice);

                    //cakeWeightText.setText("Cake weight: " + cakeWeight);
                    cakeWeightPriceText.setText(cakeWeightPrice);

                    //cakeFlavorText.setText("Cake flavor: " + cakeFlavor);
                    cakeFlavorPriceText.setText(cakeFlavorPrice);

                    //cakeFillingText.setText("Cake filling: " + cakeFilling);
                    cakeFillingPriceText.setText(cakeFillingPrice);

                    //additionalNotesText.setText("Additional notes: " + additionalNotes);
                    additionalNotesPriceText.setText(additionalNotesPrice);

                    deliveryPriceText.setText(deliveryPrice);
                    discountPriceText.setText(discountPrice);

                    quotedPrice =  snapshot.child("quotedPrice").getValue(Double.class);

                    totalPriceText.setText("$" + snapshot.child("quotedPrice").getValue(Double.class));

                    String bakerMessage = snapshot.child("responseMessage").getValue(String.class);
                    if(bakerMessage.equals("")){
                        notesFromBaker.setText("None");
                    } else{
                        notesFromBaker.setText(bakerMessage);
                    }

                    deliveryDate = snapshot.child("deliveryDate").getValue(String.class);
                    deliveryAdd = snapshot.child("deliveryAddress").getValue(String.class);

                }else{
                    Log.d("quotedetails", "snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}