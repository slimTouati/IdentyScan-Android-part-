package com.example.m_visonproject;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.provider.MediaStore;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    private static final int IMAGE_PICK_CODE = 100;
   Uri imageUri;
   Bitmap imageBp;
    Button analyse;
    LottieAnimationView loader;

    SharedPreferences myprefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myprefs =  getSharedPreferences("mvision", MODE_PRIVATE);
        Button btnCamera = (Button)findViewById(R.id.camera);
        Button btnUpload = (Button)findViewById(R.id.upload);
        analyse = (Button) findViewById(R.id.analyse);
        imageView = (ImageView)findViewById(R.id.image);
        loader = findViewById(R.id.loader);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              openGallery();
              //  startActivity(new Intent(MainActivity.this, AnalyseActivity.class));

            }
        });

        analyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout lay = findViewById(R.id.linearlayouts);
                lay.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        loader.pauseAnimation();
                        Intent i = new Intent(MainActivity.this, AnalyseActivity.class);
                        if(myprefs.getInt("cam",0) == 1)
                        {
                            i.putExtra("img", imageUri.toString());
                        }
                        else
                        {
                           i.putExtra("img", imageBp);

                        }
                        startActivity(i);
                    }
                }, 5000);
            }
        });

    }

    public void openGallery(){

        Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        
        startActivityForResult(gallery,IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){

                imageUri = data.getData();
                imageView.setVisibility(View.VISIBLE);
                analyse.setVisibility(View.VISIBLE);
                imageView.setImageURI(imageUri);
                SharedPreferences.Editor editor = myprefs.edit();
                editor.putInt("i", 2);
                editor.putInt("cam", 1);
                editor.apply();
                editor.commit();


            }else{
                imageBp = (Bitmap)data.getExtras().get("data");
                imageView.setImageBitmap(imageBp);
                imageView.setVisibility(View.VISIBLE);
                analyse.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = myprefs.edit();
                editor.putInt("i", 1);
                editor.putInt("cam", 2);
                editor.apply();
                editor.commit();


            }





    }
}
