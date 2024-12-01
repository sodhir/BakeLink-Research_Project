package com.example.bakelink.bakers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.example.bakelink.bakers.interfaces.BakeryTitleCallBack;
import com.example.bakelink.bakers.models.Cake;
import com.example.bakelink.bakers.models.RecommendationCake;
import com.example.bakelink.common.models.VisionRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        bottomNavigationView.setSelectedItemId(R.id.none);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

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

        if (cakeName.isEmpty() || description.isEmpty() || cakePrice.isEmpty() || ivCakeImageUri == null) {
            Toast.makeText(this, "All fields are required, including an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(cakePrice);
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Collect weights data from the layout
            List<String> weights = getWeightsFromList();

            // Collect flavor data from the layout
            List<String> flavors = getFlavorsFromList();

            // Collect filling data from the layout
            List<String> fillings = getFillingsFromList();

            uploadImageAndSaveDetails(cakeName, description, price, currentUserId, weights, flavors, fillings);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to collect weights data from sizes_list_layout
    private List<String> getWeightsFromList() {
        List<String> weights = new ArrayList<>();
        for (int i = 0; i < sizes_list_layout.getChildCount(); i++) {
            TextView textView = (TextView) sizes_list_layout.getChildAt(i);
            weights.add(textView.getText().toString().trim());
        }
        return weights;
    }
    private List<String> getFlavorsFromList() {
        List<String> flavors = new ArrayList<>();
        for (int i = 0; i < flavor_list_layout.getChildCount(); i++) {
            TextView textView = (TextView) flavor_list_layout.getChildAt(i);
            flavors.add(textView.getText().toString().trim());
        }
        return flavors;
    }
    private List<String> getFillingsFromList() {
        List<String> fillings = new ArrayList<>();
        for (int i = 0; i < filling_list_layout.getChildCount(); i++) {
            TextView textView = (TextView) filling_list_layout.getChildAt(i);
            fillings.add(textView.getText().toString().trim());
        }
        return fillings;
    }
    private void uploadImageAndSaveDetails(String cakeName, String description, double cakePrice, String currentUserId, List<String> weights, List<String> flavors, List<String> fillings) {
        if (ivCakeImageUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("cakeImages/" + System.currentTimeMillis() + ".jpg");
        storageRef.putFile(ivCakeImageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    // Save cake details after image upload
                    DatabaseReference cakeRef = FirebaseDatabase.getInstance().getReference("bakers").child(currentUserId).child("cakes");
                    String cakeId = cakeRef.push().getKey();

                    Cake cake = new Cake();
                    cake.setCakeId(cakeId);
                    cake.setCakeName(cakeName);
                    cake.setDescription(description);
                    cake.setPrice(cakePrice);
                    cake.setCakeImgUrl(imageUrl);
                    cake.setWeights(weights);
                    cake.setFlavors(flavors);
                    cake.setFillings(fillings);

                    cakeRef.child(cakeId).setValue(cake)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(B_AddNewCakeActivity.this, "Cake added successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(B_AddNewCakeActivity.this, B_MyAllCakesActivity.class));

                                try {
                                    InputStream inputStream = getContentResolver().openInputStream(ivCakeImageUri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] imageBytes = baos.toByteArray();
                                    String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                    sendImageToVisionAPI(base64Image, cake);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }



                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(B_AddNewCakeActivity.this, "Failed to save cake: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(B_AddNewCakeActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void sendImageToVisionAPI(String base64Image, Cake cake) {
        String apiKey = "AIzaSyCTUCTGGSKa9H_LIVNMFFLPoUnyVUiY7T4";
        String apiUrl = "https://vision.googleapis.com/v1/images:annotate?key=" + apiKey;

        VisionRequest visionRequest = new VisionRequest(base64Image);
        String jsonRequest = new Gson().toJson(visionRequest);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("VisionAPI", "Request failed", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Log.d("JSON Response", jsonResponse);
                    parseColorDetails(jsonResponse, cake);
                } else {
                    Log.e("VisionAPI", "Request failed: " + response.code() + " - " + response.message());
                    Log.e("VisionAPI", "Response body: " + response.body().string());
                }
            }
        });
    }


    private void parseColorDetails(String jsonResponse, Cake cake) {
        Log.d("JSON Response Parse", jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray colors = jsonObject.getJSONArray("responses")
                    .getJSONObject(0)
                    .getJSONObject("imagePropertiesAnnotation")
                    .getJSONObject("dominantColors")
                    .getJSONArray("colors");

            List<int[]> rgbColors = new ArrayList<>();

            for (int i = 0; i < colors.length(); i++) {
                JSONObject color = colors.getJSONObject(i).getJSONObject("color");
                int red = color.getInt("red");
                int green = color.getInt("green");
                int blue = color.getInt("blue");
                rgbColors.add(new int[]{red, green, blue});
            }

            List<List<Integer>> rgbColorsList = new ArrayList<>();
            for (int[] color : rgbColors) {
                List<Integer> colorList = new ArrayList<>();
                for (int value : color) {
                    colorList.add(value); // Convert array to List
                }

                rgbColorsList.add(colorList);
            }


            // Update the color swatches on the UI
            updateRecommendation(rgbColorsList, cake);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateRecommendation(List<List<Integer>> rgbColorsList, Cake cake) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RecommendationCake recommendationCake = new RecommendationCake();
        recommendationCake.setImageUrl(cake.getCakeImgUrl());
        recommendationCake.setRgbColors(rgbColorsList);
        recommendationCake.setCakeType("Regular");
        recommendationCake.setCake(cake);
        getBakeryTitle(currentUserId, bakeryTitle -> {
            recommendationCake.setBakerTitle(bakeryTitle);
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recommendations");
        String recommendationId = databaseReference.push().getKey();
        databaseReference.child(recommendationId).setValue(recommendationCake);
    }


    private void getBakeryTitle(String bakerId, BakeryTitleCallBack callback) {
        if (bakerId == null || bakerId.isEmpty()) {
            callback.onBakeryTitleFetched(""); // Return empty if bakerId is invalid
            return;
        }

        DatabaseReference bakersRef = FirebaseDatabase.getInstance().getReference("bakers").child(bakerId);

        bakersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("bakeryTitle")) {
                    String bakeryTitle = snapshot.child("bakeryTitle").getValue(String.class);
                    callback.onBakeryTitleFetched(bakeryTitle); // Pass the fetched title to the callback
                } else {
                    callback.onBakeryTitleFetched(""); // No bakeryTitle found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to fetch bakery title: " + error.getMessage());
                callback.onBakeryTitleFetched(""); // Return empty on failure
            }
        });
    }


}