package io.github.kolkode.trinetry.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.github.kolkode.trinetry.R;
import io.github.kolkode.trinetry.utils.Wallet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class receive_eth extends AppCompatActivity {

    ImageView qrCodeImageView;
    TextView walletAddressTextView;
    Button copyButton;

    private final String walletAddressFromApi = Wallet.getPublicAddress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_eth);

        // Bind UI elements
        qrCodeImageView = findViewById(R.id.qrCode);
        walletAddressTextView = findViewById(R.id.walletAddress);
        copyButton = findViewById(R.id.copyBtn);

        // ✅ Set values
        walletAddressTextView.setText(walletAddressFromApi);

        //Generate QR code
        generateQRCode(walletAddressFromApi);

        //Copy
        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Wallet Address", walletAddressFromApi);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Wallet address copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateQRCode(String address) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            int size = 512;
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(address, BarcodeFormat.QR_CODE, size, size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "QR code generation failed", Toast.LENGTH_SHORT).show();
        }
    }
}
