package com.atronandbeyond;

import com.atronandbeyond.Dao.DBConnection;
import com.atronandbeyond.Data.Album;
import com.atronandbeyond.Data.SearchResponse;
import com.atronandbeyond.Services.ConfigService;
import com.atronandbeyond.Services.DetailService;
import com.atronandbeyond.Services.SpotifyService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

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
        String AlbumsMenuItemStr = "Albums";

        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Settings");
        JMenuItem spotifyMenuItem = new JMenuItem(spotifyCredentialsMenuItemStr);
        JMenuItem mysqlMenuItem = new JMenuItem(mySQLCredentialsMenuItemStr);
        JMenuItem AlbumsMenuItem = new JMenuItem(AlbumsMenuItemStr);
        spotifyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog(jFrame, "Spotify Credentials", true);
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
                            configService.saveConfigToDisk();
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

        mysqlMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog(jFrame, "MySQL Credentials", true);
                jDialog.setPreferredSize(new Dimension(300, 200));

                JPanel mysqlBox = new JPanel();
                mysqlBox.setLayout(new BoxLayout(mysqlBox, BoxLayout.PAGE_AXIS));

                JButton testMysqlButton = new JButton("Test Connection");
                JButton applyMysqlButton = new JButton("Apply");

                JPanel mysqlSettingsPanel = new JPanel(new GridLayout(0, 2));
                JTextField hostnameTextfield = new JTextField(configService.getConfig().getMysqlHost(), 20);
                JTextField usernameField = new JTextField(configService.getConfig().getMysqlUsername(), 20);
                JTextField passwordField = new JTextField(configService.getConfig().getMysqlPassword(), 20);

                mysqlSettingsPanel.add(new JLabel("Hostname: "));
                mysqlSettingsPanel.add(hostnameTextfield);

                mysqlSettingsPanel.add(new JLabel("Username: "));
                mysqlSettingsPanel.add(usernameField);

                mysqlSettingsPanel.add(new JLabel("Password: "));
                mysqlSettingsPanel.add(passwordField);

                JCheckBox saveCredsCheckbox = new JCheckBox("Save to file");

                mysqlBox.add(mysqlSettingsPanel);
                mysqlBox.add(saveCredsCheckbox);
                mysqlBox.add(testMysqlButton);
                mysqlBox.add(applyMysqlButton);

                testMysqlButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("adding mysql creds to config service");

                        configService.getConfig().setMysqlHost(hostnameTextfield.getText());
                        configService.getConfig().setMysqlUsername(usernameField.getText());
                        configService.getConfig().setMysqlPassword(passwordField.getText());

                        boolean result = DBConnection.testConnection(hostnameTextfield.getText(), usernameField.getText(), passwordField.getText());
                        if (result) {
                            JOptionPane.showMessageDialog(jDialog, "Successful connection test!");
                        } else {
                            JOptionPane.showMessageDialog(jDialog, "Connection Error", "Failed Connection test :(", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                applyMysqlButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        configService.getConfig().setMysqlHost(hostnameTextfield.getText());
                        configService.getConfig().setMysqlUsername(usernameField.getText());
                        configService.getConfig().setMysqlPassword(passwordField.getText());
                        if (saveCredsCheckbox.isSelected()) {
                            configService.saveConfigToDisk();
                        }
                        jDialog.setVisible(false);
                    }
                });

                jDialog.add(mysqlBox);
                jDialog.pack();
                jDialog.setVisible(true);
            }
        });

        AlbumsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog(jFrame, "Album Table", true);
                jDialog.setPreferredSize(new Dimension(1200, 600));

                String[] columnNames = {"id", "name", "ReleaseDate", "totaltracks", "createDate"};
                Object[][] data = DBConnection.getAlbums(configService.getConfig());
                JTable table = new JTable(data, columnNames);
                jDialog.setTitle(DBConnection.getNumberAlbums(configService.getConfig()) + " Albums in DB");

                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);

                jDialog.add(scrollPane);
                jDialog.pack();
                jDialog.setVisible(true);
            }
        });

        jMenu.add(spotifyMenuItem);
        jMenu.add(mysqlMenuItem);
        jMenuBar.add(jMenu);
        jMenuBar.add(AlbumsMenuItem);

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
                JOptionPane.showMessageDialog(jFrame, "Could not connect to Spotify", "Error with Spotify API", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });

        // add to database button
        JPanel bottomPanel = new JPanel();
        JButton addToDatabaseButton = new JButton("Add to database");
        addToDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DBConnection.addAlbum(detailService.getCurrentAlbum(), configService.getConfig());
                    JOptionPane.showMessageDialog(jFrame, "Added to database: " + detailService.getCurrentAlbum().getId());
                } catch (SQLIntegrityConstraintViolationException ex) {
                    JOptionPane.showMessageDialog(jFrame, "Id already exists", "Failed to add!", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(jFrame, "Could not add to MySQL", "Failed to add!", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        bottomPanel.add(addToDatabaseButton);

        // search bar
        JPanel top = new JPanel(new FlowLayout());
        top.add(jLabel);
        top.add(textField);
        top.add(jButton);

        jFrame.setJMenuBar(jMenuBar);
        jFrame.add(top, BorderLayout.PAGE_START);
        jFrame.add(listScroller, BorderLayout.LINE_START);
        jFrame.add(detailPanelVertical, BorderLayout.CENTER);
        jFrame.add(bottomPanel, BorderLayout.PAGE_END);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}