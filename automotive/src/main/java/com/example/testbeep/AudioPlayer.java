package com.example.testbeep;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer {

    private static final String TAG = "AudioPlayer";

    public static void playBeep(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null. Cannot play beep sound.");
            return;
        }

        AssetFileDescriptor afd = null;
        try {
            Log.d(TAG, "Opening beep.wav from assets folder");
            afd = context.getAssets().openFd("beep.wav");

            long fileSize = afd.getLength();
            Log.d(TAG, "File size: " + fileSize);

            byte[] audioData = new byte[(int) fileSize];

            InputStream inputStream = afd.createInputStream();
            inputStream.read(audioData);
            inputStream.close();

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            AudioFormat audioFormat = new AudioFormat.Builder()
                    .setSampleRate(44100)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build();

            AudioTrack audioTrack = new AudioTrack.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(audioData.length)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build();

            Log.d(TAG, "Writing data to AudioTrack");
            audioTrack.write(audioData, 0, audioData.length);

            Log.d(TAG, "Setting volume to maximum");
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = (float) maxVolume / maxVolume; // Set volume to maximum
            audioTrack.setVolume(volume);

            Log.d(TAG, "Starting playback");
            audioTrack.play();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
