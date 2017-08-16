package com.example.slin.exifattempt;

import android.media.ExifInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by susanlin on 7/5/17.
 */

public class myImage implements Parcelable{
    private String filename;
    private double dedate;
    private Float fileLat, fileLong;

    public myImage(String name) {
        this.filename = name;
        try {
            geoDegree temp = new geoDegree(new ExifInterface(filename));
            fileLat = temp.getLatitude();
            fileLong = temp.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ExifInterface exif = new ExifInterface(filename);
            String tempdate = exif.getAttribute(ExifInterface.TAG_DATETIME);
            if(tempdate != null){
                tempdate = tempdate.replace(":", "");
                tempdate = tempdate.replace(' ', '.');
                Log.d("date", tempdate +" ");
                dedate = Double.parseDouble(tempdate);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private myImage(Parcel in){
        filename = in.readString();
        try {
            geoDegree temp = new geoDegree(new ExifInterface(filename));
            fileLat = temp.getLatitude();
            fileLong = temp.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //getters
    public Float getFileLat() {
        return fileLat;
    }

    public Float getFileLong() {
        return fileLong;
    }

    public String getFilename() {
        return filename;
    }

    public Double getdeDate(){
        return dedate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.filename);
    }
    public static final Parcelable.Creator <myImage> CREATOR = new Parcelable.Creator<myImage>(){
        public myImage createFromParcel(Parcel in){
            return new myImage(in);
        }

        @Override
        public myImage[] newArray(int i) {
            return new myImage[i];
        }
    };

}
