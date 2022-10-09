package kz.optimabank.optima24.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.ProfileAdapter;
import kz.optimabank.optima24.db.controllers.ProfilePictureController;
import kz.optimabank.optima24.db.entry.ProfilePicture;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.AuthorizationResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.ProfImageLoaderImpl;
import kz.optimabank.optima24.notifications.ui.NotificationsActivity;
import kz.optimabank.optima24.utility.Utilities;
import okhttp3.ResponseBody;

/**
  Created by Timur on 01.07.2017.
 */

public class ProfileActivity extends OptimaActivity implements View.OnClickListener, ProfImageLoaderImpl.Callback, ProfImageLoaderImpl.SetProfImageCallback {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int CROP_PHOTO_RC = 1;
    private static final int CAMERA_PERMISSION_RC = 0;
    private static final int PICK_OR_CAPTURE_IMAGE_RC = 2;

    @BindView(R.id.imgWindowsClose) ImageView imgWindowsClose;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvPhoneNumber) TextView tvPhoneNumber;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.iv_edit_profile_picture) View ivEditProfilePicture;
    @BindView(R.id.iv_profile_picture) ImageView ivProfilePicture;

    ProfileAdapter adapter;
    ProfImageLoaderImpl profImageLoader;
    ArrayList<String> items;
    AuthorizationResponse.User user;
    ProfilePictureController profilePictureController = ProfilePictureController.getController();

    private File currentPhotoFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);
        imgWindowsClose.setOnClickListener(this);
        ivEditProfilePicture.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapter();
        setUserData();
        if (GeneralManager.isNeedUpdateProfImage()) {
            Log.d("GM.UpdateProfImage", " = " + Utilities.getPreferences(this).getString("profImage", ""));
            profImageLoader = new ProfImageLoaderImpl();
            profImageLoader.registerCallBack(this);
            profImageLoader.getProfImage(this);
            GeneralManager.setNeedUpdateProfImage(false);
        } else {
            ProfilePicture profilePicture = profilePictureController.getPictureByPhone(user.login);
            if (profilePicture != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(profilePicture.picture, 0, profilePicture.picture.length);
                ivProfilePicture.setImageBitmap(bitmap);
            }
        }
        boolean notification = getIntent().getBooleanExtra("notification", false);
        Log.d("TAG","notification  ProfileActivity = " + notification);
        if(notification) {
            getNotificationList();
        }
//        linNotice.setVisibility(View.GONE);
    }

    private void setUserData() {
        user = GeneralManager.getInstance().getUser();
        if (user != null) {
            tvName.setText(user.fullName);
            if (user.mobilePhoneNumber != null) tvPhoneNumber.setText(user.mobilePhoneNumber);
        }
    }

    private void setAdapter() {
        items = GeneralManager.getInstance().getProfileList(this);
        adapter = new ProfileAdapter(this, items, setOnClick());
        recyclerView.setAdapter(adapter);
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if(items!=null && !items.isEmpty()) {
                    String item = items.get(position);
                    if(item.equals(getString(R.string.notice))) {
                        getNotificationList();
                    } else if(item.equals(getString(R.string.settings_title))) {
                        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    } else if(item.equals(getString(R.string.requests))) {
                        getRequestsList();
                    } else if(item.equals(getString(R.string.exiting_application))) {
                        getExitingDialog();
                    }else if(item.equals(getString(R.string.contact_bank))) {
                        getContactBank();
                    }
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgWindowsClose:
                finish();
                break;
            case R.id.iv_edit_profile_picture:
                if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_RC);
                } else {
                    startActivityForResult(getChooserIntent(), PICK_OR_CAPTURE_IMAGE_RC);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult() " + data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_OR_CAPTURE_IMAGE_RC:
                    Bitmap bitmap;
                    Log.i("ProfileActivity", "intent = " + data);
                    if (data != null && data.getData() != null) {
                        Log.i("ProfileActivity", "intent.getData() = " + data.getData());
                        bitmap = getBitmapFromUri(data.getData(), ivProfilePicture.getWidth(), ivProfilePicture.getHeight());
                    } else {
                        Log.i("ProfileActivity", "currentPhotoFile = " + currentPhotoFile);
                        Log.i("ProfileActivity", "currentPhotoFile.getAbsolutePath() = " + currentPhotoFile.getAbsolutePath());
                        bitmap = getBitmapFromUri(null, ivProfilePicture.getWidth(), ivProfilePicture.getHeight());
                        Log.i("ProfileActivity", "bitmap = " + bitmap);
                    }
                    GeneralManager.getInstance().setBitmap(bitmap);
                    startActivityForResult(new Intent(ProfileActivity.this, CropImageActivity.class), CROP_PHOTO_RC);
                    currentPhotoFile.delete();
                    break;
                case CROP_PHOTO_RC:
                    ivProfilePicture.setImageBitmap(GeneralManager.getInstance().getBitmap());
                    saveBitmap(GeneralManager.getInstance().getBitmap());

                    Bitmap bitmapCroped = GeneralManager.getInstance().getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapCroped.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    byte[] profArray = stream.toByteArray();
                    Log.i("CROP_PHOTO_RC", "byte[] profArray = " + profArray.length);
                    String base64String = Base64.encodeToString(profArray, Base64.DEFAULT);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Value", base64String);
                    }catch (JSONException e){
                        Log.i("CROP_PHOTO_RC", "JSONException = ");
                        e.printStackTrace();
                    }
                    Log.i("CROP_PHOTO_RC", "bitmap = " + base64String);
                    profImageLoader = new ProfImageLoaderImpl();
                    profImageLoader.registerSetProfImageCallBack(this);
                    profImageLoader.setProfImage(this, jsonObject);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case CAMERA_PERMISSION_RC:
                    startActivityForResult(getChooserIntent(), PICK_OR_CAPTURE_IMAGE_RC);
                    break;
            }
        }
    }

    private void getRequestsList(){
        Intent intent = new Intent(ProfileActivity.this, NavigationActivity.class);
        intent.putExtra("isRequests",true);
        startActivity(intent);
    }

    private void getNotificationList(){
        Intent intent = new Intent(ProfileActivity.this, NotificationsActivity.class);
        //intent.putExtra("isNotice",true);
        startActivity(intent);
    }

    private void getContactBank(){
        Intent intent = new Intent(ProfileActivity.this, NavigationActivity.class);
        intent.putExtra("isContact",true);
        startActivity(intent);
    }

    private void getExitingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.q_logout));
        builder.setPositiveButton(getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(ProfileActivity.this, UnauthorizedTabActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
            }
        });
        builder.setNegativeButton(getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveBitmap(Bitmap bitmap) {
        if(user != null) {
            if(user.login != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byteArray = stream.toByteArray();
                ProfilePicture profilePicture = new ProfilePicture(user.login, byteArray);
                profilePictureController.addPicture(profilePicture);
            }
        }
    }

    public Bitmap getBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
        Log.d(TAG, "reqWidth = " + reqWidth + " reqHeight = " + reqHeight);
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (uri == null) {
            BitmapFactory.decodeFile(currentPhotoFile.getAbsolutePath(), options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(currentPhotoFile.getAbsolutePath(), options);
        } else {
            try {
                InputStream stream = getContentResolver()
                        .openInputStream(uri);
                if (stream != null) {
                    BitmapFactory.decodeStream(stream, null, options);
                    stream.close();
                }

                stream = getContentResolver().openInputStream(uri);
                if (stream != null) {
                    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(stream, null, options);

                    stream.close();
                }
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Could not open bitmap URI for input stream");
            } catch (IOException e) {
                Log.d(TAG, "Problem while reading bitmap");
            }
        }

        return bitmap;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Intent getChooserIntent() {
        List<Intent> allIntents = new ArrayList<>();
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        Intent chooser = Intent.createChooser(galleryIntent, getString(R.string.profile_photo));
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            currentPhotoFile = createImageFile();
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentPhotoFile));
        }
        allIntents.add(captureIntent);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooser;
    }

    private File createImageFile() {
        // Create an image file name
        String imageFileName = "photo.jpg";
        return new File(getExternalFilesDir(null), imageFileName);
    }

    @Override
    public void jsonProfImageResponse(int statusCode, String errorMessage, ResponseBody response) {
        Log.d(TAG,"statusCode = " + statusCode);
        if(statusCode == 200) {
            Bitmap bmp = BitmapFactory.decodeStream(response.byteStream());
            GeneralManager.getInstance().setBitmap(bmp);
            ivProfilePicture.setImageBitmap(bmp);
            saveBitmap(bmp);
            Log.i("CROP_PHOTO_RC", "byte[] profArray = " + response);
        } else {
            try {
                ATFFragment.ErrorDialogFragment fragment = ATFFragment.ErrorDialogFragment.newInstance(errorMessage);
                fragment.show(getSupportFragmentManager(), "error_dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void jsonSetProfImageResponse(int statusCode, String errorMessage, BaseResponse<String> response) {
        Log.d(TAG,"statusCode = " + statusCode);
        ATFFragment.ErrorDialogFragment fragment = ATFFragment.ErrorDialogFragment.newInstance(errorMessage);
        if(statusCode == 200) {
            Toast.makeText(this, getResources().getString(R.string.profile_photo_update),Toast.LENGTH_LONG).show();
        } else {
            try {
                fragment.show(getSupportFragmentManager(), "error_dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
