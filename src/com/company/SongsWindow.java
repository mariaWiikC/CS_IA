package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class SongsWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuPlaylists, menuSongs, menuHome;
    private JLabel songsT;
    private JPanel pCenter;
    private JList listSongs;
    private DefaultListModel listModel;
    private JScrollPane listScroller;

    public SongsWindow()
    {
        super("Songs");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pCenter = new JPanel();

        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Songs");

        menuSelect = new JMenuItem("Select What to Play");
        menuSelect.addActionListener(this::actionPerformed);

        menuAddDelete = new JMenuItem("Add / Delete");
        menuAddDelete.addActionListener(this::actionPerformed2);

        menuPhotos = new JMenuItem("Photos");
        menuPhotos.addActionListener(this::actionPerformed5);

        menuPlaylists = new JMenuItem("Playlists");
        menuPlaylists.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        menuHome = new JMenuItem("Home Page");
        menuHome.addActionListener(this::actionPerformed6);

        fileMenu.add(menuAddDelete);
        fileMenu.add(menuPhotos);
        fileMenu.add(menuHome);
        fileMenu.add(menuSongs);
        fileMenu.add(menuSelect);
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        // WHAT I WANT NOW:
        // When the user selects one of the songs from the list, they can see the info of the song
        // on the side
        // I'm assuming this means I need to create a class Songs, and then I'll have a bunch of stuff here
        // like have a method .addMoodTags, and then I add the mood

        // I want to display all the uploaded songs
        String directorySongsFilePath = "src/songsFiles";

        //<editor-fold desc="Setting up the list of songs">
        File directorySongs = new File(directorySongsFilePath);
        File[] filesSongs = directorySongs.listFiles(File::isFile);

        listModel = new DefaultListModel();
        for (File f : filesSongs)
        {
            listModel.addElement(f.getName());
        }
        listSongs = new JList(listModel);
        listSongs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSongs.setLayoutOrientation(JList.VERTICAL);
        listSongs.setVisibleRowCount(-1);

        listScroller = new JScrollPane(listSongs);
        listScroller.setMaximumSize(new Dimension(250, 300));
        //</editor-fold>

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
        JFrame PhotosWindow = new PhotosWindow(); // open another JFrame
        PhotosWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed6(ActionEvent e)
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
}