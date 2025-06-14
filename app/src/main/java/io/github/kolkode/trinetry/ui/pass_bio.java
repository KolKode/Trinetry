package io.github.kolkode.trinetry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.github.kolkode.trinetry.R;
import io.github.kolkode.trinetry.utils.AppSecurity;

public class pass_bio extends AppCompatActivity {

    private EditText passwordInput, confirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_bio);

        AppSecurity security = new AppSecurity(this);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        Button savePasswordButton = findViewById(R.id.savePasswordButton);

        savePasswordButton.setOnClickListener(v -> {
            boolean done = false;
            String passInput = passwordInput.getText().toString();
            String confirmPassInput = confirmPasswordInput.getText().toString();
            if (passInput.isEmpty()) {
                Toast.makeText(this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
            } else if (confirmPassInput.isEmpty()) {
                Toast.makeText(this, "Please the password again for confirmation", Toast.LENGTH_SHORT).show();
            } else if (!passInput.equals(confirmPassInput)) {
                Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            } else {
                security.savePass(passInput);
                done = true;
            }
            if (done) {
                Intent intent = new Intent(pass_bio.this, dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
