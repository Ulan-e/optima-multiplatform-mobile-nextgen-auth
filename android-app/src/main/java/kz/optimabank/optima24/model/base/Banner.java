package kz.optimabank.optima24.model.base;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Banner implements Serializable {
    @SerializedName("ImageName")
    public String ImageName;

    @SerializedName("TargetUrl")
    public String TargetUrl;


    @SerializedName("BannerUrl")
    public String BannerUrl;

    public String getImageName() {
        return ImageName;
    }

    public String getBannerUrl() {
        return BannerUrl;
    }

    public String getTargetUrl() {
        return TargetUrl;
    }

    public Banner() {
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public void setTargetUrl(String targetUrl) {
        TargetUrl = targetUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        BannerUrl = bannerUrl;
    }
}