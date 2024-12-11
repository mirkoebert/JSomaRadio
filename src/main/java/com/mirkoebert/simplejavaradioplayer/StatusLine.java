package com.mirkoebert.simplejavaradioplayer;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

@Slf4j
public class StatusLine extends JLabel implements Observer {


    public StatusLine() {
        super("Ready to play your favorite music.", CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        log.info("Set status: {}", arg);
        try {
            if (arg == null) {
                log.warn("Can't set status null");
            } else {
                setText((String) arg);
            }
        } catch (Exception e) {
            log.warn("Can't set status {}", arg, e);
        }
    }
}
