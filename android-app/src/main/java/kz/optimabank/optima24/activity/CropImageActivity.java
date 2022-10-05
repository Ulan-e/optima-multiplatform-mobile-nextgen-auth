package kz.optimabank.optima24.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.manager.GeneralManager;

public class CropImageActivity extends AppCompatActivity {
    private static final String TAG = CropImageActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cropIV)
    CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        cropImageView.setInitialFrameScale(0.75f);
        cropImageView.setImageBitmap(GeneralManager.getInstance().getBitmap());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rotate:
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("CheckResult")
    @OnClick({R.id.ok, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                cropImageView.cropAsync(new CropCallback() {
                    @Override
                    public void onSuccess(Bitmap cropped) {
                        GeneralManager.getInstance().setBitmap(cropped);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error when cropping a bitmap", e);
                    }
                });
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

    }
}
