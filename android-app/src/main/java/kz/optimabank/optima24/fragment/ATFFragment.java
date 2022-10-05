package kz.optimabank.optima24.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.activity.ProfileActivity;
import kz.optimabank.optima24.fragment.transfer.TransferAccountsFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;

public abstract class ATFFragment extends Fragment {
    protected static final String TAG = "TAG";
    protected OptimaActivity parentActivity;
    protected Gson gson;
    protected int tag = 0;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS_MAP = 2;
    public int REQUEST_ID_MULTIPLE_PERMISSIONS_CALL = 3;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (OptimaActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(retainState());
    }

    protected boolean retainState() {
        return true;
    }

    public boolean isAttached() {
        return getActivity() != null && isAdded();
    }

    protected void hideSoftKeyBoard(Context context){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = ((Activity) context).getCurrentFocus();
        if (view == null)
            return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void onError(String message) {
        try {
            ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
            fragment.show(getFragmentManager(), "error_dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onSuccess(final FragmentActivity activity, String message) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_info)
                .setTitle(R.string.alert_info)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                activity.finish();
                            }
                        }).create().show();
    }

    protected View.OnClickListener toolBarNavigationOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        }
    };

    public static class ErrorDialogFragment extends DialogFragment {

        public static ErrorDialogFragment newInstance(String message) {
            ErrorDialogFragment fragment = new ErrorDialogFragment();

            Bundle args = new Bundle();
            args.putString("message", message);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = getArguments().getString("message");
            final FragmentActivity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setTitle(R.string.alert_error)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.cancel();
                                }
                            })
                    .create();
        }
    }

    public boolean checkPermissionMap() {
        int permissionCoarseLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFineLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            this.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS_MAP);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS_MAP:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (parentActivity != null) {
                        if (GeneralManager.getInstance().getUserLocation() == null) {
                            if (ActivityCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(parentActivity);
                                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(parentActivity, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        Log.i("MAPGMS", "LOCATION = " + location);
                                        if (location != null) {
                                            GeneralManager.getInstance().setUserLocation(location);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                break;
        }
    }

    public boolean chechPermissionCall() {
        int permissionCallPhone = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCallPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            this.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS_CALL);
            return false;
        }
        return true;
    }

    protected int getProdType(UserAccounts from, UserAccounts to, TransferAccountsFragment.CardTransferType cardTransferType) {
        int type = 0;
        if(from!=null && to!=null) {
            if (from.accountType == 2 && to.accountType == 2)
                type = Constants.TransferMoneyInsideClientAccounts;
            else if (from.accountType == 2 && to.accountType == 1)
                type = Constants.TransferMoneyInsideClientCardToAccount;
            else if (from.accountType == 1 && to.accountType == 1)
                type = Constants.TransferMoneyInsideClientAccounts;
            else if (from.accountType == 1 && to.accountType == 3)
                type = Constants.TransferMoneyToDeposit;
            else if (from.accountType == 2 && to.accountType == 3)
                type = Constants.TransferMoneyToDeposit;
            else if (from.accountType == 1 && to.accountType == 2)
                type = Constants.TransferMoneyInsideClientAccountToCard;
            else if (from.accountType == 5 && to.accountType == 5)
                type = Constants.TransferMoneyInsideClientCards;
            else if (from.accountType == 3 && to.accountType == 1) {
                type = Constants.TransferMoneyInsideClientDepositToAccount;
            } else if (from.accountType == 3 && to.accountType == 2) {
                type = Constants.TransferMoneyInsideClientDepositToCard;
            }
        } else if(from!=null) {
            if (from.accountType == 2) {
                type = Constants.TransferMoneyInsideBankFromCardAccountToAccount;
            }
            if (from.accountType == 1) {
                type = Constants.TransferMoneyInsideBankFromAccountToAccount;
            }
            if (cardTransferType == TransferAccountsFragment.CardTransferType.VISA) {
                type = Constants.TransferMoneyVisaToVisa;
            } else if (cardTransferType == TransferAccountsFragment.CardTransferType.MASTERCARD) {
                type = Constants.TransferMoneyMasterToMaster;
            }
        }
        return type;
    }

    public static int getActionBarSize(Context context) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) a.getDimension(0, 0);
        a.recycle();
        return size;
    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
