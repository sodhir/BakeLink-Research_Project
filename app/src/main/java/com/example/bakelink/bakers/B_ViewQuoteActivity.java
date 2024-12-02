package com.example.bakelink.bakers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.common.models.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    Button generateQuote, sendMessage;

    String quoteId;

    String currentUser, receiverId;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bview_quote);

        // Set welcome text
        TextView welcomeText = findViewById(R.id.welcomeText);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String bakeryName = sharedPreferences.getString("bakery_name", null); // Get bakery name
        welcomeText.setText("Welcome back, " + bakeryName + "!");

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

         generateQuote = findViewById(R.id.btnGenerateQuote);
         sendMessage = findViewById(R.id.btnUpdateOrder);

         generateQuote.setOnClickListener(view -> {
             Intent intent = new Intent(B_ViewQuoteActivity.this, B_GenerateQuoteActivity.class);
             intent.putExtra("customCakeRequestId", quoteId);

             intent.putExtra("cakeType", cakeType.getText().toString());
             intent.putExtra("cakeSize", cakeSize.getText().toString());
             intent.putExtra("cakeLayers", cakeLayers.getText().toString());
             intent.putExtra("cakeWeight", cakeWeight.getText().toString());
             intent.putExtra("cakeFlavor", cakeFlavor.getText().toString());
             intent.putExtra("cakeFilling", cakeFilling.getText().toString());
             intent.putExtra("additionalNotes", additionalNotes.getText().toString());
             intent.putExtra("bakerId", currentUser);
             intent.putExtra("customerId", customerName.getText().toString());
             intent.putExtra("imageUrl", imageUrl);
             startActivity(intent);

         });

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUser = sharedPreferences.getString("USER_ID", null);


         sendMessage.setOnClickListener(view -> {
             sendmessage();
         });

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.none);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_ViewQuoteActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_ViewQuoteActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_ViewQuoteActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_ViewQuoteActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });

    }




    private void loadQuoteDetails(String quoteId){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customCakeRequests").child(quoteId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("QuoteDetails", "Snapshot: " + snapshot.toString());

                String customer =  snapshot.child("userId").exists() ? snapshot.child("userId").getValue(String.class) : "Not specified";
                receiverId = customer;
                String type = snapshot.child("cakeType").exists()? snapshot.child("cakeType").getValue(String.class) : "Not specified";
                String size = snapshot.child("cakeSize").exists() ? snapshot.child("cakeSize").getValue(String.class): "Not specified";
                String layers = snapshot.child("noOfLayers").exists() ? snapshot.child("noOfLayers").getValue(String.class) : "Not specified";

                String weight = snapshot.child("cakeWeight").exists() ? snapshot.child("cakeWeight").getValue(String.class): "Not specified";
                String flavor = snapshot.child("flavor").exists()? snapshot.child("flavor").getValue(String.class): "Not specified";
                String filling = snapshot.child("filling").exists()? snapshot.child("filling").getValue(String.class): "Not specified";
                String notes = snapshot.child("notes").exists()? snapshot.child("notes").getValue(String.class) : "Not specified";
                String date = snapshot.child("deliveryDate").exists()? snapshot.child("deliveryDate").getValue(String.class): "Not specified";
                String time = snapshot.child("deliveryTime").exists()? snapshot.child("deliveryTime").getValue(String.class): "Not specified";
                String strDeliveryAddress = snapshot.child("deliveryAddress").exists()? snapshot.child("deliveryAddress").getValue(String.class): "Not specified";

                imageUrl = snapshot.child("imageUrl").getValue(String.class);

                customerName.setText(customer);
                cakeSize.setText(size);
                cakeType.setText(type);
                cakeLayers.setText(layers);
                cakeWeight.setText(weight);
                cakeFlavor.setText(flavor);
                cakeFilling.setText(filling);
                additionalNotes.setText(notes);
                deliveryDate.setText(date);
                deliveryTime.setText(time);
                deliveryAddress.setText(strDeliveryAddress);

                if (snapshot.child("rgbColors").exists() && snapshot.child("rgbColors").getValue() != null) {
                    GenericTypeIndicator<List<List<Integer>>> typeIndicator = new GenericTypeIndicator<List<List<Integer>>>() {};
                    List<List<Integer>> rgbColorsList = snapshot.child("rgbColors").getValue(typeIndicator);

                    // Convert List<List<Integer>> to ArrayList<int[]>
                    ArrayList<int[]> rgbColorsArrayList = new ArrayList<>();
                    if (rgbColorsList != null) {
                        for (List<Integer> colorList : rgbColorsList) {
                            int[] colorArray = new int[colorList.size()];
                            for (int i = 0; i < colorList.size(); i++) {
                                colorArray[i] = colorList.get(i); // Convert List<Integer> to int[]
                            }
                            rgbColorsArrayList.add(colorArray);
                        }
                    }


                    Log.d("RGB_COLORS", "Colors retrieved: " + rgbColorsArrayList.size());
                    for (int[] color : rgbColorsArrayList) {
                        Log.d("RGB_COLORS", "Color: " + Arrays.toString(color));
                    }

                    // Call the method to load RGB colors into the views
                    loadRgbColors(rgbColorsArrayList);
                }

                Glide.with(B_ViewQuoteActivity.this)
                        .load(imageUrl)  // Use the imageUrl directly here
                        .error(R.drawable.cakesample1)
                        .into(cakeImg);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(B_ViewQuoteActivity.this, "Failed to fetch details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRgbColors(ArrayList<int[]> receivedRgbColors) {
        if (receivedRgbColors.size() >= 5) {

            View swatch1 = findViewById(R.id.colorSwatch1);
            View swatch2 = findViewById(R.id.colorSwatch2);
            View swatch3 = findViewById(R.id.colorSwatch3);
            View swatch4 = findViewById(R.id.colorSwatch4);
            View swatch5 = findViewById(R.id.colorSwatch5);

            // Convert RGB values to Color and set the backgrounds
            swatch1.setBackgroundColor(Color.rgb(receivedRgbColors.get(0)[0], receivedRgbColors.get(0)[1], receivedRgbColors.get(0)[2]));
            swatch2.setBackgroundColor(Color.rgb(receivedRgbColors.get(1)[0], receivedRgbColors.get(1)[1], receivedRgbColors.get(1)[2]));
            swatch3.setBackgroundColor(Color.rgb(receivedRgbColors.get(2)[0], receivedRgbColors.get(2)[1], receivedRgbColors.get(2)[2]));
            swatch4.setBackgroundColor(Color.rgb(receivedRgbColors.get(3)[0], receivedRgbColors.get(3)[1], receivedRgbColors.get(3)[2]));
            swatch5.setBackgroundColor(Color.rgb(receivedRgbColors.get(4)[0], receivedRgbColors.get(4)[1], receivedRgbColors.get(4)[2]));
        }

    }

    private void sendmessage() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


        String messageID = mDatabase.child("messages").push().getKey();
        String senderID = currentUser;
        String receiverID = receiverId;
        String messageContent = "Test message";

        if (messageID != null) {

            Message message = new Message(senderID, receiverID, messageContent, ServerValue.TIMESTAMP, "unread");


            mDatabase.child("messages").child(messageID).setValue(message)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            mDatabase.child("bakers").child(senderID).child("messages").child(messageID).setValue(true);
                            mDatabase.child("users").child(receiverID).child("messages").child(messageID).setValue(true);


                            sendPushNotification(receiverID, messageContent);

                            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void sendPushNotification(String receiverID, String messageContent) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(receiverID).child("fcmToken")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String fcmToken = dataSnapshot.getValue(String.class);
                        if (fcmToken != null) {
                            sendFCMRequest(fcmToken, messageContent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void sendFCMRequest(String fcmToken, String message) {

        JSONObject notification = new JSONObject();
        try {
            notification.put("to", fcmToken);
            notification.put("priority", "high");

            JSONObject notificationBody = new JSONObject();
            notificationBody.put("title", "New Message");
            notificationBody.put("body", message);

            notification.put("notification", notificationBody);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}