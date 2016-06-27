package br.com.trendzapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import br.com.trendzapi.location.GPSTracker;

public class ScreenActivity extends AppCompatActivity {
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    private ImageView imageAd, imageClose;
    private Button button;
    private VideoView videoAd;
    private static String mExtension, mFile, mLink;

    public static void launch(Activity activity, String extension, String file, String link) {
        Intent intent = new Intent(activity, ScreenActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
        mExtension = extension;
        mFile = file;
        mLink = link;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_ad);

        imageClose = (ImageView) findViewById(R.id.image_close);
        imageAd = (ImageView) findViewById(R.id.image_ad);
        videoAd = (VideoView) findViewById(R.id.video_ad);
        button = (Button) findViewById(R.id.button);

        if(mExtension.equals(".jpeg") || mExtension.equals(".jpg") || mExtension.equals(".png") || mExtension.equals(".gif")) {
            setupImage(mFile, mLink);
        }else if(mExtension.equals(".mp4") || mExtension.equals(".avi") || mExtension.equals(".mkv")) {
            setupVideo(mFile, mLink);
        }
    }

    private void setupImage(String arquivo, final String link) {
        imageAd.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(arquivo)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageAd);

        if(!link.isEmpty()) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+link));
                    startActivity(intent);
                }
            });
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                imageClose.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    private void setupVideo(String arquivo, final String link) {
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
            mediaControls.setVisibility(View.INVISIBLE);
        }

        videoAd.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde, carregando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            videoAd.setMediaController(mediaControls);
            videoAd.setVideoURI(Uri.parse(arquivo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!link.isEmpty()) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+link));
                    startActivity(intent);
                }
            });
        }

        videoAd.requestFocus();
        videoAd.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                videoAd.seekTo(position);

                if (position == 0) {
                    videoAd.start();
                } else {
                    videoAd.pause();
                }
            }
        });

        // fim do video
        videoAd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageClose.setVisibility(View.VISIBLE);
            }
        });
    }

    public void close(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        //
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("Position", videoAd.getCurrentPosition());
        videoAd.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        position = savedInstanceState.getInt("Position");
        videoAd.seekTo(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        GPSTracker gps = new GPSTracker(ScreenActivity.this);
        gps.stopUsingGPS();
    }
}