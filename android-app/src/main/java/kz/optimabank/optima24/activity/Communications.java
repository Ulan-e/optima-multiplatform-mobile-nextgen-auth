package kz.optimabank.optima24.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;

/**
 * Created by Max on 12.09.2017.
 */

public class Communications extends OptimaActivity implements View.OnClickListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.linMobile) RelativeLayout linMobile;
    //@BindView(R.id.linTeleph) RelativeLayout linTeleph;
    @BindView(R.id.linTelephAround) RelativeLayout linTelephAround;
    @BindView(R.id.linFreeStacionar) RelativeLayout linFreeStacionar;
    //@BindView(R.id.linFB) RelativeLayout linFB;
    //@BindView(R.id.linTW) RelativeLayout linTW;
    //@BindView(R.id.linSkype) RelativeLayout linSkype;
    //@BindView(R.id.linINS) RelativeLayout linINS;
    @BindView(R.id.linMessage) RelativeLayout linMessage;
    //@BindView(R.id.linWhatsApp) RelativeLayout linWhatsApp;

    /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    RelativeLayout linMobile = (RelativeLayout) findViewById(R.id.linMobile);
    RelativeLayout linTeleph = (RelativeLayout) findViewById(R.id.linTeleph);
    RelativeLayout linTelephAround = (RelativeLayout) findViewById(R.id.linTelephAround);
    RelativeLayout linFB = (RelativeLayout) findViewById(R.id.linFB);
    RelativeLayout linTW = (RelativeLayout) findViewById(R.id.linTW);
    RelativeLayout linSkype = (RelativeLayout) findViewById(R.id.linSkype);
    RelativeLayout linINS = (RelativeLayout) findViewById(R.id.linINS);
    RelativeLayout linMessage = (RelativeLayout) findViewById(R.id.linMessage);*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_communication, null, false);
        setContentView(view);

        ButterKnife.bind(this, view);

        initTiilBar();

        linMobile.setOnClickListener(this);
        //linTeleph.setOnClickListener(this);
        linTelephAround.setOnClickListener(this);
        //linFB.setOnClickListener(this);
        //linTW.setOnClickListener(this);
        //linINS.setOnClickListener(this);
        //linWhatsApp.setOnClickListener(this);
        linMessage.setOnClickListener(this);
        linFreeStacionar.setOnClickListener(this);
        //linSkype.setOnClickListener(this);

        super.onCreate(savedInstanceState);
    }

    private void initTiilBar() {
        toolbar.setTitle("");
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.linMobile:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9595"));
                startActivity(intent);
                break;
           /* case R.id.linTeleph:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:88000800283"));
                startActivity(intent);
                break;*/
            case R.id.linTelephAround:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+996 (312) 90-59-59"));
                startActivity(intent);
                break;
//            case R.id.linFreeStacionar:
//                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0-800-800-00-00"));
//                startActivity(intent);
//                break;
            /*case R.id.linFB:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/atf.online"));
                startActivity(intent);
                break;
            case R.id.linTW:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ATFBank_kz"));
                startActivity(intent);
                break;
            case R.id.linINS:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/atfbank/"));
                startActivity(intent);
                break;
            case R.id.linSkype:

                String SKYPE_PATH_GENERAL = "com.skype.raider";
                String SKYPE_PATH_OLD = "com.skype.raider.contactsync.ContactSkypeOutCallStartActivity";
                String SKYPE_PATH_NEW = "com.skype.raider.Main";

                Intent skypeIntent = new Intent().setAction("android.intent.action.VIEW");
                skypeIntent.setData(Uri.parse("skype:ATF-Info?call"));//skype:ATF-Info?call

                if (isIntentAvailable(skypeIntent.setClassName(SKYPE_PATH_GENERAL, SKYPE_PATH_NEW))) {
                    startActivity(skypeIntent);
                } else if (isIntentAvailable(skypeIntent.setClassName(SKYPE_PATH_GENERAL, SKYPE_PATH_OLD))) {
                    startActivity(skypeIntent);
                } else {
                    buildAlert("com.skype.raider",R.string.whatsapp_download);
                    //Toast.makeText(this, getString(R.string.exception_skype), Toast.LENGTH_LONG).show();
                }

                break;*/
            case R.id.linMessage:
                try {
                    if (appInstalledOrNot("com.whatsapp")) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=996990905959"));
                        if (isIntentAvailable(intent)) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, getString(R.string.exception_mail), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        buildAlert("com.whatsapp",R.string.whatsapp_download);
                    }
                }catch (Exception e){
                    Toast.makeText(this,getString(R.string.exception_mail),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /*private void buildAlertWhatsapp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.whatsapp_download);
        builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                }

            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }*/

    private void buildAlert(final String url, int messageId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+url)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+url)));
                }

            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }

    /*private void buildAlertSkype(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.skype_download);
        builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.skype.raider")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.skype.raider")));
                }

            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }*/

    public boolean isIntentAvailable(Intent intent) {
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void sendUsingGmail(Intent intent) {
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);

        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo == null) continue;
                if ("com.google.android.gm".equals(info.activityInfo.packageName)){
                    intent.setPackage(info.activityInfo.packageName);
                    startActivity(intent);
                    return;
                }
            }
        }
        startActivity(Intent.createChooser(intent, getString(R.string.chooser_email)));
    }
}
