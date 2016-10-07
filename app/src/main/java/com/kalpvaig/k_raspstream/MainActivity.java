package com.kalpvaig.k_raspstream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
        SurfaceHolder.Callback  {

    static String USERNAME = "admin";
    static String PASSWORD = "camera";
    static String RTSP_URL = "rtsp://10.0.1.7:554/play1.sdp";

    private MediaPlayer _mediaPlayer;
    private SurfaceHolder _surfaceHolder;

    private FloatingActionButton cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Prefs.getPreference(this,"username","USERNAME").equals("USERNAME")){
            Intent intent = new Intent(this,SelectConfiguration.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
        }

        USERNAME = Prefs.getPreference(this,"username","USERNAME");
        PASSWORD = Prefs.getPreference(this,"password","PASSWORD");
        RTSP_URL = Prefs.getPreference(this,"rtsp_url","RTSP_URL");


        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cameraButton = (FloatingActionButton) findViewById(R.id.camerabutton);

        // Configure the view that renders live video.
        SurfaceView surfaceView =
                (SurfaceView) findViewById(R.id.surfaceView);
        _surfaceHolder = surfaceView.getHolder();
        _surfaceHolder.addCallback(this);
        _surfaceHolder.setFixedSize(320, 240);


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
            }
        });
    }

    /*
SurfaceHolder.Callback
*/

    @Override
    public void surfaceChanged(
            SurfaceHolder sh, int f, int w, int h) {}

    @Override
    public void surfaceCreated(SurfaceHolder sh) {
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.setDisplay(_surfaceHolder);

        Context context = getApplicationContext();
        Map<String, String> headers = getRtspHeaders();
        Uri source = Uri.parse(RTSP_URL);

        try {
            // Specify the IP camera's URL and auth headers.
            _mediaPlayer.setDataSource(context, source, headers);

            // Begin the process of setting up a video stream.
            _mediaPlayer.setOnPreparedListener(this);
            _mediaPlayer.prepareAsync();
        }
        catch (Exception e) {}
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder sh) {
        _mediaPlayer.release();
    }

    private Map<String, String> getRtspHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        String basicAuthValue = getBasicAuthValue(USERNAME, PASSWORD);
        headers.put("Authorization", basicAuthValue);
        return headers;
    }

    private String getBasicAuthValue(String usr, String pwd) {
        String credentials = usr + ":" + pwd;
        int flags = Base64.URL_SAFE | Base64.NO_WRAP;
        byte[] bytes = credentials.getBytes();
        return "Basic " + Base64.encodeToString(bytes, flags);
    }


        /*
    MediaPlayer.OnPreparedListener
    */
    @Override
    public void onPrepared(MediaPlayer mp) {
        _mediaPlayer.start();
    }


    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }


    public void saveBitmap(Bitmap bitmap) {
        Calendar current  = Calendar.getInstance();
        File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/screenshot_"+current.getTimeInMillis()+".png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }


    // Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(MainActivity.this,SelectConfiguration.class));
        }

        if (id == R.id.about_app) {
            startActivity(new Intent(MainActivity.this,AboutApp.class));
        }

        return super.onOptionsItemSelected(item);
    }


}
