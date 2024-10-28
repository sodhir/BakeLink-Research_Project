package com.example.bakelink;

import android.content.Intent;
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

import com.example.bakelink.customers.C_HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

    private void logIn(String txtEmail, String txtPass) {
        auth.signInWithEmailAndPassword(txtEmail, txtPass).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, C_HomeActivity.class);
                    intent.putExtra("email", txtEmail);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}