package com.example.bakelink.bakers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.BasePrice;
import com.example.bakelink.bakers.models.CakeWeightAndPrice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class B_MyQuoteSetupActivity extends AppCompatActivity {

    private Button btnSaveAndFinish;
    private LinearLayout priceTypeTemplateLayout;
    private FrameLayout addPriceTypeButton;
    private LinearLayout priceWeightTemplateLayout;
    private FrameLayout addPriceWeightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmy_quote_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSaveAndFinish = findViewById(R.id.btnSaveAndFinish);
        priceTypeTemplateLayout = findViewById(R.id.priceTypeTemplate);
        addPriceTypeButton = findViewById(R.id.addPriceTypeButton);
        priceWeightTemplateLayout = findViewById(R.id.priceWeightTemplate);
        addPriceWeightButton = findViewById(R.id.addPriceWeightButton);

        addPriceTypeButton.setOnClickListener(v -> {
            Log.d("ButtonClick", "Add button clicked");
            addPriceTypeSection();
        });

        addPriceWeightButton.setOnClickListener(v -> {
            addPriceByWeightSection();
        });

        btnSaveAndFinish.setOnClickListener(v -> {
            captureData();
        });



    }

    private void addPriceTypeSection() {
        LinearLayout newPriceTypeSection = new LinearLayout(this);
        newPriceTypeSection.setOrientation(LinearLayout.HORIZONTAL);
        newPriceTypeSection.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newPriceTypeSection.setPadding(16, 8, 16, 8);

        Spinner priceTypeSpinner = new Spinner(this);
        priceTypeSpinner.setId(View.generateViewId()); // Assign unique ID
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cake_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceTypeSpinner.setAdapter(adapter);

        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
        priceTypeSpinner.setLayoutParams(spinnerParams);
        priceTypeSpinner.setPadding(8, 8, 8, 8);

        TextView priceSymbolTextView = new TextView(this);
        priceSymbolTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceSymbolTextView.setText("$");
        priceSymbolTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        priceSymbolTextView.setTextColor(getResources().getColor(android.R.color.black));
        priceSymbolTextView.setTextSize(16f);
        priceSymbolTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceSymbolTextView.setPadding(10, 0, 0, 0);

        EditText priceEditText = new EditText(this);
        priceEditText.setId(View.generateViewId());
        priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
        priceEditText.setPadding(8, 8, 8, 8);
        priceEditText.setTextSize(16f);

        newPriceTypeSection.addView(priceTypeSpinner);
        newPriceTypeSection.addView(priceSymbolTextView);
        newPriceTypeSection.addView(priceEditText);

        priceTypeTemplateLayout.addView(newPriceTypeSection);
    }

    private void addPriceByWeightSection() {

        LinearLayout newWeightSection = new LinearLayout(this);
        newWeightSection.setOrientation(LinearLayout.HORIZONTAL);
        newWeightSection.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newWeightSection.setPadding(16, 8, 16, 8);


        Spinner weightSpinner = new Spinner(this);
        weightSpinner.setId(View.generateViewId());
        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_types, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);

        LinearLayout.LayoutParams weightSpinnerParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
        weightSpinner.setLayoutParams(weightSpinnerParams);
        weightSpinner.setPadding(8, 8, 8, 8);


        TextView priceSymbolTextView = new TextView(this);
        priceSymbolTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceSymbolTextView.setText("$");
        priceSymbolTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        priceSymbolTextView.setTextColor(getResources().getColor(android.R.color.black));
        priceSymbolTextView.setTextSize(16f);
        priceSymbolTextView.setPadding(10, 0, 0, 0);


        EditText extraPriceEditText = new EditText(this);
        extraPriceEditText.setId(View.generateViewId());
        extraPriceEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout.LayoutParams priceEditTextParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
        extraPriceEditText.setLayoutParams(priceEditTextParams);
        extraPriceEditText.setPadding(8, 8, 8, 8);
        extraPriceEditText.setTextSize(16f);


        TextView extraTextView = new TextView(this);
        extraTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        extraTextView.setText("extra");
        extraTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        extraTextView.setTextColor(getResources().getColor(android.R.color.black));
        extraTextView.setTextSize(16f);
        extraTextView.setPadding(10, 0, 0, 0);


        newWeightSection.addView(weightSpinner);
        newWeightSection.addView(priceSymbolTextView);
        newWeightSection.addView(extraPriceEditText);
        newWeightSection.addView(extraTextView);


        priceWeightTemplateLayout.addView(newWeightSection);
    }


    /*private void saveAndFinish() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);
        Log.d("USER_ID", "Retrieved userId in quotesetup: " + userId);
        if (userId == null) {
            Log.e("Error", "User ID is null. Cannot save data.");
            Toast.makeText(this, "Error: Unable to save data. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // Populate data dynamically from UI components
        List<BasePrice> basePrices = new ArrayList<>();
        for (int i = 0; i < priceTypeTemplateLayout.getChildCount(); i++) {
            View sectionView = priceTypeTemplateLayout.getChildAt(i);

            // Get the Spinner and EditText directly from the dynamically created section
            Spinner priceTypeSpinner = sectionView.findViewById(View.generateViewId());  // Dynamically added spinner
            EditText priceEditText = sectionView.findViewById(View.generateViewId());  // Dynamically added EditText

            if (priceTypeSpinner != null && priceEditText != null) {
                String selectedType = priceTypeSpinner.getSelectedItem().toString();
                String priceText = priceEditText.getText().toString().trim();

                if (!selectedType.isEmpty() && !priceText.isEmpty()) {
                    try {
                        int price = Integer.parseInt(priceText);
                        basePrices.add(new BasePrice(selectedType, price));
                    } catch (NumberFormatException e) {
                        Log.e("Error", "Invalid price input: " + priceText);
                    }
                }
            }
        }

        List<CakeWeightAndPrice> weightPrices = new ArrayList<>();

        for (int i = 0; i < priceWeightTemplateLayout.getChildCount(); i++) {
            View sectionView = priceWeightTemplateLayout.getChildAt(i);

            // Get the Spinner and EditText directly from the dynamically created section
            Spinner weightSpinner = sectionView.findViewById(View.generateViewId());  // Dynamically added spinner
            EditText extraPriceEditText = sectionView.findViewById(View.generateViewId());  // Dynamically added EditText

            if (weightSpinner != null && extraPriceEditText != null) {
                String selectedWeight = weightSpinner.getSelectedItem().toString();
                String extraPriceText = extraPriceEditText.getText().toString().trim();

                if (!selectedWeight.isEmpty() && !extraPriceText.isEmpty()) {
                    try {
                        int extraPrice = Integer.parseInt(extraPriceText);
                        weightPrices.add(new CakeWeightAndPrice(selectedWeight, extraPrice));
                    } catch (NumberFormatException e) {
                        Log.e("Error", "Invalid price input: " + extraPriceText);
                    }
                }
            }
        }

        Log.d("Firebase", "basePrices: " + basePrices);
        Log.d("Firebase", "weightPrices: " + weightPrices);

        // Prepare data map for Firebase
        Map<String, Object> userData = new HashMap<>();
        userData.put("quoteDefaults/basePricePerCake", basePrices);
        userData.put("quoteDefaults/cakeWeightAndPrice", weightPrices);

        mDatabase.child("bakers").child(userId).updateChildren(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Data saved successfully.");
                        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                        // Navigate to Home Activity
                        Intent intent = new Intent(B_MyQuoteSetupActivity.this, B_HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("Firebase", "Failed to save data", task.getException());
                        Toast.makeText(this, "Failed to save data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/
    private void captureData() {
        List<String> basePrices = new ArrayList<>();
        List<String> weightPrices = new ArrayList<>();


        for (int i = 0; i < priceTypeTemplateLayout.getChildCount(); i++) {
            View view = priceTypeTemplateLayout.getChildAt(i);

            if (view instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) view;


                Spinner priceTypeSpinner = (Spinner) linearLayout.getChildAt(0);
                EditText priceEditText = (EditText) linearLayout.getChildAt(2);


                String priceType = priceTypeSpinner.getSelectedItem().toString();
                String priceValue = priceEditText.getText().toString().trim();


                basePrices.add(priceType + ": $" + priceValue);
            }
        }


        for (int i = 0; i < priceWeightTemplateLayout.getChildCount(); i++) {
            View view = priceWeightTemplateLayout.getChildAt(i);

            if (view instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) view;


                Spinner weightSpinner = (Spinner) linearLayout.getChildAt(0);
                EditText extraPriceEditText = (EditText) linearLayout.getChildAt(2);


                String weightType = weightSpinner.getSelectedItem().toString();
                String extraPrice = extraPriceEditText.getText().toString().trim();


                weightPrices.add(weightType + ": $" + extraPrice);
            }
        }


        Log.d("Captured Data", "Base Prices: " + basePrices);
        Log.d("Captured Data", "Weight Prices: " + weightPrices);


        saveDataToFirebase(basePrices, weightPrices);
    }

    private void saveDataToFirebase(List<String> basePrices, List<String> weightPrices) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bakers").child(userId).child("quoteDefaults");


        Map<String, Object> quoteDefaults = new HashMap<>();

        List<Map<String, String>> basePriceList = new ArrayList<>();
        for (String basePrice : basePrices) {

            String[] parts = basePrice.split(": ");
            Map<String, String> basePriceData = new HashMap<>();
            basePriceData.put("cakeType", parts[0]);
            basePriceData.put("price", parts[1].replace("$", "").trim());
            basePriceList.add(basePriceData);
        }
        quoteDefaults.put("basePricePerCake", basePriceList);

        List<Map<String, String>> weightPriceList = new ArrayList<>();
        for (String weightPrice : weightPrices) {

            String[] parts = weightPrice.split(": ");
            String weight = parts[0].split(" ")[0];
            String priceExtra = parts[1].replace("$", "").trim();

            Map<String, String> weightPriceData = new HashMap<>();
            weightPriceData.put("weight", weight);
            weightPriceData.put("priceExtra", priceExtra);
            weightPriceList.add(weightPriceData);
        }
        quoteDefaults.put("cakeWeightAndPrice", weightPriceList);



        databaseReference.setValue(quoteDefaults)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                        // Navigate to Home Activity
                        Intent intent = new Intent(B_MyQuoteSetupActivity.this, B_HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to save data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}