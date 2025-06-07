package io.github.kolkode.trinetry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import io.github.kolkode.trinetry.R;
import io.github.kolkode.trinetry.utils.Wallet;

public class dashboard extends AppCompatActivity {
    ImageButton send,receive;
    TextView balance;

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
        balance = findViewById(R.id.balance);
        balance.setText("0.00\nETH");
        try {
            new Thread(()->{
                String eth = "0.00";
                try {
                    eth = Wallet.getBalance();
                } catch (IOException e) {
                    Toast.makeText(this,"Problem to get the Balance",Toast.LENGTH_SHORT).show();
                }
                String finalEth = eth;
                runOnUiThread(() -> balance.setText(finalEth + "\nETH"));
            }).start();
        } catch (Exception e) {
            Log.d("DashBoard_GetBalance",String.valueOf(e.getMessage()));
            Toast.makeText(this,"Error while fetching the Balance",Toast.LENGTH_SHORT).show();
        }
        send.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, send_eth.class);
            startActivity(intent);
        });
        receive.setOnClickListener(v -> {
            Intent intent=new Intent(dashboard.this,receive_eth.class);
            startActivity(intent);
        });
    }
}
