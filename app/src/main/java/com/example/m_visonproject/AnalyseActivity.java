package com.example.m_visonproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalyseActivity extends AppCompatActivity {

    ImageView IV;
    TextView Name;
    TextView Lastname;
    TextView CIN;
    TextView SonOf;
    TextView Birthday;
    TextView Birthplace;
    String urlbase = "http://192.168.43.150:3000/predictions/";
    String url;

    String name,lname,cin,sonof,birthday,birthplace;
    SharedPreferences myprefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        myprefs =  getSharedPreferences("mvision", MODE_PRIVATE);
        IV = findViewById(R.id.images);
        Name = findViewById(R.id.name);
        Lastname = findViewById(R.id.surname);
        CIN = findViewById(R.id.cinnum);
        SonOf = findViewById(R.id.sonof);
        Birthday = findViewById(R.id.dateofbirth);
        Birthplace = findViewById(R.id.birthplace);

        if(myprefs.getInt("cam",0) == 1) {
            url = urlbase + "bilel";
        }
        else url = urlbase + "slim";
        if(myprefs.getInt("i",0) == 2)
        {
            Uri v = Uri.parse(getIntent().getExtras().getString("img"));
            IV.setImageURI(v);
        }
        else
        {
            Bitmap p = getIntent().getParcelableExtra("img");
            IV.setImageBitmap(p);
        }

        getCINDetails();
        //Log.d("MehdiGG", Name.getText().toString());

    }

    private void getCINDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // JSONArray dataArray = new JSONArray(response);

                    JSONObject obj = new JSONObject(response);

                    Log.d("MehdiGG", obj.toString());


                     name = obj.getString("firstname");
                    lname = obj.getString("lastname");
                     cin = obj.getString("cin");
                     sonof = obj.getString("sonof");
                     birthday = obj.getString("birthday");
                     birthplace = obj.getString("birthplace");

                    Name.setText(name);
                    Lastname.setText(lname);
                    CIN.setText(cin);
                    SonOf.setText(sonof);
                    Birthday.setText(birthday);
                    Birthplace.setText(birthplace);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hell no", "Error " + error.getMessage());

            }
        }
        );
        Log.d("nchoufouha",stringRequest.toString());
        AppController.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setShouldCache(false);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AnalyseActivity.this, MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
