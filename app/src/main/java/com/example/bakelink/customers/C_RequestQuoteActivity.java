package com.example.bakelink.customers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.adapters.RecommendationAdapter;
import com.example.bakelink.bakers.models.RecommendationCake;
import com.example.bakelink.common.models.VisionRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class C_RequestQuoteActivity extends AppCompatActivity {

    ImageView cakeImg;
    FrameLayout uploadFrame;
    ImageButton cartIcon;
    ImageView uploadImage;

    private Uri cakeImgUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    ArrayList<int[]> rgbColorArrayList;
    RecyclerView recyclerView;
    RecommendationAdapter cakeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crequest_quote);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rgbColorArrayList = new ArrayList<>();

        cartIcon = findViewById(R.id.cart_icon);

        // Set OnClickListener for the cart icon
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to C_CartActivity
                Intent intent = new Intent(C_RequestQuoteActivity.this, C_CartActivity.class);
                startActivity(intent);
            }
        });

        //fab
        ImageButton fab = findViewById(R.id.fab_request_quote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), C_RequestQuoteActivity.class));
        });

        setupBottomNavigation();

        cakeImg = findViewById(R.id.uploadedImg);
        uploadFrame = findViewById(R.id.reqimageFrame);

        uploadFrame.setOnClickListener(v -> {
            openImagePicker();
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
            cakeImg.setImageURI(imageUri); // Display image in ImageView (if needed)
            // Save this URI to use it for uploading later
            cakeImgUri = imageUri;
            Log.d("JSON Request", "Selected image URI: " + imageUri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                sendImageToVisionAPI(base64Image);
            } catch (Exception e) {
                e.printStackTrace();
            }

                Button requestQuoteButton = findViewById(R.id.button_request_quote);
            requestQuoteButton.setOnClickListener(v -> {

                Intent intent = new Intent(C_RequestQuoteActivity.this, C_CustomCakeRequestActivity.class);
                intent.putExtra("imageUri", imageUri.toString());
                intent.putExtra("rgbColors", rgbColorArrayList);
                startActivity(intent);

            });
        }
    }

    private void sendImageToVisionAPI(String base64Image) {
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
                    parseColorDetails(jsonResponse);
                } else {
                    Log.e("VisionAPI", "Request failed: " + response.code() + " - " + response.message());
                    Log.e("VisionAPI", "Response body: " + response.body().string());
                }
            }
        });
    }


    private void parseColorDetails(String jsonResponse) {
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

            rgbColorArrayList.addAll(rgbColors);


            // Update the color swatches on the UI
            runOnUiThread(() -> updateColorSwatches(rgbColors));
            loadRecommendations(rgbColors);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateColorSwatches(List<int[]> colors) {
        if (colors.size() >= 5) { // Ensure at least 5 colors are available
            // Get the View references
            View swatch1 = findViewById(R.id.colorSwatch1);
            View swatch2 = findViewById(R.id.colorSwatch2);
            View swatch3 = findViewById(R.id.colorSwatch3);
            View swatch4 = findViewById(R.id.colorSwatch4);
            View swatch5 = findViewById(R.id.colorSwatch5);

            // Convert RGB values to Color and set the backgrounds
            swatch1.setBackgroundColor(Color.rgb(colors.get(0)[0], colors.get(0)[1], colors.get(0)[2]));
            swatch2.setBackgroundColor(Color.rgb(colors.get(1)[0], colors.get(1)[1], colors.get(1)[2]));
            swatch3.setBackgroundColor(Color.rgb(colors.get(2)[0], colors.get(2)[1], colors.get(2)[2]));
            swatch4.setBackgroundColor(Color.rgb(colors.get(3)[0], colors.get(3)[1], colors.get(3)[2]));
            swatch5.setBackgroundColor(Color.rgb(colors.get(4)[0], colors.get(4)[1], colors.get(4)[2]));
        }

    }

//    private void loadRecommendations(List<int[]> colors) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recommendations");
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<RecommendationCake> similarCakes = new ArrayList<>();
//
//                // Assuming currentCakeColors is the RGB colors of the current cake
//                ArrayList<int[]> currentCakeColors = new ArrayList<>(colors);
//
//                // Loop through the database entries and compare their colors with the current cake's colors
//                for (DataSnapshot cakeSnapshot : snapshot.getChildren()) {
//                    // Retrieve the RGB colors for each cake
//                    GenericTypeIndicator<List<List<Integer>>> typeIndicator = new GenericTypeIndicator<List<List<Integer>>>() {};
//                    List<List<Integer>> cakeColorsList = cakeSnapshot.child("rgbColors").getValue(typeIndicator);
//
//                    // Convert to ArrayList<int[]>
//                    ArrayList<int[]> cakeColors = new ArrayList<>();
//                    if (cakeColorsList != null) {
//                        for (List<Integer> colorList : cakeColorsList) {
//                            int[] colorArray = new int[colorList.size()];
//                            for (int i = 0; i < colorList.size(); i++) {
//                                colorArray[i] = colorList.get(i);
//                            }
//                            cakeColors.add(colorArray);
//                        }
//                    }
//
//                    // Compare the colors (find the smallest distance)
//                    for (int[] cakeColor : cakeColors) {
//                        for (int[] currentColor : currentCakeColors) {
//                            double distance = calculateColorDistance(cakeColor, currentColor);
//                            if (distance < 25) {  // Define a threshold for matching colors
//                                similarCakes.add(cakeSnapshot.getValue(RecommendationCake.class));
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                // Display results
//                displayMatchingCakes(similarCakes);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void loadRecommendations(List<int[]> colors) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recommendations");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<RecommendationCake> similarCakes = new ArrayList<>();
                Set<String> addedCakeIds = new HashSet<>();  // Set to track added cakes by their ID

                // Assuming currentCakeColors is the RGB colors of the current cake
                ArrayList<int[]> currentCakeColors = new ArrayList<>(colors);

                // Loop through the database entries and compare their colors with the current cake's colors
                for (DataSnapshot cakeSnapshot : snapshot.getChildren()) {
                    // Retrieve the RGB colors for each cake
                    GenericTypeIndicator<List<List<Integer>>> typeIndicator = new GenericTypeIndicator<List<List<Integer>>>() {};
                    List<List<Integer>> cakeColorsList = cakeSnapshot.child("rgbColors").getValue(typeIndicator);

                    // Convert to ArrayList<int[]>
                    ArrayList<int[]> cakeColors = new ArrayList<>();
                    if (cakeColorsList != null) {
                        for (List<Integer> colorList : cakeColorsList) {
                            int[] colorArray = new int[colorList.size()];
                            for (int i = 0; i < colorList.size(); i++) {
                                colorArray[i] = colorList.get(i);
                            }
                            cakeColors.add(colorArray);
                        }
                    }

                    boolean cakeMatched = false;  // Flag to track if this cake has matched with any color

                    // Compare the colors (find the smallest distance)
                    for (int[] cakeColor : cakeColors) {
                        for (int[] currentColor : currentCakeColors) {
                            double distance = calculateColorDistance(cakeColor, currentColor);
                            if (distance < 25) {  // Define a threshold for matching colors
                                // Check if the cake has already been added
                                String cakeId = cakeSnapshot.getKey();  // or use any unique identifier
                                if (!addedCakeIds.contains(cakeId)) {
                                    similarCakes.add(cakeSnapshot.getValue(RecommendationCake.class));
                                    addedCakeIds.add(cakeId);  // Mark this cake as added
                                }
                                cakeMatched = true;  // Once a match is found, no need to check further colors for this cake
                                break;
                            }
                        }
                        if (cakeMatched) break;  // Exit the loop early if a match was found
                    }
                }

                // Display results
                displayMatchingCakes(similarCakes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors (if any)
            }
        });
    }


    private void displayMatchingCakes(List<RecommendationCake> matchingCakes) {
        recyclerView = findViewById(R.id.recommendationRecycler);
        if (matchingCakes.isEmpty()) {
            Toast.makeText(this, "No cakes match your color scheme.", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
        } else {
            // Display matching cakes in the RecyclerView or any other UI component
            // For example, you could pass the list to an adapter and refresh the UI
            recyclerView.setVisibility(View.VISIBLE);
            cakeAdapter = new RecommendationAdapter(matchingCakes);
            recyclerView.setAdapter(cakeAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    private double calculateColorDistance(int[] color1, int[] color2) {
        int rDiff = color1[0] - color2[0];
        int gDiff = color1[1] - color2[1];
        int bDiff = color1[2] - color2[2];

        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }


    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.none);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
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
        });
    }
}