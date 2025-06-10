package io.github.kolkode.trinetry.ui;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import io.github.kolkode.trinetry.R;
import io.github.kolkode.trinetry.database.SecureWalletStorage;
import io.github.kolkode.trinetry.utils.Wallet;

public class splash_screen extends AppCompatActivity {

    Boolean isDataPresent=false;
    private static final long SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            SecureWalletStorage secureWalletStorage = new SecureWalletStorage(getApplicationContext());
            JSONObject walletData = secureWalletStorage.readWallet();
            if (walletData != null) {
                String pub = walletData.getString("publicAddress");
                String pvt = walletData.getString("privateKey");
                String mnemonic = walletData.getString("mnemonic");
                isDataPresent = true;
                Wallet.setPrivateAddress(pvt);
                Wallet.setPublicAddress(pub);
                Wallet.setMnemonics(mnemonic);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Problem to get the wallet data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        new Handler().postDelayed(() -> {
            if (isDataPresent){
                Intent intent = new Intent(splash_screen.this,dashboard.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(splash_screen.this, welcome_page.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}
