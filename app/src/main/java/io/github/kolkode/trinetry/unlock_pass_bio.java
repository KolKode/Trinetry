package io.github.kolkode.trinetry;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

import io.github.kolkode.trinetry.ui.dashboard;

public class unlock_pass_bio extends AppCompatActivity {

    private EditText passwordInput;
    private Button unlockWithPasswordButton, biometricButton;
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "CryptoWalletPrefs";
    private static final String PASSWORD_HASH_KEY = "password_hash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_pass_bio);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        passwordInput = findViewById(R.id.passwordInput);
        unlockWithPasswordButton = findViewById(R.id.unlockWithPasswordButton);
        biometricButton = findViewById(R.id.biometricButton);

        biometricButton.setOnClickListener(v -> startBiometric());

        unlockWithPasswordButton.setOnClickListener(v -> {
            String enteredPassword = passwordInput.getText().toString().trim();
            if (enteredPassword.isEmpty()) {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            String storedHash = sharedPreferences.getString(PASSWORD_HASH_KEY, "");
            String enteredHash = hashPassword(enteredPassword);

            if (storedHash.equals(enteredHash)) {
                unlockSuccess();
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG
                | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                != BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(this, "Biometric not available", Toast.LENGTH_SHORT).show();
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        unlockSuccess();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(unlock_pass_bio.this, "Biometric error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(unlock_pass_bio.this, "Biometric failed", Toast.LENGTH_SHORT).show();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Use fingerprint or face to unlock")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    private void unlockSuccess() {
        Toast.makeText(this, "Wallet unlocked", Toast.LENGTH_SHORT).show();
        // Redirect to wallet home or main screen
        startActivity(new Intent(this, dashboard.class));
        finish();
    }
}
