package kz.optimabank.optima24.model.base;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import kz.optimabank.optima24.utility.Constants;

/**
  Created by Жексенов on 02.02.2015.
 */
public class NewsItem implements Serializable {
    @SerializedName("PublishDate")
    private String publishDate;
    @SerializedName("Id")
    private int Id;
    @SerializedName("PublicationItemRecords")
    private ArrayList<PublicationItemRecord> publicationItemRecords;

    public String getPublishDate() {
        String stDate = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            stDate = Constants.VIEW_DATE_FORMAT.format(sdf.parse(publishDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stDate;
    }

    public Date getPublishDateDATE(){
        Date date = new Date();
        final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            date = format.parse(getPublishDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public int getId() {
        return this.Id;
    }

    public ArrayList<PublicationItemRecord> getPublicationItemRecords () {
        return this.publicationItemRecords;
    }


    public static class PublicationItemRecord implements Serializable {
        @SerializedName("Body")
        private String Body;
        @SerializedName("Title")
        private String Title;

        public String getBody() {
            return this.Body;
        }

        public String getTitle() {
            return this.Title;
        }
    }


}
