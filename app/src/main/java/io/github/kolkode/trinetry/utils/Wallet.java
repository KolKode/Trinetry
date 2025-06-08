package io.github.kolkode.trinetry.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.github.kolkode.trinetry.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Wallet {
    private static final String ALCHEMY_API = BuildConfig.ALCHEMY_API;
    private static final String ALCHEMY_URL = "https://eth-sepolia.g.alchemy.com/v2/"+ALCHEMY_API;
    private static final String ETHERSCAN_API = BuildConfig.ETHERSCAN_API;
    private static final String ETHERSCAN_URL = "https://api.etherscan.io/api?module=gastracker&action=gasoracle&apikey="+ETHERSCAN_API;
    private static String publicAddress,privateAddress,mnemonics;
    public static String balance;

    public static List<JSONObject> transactionHistory = new CopyOnWriteArrayList<>();
    public static List<JSONObject> getTransactionHistory() {
        return transactionHistory;
    }

    public static String getPublicAddress() {
        return publicAddress;
    }

    public static void setPublicAddress(String publicAddress) {
        Wallet.publicAddress = publicAddress;
    }

    public static String getPrivateAddress() {
        return privateAddress;
    }

    public static void setPrivateAddress(String privateAddress) {
        Wallet.privateAddress = privateAddress;
    }

    public static String getMnemonics() {
        return mnemonics;
    }

    public static void setMnemonics(String mnemonics) {
        Wallet.mnemonics = mnemonics;
    }

    private static final int[] derivationPath = {
            44 | Bip32ECKeyPair.HARDENED_BIT,
            60 | Bip32ECKeyPair.HARDENED_BIT,
            Bip32ECKeyPair.HARDENED_BIT,
            0,
            0
    };

    public static boolean isValidAddress(String address) {
        return WalletUtils.isValidAddress(address);
    }
    public static boolean isValidPrivate(String privateAddr){
        return WalletUtils.isValidPrivateKey(privateAddr);
    }
    public static void getKeys(String privateaddr) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        privateAddress = privateaddr;
        Credentials credentials = Credentials.create(privateaddr);
        publicAddress = credentials.getAddress();
    }
    public static void getKeyPairFromSeed(String memonics) {
        byte[] seed = MnemonicUtils.generateSeed(memonics, "");
        Bip32ECKeyPair masKeyPair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masKeyPair, derivationPath);
        Credentials credentials = Credentials.create(derivedKeyPair);
        mnemonics = memonics;
        publicAddress = credentials.getAddress();
        privateAddress = "0x"+credentials.getEcKeyPair().getPrivateKey().toString(16);
    }

    public static void generateKeyPairWithSeed() {
        byte[] entropy = new byte[16];
        new SecureRandom().nextBytes(entropy);
        String mne = MnemonicUtils.generateMnemonic(entropy);
        byte[] seed = MnemonicUtils.generateSeed(mne, "");
        Bip32ECKeyPair masKeyPair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masKeyPair, derivationPath);
        Credentials credentials = Credentials.create(derivedKeyPair);
        publicAddress = credentials.getAddress();
        privateAddress = credentials.getEcKeyPair().getPrivateKey().toString(16);
        mnemonics = mne;
    }

    public static void fetchTransactionHistory() {
        String address = publicAddress;
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("https://api-sepolia.etherscan.io/api").newBuilder()
                    .addQueryParameter("module", "account")
                    .addQueryParameter("action", "txlist")
                    .addQueryParameter("address", address)
                    .addQueryParameter("startblock", "0")
                    .addQueryParameter("endblock", "99999999")
                    .addQueryParameter("sort", "desc")
                    .addQueryParameter("apikey",BuildConfig.ETHERSCAN_API)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String resBody = response.body().string();
                    JSONObject json = new JSONObject(resBody);
                    JSONArray resultArray = json.getJSONArray("result");

                    transactionHistory.clear(); // reset
                    for (int i = 0; i < resultArray.length(); i++) {
                        transactionHistory.add(resultArray.getJSONObject(i));
                    }

                    Log.d("TX_HISTORY", "Fetched " + transactionHistory.size() + " transactions.");
                } else {
                    Log.e("TX_HISTORY", "HTTP Error: " + response.code());
                }
            } catch (Exception e) {
                Log.e("TX_HISTORY", "Error fetching history", e);
            }
        }).start();
    }


    public static String getBalance() throws IOException {
        String address = publicAddress;
        Web3j web3j = Web3j.build(new HttpService(ALCHEMY_URL));
        BigInteger wie = null;
        try {
            wie = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();

        } catch (IOException e) {
            Log.d("BalanceError",String.valueOf(e.getMessage()));
        }
        assert wie != null;
        return String.valueOf(Convert.fromWei(wie.toString(), Convert.Unit.ETHER));
    }
}
