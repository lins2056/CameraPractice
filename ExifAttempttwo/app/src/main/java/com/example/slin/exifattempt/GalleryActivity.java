package com.example.slin.exifattempt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity{

    private ArrayList<myImage> myImagePaths = new ArrayList<>();
    public static ArrayList<myImage> myLATImagePaths = new ArrayList<>();
    private GridView gv;
    private ImageAdapt imageAdapter;
    private int number;
    public static final String IMAGE_NAME = "IMAGE_NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        myLATImagePaths = myImagePaths;
        new sortLat().execute(myLATImagePaths);

        imageAdapter = new ImageAdapt(this, myImagePaths);

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "S_CAM_PICS");
        File[] files = directory.listFiles();
        //File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                myImagePaths.add(new myImage(files[i].toString()));
                Log.d("the list", "check this out");

            }

            gv = (GridView) findViewById(R.id.grid);
            gv.setAdapter(imageAdapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    myImage picpass = myImagePaths.get(position);
                    Intent intent = new Intent(GalleryActivity.this, MapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(IMAGE_NAME, picpass);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }



    }

    public void sortByLatitude(View v) {

        gv = (GridView) findViewById(R.id.grid);
        gv.setAdapter(new ImageAdapt(this, myLATImagePaths));
        Log.d("gallery", "sorted");
        for(int i = 0; i < myLATImagePaths.size(); i++){
            Log.d("gallery", ", " + myLATImagePaths.get(i).getFileLat());
        }
    }


    public void sortByDate(View v) {
//        myImagePaths.clear();
//
//        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "S_CAM_PICS");
//        File[] files = directory.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            myImagePaths.add(new myImage(files[i].toString()));
//
//        }

        gv = (GridView) findViewById(R.id.grid);
        gv.setAdapter(new ImageAdapt(this, myImagePaths));
    }


    //quicksorts below

}


class sortLat extends AsyncTask<ArrayList<myImage>, Void, ArrayList<myImage>>{

    public static ArrayList<myImage> result;
    int number;
    @Override
    protected ArrayList<myImage> doInBackground(ArrayList<myImage>... passing) {
        result = passing[0];
        startSort(result);
        Log.d("gallery", "doneee");

        return result;
    }

    public void startSort(ArrayList<myImage> values) {

        if (values == null || values.size() == 0) {
            return;
        }
        number = values.size();
        quicksortlat(0, number - 1);

    }

    private void quicksortlat(int low, int high) {
        int i = low, j = high;
        Float pivot = result.get(low + (high - low) / 2).getFileLat();

        while (i <= j) {
            while (result.get(i).getFileLat() < pivot) ;
            {
                i++;
            }
            while (result.get(j).getFileLat() > pivot) {
                j--;
            }
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }

        if (low < j) quicksortlat(low, j);

        if (i < high) quicksortlat(i, high);
    }

    private void exchange(int i, int j) {
        myImage temp = result.get(i);
        result.set(i, result.get(j));
        result.set(j, temp);
    }


    protected void onPostExecute(ArrayList<myImage> result){
        GalleryActivity.myLATImagePaths = result;
        Log.d("gallery", "shared!!");
    }

}