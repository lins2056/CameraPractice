package com.example.slin.exifattempt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by susanlin on 7/5/17.
 */

public class ImageAdapt extends ArrayAdapter<myImage> {

    private ArrayList<myImage> data = new ArrayList<>();
    private BitmapFactory.Options tohelpsize = new BitmapFactory.Options();

    static class ImageHolder {
        ImageView imgIcon;
        TextView description;
    }

    public ImageAdapt(Context context, ArrayList<myImage> data) {
        super(context, 0, data);
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder;

        if (row == null) {
            holder = new ImageHolder();
            row = LayoutInflater.from(getContext()).inflate(R.layout.grid_view, parent, false);
            holder.description = (TextView) row.findViewById(R.id.item_description);
            holder.imgIcon = (ImageView) row.findViewById(R.id.item_imgIcon);

            row.setTag(holder);

        } else {
            holder = (ImageHolder) row.getTag();
        }

        myImage thatMyImage = data.get(position);
        String pic = thatMyImage.getFilename();

        String theDeets = " ";
        try {
            ExifInterface exif = new ExifInterface(pic);
            theDeets += exif.getAttribute(ExifInterface.TAG_DATETIME);

        } catch (IOException e) {
            e.printStackTrace();
        }


        holder.description.setText(theDeets);

        holder.imgIcon.setImageBitmap(decodeSampledBitmapFromResource(new File(pic), 100));
        //holder.imgIcon.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(pic), 150, 150));

        return row;
    }

    public Bitmap decodeSampledBitmapFromResource(File f, int reqSize) {


        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int REQUIRED_SIZE = reqSize;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }


            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }




}
