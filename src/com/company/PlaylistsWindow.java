package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlaylistsWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuHome, menuSongs, menuQueue;
    private JPanel pCenter;
    private JLabel playlistsT;
    private JTextField searchBox;
    private ImageIcon addPlaylistIcon, deletePlaylistIcon;
    private JButton addPlaylistButton, deletePlaylistButton;

    public PlaylistsWindow() throws IOException
    {
        super("Playlists");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pCenter = new JPanel();
        pCenter.setLayout(null);
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Playlists");

        menuSelect = new JMenuItem("Select What to Play");
        menuSelect.addActionListener(this::actionPerformed);

        menuAddDelete = new JMenuItem("Add / Delete");
        menuAddDelete.addActionListener(this::actionPerformed2);

        menuPhotos = new JMenuItem("Photos");
        menuPhotos.addActionListener(this::actionPerformed5);

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
        fileMenu.add(menuSelect);
        fileMenu.add(menuHome);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        playlistsT = new JLabel("Playlists");
        playlistsT.setBounds(30, 150, 100, 35);
        pCenter.add(playlistsT);

        BufferedImage searchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel searchIconLabel = new JLabel(new ImageIcon(searchIcon));
        searchIconLabel.setBounds(30, 200, 30, 30);
        pCenter.add(searchIconLabel);

        searchBox = new JTextField();
        searchBox.setBounds(70, 200, 90, 30);
        pCenter.add(searchBox);

        addPlaylistIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addPlaylistButton = new JButton(addPlaylistIcon);
        addPlaylistButton.setBounds(100, 300, 50, 30);
        pCenter.add(addPlaylistButton);

        deletePlaylistIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deletePlaylistButton = new JButton(deletePlaylistIcon);
        deletePlaylistButton.setBounds(100, 350, 50, 30);
        pCenter.add(deletePlaylistButton);

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