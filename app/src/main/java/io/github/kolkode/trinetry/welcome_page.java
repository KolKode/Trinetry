package io.github.kolkode.trinetry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome_page extends AppCompatActivity {
    CheckBox agraybtn;
    Button newaccbtn;
    Button alreadybtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        agraybtn = findViewById(R.id.agraybtn); // Make sure this is CheckBox
        newaccbtn = findViewById(R.id.newaccbtn);
        alreadybtn = findViewById(R.id.alreadyaccbtn);

        // Disable other buttons
        newaccbtn.setEnabled(false);
        alreadybtn.setEnabled(false);

        // Set up checkbox listener
        agraybtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            newaccbtn.setEnabled(isChecked);
            alreadybtn.setEnabled(isChecked);
        });

        // Button click listeners
        alreadybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_page.this, import_eth.class);
                startActivity(intent);
            }
        });

        newaccbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_page.this, new_account.class);
                startActivity(intent);
            }
        });


    }
}
