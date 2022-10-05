package kz.optimabank.optima24.utility.crypt;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
  Created by Timur on 13.01.2017.
 */

public final class CryptoUtils {

    private static final String SHA256 = "SHA-256";
    private static final String UTF8 = "UTF-8";

    private CryptoUtils() {
    }

//    public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
//        Signature privateSignature = Signature.getInstance("SHA256withRSA");
//        privateSignature.initSign(privateKey);
//        privateSignature.update(data);
//        return privateSignature.sign();
//    }
//
//    public static PrivateKey getPemPrivateKey(Context context, String filename, String algorithm) throws Exception {
//        InputStream is = context.getAssets().open(filename);
//        byte[] keyBytes = new byte[is.available()];
//        is.read(keyBytes);
//        is.close();
//        String temp = new String(keyBytes);
//        String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----", "");
//        privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
//        Log.d(CryptoUtils.class.getSimpleName(), "Private key\n" + privKeyPEM);
//        byte[] decoded = Base64.decode(privKeyPEM, Base64.DEFAULT);
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
//        KeyFactory kf = KeyFactory.getInstance(algorithm);
//        return kf.generatePrivate(spec);
//    }

    public static String getSHA1(String text) {
        MessageDigest sha1Digest;
        try {
            sha1Digest = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            return null;
        }
        sha1Digest.reset();
        sha1Digest.update(text.getBytes());
        byte[] sha1hash = sha1Digest.digest();
        return Base64.encodeToString(sha1hash, Base64.DEFAULT);
    }

    public static String decryptMessage(String message, byte[] key) {
        return Blowfish.getInstance().deCryptStr(message, key);
    }

    public static String generateToken(String device_token, String device_secret, long timestamp, String salt) throws Exception {
        String hash = device_token + ":" + timestamp;
        String hashLeft = "", hashRight = "";

        hashLeft = encode(getPasswordHash(device_secret, salt), hash);
        Log.d("TAG", "hashLeft = " + hashLeft);

        hashRight = device_token + ":" + timestamp;
        Log.d("TAG", "hashRight = " + hashRight);

        String toHash = hashLeft + ":" + hashRight;
        Log.d("TAG", "toHash = " + toHash);

        Log.d("TAG", "Base64.encodeToString(toHash) = " + Base64.encodeToString(toHash.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP));
        return Base64.encodeToString(toHash.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
    }

    private static String getPasswordHash(String device_secret, String salt) throws Exception {
        String key = device_secret + ":" + salt;
        return encode(salt, key);
    }

    private static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Base64.encodeToString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)), Base64.NO_WRAP);
    }

    public static String getHash(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA256);
            byte[] hash = digest.digest(str.getBytes(UTF8));
            String def = Base64.encodeToString(hash, Base64.DEFAULT);
            return def.replaceAll("\n", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
