package com.example.slin.exifattempt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    private Uri photoURI;
    File theImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d("CAMERAPP", "Failed permission 1");
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("CAMERAPP", "Failed permission 2");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("CAMERAPP", "Failed permission 3");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }

//        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("CAMERAPP", "Failed permission 4");
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        }
//
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("CAMERAPP", "Failed permission 5");
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        Log.d("CAMERAPP", imageFileName);
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "S_CAM_PICS");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Log.d("CAMERAPP", directory + " file path directory");
//        File[] tempfilesave = directory.listFiles();

        theImage = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                directory      /* directory */
        );

        return theImage;
    }

    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("CAMERAPP", "resolveactivity null");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("CAMERAPP", "photofile null");
                photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            String[] projection = {theImage.getAbsolutePath()};
//            Cursor cursor = getContentResolver().query(photoURI, projection, null, null, null);
//            int column_index_data = cursor.getColumnIndexOrThrow(theImage.getAbsolutePath());
//            cursor.moveToFirst();
//            String picturePath = cursor.getString(column_index_data);


//            myImage image = new myImage();
//            image.setFilename("Test");
//            Log.d("CAMERAPP", theImage.getPath() + " the path");
//            image.setPath(theImage.getPath());
//            images.add(image);
//            images.add(image);
//            Log.d("CAMERAPP", images.size() + " array size");
//            Log.d("CAMERAPP", images.get(0).getPath() + " the first");

            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

   public void goGallery(View v){
       Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
       startActivity(intent);
   }
}
