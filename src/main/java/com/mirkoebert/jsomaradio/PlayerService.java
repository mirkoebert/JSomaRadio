package com.mirkoebert.jsomaradio;

import com.goxr3plus.streamplayer.enums.Status;
import com.mirkoebert.jsomaradio.player.ResilientStreamPlayer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.prefs.Preferences;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerService {

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
                player.stop();
            }
            case NOT_SPECIFIED, STOPPED -> {
                log.info("Start last station");
                URL currenStationUrl = stationService.getSelectedStationPlsUrl();
                log.info("Start station {}", currenStationUrl);
                final URL stream = playListService.getAudioStreamURL(currenStationUrl);
                player.playStream(stream);
            }
            default -> log.warn("Not supported operation for player status {}", playerStatus);
        }
    }

    void listItemSelected(int selectedIndex) {
        log.info("listItemSelected {}", selectedIndex);
        stationService.setSelectedStationIndex(selectedIndex);
        final URL nextStationUrl = stationService.getSelectedStationPlsUrl();
        final Status playerStatus = player.getStatus();
        switch (playerStatus) {
            case PLAYING -> {
                log.info("Switching to other station {}", nextStationUrl);
                player.stop();
                URL stream = playListService.getAudioStreamURL(nextStationUrl);
                player.playStream(stream);
            }
            case NOT_SPECIFIED, STOPPED -> {
                log.info("Player is not playing. Just start new station {}", nextStationUrl);
                URL stream = playListService.getAudioStreamURL(nextStationUrl);
                player.playStream(stream);
            }
            default -> log.warn("unsopprted operation for player status {}", playerStatus);
        }

    }


    @PostConstruct
    void preLoadLastPls() {
        // TODO put into a own thread to unblock gui
        final URL preloadStationUrl = stationService.getSelectedStationPlsUrl();
        log.info("PostConstruct: preload latest station {} playlist for faster first start", preloadStationUrl);
        playListService.getAudioStreamURL(preloadStationUrl);
    }

    public void shutDown() {
        player.stop();
    }
}
