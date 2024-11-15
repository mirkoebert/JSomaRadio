package com.mirkoebert.jsomaradio.player;

import com.goxr3plus.streamplayer.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResilientStreamPlayer {

    private final Mp3StreamPlayer mp3StreamPlayer;
    private InputStream in;
    private URL audioStreamUrl;

    public void playStream(final URL audioStreamUrl) {
        this.audioStreamUrl = audioStreamUrl;
        openNewPlayerWithNewStream();
    }

    @Scheduled(fixedRate = 2000)
    void watchDog() {
        final Status playerStatus = mp3StreamPlayer.getStatus();
        switch (playerStatus) {
            case PLAYING, PAUSED -> {
                try {
                    if (in == null) {
                        log.warn("In stream is null");
                    } else if (in.available() > 0) {
                        log.debug("In stream is ok");
                    } else {
                        log.warn("In stream is not ok");
                        openNewPlayerWithNewStream();
                    }
                } catch (IOException e) {
                    log.warn("Can't determine stream {}", e.getMessage());
                }
            }
            case STOPPED -> {
                log.info("Player state STOPPED, nothing to do");
            }
            default -> {
                log.info("Unsupported player state {}", playerStatus);
            }

        }
    }

    private void openNewPlayerWithNewStream() {
        try {
            in = audioStreamUrl.openStream();
            mp3StreamPlayer.playStream(in);
        } catch (IOException e) {
            log.error("Can't play stream: {}", audioStreamUrl);
        }
    }

    public void stop() {
        mp3StreamPlayer.stop();
        try {
            in.close();
        } catch (IOException e) {
            log.warn("Can't close stream after stopping the player");
        }
    }

    public Status getStatus() {
        return mp3StreamPlayer.getStatus();
    }
}
