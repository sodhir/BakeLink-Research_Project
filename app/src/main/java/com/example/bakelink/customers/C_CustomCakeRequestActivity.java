package com.example.bakelink.customers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.example.bakelink.customers.modal.CustomCakeRequest;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class C_CustomCakeRequestActivity extends AppCompatActivity {

    String selectedFlavor;
    String selectedFilling;
    String imageUriString;

    EditText deliveryDateEditText;
    EditText deliveryTimeEditText;
    EditText additionalNotesEditText;

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

        ImageView imageView = findViewById(R.id.img_custom_cake_img);

        // Get the image URI from the intent
        imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);

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


        requestQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomCakeRequestToDatabase();
            }

        });

    }

    private void addCustomCakeRequestToDatabase() {
        RadioGroup rdbCakeTypeGroup = findViewById(R.id.rdbCakeTypeGroup);
        RadioGroup cakeSizeRadioGroup = findViewById(R.id.rdbCakeSizeGroup);

        String cakeType = ((RadioButton) findViewById(rdbCakeTypeGroup.getCheckedRadioButtonId())).getText().toString();
        String cakeSize = ((RadioButton) findViewById(cakeSizeRadioGroup.getCheckedRadioButtonId())).getText().toString();
        String deliveryDate = deliveryDateEditText.getText().toString();
        String deliveryTime = deliveryTimeEditText.getText().toString();
        String notes = additionalNotesEditText.getText().toString();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            CustomCakeRequest cakeRequest = new CustomCakeRequest(cakeSize, cakeType, deliveryDate, deliveryTime, selectedFilling, selectedFlavor, imageUriString, notes, userId);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests");
            String requestId = databaseReference.push().getKey();

            if (requestId != null) {
                databaseReference.child(requestId).setValue(cakeRequest)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(C_CustomCakeRequestActivity.this, "Request saved successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(C_CustomCakeRequestActivity.this, "Failed to save request", Toast.LENGTH_SHORT).show();
                        });
            }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }


    }


}