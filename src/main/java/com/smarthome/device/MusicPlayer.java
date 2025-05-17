package com.smarthome.device;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;

public class MusicPlayer implements Device {
    private MediaPlayer mediaPlayer;
    private List<File> playlist;
    private int currentIndex = 0;
    private boolean isOn = false;

    public void loadPlaylist(List<File> files) {
        this.playlist = files;
        currentIndex = 0;
    }

    public void play() {
        if (playlist != null && !playlist.isEmpty()) {
            stop();
            Media media = new Media(playlist.get(currentIndex).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
    }

    public void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    public void next() {
        if (playlist == null || playlist.isEmpty()) return;
        currentIndex = (currentIndex + 1) % playlist.size();
        play();
    }

    public void prev() {
        if (playlist == null || playlist.isEmpty()) return;
        currentIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
        play();
    }

    public String getCurrentTrackName() {
        return (playlist == null || playlist.isEmpty()) ? "-" : playlist.get(currentIndex).getName();
    }

    @Override
    public void turnOn() {
        isOn = true;
        play();
    }

    @Override
    public void turnOff() {
        isOn = false;
        stop();
    }

    @Override
    public String getStatus() {
        return isOn ? "Playing" : "Stopped";
    }
}
