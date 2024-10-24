package com.example.bakelink;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.customers.C_HomeActivity;

import java.util.Random;



public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] quotes = {
                "You’re made of sweetness, sprinkles, and all things delightful.",
                "You deserve frosting, fun, and a dash of sparkle.",
                "Life’s too short, so treat yourself to sugar, spice, and everything twice!",
                "A pinch of sweetness, a sprinkle of joy—you deserve it all.",
                "You’re worth every sweet bite and a cherry on top.",
                "Whisked with love, baked with joy—you deserve the best!",
                "You deserve a little sugar, a lot of love, and endless treats.",
                "Sprinkled with happiness, frosted with kindness—that’s you.",
                "You’re the reason life needs sugar and a little bit of spice.",
                "You deserve the sweetest moments and a cake to match."
        };

        Random random = new Random();
        int index = random.nextInt(quotes.length);
        String randomQuote = quotes[index];

        TextView quoteTextView = findViewById(R.id.txtSplashQuote);
        quoteTextView.setText(randomQuote);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the splash screen duration
                Intent intent = new Intent(SplashScreenActivity.this, C_HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }


}