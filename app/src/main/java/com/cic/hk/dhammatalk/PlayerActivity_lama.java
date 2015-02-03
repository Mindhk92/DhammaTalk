package com.cic.hk.dhammatalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


public class PlayerActivity_lama extends ActionBarActivity {

    int length =0;

    ProgressDialog pDialog;
    VideoView videoView;

    String videoURL = "rtsp://103.21.95.50:1935/vod/";
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_player);
        //boolean b = getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
//        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));

        if( savedInstanceState != null ) {
            //Then the application is being reloaded
            this.videoURL = savedInstanceState.getString("filevideo");

            this.title = savedInstanceState.getString("titlevideo");
            this.length = savedInstanceState.getInt("seekTo");
            Log.d("savedInstanceState.getInt(seekTo)", this.length+"");
//            setTitle(this.title);
//            videoView = (VideoView) findViewById(R.id.MyPlayer);
//
//            pDialog = new ProgressDialog(PlayerActivity.this);
//            pDialog.setTitle(this.title);
//
//            pDialog.setMessage("Buffering...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//            try {
//                MediaController mediaController = new MediaController(PlayerActivity.this);
//                mediaController.setAnchorView(videoView);
//
//                Uri video = Uri.parse(videoURL);
//                videoView.setMediaController(mediaController);
//                videoView.setVideoURI(video);
//                videoView.seekTo(length);
//                videoView.start();
//
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//
//            videoView.requestFocus();
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    pDialog.dismiss();
//                   // videoView.start();
//                }
//            });
//            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mp.release();
//                }
//            });
        }else {

            Intent i = getIntent();
            this.videoURL += i.getExtras().getString("filevideo");

            this.title = i.getExtras().getString("titlevideo");
//            setTitle(this.title);
//            videoView = (VideoView) findViewById(R.id.MyPlayer);
//
//            pDialog = new ProgressDialog(PlayerActivity.this);
//            pDialog.setTitle(this.title);
//
//            pDialog.setMessage("Buffering...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//            try {
//                MediaController mediaController = new MediaController(PlayerActivity.this);
//                mediaController.setAnchorView(videoView);
//
//                Uri video = Uri.parse(videoURL);
//                videoView.setMediaController(mediaController);
//                videoView.setVideoURI(video);
//                videoView.start();
//
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            videoView.requestFocus();
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    pDialog.dismiss();
//                   // videoView.start();
//                }
//            });
//            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mp.release();
//                }
//            });
        }

        setTitle(this.title);

        TextView tvTitleVideo = (TextView) findViewById(R.id.tvTitleVideo);
        tvTitleVideo.setText(this.title);

//        videoView = (VideoView) findViewById(R.id.MyPlayer);

        pDialog = new ProgressDialog(PlayerActivity_lama.this);
        pDialog.setTitle(this.title);

        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
//
        try {
            MediaController mediaController = new MediaController(PlayerActivity_lama.this);
            mediaController.setAnchorView(videoView);

            Uri video = Uri.parse(videoURL);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.seekTo(length);
//            videoView.seekTo(18241);
            videoView.start();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                // videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return false;
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
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        videoView.pause();
        length=videoView.getCurrentPosition();
//        Log.d("videoView.getCurrentPosition", ""+length);

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

//        videoView.seekTo(120000);
        Log.d("savedInstanceState.getInt(seekTo)", this.length+"");
        videoView.seekTo(length);
        videoView.start();
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
//        videoView.pause();
//        length=videoView.getCurrentPosition();
        if (videoView.isPlaying()) length = videoView.getCurrentPosition();
        outState.putString("filevideo", this.videoURL);
        outState.putString("titlevideo", this.title);
        outState.putInt("seekTo",length );
        Log.d("videoView.getCurrentPosition", ""+length);

    }
}
