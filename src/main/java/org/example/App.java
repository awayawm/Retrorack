package org.example;

import org.example.Data.Album;
import org.example.Data.SearchResponse;
import org.example.Services.ConfigService;
import org.example.Services.DetailService;
import org.example.Services.SpotifyService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class App {
    String title = "RetroRack";
    String configFile = "config.dat";
    SpotifyService spotifyService;
    ConfigService configService;
    DetailService detailService;


    App(String[] args) {
        configService = new ConfigService(configFile);
        spotifyService = new SpotifyService();
        detailService = new DetailService();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        SwingUtilities.invokeLater(() -> new App(args).show());
    }


    public void show() {

        JFrame jFrame = new JFrame(title);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(800, 600));


        // Menu Setup
        String spotifyCredentialsMenuItemStr = "Spotify Credentials";
        String mySQLCredentialsMenuItemStr = "MySQL Credentials";

        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Settings");
        JMenuItem spotifyMenuItem = new JMenuItem(spotifyCredentialsMenuItemStr);
        JMenuItem mysqlMenuItem = new JMenuItem(mySQLCredentialsMenuItemStr);
        spotifyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog(jFrame, "test", true);
                jDialog.setPreferredSize(new Dimension(300, 200));

                JLabel clientIdLabel = new JLabel("Client Id: ");
                JTextField clientIdTextField = new JTextField(configService.getConfig().getSpotifyClientId(), 20);
                JPanel clientIdPanel = new JPanel(new FlowLayout());
                clientIdPanel.add(clientIdLabel);
                clientIdPanel.add(clientIdTextField);

                JLabel clientSecretLabel = new JLabel("Client Secret: ");
                JTextField clientSecretField = new JTextField(configService.getConfig().getSpotifyClientSecret(), 20);
                JPanel clientSecretPanel = new JPanel(new FlowLayout());
                clientSecretPanel.add(clientSecretLabel);
                clientSecretPanel.add(clientSecretField);

                JCheckBox saveCredsCheckbox = new JCheckBox("Save to file");
                JButton saveCredentials = new JButton("Apply");
                saveCredentials.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("adding spotify creds to config service");

                        configService.getConfig().setSpotifyClientId(clientIdTextField.getText());
                        configService.getConfig().setSpotifyClientSecret(clientSecretField.getText());

                        if (saveCredsCheckbox.isSelected()) {
                            //todo save creds to file
                            configService.saveSpotifySettingsToConfig(clientIdTextField.getText(), clientSecretField.getText());
                        }

                        jDialog.setVisible(false);
                    }
                });

                JPanel credentialsBox = new JPanel();
                credentialsBox.setLayout(new BoxLayout(credentialsBox, BoxLayout.PAGE_AXIS));
                credentialsBox.add(clientIdPanel);
                credentialsBox.add(clientSecretPanel);
                credentialsBox.add(saveCredsCheckbox);
                credentialsBox.add(saveCredentials);

                jDialog.add(credentialsBox);
                jDialog.pack();
                jDialog.setVisible(true);

            }
        });

        jMenu.add(spotifyMenuItem);
        jMenu.add(mysqlMenuItem);
        jMenuBar.add(jMenu);

        // Information Detail
        JLabel albumImageLabel = new JLabel();
        JLabel albumNameLabel = new JLabel();
        JLabel releaseDateLabel = new JLabel();
        JLabel totalTracksLabel = new JLabel();
        JLabel albumIdLabel = new JLabel();
        JLabel artistsLabel = new JLabel();

        JPanel informationGird = new JPanel(new GridLayout(0, 2));

        informationGird.add(new JLabel("id"));
        informationGird.add(albumIdLabel);
        informationGird.add(new JLabel("name"));
        informationGird.add(albumNameLabel);
        informationGird.add(new JLabel("releaseDate"));
        informationGird.add(releaseDateLabel);
        informationGird.add(new JLabel("totalTracks"));
        informationGird.add(totalTracksLabel);
        informationGird.add(new JLabel("Artists"));
        informationGird.add(artistsLabel);

        JPanel detailPanelVertical = new JPanel();
        detailPanelVertical.setLayout(new BoxLayout(detailPanelVertical, BoxLayout.PAGE_AXIS));
        detailPanelVertical.add(albumImageLabel);
        detailPanelVertical.add(informationGird);

        // Selection List
        DefaultListModel defaultListModel = new DefaultListModel();
        JList jlist = new JList(defaultListModel);
        jlist.setFixedCellWidth(200);
        jlist.setLayoutOrientation(JList.VERTICAL);
        JScrollPane listScroller = new JScrollPane(jlist);
        jlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    detailService.setIndex(detailService.getIndex() == e.getLastIndex() ? e.getFirstIndex() : e.getLastIndex());
                    detailService.updateDetails(albumImageLabel, albumNameLabel, releaseDateLabel, totalTracksLabel, albumIdLabel, artistsLabel);
                }
            }
        });

        JLabel jLabel = new JLabel("Search: ");
        JTextField textField = new JTextField(20);

        JButton jButton = new JButton("Go");
        jButton.addActionListener(e -> {
            try {
                if (configService.hasSpotifyCredentials()) {
                    String token = spotifyService.getToken(
                            configService.getConfig().getSpotifyClientId(),
                            configService.getConfig().getSpotifyClientSecret()
                    );
                    String q = textField.getText();
                    String response = spotifyService.search(token, q);
                    SearchResponse parsedResponse = spotifyService.parseSearchServiceResponse(response);

                    defaultListModel.clear();
                    defaultListModel.addAll(parsedResponse.getAlbums().stream().map(Album::getName).toList());
                    detailService.setAlbums(parsedResponse.getAlbums());
                    detailService.setIndex(0);
                    jlist.setSelectedIndex(0);
                    detailService.updateDetails(albumImageLabel, albumNameLabel, releaseDateLabel, totalTracksLabel, albumIdLabel, artistsLabel);

                } else {
                    JOptionPane.showMessageDialog(jFrame, "Enter spotify credentials in settings");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        // search bar
        JPanel top = new JPanel(new FlowLayout());
        top.add(jLabel);
        top.add(textField);
        top.add(jButton);

        jFrame.setJMenuBar(jMenuBar);
        jFrame.add(top, BorderLayout.PAGE_START);
        jFrame.add(listScroller, BorderLayout.LINE_START);
        jFrame.add(detailPanelVertical, BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}