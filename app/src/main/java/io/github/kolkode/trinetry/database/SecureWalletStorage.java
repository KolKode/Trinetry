package io.github.kolkode.trinetry.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKey;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class SecureWalletStorage {

    private final Context context;
    private final File file;
    private final MasterKey masterKey;

    public SecureWalletStorage(@NonNull Context context) throws Exception {
        this.context = context;
        this.file = new File(context.getFilesDir(), "wallet_data.json");

        this.masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
    }

    // Save the wallet (all three values securely)
    public void saveWallet(String privateKey, String publicAddress, String mnemonic) throws Exception {
        JSONObject json = new JSONObject();
        json.put("privateKey", privateKey);
        json.put("publicAddress", publicAddress);
        json.put("mnemonic", mnemonic);

        byte[] data = json.toString().getBytes(StandardCharsets.UTF_8);

        EncryptedFile encryptedFile = new EncryptedFile.Builder(
                context,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build();

        try (FileOutputStream fos = encryptedFile.openFileOutput()) {
            fos.write(data);
        }
    }

    // Read the wallet (returns a JSONObject)
    public JSONObject readWallet() throws Exception {
        if (!file.exists()) return null;

        EncryptedFile encryptedFile = new EncryptedFile.Builder(
                context,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build();

        try (FileInputStream fis = encryptedFile.openFileInput()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            String content = baos.toString(StandardCharsets.UTF_8);
            return new JSONObject(content);
        }
    }


    // Clear the wallet
    public void clear() {
        if (file.exists()) file.delete();
    }
}
