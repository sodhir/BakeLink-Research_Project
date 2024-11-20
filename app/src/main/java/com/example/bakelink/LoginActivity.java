package com.example.bakelink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.bakers.B_HomeActivity;
import com.example.bakelink.customers.C_HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView signUp;
    private EditText email;
    private EditText password;
    private Button login;
    String txtEmail;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
       // FirebaseApp.initializeApp(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.txtEmailLogin);
        password = findViewById(R.id.txtPasswordLogin);

        login = findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = email.getText().toString();
                String txtPass = password.getText().toString();
                Log.d("hello", "Clicked");
                if(TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPass)){
                    Toast.makeText(LoginActivity.this, "Please enter the email and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    logIn(txtEmail, txtPass);
                }
            }
        });

        signUp = findViewById(R.id.txtSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void logIn(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the user ID
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();

                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("USER_ID", userId);  // Save the user ID with a key
                            editor.apply();

                            // Retrieve user type from Firebase
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        User user = snapshot.getValue(User.class);
                                        Log.d("hello", "User type: " + user.getUserType());
                                        if (user != null) {
                                            String userType = user.getUserType();
                                            // Route user based on their type
                                            if ("Baker".equals(userType)) {
                                                // Fetch bakery name from 'bakers' collection
                                                DatabaseReference bakerRef = FirebaseDatabase.getInstance().getReference("bakers").child(userId);
                                                bakerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot bakerSnapshot) {
                                                        if (bakerSnapshot.exists()) {
                                                            String bakeryName = bakerSnapshot.child("bakeryTitle").getValue(String.class);
                                                            Log.d("bakername",bakeryName);
                                                            if (bakeryName != null) {
                                                                SharedPreferences b_sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                                                                SharedPreferences.Editor b_editor = b_sharedPreferences.edit();
                                                                b_editor.putString("bakery_name", bakeryName);
                                                                b_editor.apply();
                                                            }
                                                            Intent intent = new Intent(LoginActivity.this, B_HomeActivity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(LoginActivity.this, "Baker data not found.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError error) {
                                                        Toast.makeText(LoginActivity.this, "Error fetching baker data.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else if ("Customer".equals(userType)) {
                                                SharedPreferences c_sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                                                SharedPreferences.Editor c_editor = c_sharedPreferences.edit();
                                                c_editor.putString("customer_email", email); // Save customer email
                                                c_editor.apply();
                                               Intent intent = new Intent(LoginActivity.this, C_HomeActivity.class);
                                               startActivity(intent);
                                            }
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}