package com.smarthome.ui;

import com.smarthome.device.*;
import com.smarthome.home.ActionLogger;
import com.smarthome.home.SmartHomeFacade;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class SmartHomeApp extends Application {

    private SmartHomeFacade smartHome;
    private ActionLogger logger;
    private TextArea logArea;
    private File playlistFile = new File("playlist.txt");

    @Override
    public void start(Stage stage) {
        // --- Логика и комнаты ---
        logger = new ActionLogger();

        Room livingRoom = new Room("Гостиная");
        Room kitchen = new Room("Кухня");

        livingRoom.addDevice("Light", new Light());
        livingRoom.addDevice("AC", new AC());
        livingRoom.addDevice("Speaker", new Speaker());

        kitchen.addDevice("Light", new Light());
        kitchen.addDevice("AC", new AC());
        kitchen.addDevice("Speaker", new Speaker());

        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.loadPlaylistFromFile(playlistFile); // автозагрузка

        smartHome = new SmartHomeFacade(livingRoom, kitchen, musicPlayer, logger);

        // --- Интерфейс управления комнатами ---
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label roomLabel = new Label("Управление комнатами:");
        GridPane roomControl = new GridPane();
        roomControl.setHgap(10);
        roomControl.setVgap(5);

        int row = 0;
        for (Room room : List.of(livingRoom, kitchen)) {
            String name = room.getName();

            Button lightOn = new Button("Свет ON");
            Button lightOff = new Button("Свет OFF");
            Button acOn = new Button("AC ON");
            Button acOff = new Button("AC OFF");
            Button spkOn = new Button("Динамик ON");
            Button spkOff = new Button("Динамик OFF");

            lightOn.setOnAction(e -> { room.turnOn("Light"); logger.log(name, "включен свет"); updateLog(); });
            lightOff.setOnAction(e -> { room.turnOff("Light"); logger.log(name, "выключен свет"); updateLog(); });
            acOn.setOnAction(e -> { room.turnOn("AC"); logger.log(name, "включен кондиционер"); updateLog(); });
            acOff.setOnAction(e -> { room.turnOff("AC"); logger.log(name, "выключен кондиционер"); updateLog(); });
            spkOn.setOnAction(e -> { room.turnOn("Speaker"); logger.log(name, "включен динамик"); updateLog(); });
            spkOff.setOnAction(e -> { room.turnOff("Speaker"); logger.log(name, "выключен динамик"); updateLog(); });

            roomControl.add(new Label(name), 0, row);
            roomControl.addRow(++row, lightOn, lightOff, acOn, acOff, spkOn, spkOff);
            row++;
        }

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(120);

        // --- Кнопка открытия музыкального плеера ---
        Button openPlayerBtn = new Button("Открыть плеер");
        openPlayerBtn.setOnAction(e -> {
            MusicPlayerWindow playerWindow = new MusicPlayerWindow(smartHome.getMusicPlayer(), playlistFile);
            playerWindow.show();
        });

        root.getChildren().addAll(roomLabel, roomControl, new Label("История действий:"), logArea, openPlayerBtn);

        // --- Окно сцены ---
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Умный Дом");

        // Сохраняем плейлист при выходе
        stage.setOnCloseRequest(e -> musicPlayer.savePlaylistToFile(playlistFile));

        stage.show();
    }

    private void updateLog() {
        logArea.clear();
        smartHome.getLogger().getLogs().forEach(line -> logArea.appendText(line + "\n"));
    }
}
