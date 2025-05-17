package com.smarthome.ui;

import com.smarthome.device.Heater;
import com.smarthome.device.Light;
import com.smarthome.device.MusicPlayer;
import com.smarthome.home.SmartHomeFacade;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class SmartHomeApp extends Application {

    private SmartHomeFacade smartHome;
    private Label statusLabel;
    private Label songLabel;

    @Override
    public void start(Stage stage) {
        // Инициализация устройств
        Light light = new Light();
        Heater heater = new Heater();
        MusicPlayer musicPlayer = new MusicPlayer();
        smartHome = new SmartHomeFacade(light, heater, musicPlayer);

        // UI компоненты
        Button btnMorning = new Button("Утро");
        Button btnLeave = new Button("Ушел из дома");
        Button btnLoad = new Button("Загрузить плейлист");
        Button btnPlay = new Button("▶");
        Button btnPause = new Button("⏸");
        Button btnNext = new Button("▶▶");
        Button btnPrev = new Button("◀◀");

        statusLabel = new Label("Состояние: -");
        songLabel = new Label("Текущий трек: -");

        // Действия
        btnMorning.setOnAction(e -> {
            smartHome.goodMorning();
            updateStatus();
        });

        btnLeave.setOnAction(e -> {
            smartHome.leaveHome();
            updateStatus();
        });

        btnLoad.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Выберите MP3 файлы");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")
            );
            List<File> files = chooser.showOpenMultipleDialog(stage);
            if (files != null && !files.isEmpty()) {
                smartHome.getMusicPlayer().loadPlaylist(files);
                songLabel.setText("Текущий трек: " + smartHome.getMusicPlayer().getCurrentTrackName());
            }
        });

        btnPlay.setOnAction(e -> {
            smartHome.getMusicPlayer().play();
            updateStatus();
        });

        btnPause.setOnAction(e -> {
            smartHome.getMusicPlayer().pause();
            updateStatus();
        });

        btnNext.setOnAction(e -> {
            smartHome.getMusicPlayer().next();
            songLabel.setText("Текущий трек: " + smartHome.getMusicPlayer().getCurrentTrackName());
        });

        btnPrev.setOnAction(e -> {
            smartHome.getMusicPlayer().prev();
            songLabel.setText("Текущий трек: " + smartHome.getMusicPlayer().getCurrentTrackName());
        });

        VBox root = new VBox(10,
                new HBox(10, btnMorning, btnLeave, btnLoad),
                new HBox(10, btnPrev, btnPlay, btnPause, btnNext),
                songLabel,
                statusLabel
        );
        root.setStyle("-fx-padding: 20;");

        stage.setScene(new Scene(root, 500, 200));
        stage.setTitle("Умный Дом + Плеер");
        stage.show();
    }

    private void updateStatus() {
        statusLabel.setText("Состояние: " + smartHome.getStatusReport());
    }
}
