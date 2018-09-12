package com.agustinreinoso.magic8all.managers;

import com.agustinreinoso.magic8all.abstractbehaviors.Recorder;

public class RecorderManager {
    private Recorder recorder;
    private boolean isRecording = false;
    private boolean isWatching = false;

    public RecorderManager(Recorder recorder) {
        this.recorder = recorder;
    }

    public void startOperation() {
        if (isRecording) {
            stopRecording();
        } else {
            starRecording();
        }
    }

    public void controlPlaying() {
        if (isWatching) {
            stopMediaPlaying();
        } else {
            playMedia();
        }
    }

    public void starRecording() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRecording = true;
                recorder.recordMedia("ask");
            }
        }).start();
    }


    public void stopRecording() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                isRecording = false;
                recorder.stopRecordMedia("ask");
            }
        }).start();
    }

    private void playMedia() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recorder.playMedia();
                isWatching = true;
            }
        }).start();
    }

    private void stopMediaPlaying() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recorder.stopPlayMedia();
                isWatching = false;
            }
        }).start();
    }

    public void cleanReources() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recorder.cleanResources();
            }
        });
    }

}
