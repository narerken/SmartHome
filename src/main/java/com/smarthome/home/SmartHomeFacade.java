package com.smarthome.home;

import com.smarthome.device.MusicPlayer;
import com.smarthome.device.Room;

public class SmartHomeFacade {

    private final Room livingRoom;
    private final Room kitchen;
    private final MusicPlayer musicPlayer;
    private final ActionLogger logger;

    public SmartHomeFacade(Room livingRoom, Room kitchen, MusicPlayer musicPlayer, ActionLogger logger) {
        this.livingRoom = livingRoom;
        this.kitchen = kitchen;
        this.musicPlayer = musicPlayer;
        this.logger = logger;
    }


    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public Room getLivingRoom() {
        return livingRoom;
    }

    public Room getKitchen() {
        return kitchen;
    }

    public ActionLogger getLogger() {
        return logger;
    }
}