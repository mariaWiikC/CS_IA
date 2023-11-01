package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SelectPlaylistWindow extends JFrame implements Runnable
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuHome, menuSongs, menuQueue;
    private JPanel pCenter;
    protected ArrayList<String> playlistFileContent, songsPaths = new ArrayList<>();
    PlaylistMethods playlistsObject;
    HomePageMethods homePageObject;

    public SelectPlaylistWindow() throws IOException
    {

        //<editor-fold desc="General Frame stuff">
        super("Select Playlist");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pCenter = new JPanel();
        pCenter.setLayout(null);
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(pCenter);
        pCenter.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        //</editor-fold>

        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Playlists");

        menuAddDelete = new JMenuItem("Add / Delete");
        menuAddDelete.addActionListener(this::actionPerformed2);

        menuPhotos = new JMenuItem("Photos");
        // menuPhotos.addActionListener(this::actionPerformed5);

        menuHome = new JMenuItem("Home Page");
        menuHome.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        menuQueue = new JMenuItem("Queue");
        menuQueue.addActionListener(this::actionPerformed6);

        fileMenu.add(menuAddDelete);
        fileMenu.add(menuPhotos);
        fileMenu.add(menuQueue);
        fileMenu.add(menuSongs);
        fileMenu.add(menuHome);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        playlistsObject = new PlaylistMethods();
        playlistsObject.setPriority(10);

        homePageObject = new HomePageMethods();
        homePageObject.setPriority(9);

        homePageObject.smtElse();

        //<editor-fold desc="LAYOUT">
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(playlistsObject.listScroller)
                        .addComponent(playlistsObject.playButton)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(playlistsObject.listScroller)
                        .addComponent(playlistsObject.playButton)
        );
        //</editor-fold>
    }

    //<editor-fold desc="Menu bar actions">
    public void actionPerformed2(ActionEvent e)
    {
        JFrame AddDeleteWindow = null; // open another JFrame
        try
        {
            AddDeleteWindow = new AddDeleteWindow();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
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
        JFrame HomePageWindow = null; // open another JFrame
        try
        {
            HomePageWindow = new HomePageWindow();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        HomePageWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    /*
    public void actionPerformed5(ActionEvent e)
    {
        JFrame PhotosWindow = new PhotosWindow(); // open another JFrame
        PhotosWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

     */

    public void actionPerformed6(ActionEvent e)
    {
        JFrame QueuePreviewWindow = new QueuePreviewWindow(); // open another JFrame
        QueuePreviewWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    @Override
    public void run()
    {
        Thread t = Thread.currentThread();
        System.out.print(t.getName());

        //checks if this thread is alive
        System.out.println(", status = " + t.isAlive());
    }
    //</editor-fold>
}
