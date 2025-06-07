package io.github.kolkode.trinetry.ui;
import io.github.kolkode.trinetry.BuildConfig;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EthTransfer {
    private static final String ALCHEMY_API = BuildConfig.ALCHEMY_API;
    private static final String ETHERSCAN_API = BuildConfig.ETHERSCAN_API;
    public static String baseGas,safeGas,proposedGas,fastGas;
    private static final String ALCHEMY_URL = "https://eth-sepolia.g.alchemy.com/v2/"+ALCHEMY_API;
    private static final String ETHERSCAN_URL = "https://api.etherscan.io/api?module=gastracker&action=gasoracle&apikey="+ETHERSCAN_API;

    public void getGas() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ETHERSCAN_URL)
                .get()
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            assert response.body() != null;
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            JSONObject result = json.getJSONObject("result");
            fastGas = result.getString("FastGasPrice");
            proposedGas = result.getString("ProposeGasPrice");
            safeGas = result.getString("SafeGasPrice");
            baseGas = result.getString("suggestBaseFee");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sendEth(String privateKey, String toAddress, BigDecimal amountInEther,String gasInWie) {
        try {
            Web3j web3 = Web3j.build(new HttpService(ALCHEMY_URL));
            Credentials credentials = Credentials.create(privateKey);
            EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
                    credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            BigInteger gasPrice = Convert.toWei(gasInWie, Convert.Unit.GWEI).toBigInteger();
            BigInteger gasLimit = BigInteger.valueOf(21000);
            BigInteger value = Convert.toWei(amountInEther, Convert.Unit.ETHER).toBigInteger();
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce, gasPrice, gasLimit, toAddress, value);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = "0x" + bytesToHex(signedMessage);
            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
            if (ethSendTransaction.hasError()) {
                Log.e("ETH", "Transaction Error: " + ethSendTransaction.getError().getMessage());
            } else {
                String txHash = ethSendTransaction.getTransactionHash();
                Log.d("ETH", "Transaction Hash: " + txHash);
            }
            return ethSendTransaction.getTransactionHash();
        } catch (Exception e) {
            Log.e("ETH", "Transfer failed", e);
            return  "Failed";
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes)
            result.append(String.format("%02x", b));
        return result.toString();
    }

}
