package com.mirkoebert.simplejavaradioplayer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
class StationPreLoader {

    private final StationService stationService;
    private final PlayListService playListService;

    void loadAllAudioStreamUrls() {
        Arrays.stream(stationService.getStationsPls()).map(stationService::getStationPlsUrl).forEach(playListService::getAudioStreamURL);
    }

}
