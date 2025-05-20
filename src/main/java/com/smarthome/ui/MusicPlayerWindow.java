package com.smarthome.ui;

import com.smarthome.device.MusicPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class MusicPlayerWindow extends Stage {

    private final MusicPlayer musicPlayer;
    private final File playlistFile;

    private final Label songLabel = new Label("Текущий трек: -");
    private final Label timeLabel = new Label("00:00 / 00:00");
    private final ListView<String> songList = new ListView<>();
    private Timeline timer;

    public MusicPlayerWindow(MusicPlayer musicPlayer, File playlistFile) {
        this.musicPlayer = musicPlayer;
        this.playlistFile = playlistFile;

        // --- Левый блок: кнопки и инфо ---
        VBox leftPane = new VBox(10);
        leftPane.setPadding(new Insets(10));

        Button btnLoad = new Button("Загрузить треки");
        Button btnPlay = new Button("▶");
        Button btnPause = new Button("⏸");
        Button btnNext = new Button("▶▶");
        Button btnPrev = new Button("◀◀");
        Button btnDelete = new Button("Удалить выбранное");

        HBox controls = new HBox(10, btnPrev, btnPlay, btnPause, btnNext);
        HBox topButtons = new HBox(10, btnLoad, btnDelete);

        leftPane.getChildren().addAll(topButtons, songLabel, timeLabel, controls);

        // --- Правый блок: список песен ---
        VBox rightPane = new VBox(10);
        rightPane.setPadding(new Insets(10));
        songList.setPrefWidth(250);
        songList.setPrefHeight(250);
        rightPane.getChildren().addAll(new Label("Список треков:"), songList);

        // --- Основной layout: горизонтально ---
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(10));

        // --- Логика кнопок ---
        btnLoad.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Выберите MP3 файлы");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
            List<File> files = chooser.showOpenMultipleDialog(this);
            if (files != null) {
                musicPlayer.loadPlaylist(files);
                musicPlayer.savePlaylistToFile(playlistFile);
                refreshPlaylistDisplay();
            }
        });

        btnPlay.setOnAction(e -> {
            musicPlayer.play();
            songLabel.setText("▶ " + musicPlayer.getCurrentTrackName());
        });

        btnPause.setOnAction(e -> {
            musicPlayer.pause();
            songLabel.setText("⏸ " + musicPlayer.getCurrentTrackName());
        });

        btnNext.setOnAction(e -> {
            musicPlayer.next();
            songLabel.setText("▶ " + musicPlayer.getCurrentTrackName());
        });

        btnPrev.setOnAction(e -> {
            musicPlayer.prev();
            songLabel.setText("▶ " + musicPlayer.getCurrentTrackName());
        });

        btnDelete.setOnAction(e -> {
            int selectedIndex = songList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && musicPlayer.getPlaylist() != null && selectedIndex < musicPlayer.getPlaylist().size()) {
                musicPlayer.getPlaylist().remove(selectedIndex);
                musicPlayer.savePlaylistToFile(playlistFile);
                refreshPlaylistDisplay();
                songLabel.setText("Текущий трек: " + musicPlayer.getCurrentTrackName());
            }
        });

        refreshPlaylistDisplay();
        startTimer();

        setScene(new Scene(root, 600, 300));
        setTitle("Музыкальный плеер");

        setOnCloseRequest(e -> {
            if (timer != null) timer.stop();
            musicPlayer.savePlaylistToFile(playlistFile);
        });
    }

    private void refreshPlaylistDisplay() {
        songList.getItems().clear();
        if (musicPlayer.getPlaylist() != null) {
            musicPlayer.getPlaylist().forEach(f -> songList.getItems().add(f.getName()));
        }
        songLabel.setText("Текущий трек: " + musicPlayer.getCurrentTrackName());
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimeLabel()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimeLabel() {
        Duration current = musicPlayer.getCurrentTime();
        Duration total = musicPlayer.getTotalDuration();
        String currentStr = formatDuration(current);
        String totalStr = formatDuration(total);
        timeLabel.setText(currentStr + " / " + totalStr);
    }

    private String formatDuration(Duration dur) {
        if (dur == null || dur.isUnknown() || dur.lessThan(Duration.ZERO)) {
            return "00:00";
        }
        int totalSeconds = (int) dur.toSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
