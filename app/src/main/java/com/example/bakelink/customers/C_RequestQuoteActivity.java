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

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.example.bakelink.common.models.VisionRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.NotNull;
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

public class C_RequestQuoteActivity extends AppCompatActivity {

    ImageView cakeImg;
    FrameLayout uploadFrame;
    ImageButton cartIcon;
    ImageView uploadImage;

    private Uri cakeImgUri;
    private static final int PICK_IMAGE_REQUEST = 1;

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

            // Update the color swatches on the UI
            runOnUiThread(() -> updateColorSwatches(rgbColors));
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



//    private void analyzeImageColors(Uri imageUri) {
//        try (ImageAnnotatorClient visionClient = ImageAnnotatorClient.create()) {
//            ByteString imageBytes = ByteString.readFrom(getContentResolver().openInputStream(imageUri));
//            Image image = Image.newBuilder().setContent(imageBytes).build();
//
//            Feature feature = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build();
//            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
//                    .addFeatures(feature)
//                    .setImage(image)
//                    .build();
//
//            AnnotateImageResponse response = visionClient.batchAnnotateImages(Collections.singletonList(request)).getResponses(0);
//            if (response.hasError()) {
//                Log.e("Vision API", "Error: " + response.getError().getMessage());
//                return;
//            }
//
//            // Extract color information
//            List<ColorInfo> colors = response.getImagePropertiesAnnotation().getDominantColors().getColorsList();
//            for (ColorInfo color : colors) {
//                Log.d("Color", "Color: " + color.getColor() + " - Score: " + color.getScore());
//            }
//        } catch (Exception e) {
//            Log.e("Vision API", "Failed to analyze image colors", e);
//        }
//    }

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