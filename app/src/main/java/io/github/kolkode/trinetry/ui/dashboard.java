package io.github.kolkode.trinetry.ui;

import android.annotation.SuppressLint;
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
    ImageButton send,receive,set_btn,refreshBtn;

    TextView balance;

    @SuppressLint("MissingInflatedId")
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
        set_btn = findViewById(R.id.set_btn);
        refreshBtn = findViewById(R.id.receiveBtn);
        balance = findViewById(R.id.balance);

        refreshBtn.setOnClickListener(v -> refresh());

        set_btn.setOnClickListener(v ->{
            startActivity(new Intent(dashboard.this,dash_setting.class));
        });

        refresh();


//        RecyclerView rvTransactionHistory = findViewById(R.id.rvTransactionHistory);
//        rvTransactionHistory.setLayoutManager(new LinearLayoutManager(this));
//
//// Initialize your list somewhere in the Activity:
//        List<EthTransaction> transactionList = new ArrayList<>();
//
//// Create adapter and set to RecyclerView
//        TransactionAdapter adapter = new TransactionAdapter(transactionList);
//        rvTransactionHistory.setAdapter(adapter);
    }

    private void refresh() {
        balance.setText("0\nETH");
        try {
            new Thread(() -> {
                String eth = "0";
                try {
                    eth = Wallet.getBalance();
                } catch (IOException e) {
                    Toast.makeText(this, "Problem to get the Balance", Toast.LENGTH_SHORT).show();
                }
                String finalEth = eth;
                runOnUiThread(() -> {
                    Wallet.balance = finalEth;
                    balance.setText(finalEth + "\nETH");
                });
            }).start();
        } catch (Exception e) {
            Log.d("DashBoard_GetBalance", String.valueOf(e.getMessage()));
            Toast.makeText(this, "Error while fetching the Balance", Toast.LENGTH_SHORT).show();
        }
        send.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, send_eth.class);
            startActivity(intent);
        });
        receive.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, receive_eth.class);
            startActivity(intent);
        });
    }
}
