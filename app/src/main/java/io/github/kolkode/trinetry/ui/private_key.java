package io.github.kolkode.trinetry.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.security.NoSuchAlgorithmException;
import java.security.*;

import io.github.kolkode.trinetry.R;
import io.github.kolkode.trinetry.utils.Wallet;

public class private_key extends AppCompatActivity {

   EditText entertxt;
   Button importbtn;
   Button backbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_private_key);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        entertxt=findViewById(R.id.entertxt);
        importbtn=findViewById(R.id.importbtn);
        backbtn=findViewById(R.id.backbtn);

        backbtn.setOnClickListener(v -> finish());

        importbtn.setOnClickListener(v -> {
            String text = entertxt.getText().toString().trim();
            if(Wallet.isValidPrivate(text)){
                entertxt.setText(text);
                Wallet.getKeyPairFromSeed(text);
                Intent intent=new Intent(private_key.this, dashboard.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Please enter a valid Private Key", Toast.LENGTH_SHORT).show();
            }
        });
    }
}