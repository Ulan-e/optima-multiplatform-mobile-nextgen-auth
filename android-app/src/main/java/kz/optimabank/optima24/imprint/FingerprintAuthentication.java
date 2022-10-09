package kz.optimabank.optima24.imprint;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import kg.optima.mobile.R;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Utilities.getPreferences;

public class FingerprintAuthentication {
    private KeyguardManager mKeyguardManager;
    private FingerprintManager mFingerprintManager;
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private Cipher mCipher;
    private final SharedPreferences mSharedPreferences;
    private final Context context;
    private final String KEY_NAME = "my_key";

    public FingerprintAuthentication(Context context) {
        this.context = context;
        mSharedPreferences = getPreferences(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mKeyguardManager = context.getSystemService(KeyguardManager.class);
            mFingerprintManager = context.getSystemService(FingerprintManager.class);
            try {
                mKeyStore = KeyStore.getInstance("AndroidKeyStore");
                mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                mCipher =  Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("InflateParams")
    @TargetApi(Build.VERSION_CODES.M)
    public boolean imprintAuthentication(final CheckBox cbUseImprint) {

        if (!mSharedPreferences.getBoolean(context.getString(R.string.use_fingerprint_to_authenticate_key), false)) {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            if (cbUseImprint != null) {
                cbUseImprint.setVisibility(View.VISIBLE);
            }

            if (!mKeyguardManager.isKeyguardSecure()) {
                if (cbUseImprint != null) {
                    cbUseImprint.setClickable(false);
                }
                return false;
            }

            if (!mFingerprintManager.hasEnrolledFingerprints()) {
                if (cbUseImprint != null) {
                    cbUseImprint.setClickable(false);
                }
            }
            return false;
        } else {

            if (!mKeyguardManager.isKeyguardSecure()) {
                if (cbUseImprint != null) {
                    cbUseImprint.setVisibility(View.VISIBLE);
                    cbUseImprint.setClickable(false);
                }
                return false;
            }

            if (!mFingerprintManager.hasEnrolledFingerprints()) {
                if (cbUseImprint != null) {
                    cbUseImprint.setVisibility(View.VISIBLE);
                    cbUseImprint.setClickable(false);
                }
                return false;
            }

            createKey();
            return initCipher();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        if(mKeyStore!=null&&mKeyGenerator!=null) {
            try {
                mKeyStore.load(null);
                // Set the alias of the entry in Android KeyStore where the key will appear
                // and the constrains (purposes) in the constructor of the Builder
                mKeyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        // Require the user to authenticate with a fingerprint to authorize every use
                        // of the key
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
                mKeyGenerator.generateKey();
            } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                    | CertificateException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean initCipher() {
        if(mKeyStore==null||mCipher==null) {
            Log.d(Constants.TAG, "mKeyStore or mCipher = null");
            return false;
        }
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }

    public FingerprintUiHelper.FingerprintUiHelperBuilder getFingerprintUiHelperBuilder() {
        return new FingerprintUiHelper.FingerprintUiHelperBuilder(mFingerprintManager);
    }
}
