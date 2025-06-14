package io.github.kolkode.trinetry.database;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSecurityStorage {
    private final Context context;
    private String appPassHash;
    private boolean isDataPresent = false;
    private SharedPreferences sharedPreferences;

    public AppSecurityStorage(Context context) {
        this.context = context;
        getPreference();
        if (isDataPresent)
            getAppPass();
    }

    public boolean isDataPresent() {
        return isDataPresent;
    }

    public String getAppPassHash() {
        return appPassHash;
    }

    public void setAppPassHash(String appPassHash) {
        this.appPassHash = appPassHash;
        setAppPass(appPassHash);
    }

    private void getPreference() {
        sharedPreferences = context.getSharedPreferences("AppLoginData", Context.MODE_PRIVATE);
        this.isDataPresent = sharedPreferences.contains("appPassword");
    }

    private void getAppPass() {
        this.appPassHash = sharedPreferences.getString("appPassword", null);
    }

    private void setAppPass(String appPassHash) {
        sharedPreferences.edit().putString("appPassword", appPassHash).apply();
        isDataPresent = true;
    }
}
