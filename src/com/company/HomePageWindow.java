package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageWindow extends JFrame implements ActionListener
{
    private JMenuBar menuBar;
    private JPanel p;
    private JPanel pCenter;
    private JButton toSelectPlayWindow, toAddDeleteWindow, toPhotosWindow, toPlaylistsWindow,
            toQueuePreviewWindow, toSongsWindow;
    private JLabel searchT, selectYourMoodT;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuPlaylists, menuSongs, menuQueue, menuHome;
    // nameOfPlaylistT, nameOfSongT -> these two change according to what is playingggg


    public HomePageWindow()
    {
        super("Audio Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        menuSelect = new JMenuItem("Select What to Play Page");
        menuAddDelete = new JMenuItem("Add / Delete Page");
        menuPhotos = new JMenuItem("Photos Page");
        menuPlaylists = new JMenuItem("Playlists Page");
        menuSongs = new JMenuItem("Songs Page");
        menuQueue = new JMenuItem("Queue Page");
        menuHome = new JMenuItem("Home Page");
        // add the action listeners to lead to the other windows
        menuBar.add(menuAddDelete); menuBar.add(menuPhotos); menuBar.add(menuQueue);
        menuBar.add(menuSongs); menuBar.add(menuSelect); menuBar.add(menuPlaylists);
        menuBar.add(menuHome);
        setJMenuBar(menuBar);

        // adding a panel so I can add buttons here, buttons weren't moving without this :/
        // i think the layout is too small and not positioned correctly
        // i need to move it so my buttons show up
        pCenter = new JPanel();
        pCenter.setLayout(null);
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        // get the positions right
        toSelectPlayWindow = new JButton("Select a Song or Playlist");
        toSelectPlayWindow.setBounds(550, 50, 225, 35);
        pCenter.add(toSelectPlayWindow);

        toAddDeleteWindow = new JButton("Add / Delete");
        toAddDeleteWindow.setBounds(30, 90, 100, 35);
        pCenter.add(toAddDeleteWindow);

        toSongsWindow = new JButton("Songs");
        toSongsWindow.setBounds(30, 400, 100, 35);
        pCenter.add(toSongsWindow);

        toPlaylistsWindow = new JButton("Playlists");
        toPlaylistsWindow.setBounds(30, 500, 100, 35);
        pCenter.add(toPlaylistsWindow);

        toQueuePreviewWindow = new JButton("Queue");
        toQueuePreviewWindow.setBounds(1100, 400, 100, 35);
        pCenter.add(toQueuePreviewWindow);

        toPhotosWindow = new JButton("Photos");
        toPhotosWindow.setBounds(1100, 90, 100, 35);
        pCenter.add(toPhotosWindow);

        searchT = new JLabel("Search");
        searchT.setBounds(30, 150, 100, 35);
        pCenter.add(searchT);

        selectYourMoodT = new JLabel("Select Your Mood");
        selectYourMoodT.setBounds(1100, 500, 150, 35);
        pCenter.add(selectYourMoodT);

        // is the search thingy where the user writes a "text field"?

        setVisible(true);
    }

    @Override
    // i found this here: https://stackoverflow.com/questions/9569700/java-call-method-via-jbutton
    public void actionPerformed(ActionEvent e)
    {
        JFrame SelectPlayWindow = new SelectPlayWindow(); // open another JFrame
        SelectPlayWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed2(ActionEvent e)
    {
        JFrame AddDeleteWindow = new AddDeleteWindow(); // open another JFrame
        AddDeleteWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed3(ActionEvent e)
    {
        JFrame SongsWindow = new SongsWindow(); // open another JFrame
        SongsWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed4(ActionEvent e)
    {
        JFrame PlaylistsWindow = new PlaylistsWindow(); // open another JFrame
        PlaylistsWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed5(ActionEvent e)
    {
        JFrame PhotosWindow = new PhotosWindow(); // open another JFrame
        PhotosWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed6(ActionEvent e)
    {
        JFrame QueuePreviewWindow = new QueuePreviewWindow(); // open another JFrame
        QueuePreviewWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }
}