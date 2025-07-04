package io.github.kolkode.trinetry.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.kolkode.trinetry.R;

public class import_eth extends AppCompatActivity {
    Button recoverybtn;
    Button privatebtn;
    Button backbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_import_eth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recoverybtn=findViewById(R.id.recoverybtn);
        privatebtn=findViewById(R.id.privatebtn);
        backbtn=findViewById(R.id.backbtn);

        backbtn.setOnClickListener(v -> finish());

        recoverybtn.setOnClickListener(v -> {
            Intent intent=new Intent(import_eth.this, recovery.class);
            startActivity(intent);
        });

        privatebtn.setOnClickListener(v -> {
            Intent intent=new Intent(import_eth.this, private_key.class);
            startActivity(intent);
        });


    }
}