package io.github.kolkode.trinetry.utils;

import android.content.Context;

import org.json.JSONObject;

import io.github.kolkode.trinetry.database.SecureWalletStorage;

public class WalletManager {
    private final SecureWalletStorage storage;

    public WalletManager(Context context) throws Exception {
        this.storage = new SecureWalletStorage(context);
    }

    public void saveWalletData(String privateKey, String publicAddress, String mnemonic) throws Exception {
        storage.saveWallet(privateKey, publicAddress, mnemonic);
    }

    public JSONObject loadWalletData() throws Exception {
        return storage.readWallet();
    }

    public void clearWallet() {
        storage.clear();
    }
}
