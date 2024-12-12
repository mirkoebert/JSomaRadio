package com.mirkoebert.simplejavaradioplayer.player;

import com.goxr3plus.streamplayer.enums.Status;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(ResilientStreamPlayer.class)
class ResilientStreamPlayerTest {

    @MockitoBean
    private Mp3StreamPlayer mp3StreamPlayer;
    @Autowired
    private ResilientStreamPlayer cut;

    @SneakyThrows
    @Test
    void playStream() {
        cut.playStream(null);

        //cut.playStream(new URL("malformed url"));
        cut.playStream(new URL("http://ebert-p.com"));
        verify(mp3StreamPlayer).playStream(any());
    }

    @Test
    void watchDogPlayerStatusNull() {
        cut.watchDog();
    }

    @Test
    void watchDog() {
        when(mp3StreamPlayer.getStatus()).thenReturn(Status.PLAYING);
        cut.watchDog();
    }

    @Test
    void stop() {
    }
}