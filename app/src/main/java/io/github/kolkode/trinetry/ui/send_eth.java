package io.github.kolkode.trinetry.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.kolkode.trinetry.R;
import io.github.kolkode.trinetry.databinding.ActivitySendEthBinding;
import io.github.kolkode.trinetry.utils.EthTransfer;
import io.github.kolkode.trinetry.utils.Wallet;

public class send_eth extends AppCompatActivity {
    EthTransfer ethTransfer = new EthTransfer();
    TextView myAddress,baseGas,safeGas,proposedGas,fastGas;
    EditText gasPrice,sendingAmmount,sendingAddress;
    Button transferButton;

    private ActivitySendEthBinding binding;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                showCamera();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });
    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher =
        registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                setResult(result.getContents());
            }
        });
    private void setResult(String contents) {
        binding.sendingAddress.setText(extractEthereumAddress(contents));
    }

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan QR code");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(true);
        qrCodeLauncher.launch(options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendEthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.scanbtn.setOnClickListener(view -> {
            checkPermissionAndShowCamera();
        });
        myAddress=findViewById(R.id.myAddress);
        baseGas=findViewById(R.id.baseGas);
        safeGas=findViewById(R.id.safeGas);
        proposedGas=findViewById(R.id.proposedGas);
        fastGas=findViewById(R.id.fastGas);
        gasPrice=findViewById(R.id.gasPrice);
        sendingAddress=findViewById(R.id.sendingAddress);
        sendingAmmount=findViewById(R.id.sendingAmmount);
        transferButton=findViewById(R.id.transferBtn);

        myAddress.setText(Wallet.getPublicAddress());
        new Thread(()->{
            try {
                ethTransfer.getGas();
            } catch (IOException e) {
                Toast.makeText(this,"Problem to fetch the gas fee(transaction cost)",Toast.LENGTH_SHORT).show();
            }
            runOnUiThread(()->{
                baseGas.setText(EthTransfer.getBaseGas()+" GWEI");
                safeGas.setText(EthTransfer.getSafeGas()+" GWEI");
                proposedGas.setText(EthTransfer.getProposedGas()+" GWEI");
                fastGas.setText(EthTransfer.getFastGas()+" GWEI");
                gasPrice.setText(EthTransfer.getProposedGas());
            });
        }).start();

        transferButton.setOnClickListener(v -> {
            String sAdd = sendingAddress.getText().toString().trim();
            String gP = gasPrice.getText().toString().trim();
            String sA = sendingAmmount.getText().toString().trim();
            if (gP.isEmpty()) {
                Toast.makeText(this, "Please enter the gas price", Toast.LENGTH_LONG).show();
            } else if (!isPositiveDecimal(gP)) {
                Toast.makeText(this, "Please input a valid gas price", Toast.LENGTH_LONG).show();
            } else if (sA.isEmpty()) {
                Toast.makeText(this, "Please input an amount to send", Toast.LENGTH_LONG).show();
            } else if (!isPositiveDecimal(sA)) {
                Toast.makeText(this, "Please input a valid amount", Toast.LENGTH_LONG).show();
            } else if (sAdd.isEmpty()) {
                Toast.makeText(this, "Please enter a sending address", Toast.LENGTH_LONG).show();
            } else if (!Wallet.isValidAddress(sAdd)) {
                Toast.makeText(this, "The address is not valid.\nPlease provide a valid address.", Toast.LENGTH_LONG).show();
            } else {
                try {
//                    BigDecimal totalCost = Convert.toWei(sA, Convert.Unit.ETHER).add(Convert.toWei(gP, Convert.Unit.GWEI));
//                    BigDecimal walletBalance = Convert.toWei(Wallet.balance, Convert.Unit.GWEI);
//                    if (totalCost.compareTo(walletBalance) > 0) {
//                        Toast.makeText(this, "Insufficient balance to cover gas + amount", Toast.LENGTH_SHORT).show();
//                    } else {
                    new Thread(()->{
                        EthTransfer.sendEth(sAdd, new BigDecimal(sA), gP);
                    }).start();
//                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid input or internal error", Toast.LENGTH_SHORT).show();
                    Log.e("TransferError", "Transfer logic failed", e);
                }
            }
        });
    }

    private String extractEthereumAddress(String input) {
        Pattern pattern = Pattern.compile("0x[a-fA-F0-9]{40}");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public boolean isPositiveDecimal(String input) {
        try {
            BigDecimal value = new BigDecimal(input.trim());
            return value.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void checkPermissionAndShowCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
}
