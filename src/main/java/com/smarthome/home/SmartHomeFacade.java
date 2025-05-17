package com.smarthome.home;

import com.smarthome.device.Device;
import com.smarthome.device.Heater;
import com.smarthome.device.Light;
import com.smarthome.device.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeFacade {
    private final Light light;
    private final Heater heater;
    private final MusicPlayer musicPlayer;
    private final List<Device> allDevices;

    public SmartHomeFacade(Light light, Heater heater, MusicPlayer musicPlayer) {
        this.light = light;
        this.heater = heater;
        this.musicPlayer = musicPlayer;

        this.allDevices = new ArrayList<>();
        allDevices.add(light);
        allDevices.add(heater);
        allDevices.add(musicPlayer);
    }

    public void goodMorning() {
        light.turnOn();
        heater.turnOn();
        musicPlayer.turnOn();
    }

    public void leaveHome() {
        for (Device d : allDevices) {
            d.turnOff();
        }
    }

    public String getStatusReport() {
        return "Свет: " + light.getStatus() +
                ", Отопление: " + heater.getStatus() +
                ", Музыка: " + musicPlayer.getStatus() +
                ", Трек: " + musicPlayer.getCurrentTrackName();
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public Light getLight() {
        return light;
    }

    public Heater getHeater() {
        return heater;
    }
}
