package com.agustinreinoso.magic8all.abstractbehaviors;

public interface Recorder {

    public  boolean recordMedia(String media);
    public  boolean stopRecordMedia(String media);
    public  void playMedia();
    public  void stopPlayMedia();
    public  void cleanResources();
}
