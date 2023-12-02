package com.company;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

public class AddDeleteWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuPhotos, menuPlaylists, menuSongs, menuHome;
    private JPanel pCenter;
    private JLabel songsT;
    private JTextField songSearchBox, instrumentsSearchBox, themesSearchBox;
    private ImageIcon addSongIcon, deleteSongIcon, addInstrumentIcon, deleteInstrumentIcon,
            addThemeIcon, deleteThemeIcon;
    private JButton addSongButton, deleteSongButton, addInstrumentButton, deleteInstrumentButton,
            addThemeButton, deleteThemeButton, validateButton, goHome;
    private JTextField inputField;
    private String[] newNameArr = new String[1];
    private JScrollPane listScroller;
    PlaylistsWindow playlistObject;
    HomePageMethods homePageMethodsObject;
    AddingDeleting addingDeletingObject = new AddingDeleting();

    public AddDeleteWindow() throws IOException
    {
        // if i add three songs in a row, it adds the same to the list three times
        // If I add more than one song, the following ones are not added as wav files -> whenever there
        // clean the input field after using it
        super("Add/Delete");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pCenter = new JPanel();
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(pCenter);
        pCenter.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        goHome = new JButton("Return to Home Page");
        goHome.setBounds(500, 350, 300, 50);
        goHome.addActionListener(this::toHomePage);
        pCenter.add(goHome);

        // the text field I'm doing this with can be the specific for each type of element
        // use the specific one for song, instrument, wtv
        inputField = new JTextField(5);
        inputField.setMaximumSize(new Dimension(200, 30));
        validateButton = new JButton("Confirm");
        validateButton.addActionListener(this::validateButtonAction);

        inputField.setEnabled(false);
        validateButton.setEnabled(false);

        playlistObject = new PlaylistsWindow();
        playlistObject.dispose();

        homePageMethodsObject = new HomePageMethods();

        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Add / Delete Page");

        menuHome = new JMenuItem("Home Page");
        menuHome.addActionListener(this::actionPerformed2);

        menuPhotos = new JMenuItem("Photos");
        // menuPhotos.addActionListener(this::actionPerformed5);

        menuPlaylists = new JMenuItem("Playlists");
        menuPlaylists.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        // fileMenu.add(menuHome);
        fileMenu.add(menuPhotos);
        fileMenu.add(menuSongs);
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        songsT = new JLabel("Songs");

        // song search
        //<editor-fold desc="Song Add/Delete">
        BufferedImage songSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel songSearchIconLabel = new JLabel(new ImageIcon(songSearchIcon));
        // pCenter.add(songSearchIconLabel);

        songSearchBox = new JTextField();
        // pCenter.add(songSearchBox);

        addSongIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addSongButton = new JButton(addSongIcon);
        addSongButton.addActionListener(this::actionPerformedAddSong);
        // pCenter.add(addSongButton);

        deleteSongIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteSongButton = new JButton(deleteSongIcon);
        deleteSongButton.addActionListener((ActionEvent e) -> addingDeletingObject.deleteSong());
        // pCenter.add(deleteSongButton);
        //</editor-fold>

        // instrument tag search
        //<editor-fold desc="Instrument Tag Add/Delete">
        BufferedImage instrumentSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel instrumentSearchIconLabel = new JLabel(new ImageIcon(instrumentSearchIcon));
        // pCenter.add(instrumentSearchIconLabel);

        instrumentsSearchBox = new JTextField();
        instrumentsSearchBox.setMaximumSize(new Dimension(200, 30));
        // pCenter.add(instrumentsSearchBox);

        addInstrumentIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addInstrumentButton = new JButton(addInstrumentIcon);
        // pCenter.add(addInstrumentButton);

        deleteInstrumentIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteInstrumentButton = new JButton(deleteInstrumentIcon);
        // pCenter.add(deleteInstrumentButton);
        //</editor-fold>

        // theme tag search
        //<editor-fold desc="Theme Tag Add/Delete">
        BufferedImage themeSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel themeSearchIconLabel = new JLabel(new ImageIcon(themeSearchIcon));
        // pCenter.add(themeSearchIconLabel);

        themesSearchBox = new JTextField();
        themesSearchBox.setMaximumSize(new Dimension(200, 30));
        // pCenter.add(themesSearchBox);

        addThemeIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addThemeButton = new JButton(addThemeIcon);
        // pCenter.add(addThemeButton);

        deleteThemeIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteThemeButton = new JButton(deleteThemeIcon);
        // pCenter.add(deleteThemeButton);
        //</editor-fold>

        listScroller = new JScrollPane(addingDeletingObject.listSongs);
        listScroller.setMaximumSize(new Dimension(250, 300));

        //<editor-fold desc="LAYOUT">
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(songsT))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(songSearchIconLabel)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(inputField)
                                        .addComponent(listScroller))

                                .addComponent(validateButton)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(addSongButton)
                                        .addComponent(deleteSongButton)))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(songsT))
                        .addGroup(layout.createParallelGroup()
                                .addComponent(songSearchIconLabel)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(inputField)
                                        .addComponent(listScroller)
                                )
                                .addComponent(validateButton)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(addSongButton)
                                        .addComponent(deleteSongButton)))
        );
        //</editor-fold>

        setVisible(true);
    }

    //<editor-fold desc="Menu bar actions">
    public void actionPerformed2(ActionEvent e)
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
        dispose(); // close page
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
    //</editor-fold>

    public void toHomePage(ActionEvent e)
    {
        try
        {
            HomePageWindow homePageWindow = new HomePageWindow();
            homePageWindow.setVisible(true);
            dispose();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }

    public void actionPerformedAddSong(ActionEvent e)
    {
        addingDeletingObject.addSong();
        // I need to get the user's input as a string and substitute in place of "NewName"
        inputField.setEnabled(true);
        validateButton.setEnabled(true);
    }

    void validateButtonAction(ActionEvent e)
    {
        String newNameStr = inputField.getText();
        addingDeletingObject.validateButton(newNameStr);

        inputField.setEnabled(false);
        validateButton.setEnabled(false);
        inputField.setText("");
    }
}