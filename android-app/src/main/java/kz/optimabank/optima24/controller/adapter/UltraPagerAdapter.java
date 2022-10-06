package kz.optimabank.optima24.controller.adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kg.optima.mobile.R;
import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.model.base.Banner;
import kz.optimabank.optima24.utility.BannersDirectoryNamePreferences;
import kz.optimabank.optima24.utility.ImageDownloader;

public class UltraPagerAdapter extends PagerAdapter {
    private final List<Banner> banners;

    public UltraPagerAdapter(List<Banner> banners) {
        this.banners = banners;
    }

    @Override
    public int getCount() {
        return banners != null ? banners.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.banner_layout, null);
        ImageView background = view.findViewById(R.id.background);
        final ProgressBar mapLoader = view.findViewById(R.id.loader);
        mapLoader.setVisibility(View.VISIBLE);
        final Banner banner = banners.get(position);
        if(banner != null) {
            if (banner.getBannerUrl() != null) {
                String imageName = banner.getImageName();
                String bannerDirectoryName = BannersDirectoryNamePreferences.getInstance(OptimaApp.Companion.getInstance()).returnDirectory();
                ImageDownloader.loadImageFromStorage(bannerDirectoryName, background, imageName);
                if (!hasImage(background)) {
                    mapLoader.setVisibility(View.VISIBLE);
                } else {
                    mapLoader.setVisibility(View.GONE);
                }
            }
        }

        if (banner.TargetUrl != null && !banner.TargetUrl.isEmpty()) {
            background.setOnClickListener(v -> {
                Uri uri = Uri.parse(banner.TargetUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    mapLoader.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
        container.addView(view);
        return view;
    }

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = drawable != null;
        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }
        return hasImage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
