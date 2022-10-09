package kz.optimabank.optima24.receipt.ui;

import static kz.optimabank.optima24.utility.CardNumberUtils.getMaskedCardNumber;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_MODEL;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_TYPE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;

import kg.optima.mobile.R;
import kg.optima.mobile.databinding.ActivityReceiptBinding;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.model.ReceiptType;
import kz.optimabank.optima24.receipt.model.ReceiptModel;
import kz.optimabank.optima24.utility.ShareUtility;


/**
 * This class generates receipt on operation based on existed data
 */
public class ReceiptActivity extends OptimaActivity {

    private static final int DELAY = 200;

    private ActivityReceiptBinding binding;
    private ReceiptModel receiptModel;
    private ReceiptType receiptType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarView.setNavigationOnClickListener(v -> finish());

        getArgs();
        setReceiptDetails();
        shareScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void shareScreen() {
        binding.btnShare.setOnClickListener(view -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Bitmap screen = convertViewToBitmap(binding.scrollViewReceiptDetails);
            ShareUtility.shareBitmap(this, screen);
        }, DELAY));
    }

    // конвертируем view в Bitmap
    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void getArgs() {
        Intent intentData = getIntent();
        if (intentData != null) {
            receiptModel = (ReceiptModel) intentData.getParcelableExtra(RECEIPT_MODEL);
            receiptType = (ReceiptType) intentData.getSerializableExtra(RECEIPT_TYPE);
        }
    }

    private void setReceiptDetails() {
        if (receiptType != null) {
            switch (receiptType) {
                case TRANSFER: {
                    showTransferReceipt();
                    break;
                }
                case PAYMENT: {
                    showPaymentReceipt();
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void showTransferReceipt() {
        if (receiptModel != null) {
            binding.receiptDetails.textOperationTime.setText(receiptModel.getOperationTime());
            binding.receiptDetails.textReceiptNumber.setText(receiptModel.getReceiptNumber());
            binding.receiptDetails.textSenderCardNumber.setText(receiptModel.getSendCardNumber());
            String recipientName = receiptModel.getRecipientName();

            if (recipientName == null || recipientName.equals("")) {
                binding.receiptDetails.titleRecipientName.setVisibility(View.GONE);
                binding.receiptDetails.textRecipientName.setVisibility(View.GONE);
            } else {
                binding.receiptDetails.textRecipientName.setText(recipientName);
            }

            if(receiptModel.getType() == 1){
                binding.receiptDetails.titleRecipientCardNumber.setText(
                        getResources().getString(R.string.phone_number_requesites)
                );
            }

            String maskedCardNumber = getMaskedCardNumber(receiptModel.getRecipientCardNumber());
            binding.receiptDetails.textRecipientCardNumber.setText(maskedCardNumber);
            binding.receiptDetails.textAmount.setText(receiptModel.getAmount());
            binding.receiptDetails.textFee.setText(receiptModel.getFee());

            binding.receiptDetails.sealLayout.sealText.setText(receiptModel.getOperationStatus());
        }
    }

    private void showPaymentReceipt() {
        if (receiptModel != null) {
            binding.receiptDetails.textOperationTime.setText(receiptModel.getOperationTime());
            binding.receiptDetails.textReceiptNumber.setText(receiptModel.getReceiptNumber());
            binding.receiptDetails.textSenderCardNumber.setText(receiptModel.getSendCardNumber());
            binding.receiptDetails.titleRecipientName.setText(getResources().getString(R.string.service_name));
            binding.receiptDetails.textRecipientName.setText(receiptModel.getServiceName());
            binding.receiptDetails.titleRecipientCardNumber.setText(getResources().getString(R.string.account_number));
            binding.receiptDetails.textRecipientCardNumber.setText(receiptModel.getAccountNumber());

            binding.receiptDetails.titleAmount.setText(getResources().getString(R.string.amount));
            binding.receiptDetails.textAmount.setText(receiptModel.getFee());
            binding.receiptDetails.titleFee.setText(getResources().getString(R.string.amount_including_commission));
            binding.receiptDetails.textFee.setText(receiptModel.getAmount());
            binding.receiptDetails.sealLayout.sealText.setText(receiptModel.getOperationStatus());
        }
    }
}