package org.example;

import org.example.Data.Album;
import org.example.Data.SearchResponse;
import org.example.Services.SpotifyService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class App {
    String title = "RetroRack";
    SpotifyService service;

    App(String[] args) {
        service = new SpotifyService();
//        service.setCredentials(clientId, clientSecret);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App(args).show();
            }
        });
    }


    public void show() {

        JFrame jFrame = new JFrame(title);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(800, 600));

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
                JTextField clientIdTextField = new JTextField(service.getClientId(),20);
                JPanel clientIdPanel = new JPanel(new FlowLayout());
                clientIdPanel.add(clientIdLabel);
                clientIdPanel.add(clientIdTextField);

                JLabel clientSecretLabel = new JLabel("Client Secret: ");
                JTextField clientSecretField = new JTextField(service.getClientSecret(), 20);
                JPanel clientSecretPanel = new JPanel(new FlowLayout());
                clientSecretPanel.add(clientSecretLabel);
                clientSecretPanel.add(clientSecretField);

                JButton saveCredentials = new JButton("Apply");
                saveCredentials.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("saving credentials");
                        service.setCredentials(clientIdTextField.getText(), clientSecretField.getText());
                        jDialog.setVisible(false);
                    }
                });

                JPanel credentialsBox = new JPanel();
                credentialsBox.setLayout(new BoxLayout(credentialsBox, BoxLayout.PAGE_AXIS));
                credentialsBox.add(clientIdPanel);
                credentialsBox.add(clientSecretPanel);
                credentialsBox.add(saveCredentials);

                jDialog.add(credentialsBox);
                jDialog.pack();
                jDialog.setVisible(true);

            }
        });
        jMenu.add(spotifyMenuItem);
        jMenu.add(mysqlMenuItem);
        jMenuBar.add(jMenu);

        JList jlist = new JList();
        jlist.setFixedCellWidth(200);
        JLabel jLabel = new JLabel("Search: ");
        JTextField textField = new JTextField(20);
        JButton jButton = new JButton("Go");
        jButton.addActionListener(e -> {
            try {
                if (service.hasCredentials()) {
                    String token = service.getToken();
                    String q = textField.getText();
                    String response = service.search(token, q);
                    SearchResponse parsedResponse = service.parseSearchServiceResponse(response);
                    Object[] list = parsedResponse.getAlbums().stream().map(Album::getName).toList().toArray();
                    jlist.setListData(list);
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

        // list/detail view
//        JPanel bottomHalf = new JPanel();
//        bottomHalf.setLayout(new BoxLayout(bottomHalf, BoxLayout.X_AXIS));
//        bottomHalf.add(jlist);

        // separate search from list/detail view
//        JPanel topBottom = new JPanel();
//        topBottom.setLayout(new BoxLayout(topBottom, BoxLayout.Y_AXIS));
//        topBottom.add(top);
//        topBottom.add(bottomHalf);

        jFrame.setJMenuBar(jMenuBar);
        jFrame.add(top, BorderLayout.PAGE_START);
        jFrame.add(jlist, BorderLayout.LINE_START);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}