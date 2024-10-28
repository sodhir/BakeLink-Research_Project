package com.example.bakelink.customers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class C_RequestQuoteActivity extends AppCompatActivity {

    ImageView cakeImg;
    Button uploadButton;

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
        setupBottomNavigation();

        cakeImg = findViewById(R.id.uploadedImg);
        uploadButton = findViewById(R.id.btnUpload);

       uploadButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openImagePicker();
           }
       });



    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            cakeImg.setImageURI(imageUri); // Display the selected image

                            Button requestQuoteButton = findViewById(R.id.button_request_quote);
                            requestQuoteButton.setOnClickListener(v -> {

                                Intent intent = new Intent(C_RequestQuoteActivity.this, C_CustomCakeRequestActivity.class);
                                intent.putExtra("imageUri", imageUri.toString());
                                startActivity(intent);

                            });
                        }

                    }
                }
            });

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
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