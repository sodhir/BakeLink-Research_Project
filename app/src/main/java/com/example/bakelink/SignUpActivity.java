package com.example.bakelink;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.bakers.B_HomeActivity;
import com.example.bakelink.bakers.B_ProfileSetupActivity;
import com.example.bakelink.customers.C_HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextView signIn;
    private Button signUp;

    private ProgressDialog progressDialog;

    EditText txtEmailSignUp;
    EditText txtPasswordSignUp;
    RadioGroup rdbCakeTypeGroup;
    RadioButton rdbBaker;
    RadioButton rdbCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signIn = findViewById(R.id.txtSignIn);
        signUp = findViewById(R.id.btnSignUp);
        txtEmailSignUp = findViewById(R.id.txtEmailSignUp);
        txtPasswordSignUp = findViewById(R.id.txtPasswordSignUp);
        rdbCakeTypeGroup = findViewById(R.id.rdbCakeTypeGroup);
        rdbBaker = findViewById(R.id.rdbBaker);
        rdbCustomer = findViewById(R.id.rdbCaustomer);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });
    }

    private void userSignUp() {
        String email = txtEmailSignUp.getText().toString().trim();
        String password = txtPasswordSignUp.getText().toString().trim();
        String userType;

        if (!rdbBaker.isChecked() && !rdbCustomer.isChecked()) {
            // Show an error message and exit the method if no radio button is selected
            Toast.makeText(this, "Please select either Baker or Customer.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Initialize userType based on the selected radio button
            userType = rdbBaker.isChecked() ? "Baker" : "Customer";
        }

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Signing up...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get user ID
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("USER_ID", userId); // Save user ID to SharedPreferences
                            editor.apply();
                            Log.d("USER_ID", "Retrieved userId: " + userId);

                            // Create a user object
                            User user = new User(email, userType);

                            // Save to Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            databaseReference.child(userId).setValue(user)
                                    .addOnCompleteListener(saveTask -> {
                                        progressDialog.dismiss();
                                        if (saveTask.isSuccessful()) {
                                            //Toast.makeText(SignUpActivity.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();

                                            // Navigate based on userType
                                            if (userType.equals("Baker")) {
                                                Intent intent = new Intent(SignUpActivity.this, B_ProfileSetupActivity.class);
                                                startActivity(intent);
                                            } else {
                                                SharedPreferences c_sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                                                SharedPreferences.Editor c_editor = c_sharedPreferences.edit();
                                                c_editor.putString("customer_email", email); // Save customer email
                                                c_editor.apply();

                                                Intent intent = new Intent(SignUpActivity.this, C_HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
    }
}