package io.github.kolkode.trinetry.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.kolkode.trinetry.R;

public class dashboard extends AppCompatActivity {
    ImageButton send,receive;


    @SuppressLint({ "WrongViewCast", "MissingInflatedId" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        send = findViewById(R.id.sendBtn);
        receive=findViewById(R.id.receiveBtn);
        send.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, send_eth.class);
            startActivity(intent);
        });
////        receive.setOnClickListener(v -> {
////            Intent intent=new Intent(dashboard.this,receive_eth.class);
////            startActivity(intent);
//        });
        try {

            receive.setOnClickListener(v -> {
                Intent intent=new Intent(dashboard.this,receive_eth.class);
                startActivity(intent);
            });
        }catch (Exception e){
            Log.d("page crash",e.getMessage());
        }
    }
}
