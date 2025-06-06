package io.github.kolkode.trinetry.ui;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import io.github.kolkode.trinetry.R;

public class splash_screen extends AppCompatActivity {

    private static final long SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to the Welcome Page
                Intent intent = new Intent(splash_screen.this, welcome_page.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}
