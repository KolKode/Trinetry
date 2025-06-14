package io.github.kolkode.trinetry;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.github.kolkode.trinetry.ui.dashboard;

public class pass_bio extends AppCompatActivity {

    private EditText passwordInput, confirmPasswordInput;
    private Button savePasswordButton;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "CryptoWalletPrefs";
    private static final String PASSWORD_HASH_KEY = "password_hash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_bio);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        savePasswordButton = findViewById(R.id.savePasswordButton);

        savePasswordButton.setOnClickListener(v -> {
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            String passwordHash = hashPassword(password);
            if (passwordHash != null) {
                sharedPreferences.edit().putString(PASSWORD_HASH_KEY, passwordHash).apply();
                Toast.makeText(this, "Password saved securely", Toast.LENGTH_SHORT).show();

                // Move to next screen (e.g., biometric setup or wallet)
                startActivity(new Intent(this, dashboard.class));
                finish();
            } else {
                Toast.makeText(this, "Error hashing password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b)); // Convert to hex string
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
