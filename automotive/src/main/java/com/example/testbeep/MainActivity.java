package com.example.testbeep;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAudioTrack, btnBeeper, btnToneGenerator;
    private ToneGenerator toneGenerator;
    private AudioTrack player;
    private Beeper mBeeper;
    private static final Uri FILENAME = Uri.parse("beep.wav");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

        mBeeper = new Beeper(this);

        btnAudioTrack = findViewById(R.id.AudioTrack_static_mode);
        btnBeeper = findViewById(R.id.Beeper);
        btnToneGenerator = findViewById(R.id.ToneGenerator);

        btnAudioTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayer.playBeep(MainActivity.this);
            }
        });

        btnBeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBeep();
            }
        });

        btnToneGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeeper.beep("hello", FILENAME);;
            }
        });
    }

    private void playBeep() {
        mBeeper.beep(getPackageName(), Uri.parse("com.example.testbeep/res/beep")); // Call beep method of Beeper instance
    }

    private void playTone() {
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toneGenerator != null) {
            toneGenerator.release();
        }
        if (player != null) {
            player.stop();
            player.release();
        }
    }
}