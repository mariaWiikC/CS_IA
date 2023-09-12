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
import java.util.ArrayList;

public class AddDeleteWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuPhotos, menuPlaylists, menuSongs, menuQueue, menuHome;
    private JPanel pCenter, pAddDeleteSongs, pAddDeleteInstruments, pAddDeleteThemes;
    private JLabel songsT, tagsT, instrumentsT, themesT;
    private JTextField songSearchBox, instrumentsSearchBox, themesSearchBox;
    private ImageIcon addSongIcon, deleteSongIcon, addInstrumentIcon, deleteInstrumentIcon,
            addThemeIcon, deleteThemeIcon;
    private JButton addSongButton, deleteSongButton, addInstrumentButton, deleteInstrumentButton,
            addThemeButton, deleteThemeButton;
    private JTextField inputField;
    private JButton validateButton;
    private String[] newNameArr = new String[1];
    private File chosenFile;
    private JLabel filler1, filler2, filler3;
    private JList allSongsList;
    private JScrollPane songsListScroller;

    public AddDeleteWindow() throws IOException
    {
        // NEXT STEP IS TO MAKE SONGS VISIBLE
        // When they click the buttons, a message can appear on the screen and be like, "do this", with the instructions
        // position the labels better
        // why is it showing two messages when I delete a song
        // the group layout seems like an overkill
        // i should have a panel with horizontal layout -> and then a vertical when i need things
        // pilled up (like the + and - button)
        // box layout?
        super("Add/Delete");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pCenter = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pCenter.setPreferredSize(screenSize);
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        // maybe the BorderLayout.Center thing isn't needed
        add(pCenter);

        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.PAGE_AXIS));

        pAddDeleteSongs = new JPanel();
        pAddDeleteInstruments = new JPanel();
        pAddDeleteThemes = new JPanel();
        // Do I need to add the elements in a certain order? -> i think so

        // the text field I'm doing this with can be the specific for each type of element
        // use the specific one for song, instrument, wtv
        // what does the column number mean
        //<editor-fold desc="Song Text field (the one that should work)">
        inputField = new JTextField(5);
        validateButton = new JButton("Confirm");

        pCenter.add(inputField);
        pCenter.add(validateButton);
        inputField.setEnabled(false);
        validateButton.setEnabled(false);
        //</editor-fold>

        //<editor-fold desc="Place Fillers">
        // PLACE FILLERS FOR WHERE THE LIST OF SONGS WILL BE
        filler1 = new JLabel("filler1");
        filler2 = new JLabel("filler2");
        filler3 = new JLabel("filler3");
        pCenter.add(filler1);
        pCenter.add(filler2);
        pCenter.add(filler3);
        //</editor-fold>


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

        //<editor-fold desc="Some Labels">
        songsT = new JLabel("Songs");
        // pCenter.add(songsT);

        tagsT = new JLabel("Tags");
        // pCenter.add(tagsT);

        instrumentsT = new JLabel("Instruments");
        //pCenter.add(instrumentsT);

        themesT = new JLabel("Themes");
        // pCenter.add(themesT);
        //</editor-fold>

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
        pAddDeleteSongs.add(addSongButton);

        deleteSongIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteSongButton = new JButton(deleteSongIcon);
        deleteSongButton.addActionListener(this::actionPerformedDeleteSong);
        // pCenter.add(deleteSongButton);
        pAddDeleteSongs.add(deleteSongButton);
        //</editor-fold>

        // instrument tag search
        //<editor-fold desc="Instrument Tag Add/Delete">
        BufferedImage instrumentSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel instrumentSearchIconLabel = new JLabel(new ImageIcon(instrumentSearchIcon));
        // pCenter.add(instrumentSearchIconLabel);

        instrumentsSearchBox = new JTextField();
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
        // pCenter.add(themesSearchBox);

        addThemeIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addThemeButton = new JButton(addThemeIcon);
        // pCenter.add(addThemeButton);

        deleteThemeIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteThemeButton = new JButton(deleteThemeIcon);
        // pCenter.add(deleteThemeButton);
        //</editor-fold>

        // I want to display all the uploaded songs
        // I can't even get the names of the files... aaaa
        String directorySongsFilePath = "src/songsFiles";

        File directorySongs = new File(directorySongsFilePath);
        File[] filesSongs = directorySongs.listFiles(File::isFile);

        // now how do I add these to the beautiful layout

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