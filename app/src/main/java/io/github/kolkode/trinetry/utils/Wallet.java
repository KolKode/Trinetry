package io.github.kolkode.trinetry.utils;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;

import java.security.*;

public class Wallet {
    private static String publicAddress,privateAddress,mnemonics;

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

}
