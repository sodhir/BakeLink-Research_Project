package com.example.bakelink.bakers;

import android.content.Intent;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        // Handle Add Price Type Button Click
        addPriceTypeButton.setOnClickListener(v -> {
            Log.d("ButtonClick", "Add button clicked");
            addPriceTypeSection();
        });

        // Handle Add Price by Weight Button Click
        addPriceWeightButton.setOnClickListener(v -> {
            addPriceByWeightSection();
        });

        // Save and Finish button click listener
        btnSaveAndFinish.setOnClickListener(v -> saveAndFinish());


        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        // Deselect the item (no item should be highlighted)
        bottomNavigationView.setSelectedItemId(-1);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.setSelectedItemId(-1);
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_MyQuoteSetupActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_MyQuoteSetupActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_MyQuoteSetupActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_MyQuoteSetupActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });

    }

    private void addPriceTypeSection() {
        // Create a new LinearLayout to hold the Spinner and EditText for a new price type section
        LinearLayout newPriceTypeSection = new LinearLayout(this);
        newPriceTypeSection.setOrientation(LinearLayout.HORIZONTAL);
        newPriceTypeSection.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newPriceTypeSection.setPadding(16, 8, 16, 8);

        // Create the Spinner for Cake Type (Dropdown)
        Spinner priceTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cake_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceTypeSpinner.setAdapter(adapter);

        // Adjust weight for spinner to take less space
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);  // Set weight to 60% for the spinner
        priceTypeSpinner.setLayoutParams(spinnerParams);
        priceTypeSpinner.setPadding(8, 8, 8, 8);  // Add padding to spinner
        //priceTypeSpinner.setBackgroundResource(R.drawable.spinner_background);


        // Create the TextView for "$" (to show currency symbol)
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
        priceSymbolTextView.setPadding(10, 0, 0, 0);  // Padding for spacing

        // Create the EditText for entering the price
        EditText priceEditText = new EditText(this);
        priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));  // Set weight to 40% for the EditText
        priceEditText.setPadding(8, 8, 8, 8);
        priceEditText.setTextSize(16f);

        // Add the Spinner, TextView (for "$"), and EditText to the new LinearLayout
        newPriceTypeSection.addView(priceTypeSpinner);
        newPriceTypeSection.addView(priceSymbolTextView);
        newPriceTypeSection.addView(priceEditText);

        // Add the new price type section to the priceTypeTemplateLayout
        priceTypeTemplateLayout.addView(newPriceTypeSection);
    }

    private void addPriceByWeightSection() {
        // Create a new LinearLayout for the weight-based price section
        LinearLayout newWeightSection = new LinearLayout(this);
        newWeightSection.setOrientation(LinearLayout.HORIZONTAL);
        newWeightSection.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newWeightSection.setPadding(16, 8, 16, 8);

        // Create the Spinner for Weight selection
        Spinner weightSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_types, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);

        LinearLayout.LayoutParams weightSpinnerParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);  // Set width to 60%
        weightSpinner.setLayoutParams(weightSpinnerParams);
        weightSpinner.setPadding(8, 8, 8, 8);  // Apply padding to the Spinner

        // Create TextView for "$" symbol
        TextView priceSymbolTextView = new TextView(this);
        priceSymbolTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceSymbolTextView.setText("$");
        priceSymbolTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        priceSymbolTextView.setTextColor(getResources().getColor(android.R.color.black));
        priceSymbolTextView.setTextSize(16f);
        priceSymbolTextView.setPadding(10, 0, 0, 0);

        // Create the EditText for entering additional price
        EditText extraPriceEditText = new EditText(this);
        extraPriceEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout.LayoutParams priceEditTextParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);  // Set width to 30%
        extraPriceEditText.setLayoutParams(priceEditTextParams);
        extraPriceEditText.setPadding(8, 8, 8, 8);
        extraPriceEditText.setTextSize(16f);

        // Create TextView for "extra" text
        TextView extraTextView = new TextView(this);
        extraTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        extraTextView.setText("extra");
        extraTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        extraTextView.setTextColor(getResources().getColor(android.R.color.black));
        extraTextView.setTextSize(16f);
        extraTextView.setPadding(10, 0, 0, 0);

        // Add views to the LinearLayout for the weight section
        newWeightSection.addView(weightSpinner);
        newWeightSection.addView(priceSymbolTextView);
        newWeightSection.addView(extraPriceEditText);
        newWeightSection.addView(extraTextView);

        // Add the new weight section to the priceWeightTemplateLayout
        priceWeightTemplateLayout.addView(newWeightSection);
    }


    private void saveAndFinish() {

        Intent intent = new Intent(B_MyQuoteSetupActivity.this, B_HomeActivity.class);
        startActivity(intent);
        finish();
    }

}