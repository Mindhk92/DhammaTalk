package com.cic.hk.dhammatalk;

//import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
//import android.widget.LinearLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        YouTubePlayer.OnFullscreenListener {
    //private YouTubePlayer YPlayer;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
   // int length =0;

    private LinearLayout baseLayout;

    private View otherViews;
   // ProgressDialog pDialog;
//    VideoView videoView;
    YouTubePlayerView youTubeView;
    //    String videoURL = "rtsp://103.21.95.50:1935/vod/";
    String videoURL = "";
    String title = "";
    String description = "";

    private boolean fullscreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        baseLayout = (LinearLayout) findViewById(R.id.player_activity_main_layout);

        Intent i = getIntent();
        this.videoURL += i.getExtras().getString("filevideo");
        this.title = i.getExtras().getString("titlevideo");
        this.description = i.getExtras().getString("descriptionvideo");
         youTubeView = (YouTubePlayerView)
                findViewById(R.id.youtube_view);

        //youTubeView.bringToFront();
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
        otherViews = findViewById(R.id.other_views);

        TextView tvTitleVideo = (TextView) findViewById(R.id.tvTitleVideo);
        tvTitleVideo.setText(title);

        TextView tvDescriptionVideo = (TextView) findViewById(R.id.tvDescriptionVideo);
        tvDescriptionVideo.setText(this.description);
        tvDescriptionVideo.setMovementMethod(new ScrollingMovementMethod());

        doLayout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer %s",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(DeveloperKey.DEVELOPER_KEY, this);
        }
    }
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
//        this.YPlayer = player;
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        //player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        if (!wasRestored) {
            player.cueVideo(this.videoURL);
        }
    }

    @Override
    public void onFullscreen(boolean b) {
        fullscreen = b;
        doLayout();
    }

    private void doLayout() {
        LinearLayout.LayoutParams playerParams =
                (LinearLayout.LayoutParams) youTubeView.getLayoutParams();

        if (fullscreen) {
            playerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.height = LinearLayout.LayoutParams.MATCH_PARENT;

            otherViews.setVisibility(View.GONE);
        } else {
            otherViews.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams otherViewsParams = otherViews.getLayoutParams();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerParams.width = otherViewsParams.width = 0;
                playerParams.height = WRAP_CONTENT;
                otherViewsParams.height = MATCH_PARENT;
                playerParams.weight = 1;
                baseLayout.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                playerParams.width = otherViewsParams.width = MATCH_PARENT;
                playerParams.height = WRAP_CONTENT;
                playerParams.weight = 0;
                otherViewsParams.height = 0;
                baseLayout.setOrientation(LinearLayout.VERTICAL);
            }
        }
    }
}
