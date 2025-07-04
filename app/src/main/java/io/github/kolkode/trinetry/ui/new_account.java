package io.github.kolkode.trinetry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.kolkode.trinetry.R;
import io.github.kolkode.trinetry.utils.Wallet;

public class new_account extends AppCompatActivity {
    Button createAccBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createAccBtn = findViewById(R.id.createaccbtn);
        createAccBtn.setOnClickListener(v -> {
            Wallet.generateKeyPairWithSeed(this);
            Intent intent = new Intent(new_account.this, pass_bio.class);
            startActivity(intent);
            finish();
        });
    }
}