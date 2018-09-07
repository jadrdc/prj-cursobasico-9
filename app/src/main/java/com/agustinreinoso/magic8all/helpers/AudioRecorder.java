package com.agustinreinoso.magic8all.helpers;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import com.agustinreinoso.magic8all.abstractbehaviors.Recorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AudioRecorder implements Recorder {
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String file;

    public AudioRecorder() {
        mediaRecorder = new MediaRecorder();

    }

    @Override
    public boolean recordMedia(String media) {

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        } else {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + dateFormat.format(new Date()) + ".m4a";
        mediaRecorder.setOutputFile(file);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean stopRecordMedia(String media) {
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void playMedia() {
        if (file != null) {

            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(file);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void stopPlayMedia() {

        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void cleanResources() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
