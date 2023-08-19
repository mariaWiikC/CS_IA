package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class PhotosWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuAddDelete, menuHome, menuPlaylists, menuSongs, menuQueue;

    public PhotosWindow()
    {
        super("Photos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Photos");

        menuSelect = new JMenuItem("Select What to Play");
        menuSelect.addActionListener(this::actionPerformed);

        menuAddDelete = new JMenuItem("Add / Delete");
        menuAddDelete.addActionListener(this::actionPerformed2);

        menuHome = new JMenuItem("Home Page");
        menuHome.addActionListener(this::actionPerformed5);

        menuPlaylists = new JMenuItem("Playlists");
        menuPlaylists.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        menuQueue = new JMenuItem("Queue");
        menuQueue.addActionListener(this::actionPerformed6);

        fileMenu.add(menuAddDelete);
        fileMenu.add(menuHome);
        fileMenu.add(menuQueue);
        fileMenu.add(menuSongs);
        fileMenu.add(menuSelect);
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        JFrame SelectPlayWindow = new SelectPlayWindow(); // open another JFrame
        SelectPlayWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

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
        JFrame PlaylistsWindow = null; // open another JFrame
        try
        {
            PlaylistsWindow = new PlaylistsWindow();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        PlaylistsWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed5(ActionEvent e)
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

    public void actionPerformed6(ActionEvent e)
    {
        JFrame QueuePreviewWindow = new QueuePreviewWindow(); // open another JFrame
        QueuePreviewWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }
}