package kz.optimabank.optima24.db.controllers;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.Banner;
import kz.optimabank.optima24.utility.BannersDirectoryNamePreferences;
import kz.optimabank.optima24.utility.ImageDownloader;

public class BannerController {
    private  static BannerController controller;
    private final String PRIMARY_KEY = "ImageName";
    private boolean isNeedDownload = false;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public static BannerController getController() {
        if (controller == null) {
            controller = new BannerController();
        }
        return controller;
    }

    public ArrayList<Banner> getAllBanners() {
        return null;
       // return new ArrayList<>(getRealm().where(Banner.class).findAll());
    }

    public void clearCache() {
        removeBannerDirectory();
        //getRealm().executeTransaction(realm -> realm.where(Banner.class).findAll().deleteAllFromRealm());
    }

    public void validateBanners(ArrayList<Banner> banners) {
//        getRealm().executeTransaction(bgRealm -> {
//            for (Banner banner : banners) {
//                Banner _banner = bgRealm.where(Banner.class)
//                        .equalTo(PRIMARY_KEY, banner.ImageName)
//                        .and()
//                        .equalTo("TargetUrl", banner.TargetUrl)
//                        .and()
//                        .equalTo("BannerUrl", banner.BannerUrl)
//                        .findFirst();
//                if (_banner == null) {
//                    isNeedDownload = true;
//                    break;
//                }
//            }
//        });
//
//        if (isNeedDownload) {
//            removeBannerDirectory();
//            getRealm().executeTransaction(realm -> {
//                realm.where(Banner.class).findAll().deleteAllFromRealm();
//                realm.insert(banners);
//            });
//            for (Banner banner : banners) {
//                ImageDownloader.getBannerImage(context, OptimaBank.getInstance().getOpenSessionHeader(null), banner.BannerUrl, banner.ImageName);
//            }
//        }
    }

    public void removeBannerDirectory() {
        String bannerDirectoryName = BannersDirectoryNamePreferences.getInstance(context).returnDirectory();
        File file = new File(bannerDirectoryName);
        if (file.exists()) {
            String deleteCmd = "rm -r " + bannerDirectoryName;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){}
}
