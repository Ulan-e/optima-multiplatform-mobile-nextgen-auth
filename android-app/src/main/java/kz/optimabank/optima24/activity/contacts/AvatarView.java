package kz.optimabank.optima24.activity.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;

import kg.optima.mobile.R;

public class AvatarView extends FrameLayout {

    private View view;
    private ShapeableImageView imageView;
    private TextView textView;

    public AvatarView(@NonNull Context context) {
        super(context);
    }

    public AvatarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        view = inflate(context, R.layout.layout_contact_image, this);
        imageView = findViewById(R.id.image_circle_background);
        textView = findViewById(R.id.text_initials);
    }

    public void setTextInitials(String initials) {
        if (initials == null) return;

        String text = getInitials(initials);
        textView.setText(text);
    }

    public void setRandomBackground(int color) {
        imageView.setBackgroundColor(color);
    }

    private String getInitials(String fullName) {
        return fullName.length() < 2 ? fullName : fullName.substring(0, 2);
    }
}