package com.mirkoebert.jsomaradio;

import com.goxr3plus.streamplayer.enums.Status;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class PlayerV2 extends StreamPlayer implements StreamPlayerListener {

    private final PlayListService playListService;

    PlayerV2(PlayListService playListService) {
        this.playListService = playListService;
        addStreamPlayerListener(this);
    }

    void playIt(String selectStationPlsUrl) {
        log.info("Play Button pressed");
        if (Objects.requireNonNull(getStatus()) == Status.PLAYING) {
            stop();
        } else {
            try {
                final URL audioFilePath = playListService.getAudioStreamURL(selectStationPlsUrl);
                playIt2(audioFilePath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    void playIt2(final URL audioFilePath) throws StreamPlayerException, IOException {
        log.info("playIt2: {}", audioFilePath);
        final InputStream in = audioFilePath.openStream();
        final BufferedInputStream buf = new BufferedInputStream(in);
        open(buf);
        play();
    }

    @Override
    public void opened(Object dataSource, Map<String, Object> properties) {

    }

    @Override
    public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {

    }

    @Override
    public void statusUpdated(StreamPlayerEvent event) {

    }
}
