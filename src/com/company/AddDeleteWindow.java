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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AddDeleteWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuPhotos, menuPlaylists, menuSongs, menuQueue, menuHome;
    private JPanel pCenter;
    private JLabel songsT, tagsT, instrumentsT, themesT;
    private JTextField songSearchBox, instrumentsSearchBox, themesSearchBox;
    private ImageIcon addSongIcon, deleteSongIcon, addInstrumentIcon, deleteInstrumentIcon,
            addThemeIcon, deleteThemeIcon;
    private JButton addSongButton, deleteSongButton, addInstrumentButton, deleteInstrumentButton,
            addThemeButton, deleteThemeButton;
    private JTextField inputField;
    private JButton validateButton;
    private JPanel newNamePanel;
    private String[] newNameArr = new String[1];
    private File chosenFile;

    public AddDeleteWindow() throws IOException
    {
        // NEXT STEP IS TO MAKE SONGS VISIBLE
        super("Add/Delete");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pCenter = new JPanel();
        //pCenter.setLayout(null);
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        newNamePanel = new JPanel();
        newNamePanel.setPreferredSize(new Dimension(300, 100));
        newNamePanel.setLayout(new FlowLayout());

        // the text field I'm doing this with can be the specific for each type of element
        // use the specific one for song, instrument, wtv
        inputField = new JTextField(15);
        validateButton = new JButton("Confirm");

        newNamePanel.add(inputField);
        newNamePanel.add(validateButton);
        pCenter.add(newNamePanel, BorderLayout.SOUTH);
        inputField.setEnabled(false);
        validateButton.setEnabled(false);


        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Add / Delete Page");

        menuSelect = new JMenuItem("Select What to Play");
        menuSelect.addActionListener(this::actionPerformed);

        menuHome = new JMenuItem("Home Page");
        menuHome.addActionListener(this::actionPerformed2);

        menuPhotos = new JMenuItem("Photos");
        menuPhotos.addActionListener(this::actionPerformed5);

        menuPlaylists = new JMenuItem("Playlists");
        menuPlaylists.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        menuQueue = new JMenuItem("Queue");
        menuQueue.addActionListener(this::actionPerformed6);

        fileMenu.add(menuHome);
        fileMenu.add(menuPhotos);
        fileMenu.add(menuQueue);
        fileMenu.add(menuSongs);
        fileMenu.add(menuSelect);
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        // all locations are wronggggggggg
        //<editor-fold desc="Some Labels">
        songsT = new JLabel("Songs");
        songsT.setBounds(30, 150, 100, 35);
        pCenter.add(songsT);

        tagsT = new JLabel("Tags");
        tagsT.setBounds(130, 100, 100, 35);
        pCenter.add(tagsT);

        instrumentsT = new JLabel("Instruments");
        instrumentsT.setBounds(130, 100, 100, 35);
        pCenter.add(instrumentsT);

        themesT = new JLabel("Themes");
        themesT.setBounds(230, 150, 100, 35);
        pCenter.add(themesT);
        //</editor-fold>

        // song search
        //<editor-fold desc="Song Add/Delete">
        BufferedImage songSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel songSearchIconLabel = new JLabel(new ImageIcon(songSearchIcon));
        songSearchIconLabel.setBounds(30, 200, 30, 30);
        pCenter.add(songSearchIconLabel);

        songSearchBox = new JTextField();
        songSearchBox.setBounds(70, 200, 90, 30);
        pCenter.add(songSearchBox);

        addSongIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addSongButton = new JButton(addSongIcon);
        addSongButton.setBounds(100, 300, 50, 30);
        addSongButton.addActionListener(this::actionPerformedAddSong);
        pCenter.add(addSongButton);

        deleteSongIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteSongButton = new JButton(deleteSongIcon);
        deleteSongButton.setBounds(100, 350, 50, 30);
        deleteSongButton.addActionListener(this::actionPerformedDeleteSong);
        pCenter.add(deleteSongButton);
        //</editor-fold>

        // instrument tag search
        //<editor-fold desc="Instrument Tag Add/Delete">
        BufferedImage instrumentSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel instrumentSearchIconLabel = new JLabel(new ImageIcon(instrumentSearchIcon));
        instrumentSearchIconLabel.setBounds(130, 200, 30, 30);
        pCenter.add(instrumentSearchIconLabel);

        instrumentsSearchBox = new JTextField();
        instrumentsSearchBox.setBounds(180, 200, 90, 30);
        pCenter.add(instrumentsSearchBox);

        addInstrumentIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addInstrumentButton = new JButton(addInstrumentIcon);
        addInstrumentButton.setBounds(270, 300, 50, 30);
        pCenter.add(addInstrumentButton);

        deleteInstrumentIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteInstrumentButton = new JButton(deleteInstrumentIcon);
        deleteInstrumentButton.setBounds(270, 350, 50, 30);
        pCenter.add(deleteInstrumentButton);
        //</editor-fold>

        // theme tag search
        //<editor-fold desc="Theme Tag Add/Delete">
        BufferedImage themeSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel themeSearchIconLabel = new JLabel(new ImageIcon(themeSearchIcon));
        themeSearchIconLabel.setBounds(330, 200, 30, 30);
        pCenter.add(themeSearchIconLabel);

        themesSearchBox = new JTextField();
        themesSearchBox.setBounds(380, 200, 90, 30);
        pCenter.add(themesSearchBox);

        addThemeIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addThemeButton = new JButton(addThemeIcon);
        addThemeButton.setBounds(480, 100, 50, 30);
        pCenter.add(addThemeButton);

        deleteThemeIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteThemeButton = new JButton(deleteThemeIcon);
        deleteThemeButton.setBounds(480, 100, 50, 30);
        pCenter.add(deleteThemeButton);
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
        JFrame QueuePreviewWindow = new QueuePreviewWindow(); // open another JFrame
        QueuePreviewWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformedAddSong(ActionEvent e)
    {
        JFileChooser songUpload = new JFileChooser();
        int res2 = songUpload.showSaveDialog(null);

        // copying the file
        if (res2 == JFileChooser.APPROVE_OPTION)
        {
            File songPath = new File(songUpload.getSelectedFile().getAbsolutePath());

            Path sourcePath = Path.of(songUpload.getSelectedFile().getAbsolutePath());
            String targetPath = "src/newSong";
            File recentSong;

            try
            {
                Files.copy(sourcePath, Path.of(targetPath), new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }

            // find most recently edited file
            String directoryFilePath = "src";

            File directory = new File(directoryFilePath);
            File[] files = directory.listFiles(File::isFile);
            long lastModifiedTime = Long.MIN_VALUE;
            chosenFile = null;

            if (files != null)
            {
                for (File file : files)
                {
                    if (file.lastModified() > lastModifiedTime)
                    {
                        chosenFile = file;
                        lastModifiedTime = file.lastModified();
                    }
                }
            }

            // I need to get the user's input as a string and substitute in place of "NewName"
            inputField.setEnabled(true);
            validateButton.setEnabled(true);

            validateButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    newNameArr[0] = inputField.getText();
                    JOptionPane.showMessageDialog(null, "File information saved");
                    String newName = "src/songsFiles/" + newNameArr[0];
                    System.out.println(newName);
                    File rename = new File(newName);

                    System.out.println(chosenFile.renameTo(rename));
                    inputField.setEnabled(false);
                    validateButton.setEnabled(false);
                }
            });

        }
    }

    public void actionPerformedDeleteSong(ActionEvent e)
    {
        // When they click the delete button, make them type the name of the file to be deleted
        // say it's fore security, we want the user to be sure of the deletion, not just an accident
        inputField.setEnabled(true);
        validateButton.setEnabled(true);

        validateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                newNameArr[0] = inputField.getText();
                JOptionPane.showMessageDialog(null, "File deleted");
                String nameFile = "src/songsFiles/" + newNameArr[0];
                try
                {
                    Files.deleteIfExists(Path.of(nameFile));
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }

                inputField.setEnabled(false);
                validateButton.setEnabled(false);
            }
        });

    }
}