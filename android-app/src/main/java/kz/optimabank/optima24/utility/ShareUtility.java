package kz.optimabank.optima24.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.uttampanchasara.pdfgenerator.CreatePdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import kg.optima.mobile.BuildConfig;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.gson.response.BankRequisitesResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;

public final class ShareUtility {

    private static final String TAG = "ShareUtilityTag";
    private static final String IMAGE_NAME = "optima-receipt.png";
    private static final String IMAGE_FOLDER = "images";
   private static final String FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

    public static void shareViaText(Context context, String textBody) {
        Intent shareIntent = new Intent();
        shareIntent.setType("text/plain");
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, textBody);
        context.startActivity(shareIntent);
    }

    // метод для отправки изображения
    public static void shareBitmap(Context context, Bitmap bitmap) {
        saveBitmap(context, bitmap);
        Uri contentUri = getBitmapUri(context);
        shareBitmapViaIntent(context, contentUri);
    }

    // сохраняем файл в кэш
    private static void saveBitmap(Context context, Bitmap bitmap) {
        try {
            File cacheFile = new File(context.getCacheDir(), IMAGE_FOLDER);
            cacheFile.mkdirs();
            FileOutputStream stream = new FileOutputStream(cacheFile + "/" + IMAGE_NAME);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (IOException exception) {
            Log.e(TAG, "Error saving file to FileProvider " + exception.getLocalizedMessage());
        }
    }

    // получаем файл из кэша
    private static Uri getBitmapUri(Context context) {
        File imagePath = new File(context.getCacheDir(), IMAGE_FOLDER);
        File newFile = new File(imagePath, IMAGE_NAME);
        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, newFile);
    }

    // отправка файла через Intent
    private static void shareBitmapViaIntent(Context context, Uri contentUri) {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    public static void exportToPdfFromHtml(Context context, BankRequisitesResponse requisitesResponse, UserAccounts userAccounts, Boolean isCardRequisites, String currency) {
        StringBuilder text = new StringBuilder();
        String html;
        //формирование html для PDF
        if (isCardRequisites) {
            Log.d("ShareUtility", "is Card not national");
            UserAccounts.Cards currentCard = (UserAccounts.Cards) userAccounts;
            if (requisitesResponse.getData().getRequisitesSwiftTransfer() != null && requisitesResponse.getData().getRequisitesSwiftTransfer().size() != 0) {
                text.append("<table>");
                text.append("<tr>");
                text.append("<th>CURRENCY</th>");
                text.append("<th>:56A INTERMEDIARY BANK:</th>");
                text.append("<th>:57A BENEFICIARY BANK:</th>");
                text.append("<th>:59 BENEFICIARY:</th>");
                text.append("<th>:70 DETAILS OF PAYMENT:</th>");
                text.append("</tr>");
                for (BankRequisitesResponse.Data.RequisitesSwiftTransfer swiftData : requisitesResponse.getData().getRequisitesSwiftTransfer()) {
                    String beneficiary = swiftData.getBeneficiary();
                    beneficiary = beneficiary.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                    beneficiary = beneficiary.replace("<#BankAccount>", requisitesResponse.getData().getAccount());
                    beneficiary = beneficiary.replace("<#UserAccount>", GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number);
                    beneficiary = beneficiary.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                    String detailsOfPayment = swiftData.getDetailOfPayments();
                    detailsOfPayment = detailsOfPayment.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                    detailsOfPayment = detailsOfPayment.replace("<#BankAccount>", requisitesResponse.getData().getAccount());
                    detailsOfPayment = detailsOfPayment.replace("<#UserAccount>", GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number);
                    detailsOfPayment = detailsOfPayment.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                    text.append("<tr>");
                    text.append("<td><b>").append(currency).append("</b></td>");
                    text.append("<td>").append(swiftData.getIntermediaryBank()).append("</td>");
                    text.append("<td>").append(swiftData.getBeneficiaryBank()).append("</td>");
                    text.append("<td>").append(beneficiary).append("</td>");
                    text.append("<td>").append(detailsOfPayment).append("</td>");
                    text.append("</tr>");
                }
                text.append("</table>");
            } else {
                Log.d("ShareUtility", "is Card national");
                text.append("<table>");
                text.append("<tr>");
                text.append("<th>Валюта</th>");
                text.append("<th>Банк получатель</th>");
                text.append("<th>Получатель</th>");
                text.append("<th>Назначение платежа</th>");
                text.append("</tr>");
                text.append("<tr>");
                text.append("<td><br>").append(currentCard.currency).append("</b></td>");
                text.append("<td>").append(context.getString(R.string.bank_of_name)).append("\nАдрес:").append(requisitesResponse.getData().getAddresss()).append("\nБИК:").append(requisitesResponse.getData().getBic()).append("</td>");
                text.append("<td>").append(GeneralManager.getInstance().getUser().fullName).append("\nБанковский счет:").append(requisitesResponse.getData().getAccount()).append("\nСчет клиента:").append(GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number).append("</td>");
                text.append("<td>" + "Назначение платежа (за что, номер инвойса, контракта, дата)" + "\nСчет клиента:").append(GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number).append("</td>");
                text.append("</tr>");
                text.append("</table>");
            }
        } else {
            if (requisitesResponse.getData().getRequisitesSwiftTransfer() != null && requisitesResponse.getData().getRequisitesSwiftTransfer().size() != 0) {
                Log.d("ShareUtility", "is not Card not national");
                text.append("<table>");
                text.append("<tr>");
                text.append("<th>CURRENCY</th>");
                text.append("<th>:56A INTERMEDIARY BANK:</th>");
                text.append("<th>:57A BENEFICIARY BANK:</th>");
                text.append("<th>:59 BENEFICIARY:</th>");
                text.append("<th>:70 DETAILS OF PAYMENT:</th>");
                text.append("</tr>");
                for (BankRequisitesResponse.Data.RequisitesSwiftTransfer swiftData : requisitesResponse.getData().getRequisitesSwiftTransfer()) {
                    String beneficiary = swiftData.getBeneficiary();
                    beneficiary = beneficiary.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                    beneficiary = beneficiary.replace("<#BankAccount>", requisitesResponse.getData().getAccount());
                    beneficiary = beneficiary.replace("<#UserAccount>", userAccounts.number);
                    beneficiary = beneficiary.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                    String detailsOfPayment = swiftData.getDetailOfPayments();
                    detailsOfPayment = detailsOfPayment.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                    detailsOfPayment = detailsOfPayment.replace("<#BankAccount>", requisitesResponse.getData().getAccount());
                    detailsOfPayment = detailsOfPayment.replace("<#UserAccount>", userAccounts.number);
                    detailsOfPayment = detailsOfPayment.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                    text.append("<tr>");
                    text.append("<td><b>").append(currency).append("</b></td>");
                    text.append("<td>").append(swiftData.getIntermediaryBank()).append("</td>");
                    text.append("<td>").append(swiftData.getBeneficiaryBank()).append("</td>");
                    text.append("<td>").append(beneficiary).append("</td>");
                    text.append("<td>").append(detailsOfPayment).append("</td>");
                    text.append("</tr>");
                }
                text.append("</table>");
            } else {
                Log.d("ShareUtility", "is not Card national");
                text.append("<table>");
                text.append("<tr>");
                text.append("<th>Валюта</th>");
                text.append("<th>Банк получатель</th>");
                text.append("<th>Получатель</th>");
                text.append("<th>Назначение платежа</th>");
                text.append("</tr>");
                text.append("<tr>");
                text.append("<td><br>").append(userAccounts.currency).append("</b></td>");
                text.append("<td>" + "").append(context.getString(R.string.bank_of_name)).append("\nАдрес:").append(requisitesResponse.getData().getAddresss()).append("\nБИК:").append(requisitesResponse.getData().getBic()).append("</td>");
                text.append("<td>").append(GeneralManager.getInstance().getUser().fullName).append("\nБанковский счет:").append(requisitesResponse.getData().getAccount()).append("\nСчет клиента:").append(userAccounts.number).append("</td>");
                text.append("<td>" + "Назначение платежа (за что, номер инвойса, контракта, дата)" + "\nСчет клиента:").append(userAccounts.number).append("</td>");
                text.append("</tr>");
                text.append("</table>");
            }
        }
        html = text.toString();
        String fileName = "Optima 24" + Calendar.getInstance().getTime();
        new CreatePdf(context)
                .setPdfName(fileName)
                .openPrintDialog(false)
                .setContent(html.replace("<td>", "<td style='border-bottom: 1px dotted #434343;'>"))
                .setPageSize(new PrintAttributes.MediaSize("ISO_A4_ALBUM", "android", 11690, 11690))
                .setFilePath(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath() + "Optima 24")
                .setCallbackListener(new CreatePdf.PdfCallbackListener() {
                    @Override
                    public void onFailure(String s) {
                        Toast.makeText(context, "Ошибка экспорта" + s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String s) {
                        Uri fileUri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, new File(s));
                        try {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                            sendIntent.setType("text/plain");
                            context.startActivity(sendIntent);
                        } catch (Exception e) {
                            Log.e("ShareUtility", e.getMessage() + " ");
                        }
                    }
                }).create();
    }
}
