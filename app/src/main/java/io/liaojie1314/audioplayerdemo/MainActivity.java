package io.liaojie1314.audioplayerdemo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView mPlayerPosition;
    private TextView mPlayerDuration;
    private SeekBar mSeekBar;
    private ImageView mBtRew;
    private ImageView mBtPlay;
    private ImageView mBtPause;
    private ImageView mBtFf;

    MediaPlayer mMediaPlayer;
    Handler mHandler = new Handler();
    Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        int duration = mMediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        mPlayerDuration.setText(sDuration);
        mBtPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtPlay.setVisibility(View.GONE);
                mBtPause.setVisibility(View.VISIBLE);
                mMediaPlayer.start();
                mSeekBar.setMax(mMediaPlayer.getDuration());
                mHandler.postDelayed(mRunnable, 0);
            }
        });
        mBtPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtPause.setVisibility(View.GONE);
                mBtPlay.setVisibility(View.VISIBLE);
                mMediaPlayer.pause();
                mHandler.removeCallbacks(mRunnable);
            }
        });
        mBtFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                int duration = mMediaPlayer.getDuration();
                if (mMediaPlayer.isPlaying() && duration != currentPosition) {
                    currentPosition += 5000;
                    mPlayerPosition.setText(convertFormat(currentPosition));
                    mMediaPlayer.seekTo(currentPosition);
                }
            }
        });

        mBtRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                if (mMediaPlayer.isPlaying() && currentPosition > 5000) {
                    currentPosition -= 5000;
                    mPlayerPosition.setText(convertFormat(currentPosition));
                    mMediaPlayer.seekTo(currentPosition);
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //拖动进度条
                    mMediaPlayer.seekTo(progress);
                }
                mPlayerPosition.setText(convertFormat(mMediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBtPause.setVisibility(View.GONE);
                mBtPlay.setVisibility(View.VISIBLE);
                mMediaPlayer.seekTo(0);
            }
        });
    }

    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration),
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    private void initView() {
        mPlayerPosition = findViewById(R.id.player_position);
        mPlayerDuration = findViewById(R.id.player_duration);
        mSeekBar = findViewById(R.id.seek_bar);
        mBtRew = findViewById(R.id.bt_rew);
        mBtPlay = findViewById(R.id.bt_play);
        mBtPause = findViewById(R.id.bt_pause);
        mBtFf = findViewById(R.id.bt_ff);
        mMediaPlayer = MediaPlayer.create(this, R.raw.music);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                mHandler.postDelayed(this, 500);
            }
        };
    }
}