package io.github.kolkode.trinetry.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.kolkode.trinetry.R;

public class dash_setting extends AppCompatActivity {
    Button rcph_btn,pvtkey_btn;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch test_swtch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pvtkey_btn = findViewById(R.id.pvtkey_btn);
        rcph_btn = findViewById(R.id.rcph_btn);
        test_swtch = findViewById(R.id.test_swtch);

        rcph_btn.setOnClickListener(v ->{
            startActivity(new Intent(dash_setting.this,show_recovery.class));
        });
        pvtkey_btn.setOnClickListener(v ->{
            startActivity(new Intent(dash_setting.this,show_private.class));
        });

    }
}