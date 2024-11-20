package com.example.bakelink.bakers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class B_AddNewCakeActivity extends AppCompatActivity {

    private EditText etCakeName, etDescription, etCakePrice;
    private ImageView ivCakeImage;
    private Button btnDone;
    private FrameLayout ivCakeFrame;
    private Uri ivCakeImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private LinearLayout sizes_list_layout;
    private LinearLayout flavor_list_layout;
    private LinearLayout filling_list_layout;
    private WeightsBottomSheetFragment weightsBottomSheetFragment;
    private FlavorsBottomSheetFragment flavorsBottomSheetFragment;
    private FillingsBottomSheetFragment fillingsBottomSheetFragment;
    private FrameLayout addWeightButton;
    private FrameLayout addFlavorButton;
    private FrameLayout addFillingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_badd_new_cake);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        etCakeName = findViewById(R.id.nckCakeName);
        etDescription = findViewById(R.id.nckDescription);
        etCakePrice = findViewById(R.id.nckCakePrice);
        ivCakeImage = findViewById(R.id.ivCakeImage);
        btnDone = findViewById(R.id.btnDone);
        ivCakeFrame = findViewById(R.id.ivCakeFrame);
        sizes_list_layout = findViewById(R.id.sizes_list_layout);
        flavor_list_layout = findViewById(R.id.flavor_list_layout);
        filling_list_layout = findViewById(R.id.filling_list_layout);
        addWeightButton = findViewById(R.id.add_weight_button);
        addFlavorButton = findViewById(R.id.add_flavor_button);
        addFillingButton = findViewById(R.id.add_filling_button);

        // Set listener for cake image upload
        ivCakeFrame.setOnClickListener(v -> {
            //functionality for upload picture
            //Toast.makeText(this, "Upload Cake Image", Toast.LENGTH_SHORT).show();
            openImagePicker();
        });

        //Set listener for adding weights
        addWeightButton.setOnClickListener(v -> {
            weightsBottomSheetFragment = new WeightsBottomSheetFragment();
            weightsBottomSheetFragment.show(getSupportFragmentManager(), weightsBottomSheetFragment.getTag());
        });

        //Set listener for adding flavors
        addFlavorButton.setOnClickListener(v -> {
            flavorsBottomSheetFragment = new FlavorsBottomSheetFragment();
            flavorsBottomSheetFragment.show(getSupportFragmentManager(), flavorsBottomSheetFragment.getTag());
        });

        //Set listener for adding fillings
        addFillingButton.setOnClickListener(v -> {
            fillingsBottomSheetFragment = new FillingsBottomSheetFragment();
            fillingsBottomSheetFragment.show(getSupportFragmentManager(), fillingsBottomSheetFragment.getTag());
        });


        // Done button click listener
        btnDone.setOnClickListener(v -> saveCakeDetails());

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        // Deselect the item (no item should be highlighted)
        bottomNavigationView.setSelectedItemId(-1);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.setSelectedItemId(R.id.none);
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_AddNewCakeActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_AddNewCakeActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_AddNewCakeActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_AddNewCakeActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // This is the URI of the selected image
            ivCakeImage.setImageURI(imageUri); // Display image in ImageView (if needed)
            // Save this URI to use it for uploading later
            ivCakeImageUri = imageUri;
        }
    }


    public void addWeightsToList(String weights) {
        TextView textView = new TextView(this);
        // Set the text for the TextView
        textView.setText(weights);
        // Set the text color
        textView.setTextColor(getResources().getColor(android.R.color.black));
        // Set the text size
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        // Set padding
        textView.setPadding(0, 8, 0, 8);
        // Set the layout parameters (margin top)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topMargin = 8; // 8dp margin top
        // Apply the layout parameters to the TextView
        textView.setLayoutParams(layoutParams);
        // Add the TextView to the layout
        sizes_list_layout.addView(textView);
    }

    public void addFlavorsToList(String flavor) {
        TextView textView = new TextView(this);
        // Set the text for the TextView
        textView.setText(flavor);
        // Set the text color
        textView.setTextColor(getResources().getColor(android.R.color.black));
        // Set the text size
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        // Set padding
        textView.setPadding(0, 8, 0, 8);
        // Set the layout parameters (margin top)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topMargin = 8; // 8dp margin top
        // Apply the layout parameters to the TextView
        textView.setLayoutParams(layoutParams);
        // Add the TextView to the layout
        flavor_list_layout.addView(textView);
    }

    public void addFillingsToList(String fillings) {
        TextView textView = new TextView(this);
        // Set the text for the TextView
        textView.setText(fillings);
        // Set the text color
        textView.setTextColor(getResources().getColor(android.R.color.black));
        // Set the text size
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        // Set padding
        textView.setPadding(0, 8, 0, 8);
        // Set the layout parameters (margin top)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topMargin = 8; // 8dp margin top
        // Apply the layout parameters to the TextView
        textView.setLayoutParams(layoutParams);
        // Add the TextView to the layout
        filling_list_layout.addView(textView);
    }

    private void saveCakeDetails() {
        String cakeName = etCakeName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String cakePrice = etCakePrice.getText().toString().trim();

        /*if (cakeName.isEmpty() || description.isEmpty() || cakePrice.isEmpty()) {
            Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
            return;
        }*/

        // Create a new Cake object
        // Create a Cake model and a database method to save the cake
        // Cake newCake = new Cake(cakeName, description, cakePrice, imageUri.toString());

        // code to save data to database


        Toast.makeText(this, "Cake added successfully", Toast.LENGTH_SHORT).show();


        startActivity(new Intent(B_AddNewCakeActivity.this, B_MyAllCakesActivity.class));
        finish();
    }
}