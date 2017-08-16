package com.example.slin.exifattempt;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private String pathname;
    private myImage choosefile;
    private GoogleApiClient mGoogleApiClient;
    public static final int fromMapsAct = 3;
    private boolean fileOperation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            choosefile = b.getParcelable(GalleryActivity.IMAGE_NAME);
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


    public void deletePic(View v) {
        Toast.makeText(getApplicationContext(), " File Deleted. Head back to Home page. ", Toast.LENGTH_SHORT).show();
        File tobe = new File(choosefile.getFilename());
        tobe.delete();
        startActivity(new Intent(MapsActivity.this, MainActivity.class));

    }

    public void toGDrive(View v) {
        if (checkSelfPermission(Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("maps drive", "Failed permission 1");
            requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, 4);
        }
        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Log.d("maps drive", "Failed permission 1");
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 5);
        }
        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
        Log.d("maps drive", mGoogleApiClient.toString() + "<-- the client. Supposedly done connecting");
        fileOperation = true;
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(newDriveContentsCallback);

    }


    final ResultCallback<DriveApi.DriveContentsResult> newDriveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (result.getStatus().isSuccess()) {
                        if (fileOperation == true) {

                            Log.d("maps drive", "going on to make file!");
                            CreateFileOnGoogleDrive(result);
                        }
                    }
                }
            };


    public void CreateFileOnGoogleDrive(DriveApi.DriveContentsResult result) {

        final DriveContents driveContents = result.getDriveContents();

        new Thread() {
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(choosefile.getFilename());

                OutputStream outputStream = driveContents.getOutputStream();
                ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream);

                try {
                    outputStream.write(bitmapStream.toByteArray());
                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                }

                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(choosefile.getFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String namedate = exif.getAttribute(ExifInterface.TAG_DATETIME);

                MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                        .setMimeType("image/jpeg").setTitle(namedate + ".jpeg").build();


                Drive.DriveApi.getRootFolder(mGoogleApiClient).createFile(mGoogleApiClient, metadataChangeSet, driveContents)
                        .setResultCallback(fileCallback);

            }

        }.start();

    }


    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (result.getStatus().isSuccess()) {

                        Toast.makeText(getApplicationContext(), result.getDriveFile().getDriveId() + "file created", Toast.LENGTH_LONG).show();

                    }

                    return;

                }
            };


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
        if (choosefile.getFileLat() != null) {
            LatLng sydney = new LatLng(choosefile.getFileLat(), choosefile.getFileLong());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Taken Here")
                    .icon(BitmapDescriptorFactory.fromBitmap(ThumbnailUtils
                            .extractThumbnail(BitmapFactory.decodeFile(choosefile.getFilename()), 64, 64))));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("maps drive", "connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("maps drive", "connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("maps drive", "connection failed");
        if (!connectionResult.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */

        try {

            connectionResult.startResolutionForResult(this, 1);

        } catch (IntentSender.SendIntentException e) {

            Log.e("maps drive", "exception while starting resolution activity");
        }
    }


}
