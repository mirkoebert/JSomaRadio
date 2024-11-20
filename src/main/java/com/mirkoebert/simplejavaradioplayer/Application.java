package com.mirkoebert.simplejavaradioplayer;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class Application extends JFrame {

    private final StationService stationService = new StationService();
    private final String nameAndVersion;
    @Autowired
    private PlayerService playerService;
    private JDialog donationBox;
    private JDialog aboutBox;

    public Application() {
        nameAndVersion = getClass().getPackage().getImplementationTitle() + " " + getClass().getPackage().getImplementationVersion();
        initUI();
        log.info(nameAndVersion);
        Thread stopPlayerHook = new Thread(() -> playerService.shutDown());
        Runtime.getRuntime().addShutdownHook(stopPlayerHook);
    }

    public static void main(String[] args) {
        log.info("Welcome on Board of the Bebop");

        var ctx = new SpringApplicationBuilder(Application.class).headless(false).web(WebApplicationType.NONE)
                .run(args);

        EventQueue.invokeLater(() -> {
            var ex = ctx.getBean(Application.class);
            ex.setVisible(true);
        });
    }

    private void initUI() {
        var playButton = new JButton("Play");
        playButton.addActionListener((ActionEvent event) -> playerService.playButtonClicked());

        var quitButton = new JButton("Quit");
        quitButton.addActionListener((ActionEvent event) -> System.exit(0));

        var donateButton = new JButton("Donate");
        donateButton.addActionListener((ActionEvent event) -> openSomaFmDonateLinkInDefaultBrowser());

        var aboutButton = new JButton("About");
        aboutButton.addActionListener((ActionEvent event) -> aboutBox.setVisible(true));

        final JList<String> stationList = new JList<>(stationService.getStationsNames());
        stationList.addListSelectionListener((ListSelectionEvent event) -> {
            if (!event.getValueIsAdjusting()) {
                log.info("Select {}", stationList.getSelectedIndex());
                playerService.listItemSelected(stationList.getSelectedIndex());
            }

        });
        JScrollPane jsp = new JScrollPane(stationList);
        createLayout(jsp, quitButton, playButton, donateButton, aboutButton);

        setTitle(getClass().getPackage().getImplementationTitle());
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        playButton.requestFocus();
    }

    private void createLayout(final JComponent main, final JButton... buttons) {
        var pane = getContentPane();
        var gl = new BorderLayout();
        pane.setLayout(gl);

        FlowLayout fl = new FlowLayout();
        JPanel buttonRow = new JPanel(fl);
        Arrays.stream(buttons).forEach(b -> {
            buttonRow.add(b);
            b.setBorder(BorderFactory.createEmptyBorder(12, 20, 10, 20));
        });
        var bp = new JPanel();
        bp.add(buttonRow);

        pane.add(bp, BorderLayout.NORTH);


        var lp = new JPanel();
        lp.add(main);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 12, 20));
        main.setPreferredSize(new Dimension(350, 400));
        pane.add(lp, BorderLayout.CENTER);

        val pane1 = new JOptionPane("Please support Soma FM and go to https://somafm.com/support/", INFORMATION_MESSAGE);
        donationBox = pane1.createDialog(pane, "Donate");

        val pane2 = new JOptionPane(nameAndVersion + "\nhttps://github.com/mirkoebert/SimpleJavaRadioPlayerForSomaFM", INFORMATION_MESSAGE);
        aboutBox = pane2.createDialog(pane, "About");
    }

    private void openSomaFmDonateLinkInDefaultBrowser() {
        URI uri;
        try {
            uri = new URI("https://somafm.com/support/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(uri);
        } catch (Exception ex) {
            log.warn("Fallback: show info box");
            donationBox.setVisible(true);
        }
    }

}
