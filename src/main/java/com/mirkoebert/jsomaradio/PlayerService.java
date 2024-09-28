package com.mirkoebert.jsomaradio;

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

    private URL currenStationUrl;
    private URL nextStationUrl;

    private final Mp3StreamPlayer player;
    private Preferences prefs = Preferences.userNodeForPackage(getClass());
    private final StationService stationService;
    private final PlayListService playListService;

    void buttonClicked(){
        log.info("play or stop");
        if (currenStationUrl == null){
            currenStationUrl = stationService.getSelectedStationPlsUrl();
            log.info("Start station {}", currenStationUrl);
            final URL stream = playListService.getAudioStreamURL(currenStationUrl);
            player.playStream(stream);
        } else {
            stop();
        }
    }

    void listItemSelected(int selectedIndex){
        log.info("listItemSelected {}", selectedIndex);
        nextStationUrl = stationService.getStationPlsUrl(selectedIndex);
        if (currenStationUrl == null){
            log.info("Start station {}", nextStationUrl);
            URL stream = playListService.getAudioStreamURL(nextStationUrl);
            player.playStream(stream);
            currenStationUrl = nextStationUrl;
            stationService.setSelectedStationIndex(selectedIndex);
        } else if (nextStationUrl.toString().equals(currenStationUrl.toString())){
            stop();
        } else {
            log.info("Switch to station {}", nextStationUrl);
            currenStationUrl = nextStationUrl;
            URL stream = playListService.getAudioStreamURL(nextStationUrl);
            player.stop();
            player.playStream(stream);
            stationService.setSelectedStationIndex(selectedIndex);
        }
    }

    private void stop() {
        log.info("Stop station {}", currenStationUrl);
        player.stop();
        nextStationUrl = currenStationUrl;
        currenStationUrl = null;
    }

    @PostConstruct
    void preLoadLastPls(){
        // TODO put into a own thread to unblock gui    
        final URL preloadStationUrl = stationService.getSelectedStationPlsUrl();
        log.info("PostConstruct: preload latest station {} playlist for faster start", preloadStationUrl);
        playListService.getAudioStreamURL(preloadStationUrl);
    }
}
