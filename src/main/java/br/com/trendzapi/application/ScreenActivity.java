package br.com.trendzapi.application;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import br.com.trendzapi.R;

public class ScreenActivity extends AppCompatActivity {
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private ImageView imageClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_ad);

        imageClose = (ImageView) findViewById(R.id.image_close);

        String extensao = getIntent().hasExtra("extensao") ? getIntent().getStringExtra("extensao") : "";
        String arquivo = getIntent().hasExtra("arquivo") ? getIntent().getStringExtra("arquivo") : "";

        if(extensao.equals(".jpeg") || extensao.equals(".jpg") || extensao.equals(".png")) {
            setupImage(arquivo);
        }else if(extensao.equals(".mp4") || extensao.equals(".avi") || extensao.equals(".mkv")) {
            setupVideo(arquivo);
        }
    }

    private void setupImage(String arquivo) {
        ImageView imageAd = (ImageView) findViewById(R.id.image_ad);
        imageAd.setVisibility(View.VISIBLE);

        Picasso.with(this)
                .load(arquivo)
                .into(imageAd);

        imageClose.setVisibility(View.VISIBLE);
    }

    private void setupVideo(String arquivo) {
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
            mediaControls.setVisibility(View.INVISIBLE);
        }

        final VideoView videoAd = (VideoView) findViewById(R.id.video_ad);
        videoAd.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            videoAd.setMediaController(mediaControls);
            videoAd.setVideoURI(Uri.parse(arquivo));

        } catch (Exception e) {
            e.printStackTrace();
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
}