package com.example.bakelink.customers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.RecommendationCake;
import com.example.bakelink.customers.modal.CustomCakeRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class C_CustomCakeRequestActivity extends AppCompatActivity {

    String selectedFlavor;
    String selectedFilling;
    String imageUriString;
    EditText deliveryDateEditText;
    EditText deliveryTimeEditText;
    EditText additionalNotesEditText;
    ImageButton cartIcon;

    EditText deliveryAddress;

    private Uri myImageUri;

    ArrayList<int[]> receivedRgbColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ccustom_cake_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartIcon = findViewById(R.id.cart_icon);


        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(C_CustomCakeRequestActivity.this, C_CartActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView = findViewById(R.id.img_custom_cake_img);


        imageUriString = getIntent().getStringExtra("imageUri");
        receivedRgbColors = (ArrayList<int[]>) getIntent().getSerializableExtra("rgbColors");

        loadRgbColors(receivedRgbColors);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);
            myImageUri = imageUri;

        }


        Button requestQuote = findViewById(R.id.btnRequestQuotes);


        ChipGroup flavorChipGroup = findViewById(R.id.cakeFlavorChipGroup);
        flavorChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId != -1) { // A chip is selected
                    Chip selectedFlavorChip = findViewById(checkedId);
                     selectedFlavor = selectedFlavorChip.getText().toString();
                     Log.d("db", "Flavor: " + selectedFlavor);
                }
            }
        });

        ChipGroup fillingChipGroup = findViewById(R.id.fillingChipGroup);
        fillingChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId != -1) { // A chip is selected
                    Chip selectedFillingChip = findViewById(checkedId);
                    selectedFilling = selectedFillingChip.getText().toString();
                }
            }
        });

        deliveryDateEditText = findViewById(R.id.txtDeliveryDate);
        deliveryTimeEditText = findViewById(R.id.txtDeliveryTime);
        additionalNotesEditText = findViewById(R.id.txtAdditionalNotes);
        deliveryAddress = findViewById(R.id.txtDeliveryAddress);

        deliveryDateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                deliveryDateEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }, year, month, day);
            datePickerDialog.show();
        });

        deliveryTimeEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
                deliveryTimeEditText.setText(selectedHour + ":" + (selectedMinute < 10 ? "0" + selectedMinute : selectedMinute));
            }, hour, minute, true);
            timePickerDialog.show();
        });


        requestQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomCakeRequestToDatabase(myImageUri);
            }

        });

        //fab
        ImageButton fab = findViewById(R.id.fab_request_quote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), C_RequestQuoteActivity.class));
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

    private void loadRgbColors(ArrayList<int[]> receivedRgbColors) {
        if (receivedRgbColors.size() >= 5) { // Ensure at least 5 colors are available
            // Get the View references
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

    private void addCustomCakeRequestToDatabase(Uri imageUri) {
        RadioGroup rdbCakeTypeGroup = findViewById(R.id.rdbCakeTypeGroup);
        RadioGroup cakeSizeRadioGroup = findViewById(R.id.rdbCakeSizeGroup);
        RadioGroup layerRadioGroup = findViewById(R.id.rdbLayerGroup);
        RadioGroup weightRadioGroup = findViewById(R.id.rdbWeightGroup);

        String cakeType = ((RadioButton) findViewById(rdbCakeTypeGroup.getCheckedRadioButtonId())).getText().toString();
        String cakeSize = ((RadioButton) findViewById(cakeSizeRadioGroup.getCheckedRadioButtonId())).getText().toString();
        String cakeLayers = ((RadioButton) findViewById(layerRadioGroup.getCheckedRadioButtonId())).getText().toString();
        String cakeWeight = ((RadioButton) findViewById(weightRadioGroup.getCheckedRadioButtonId())).getText().toString();

        String deliveryDate = deliveryDateEditText.getText().toString();
        String deliveryTime = deliveryTimeEditText.getText().toString();
        String notes = additionalNotesEditText.getText().toString();
        String deliveryAddress = this.deliveryAddress.getText().toString();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

           // CustomCakeRequest cakeRequest = new CustomCakeRequest(cakeSize, cakeType, deliveryDate, deliveryTime, selectedFilling, selectedFlavor, imageUriString, notes, userId);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("custom_cake_images/" + UUID.randomUUID().toString());

            // Upload the image
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the image URL after a successful upload
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                            String imageUrl = uri.toString();
                            CustomCakeRequest cakeRequest = new CustomCakeRequest(cakeSize, cakeType, deliveryDate, deliveryTime, selectedFilling, selectedFlavor, imageUrl, notes, userId);
                            cakeRequest.setDeliveryAddress(deliveryAddress);
                            cakeRequest.setNoOfLayers(cakeLayers);
                            cakeRequest.setCakeWeight(cakeWeight);

                                    List<List<Integer>> rgbColorsList = new ArrayList<>();
                                    for (int[] color : receivedRgbColors) {
                                        List<Integer> colorList = new ArrayList<>();
                                        for (int value : color) {
                                            colorList.add(value); // Convert array to List
                                        }

                                        rgbColorsList.add(colorList);
                                    }
                            cakeRequest.setRgbColors(rgbColorsList);
                            cakeRequest.setCakeRequestStatus("Pending");

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests");
                            String requestId = databaseReference.push().getKey();

                            if (requestId != null) {
                                databaseReference.child(requestId).setValue(cakeRequest)
                                        .addOnSuccessListener(aVoid -> {
                                            //updateRecommendation(rgbColorsList, imageUrl, requestId);
                                            Toast.makeText(C_CustomCakeRequestActivity.this, "Request saved successfully!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), C_CakeRequestsActivity.class));
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(C_CustomCakeRequestActivity.this, "Failed to save request", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
                            }

                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // progressBar.setVisibility(View.GONE); // Hide loading indicator
                    });
        }


    }

    private void updateRecommendation(List<List<Integer>> rgbColorsList, String imageUrl, String requestId) {

        RecommendationCake recommendationCake = new RecommendationCake();
        recommendationCake.setImageUrl(imageUrl);
        recommendationCake.setRgbColors(rgbColorsList);
        recommendationCake.setCustomCakeRequestId(requestId);
        recommendationCake.setCakeType("Custom");
        recommendationCake.setBakerTitle("No Baker");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recommendations");
        String recommendationId = databaseReference.push().getKey();
        databaseReference.child(recommendationId).setValue(recommendationCake);
    }
}