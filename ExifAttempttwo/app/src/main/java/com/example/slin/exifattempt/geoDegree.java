package com.example.slin.exifattempt;

import android.media.ExifInterface;
import android.util.Log;

/**
 * Created by susanlin on 8/9/17.
 */

public class geoDegree {

    private Float Latitude = null;
    private Float Longitude = null;

    public geoDegree(ExifInterface exif) {
        String attrLatitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String attrLatitude_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String attrLongitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String attrLongitude_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        Log.d("exifattempt", " lat " + attrLatitude + attrLatitude_REF + "long" + attrLongitude + attrLongitude_REF);

        if ((attrLatitude != null) && (attrLatitude_REF != null) && (attrLongitude != null) && (attrLongitude_REF != null)) {
            if (attrLatitude_REF.equals("N")) {
                Latitude = convertToDegree(attrLatitude);
            } else {
                Latitude = 0 - convertToDegree(attrLatitude);
            }

            if (attrLongitude.equals("E")) {
                Longitude = convertToDegree(attrLongitude);
            } else {
                Longitude = 0 - convertToDegree(attrLongitude);
            }

            Log.d("exifattempt", Latitude + Longitude + " ");
        }
    }



    public Float convertToDegree(String stringDMS) {
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;

    }
    public Float getLatitude(){
        return Latitude;
    }

    public Float getLongitude() {
        return Longitude;
    }
}

