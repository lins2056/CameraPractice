package com.example.slin.exifattempt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String pathname;
    private myImage choosefile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle b = this.getIntent().getExtras();
        if(b!= null){
            choosefile = b.getParcelable(GalleryActivity.IMAGE_NAME);
        }

        if(choosefile.getFilename() == null){
            File tobe = new File(choosefile.getFilename());
            tobe.delete();
        }
        //get reference to the text
        TextView tv = findViewById(R.id.maps_description);
        //get coordinates

        //compile text
        String theDeets = "EXIF METADATA \n\n";
        try {
            ExifInterface exif = new ExifInterface(choosefile.getFilename());
            theDeets += "Datetime: \n" + exif.getAttribute(ExifInterface.TAG_DATETIME) + "\n";
            theDeets += "Latitude: \n" + exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) + "\n" + "\n";
            theDeets += "Longitude: \n" + exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) + "\n" + "\n";


        } catch (IOException e) {
            e.printStackTrace();
        }
        tv.setText(theDeets);

        //set image
        ImageView iv = findViewById(R.id.maps_picture);

        iv.setImageBitmap(BitmapFactory.decodeFile(choosefile.getFilename()));
    }





    public void deletePic(View v){
        File tobe = new File(choosefile.getFilename());
        tobe.delete();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if(choosefile.getFileLat() != null){
            LatLng sydney = new LatLng(choosefile.getFileLat(), choosefile.getFileLong());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Taken Here")
                    .icon(BitmapDescriptorFactory.fromBitmap(ThumbnailUtils
                            .extractThumbnail(BitmapFactory.decodeFile(choosefile.getFilename()), 64, 64))));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }
}
