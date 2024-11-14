package com.example.bakelink.bakers;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class B_ViewQuoteActivity extends AppCompatActivity {
    ImageView cakeImg;
    TextView customerName;
    TextView cakeType;
    TextView cakeSize;
    TextView cakeLayers;
    TextView cakeWeight;
    TextView cakeFlavor;
    TextView cakeFilling;
    TextView additionalNotes;
    TextView deliveryDate;
    TextView deliveryTime;
    TextView deliveryAddress;

    String quoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bview_quote);

         cakeImg = findViewById(R.id.cakeImage);
         customerName = findViewById(R.id.vqCustomerName);
         cakeType = findViewById(R.id.vqCakeType);
         cakeSize = findViewById(R.id.vqCakeSize);
         cakeLayers = findViewById(R.id.vqCakeLayers);
         cakeWeight = findViewById(R.id.vqCakeWeight);
         cakeFlavor = findViewById(R.id.vqCakeFlavor);
         cakeFilling = findViewById(R.id.vqCakeFilling);
         additionalNotes = findViewById(R.id.vqAdditionalNotes);
         deliveryDate = findViewById(R.id.vqDeliveryDate);
         deliveryTime = findViewById(R.id.vqDeliveryTime);
         deliveryAddress = findViewById(R.id.vqDeliveryAddress);

         quoteId = getIntent().getStringExtra("quoteId");
         loadQuoteDetails(quoteId);

    }


    private void loadQuoteDetails(String quoteId){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customCakeRequests").child(quoteId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("QuoteDetails", "Snapshot: " + snapshot.toString());

                String customer =  snapshot.child("userId").exists() ? snapshot.child("userId").getValue(String.class) : "Not specified";
                String type = snapshot.child("cakeType").exists()? snapshot.child("cakeType").getValue(String.class) : "Not specified";
                String size = snapshot.child("cakeSize").exists() ? snapshot.child("cakeSize").getValue(String.class): "Not specified";
                String layers = snapshot.child("cakeLayers").exists() ? snapshot.child("cakeLayers").getValue(String.class) : "Not specified";

                String weight = snapshot.child("cakeWeight").exists() ? snapshot.child("cakeWeight").getValue(String.class): "Not specified";
                String flavor = snapshot.child("flavor").exists()? snapshot.child("flavor").getValue(String.class): "Not specified";
                String filling = snapshot.child("filling").exists()? snapshot.child("filling").getValue(String.class): "Not specified";
                String notes = snapshot.child("notes").exists()? snapshot.child("notes").getValue(String.class) : "Not specified";
                String date = snapshot.child("deliveryDate").exists()? snapshot.child("deliveryDate").getValue(String.class): "Not specified";
                String time = snapshot.child("deliveryTime").exists()? snapshot.child("deliveryTime").getValue(String.class): "Not specified";
                String strDeliveryAddress = snapshot.child("deliveryAddress").exists()? snapshot.child("deliveryAddress").getValue(String.class): "Not specified";

                customerName.setText("Customer Name : " + customer);
                cakeSize.setText("Cake Size : " + size);
                cakeType.setText("Cake Type : " + type);
                cakeLayers.setText("Number of Layers : " + layers);
                cakeWeight.setText("Cake Weight : " + weight);
                cakeFlavor.setText("Cake Flavor : " + flavor);
                cakeFilling.setText("Cake Fillings : " + filling);
                additionalNotes.setText("Additional Notes : " + notes);
                deliveryDate.setText("Delivery Date : " + date);
                deliveryTime.setText("Delivery Time : " + time);
                deliveryAddress.setText("Delivery Address : " + strDeliveryAddress);

                Glide.with(B_ViewQuoteActivity.this)
                        .load(snapshot.child("imageUrl").getValue(String.class))
                        .error(R.drawable.cakesample1)
                        .into(cakeImg);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(B_ViewQuoteActivity.this, "Failed to fetch details.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}