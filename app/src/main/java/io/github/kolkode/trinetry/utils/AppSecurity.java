package io.github.kolkode.trinetry.utils;

import android.content.Context;

import java.security.MessageDigest;

import io.github.kolkode.trinetry.database.AppSecurityStorage;

public class AppSecurity {
    private final AppSecurityStorage securityStorage;
    private boolean isDataPresent = false;
    private String appPass;
    public AppSecurity(Context context) {
        securityStorage = new AppSecurityStorage(context);
        this.isDataPresent = securityStorage.isDataPresent();
        if (this.isDataPresent) {
            this.appPass = securityStorage.getAppPassHash();
        }
    }

    public static String convertToHash(String pass) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(pass.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash)
                hexString.append(String.format("%02x", b));
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean check(String given) {
        String passHash = convertToHash(given);
        return passHash.equals(appPass);
    }

    public void savePass(String given) {
        String passHash = convertToHash(given);
        securityStorage.setAppPassHash(passHash);
    }
}
