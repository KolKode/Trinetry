package io.github.kolkode.trinetry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class recovery extends AppCompatActivity {

    private Button subbtn;
    private Button backbtn;
    private EditText p1txt, p2txt, p3txt, p4txt, p5txt, p6txt,
            p7txt, p8txt, p9txt, p10txt, p11txt, p12txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recovery);

        // Find views after layout is set
        subbtn = findViewById(R.id.subbtn);
        backbtn = findViewById(R.id.backbtn);

        p1txt = findViewById(R.id.seed1);
        p2txt = findViewById(R.id.seed2);
        p3txt = findViewById(R.id.seed3);
        p4txt = findViewById(R.id.seed4);
        p5txt = findViewById(R.id.seed5);
        p6txt = findViewById(R.id.seed6);
        p7txt = findViewById(R.id.seed7);
        p8txt = findViewById(R.id.seed8);
        p9txt = findViewById(R.id.seed9);
        p10txt = findViewById(R.id.seed10);
        p11txt = findViewById(R.id.seed11);
        p12txt = findViewById(R.id.seed12);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
