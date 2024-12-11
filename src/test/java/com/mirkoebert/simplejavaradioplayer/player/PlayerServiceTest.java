package com.mirkoebert.simplejavaradioplayer.player;

import com.goxr3plus.streamplayer.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({PlayerService.class, StationService.class})
class PlayerServiceTest {

    @MockitoBean
    private ResilientStreamPlayer player;
    @Autowired
    private StationService stationService;
    @MockitoBean
    private PlayListService playListService;

    @Autowired
    private PlayerService cut;

    @Test
    void simple() {
        assertThat(cut).isNotNull();
    }

    @Test
    void playButtonClickedPlay() {
        when(player.getStatus()).thenReturn(Status.STOPPED);
        cut.playButtonClicked();

        InOrder inOrder = Mockito.inOrder(player, playListService);
        inOrder.verify(playListService).getAudioStreamURL(any());
        inOrder.verify(player).playStream(any());
    }

    @Test
    void playButtonClickedStop() {
        when(player.getStatus()).thenReturn(Status.PLAYING);
        cut.playButtonClicked();
        verify(player).stop();
    }

    @Test
    void listItemSelected() {
        when(player.getStatus()).thenReturn(Status.PLAYING);
        cut.listItemSelected(1);

        InOrder inOrder = Mockito.inOrder(player, playListService);
        inOrder.verify(player).stop();
        inOrder.verify(playListService).getAudioStreamURL(any());
        inOrder.verify(player).playStream(any());
    }
}