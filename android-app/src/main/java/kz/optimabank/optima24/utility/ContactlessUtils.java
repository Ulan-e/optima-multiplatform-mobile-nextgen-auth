package kz.optimabank.optima24.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Vibrator;
import android.util.Log;

import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.PaymentResultActivity;


public class ContactlessUtils {
    public static void vibrate(Context context, long duration) {
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(duration);
    }

    public static boolean canPayWithNfc(Context context) {
        NfcManager nfcManager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        if (nfcManager == null) {
            return false;
        }
        NfcAdapter adapter = nfcManager.getDefaultAdapter();
        return adapter != null;
//        boolean def = context.getPackageManager().hasSystemFeature(FEATURE_NFC_HOST_CARD_EMULATION);
//
//        if (!adapter.isEnabled() || !def) {
//            return false;
//        }
    }

    public static boolean nfcIsEnabled(Context context) {
        NfcManager nfcManager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        if (nfcManager != null) {
            NfcAdapter adapter = nfcManager.getDefaultAdapter();
            if (adapter != null) {
                return adapter.isEnabled();
            }
        }

        return false;
    }

    public static void launch(Context context, boolean success) {
        Intent intent = new Intent(context, PaymentResultActivity.class);
        intent.putExtra(PaymentResultActivity.KEY_RESULT, success);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showAlertError(Context context, String errorCode, String errorDescription) {
        android.app.AlertDialog.Builder alertBox = new android.app.AlertDialog.Builder(context);
        alertBox.setTitle(context.getResources().getString(R.string.alert_error));
        Log.d("TAG","call getErrorMessage = " + errorCode);
        alertBox.setCancelable(false);
        alertBox.setMessage(getErrorMessage(context, errorCode));
        alertBox.setPositiveButton(context.getResources().getString(R.string.status_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBox.show();
    }

    public static String getErrorMessage(Context context, String errorCode) {
        switch (errorCode) {
            case "INTERNAL_SERVICE_FAILURE":
                return context.getResources().getString(R.string.INTERNAL_SERVICE_FAILURE);
            case "INVALID_FIELD_VALUE":
                return context.getResources().getString(R.string.INTERNAL_SERVICE_FAILURE);
            case "INVALID_FIELD_LENGTH":
                return context.getResources().getString(R.string.INTERNAL_SERVICE_FAILURE);
            case "INVALID_JSON":
                return context.getResources().getString(R.string.INTERNAL_SERVICE_FAILURE);
            case "MISSING_REQUIRED_FIELD":
                return context.getResources().getString(R.string.INTERNAL_SERVICE_FAILURE);
            case "CRYPTOGRAPHY_ERROR":
                return context.getResources().getString(R.string.CRYPTOGRAPHY_ERROR);
            case "NO_ACTIVE_SESSION":
                return context.getResources().getString(R.string.NO_ACTIVE_SESSION);
            case "RESOURCE_NOT_FOUND":
                return context.getResources().getString(R.string.RESOURCE_NOT_FOUND);
            case "RNS_UNAVAILABLE":
                return context.getResources().getString(R.string.RNS_UNAVAILABLE);
            case "INVALID_WORKFLOW":
                return context.getResources().getString(R.string.INVALID_WORKFLOW);
            case "INVALID_TOKEN_STATUS":
                return context.getResources().getString(R.string.INVALID_TOKEN_STATUS);
            case "INVALID_TASK_ID":
                return context.getResources().getString(R.string.INVALID_TASK_ID);
            case "INVALID_MPA_VERSION":
                return context.getResources().getString(R.string.INVALID_MPA_VERSION);
            case "UNKNOWN_HOST":
                return context.getResources().getString(R.string.UNKNOWN_HOST);
            case "REGISTRATION_IN_PROGRESS":
                return context.getResources().getString(R.string.REGISTRATION_IN_PROGRESS);
            case "REGISTRATION_EXPIRED":
                return context.getResources().getString(R.string.REGISTRATION_EXPIRED);
            case "DUPLICATE_PAYMENT_APP_INSTANCE_ID":
                return context.getResources().getString(R.string.DUPLICATE_PAYMENT_APP_INSTANCE_ID);
            case "VTS_COMMUNICATION_ERROR":
                return context.getResources().getString(R.string.VTS_COMMUNICATION_ERROR);
            case "VTS_TEMP_ERROR":
                return context.getResources().getString(R.string.VTS_TEMP_ERROR);
            case "VTS_INVALID_PARAMETERS":
                return context.getResources().getString(R.string.VTS_INVALID_PARAMETERS);
            case "VTS_OPERATIONS_NOT_ALLOWED":
                return context.getResources().getString(R.string.VTS_OPERATIONS_NOT_ALLOWED);
            case "VTS_SERVICE_ERROR":
                return context.getResources().getString(R.string.VTS_SERVICE_ERROR);
            case "VTS_INVALID_CARD":
                return context.getResources().getString(R.string.VTS_INVALID_CARD);
            case "VTS_ERROR":
                return context.getResources().getString(R.string.VTS_ERROR);
            case "UNKNOWN_CRDPRODUCT":
                return context.getResources().getString(R.string.UNKNOWN_CRDPRODUCT);
            case "NO_CARDS_AVAILABLE":
                return context.getResources().getString(R.string.NO_CARDS_AVAILABLE);
            case "INVALID_TOKEN_UNIQUE_REFERENCE":
                return context.getResources().getString(R.string.INVALID_TOKEN_UNIQUE_REFERENCE);
            case "INVALID_ELIGIBILITY_RECEIPT":
                return context.getResources().getString(R.string.INVALID_ELIGIBILITY_RECEIPT);
            case "INVALID_TERMS_AND_CONDITIONS":
                return context.getResources().getString(R.string.INVALID_TERMS_AND_CONDITIONS);
            case "PAN_ALREADY_DIGITIZED":
                return context.getResources().getString(R.string.PAN_ALREADY_DIGITIZED);
            default:
                return context.getResources().getString(R.string.internet_connection_error);
        }
    }
}
