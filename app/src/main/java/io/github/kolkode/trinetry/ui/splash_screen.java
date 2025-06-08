package io.github.kolkode.trinetry.ui;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import io.github.kolkode.trinetry.R;
import io.github.kolkode.trinetry.utils.Wallet;
import io.github.kolkode.trinetry.utils.WalletManager;

public class splash_screen extends AppCompatActivity {

    WalletManager walletManager = null;
    Boolean isDataPresent=false;
    private static final long SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            JSONObject wallet = walletManager.loadWalletData();
            if (wallet != null) {
                String pub = wallet.getString("publicAddress");
                String pvt = wallet.getString("privateKey");
                String mnemonic = wallet.getString("mnemonic");
                isDataPresent = true;
                Wallet.setPrivateAddress(pvt);
                Wallet.setPublicAddress(pub);
                Wallet.setMnemonics(mnemonic);
            }
        } catch (Exception e) {
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
