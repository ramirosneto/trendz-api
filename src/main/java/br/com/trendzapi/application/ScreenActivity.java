package br.com.trendzapi.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import br.com.trendzapi.R;

public class ScreenActivity extends AppCompatActivity {
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private ImageView imageClose;

    ImageView imageAd;
    VideoView videoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_ad);

        imageClose = (ImageView) findViewById(R.id.image_close);
        imageAd = (ImageView) findViewById(R.id.image_ad);
        videoAd = (VideoView) findViewById(R.id.video_ad);

        String extensao = getIntent().hasExtra("extensao") ? getIntent().getStringExtra("extensao") : "";
        String arquivo = getIntent().hasExtra("arquivo") ? getIntent().getStringExtra("arquivo") : "";
        String link = getIntent().hasExtra("link") ? getIntent().getStringExtra("link") : "null";

        if(extensao.equals(".jpeg") || extensao.equals(".jpg") || extensao.equals(".png") || extensao.equals(".gif")) {
            setupImage(arquivo, link);
        }else if(extensao.equals(".mp4") || extensao.equals(".avi") || extensao.equals(".mkv")) {
            setupVideo(arquivo, link);
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

        if(link.equals("null")) {
            imageAd.setOnClickListener(null);
        }else {
            imageAd.setOnClickListener(new View.OnClickListener() {
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

        if(link.equals("null")) {
            videoAd.setOnClickListener(null);
        }else {
            videoAd.setOnClickListener(new View.OnClickListener() {
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
}