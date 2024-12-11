package com.mirkoebert.simplejavaradioplayer;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatusLineTest {

    @Test
    void updateNull() {
        val cut = new StatusLine();
        final String orginMessage = cut.getText();
        assertThat(orginMessage).hasToString("Ready to play your favorite music.");

        cut.update(null, null);

        assertThat(cut.getText()).hasToString(orginMessage);
    }
}