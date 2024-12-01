package com.mirkoebert.simplejavaradioplayer;

import com.goxr3plus.streamplayer.enums.Status;
import com.mirkoebert.simplejavaradioplayer.player.ResilientStreamPlayer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Observable;
import java.util.prefs.Preferences;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerService extends Observable {

    private final ResilientStreamPlayer player;
    private final StationService stationService;
    private final PlayListService playListService;
    private Preferences prefs = Preferences.userNodeForPackage(getClass());

    void playButtonClicked() {
        log.info("play or stop");
        final Status playerStatus = player.getStatus();
        switch (playerStatus) {
            case PLAYING -> {
                log.info("Stop");
                setStatus("Player stoped");
                player.stop();
            }
            case NOT_SPECIFIED, STOPPED -> {
                log.info("Start last station");
                setStatus("Player started with last station: " + stationService.getStationsNames()[stationService.getSelectedStationIndex()]);
                URL currenStationUrl = stationService.getSelectedStationPlsUrl();
                log.info("Start station {}", currenStationUrl);
                play(currenStationUrl);
            }
            default -> log.warn("Not supported operation for player status {}", playerStatus);
        }
    }

    void listItemSelected(int selectedIndex) {
        log.info("listItemSelected {}", selectedIndex);
        setStatus("Play station: " + stationService.getStationsNames()[selectedIndex]);
        stationService.setSelectedStationIndex(selectedIndex);
        final URL nextStationUrl = stationService.getSelectedStationPlsUrl();
        final Status playerStatus = player.getStatus();
        switch (playerStatus) {
            case PLAYING -> {
                log.info("Switching to other station {}", nextStationUrl);
                player.stop();
                play(nextStationUrl);
            }
            case NOT_SPECIFIED, STOPPED -> {
                log.info("Player is not playing. Just start new station {}", nextStationUrl);
                play(nextStationUrl);
            }
            default -> log.warn("unsupported operation for player status {}", playerStatus);
        }

    }

    private void play(final URL nextStationUrl) {
        final URL stream = playListService.getAudioStreamURL(nextStationUrl);
        player.playStream(stream);
    }


    @PostConstruct
    void preLoadLastPls() {
        // TODO put into a own thread to unblock gui
        final URL preloadStationUrl = stationService.getSelectedStationPlsUrl();
        log.info("PostConstruct: preload latest station {} playlist for faster first start", preloadStationUrl);
        playListService.getAudioStreamURL(preloadStationUrl);
    }

    public void shutDown() {
        log.info("See You Space Cowboy");
        player.stop();
    }

    public void setStatus(String status) {
        setChanged();
        notifyObservers(status);
    }
}
