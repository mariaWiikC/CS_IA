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
    private JPanel pCenter, pTop, pRest, pSongs, pInputList, pConfirmButtons, pInSongs, pInstruments,
        pInInstruments, pInputListInstrument, pConfirmButtonsInstrument, pThemes, pInThemes, pInputListTheme, pConfirmButtonsTheme,
            pJustTextField, pJustTextField2, pJustTextField3;
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

        pTop = new JPanel();
        pTop.setLayout(new BoxLayout(pTop, BoxLayout.LINE_AXIS));

        pRest = new JPanel();
        pRest.setLayout(new BoxLayout(pRest, BoxLayout.LINE_AXIS));

        // panels songs
        pSongs = new JPanel();
        pSongs.setLayout(new BoxLayout(pSongs, BoxLayout.PAGE_AXIS));

        pInSongs = new JPanel();
        pInSongs.setLayout(new BoxLayout(pInSongs, BoxLayout.LINE_AXIS));

        pInputList = new JPanel();
        pInputList.setLayout(new BoxLayout(pInputList, BoxLayout.PAGE_AXIS));

        pConfirmButtons = new JPanel();
        pConfirmButtons.setLayout(new BoxLayout(pConfirmButtons, BoxLayout.PAGE_AXIS));

        // panels instruments
        pInstruments = new JPanel();
        pInstruments.setLayout(new BoxLayout(pInstruments, BoxLayout.PAGE_AXIS));

        pInInstruments = new JPanel();
        pInInstruments.setLayout(new BoxLayout(pInInstruments, BoxLayout.LINE_AXIS));

        pInputListInstrument = new JPanel();
        pInputListInstrument.setLayout(new BoxLayout(pInputListInstrument, BoxLayout.PAGE_AXIS));

        pConfirmButtonsInstrument = new JPanel();
        pConfirmButtonsInstrument.setLayout(new BoxLayout(pConfirmButtonsInstrument, BoxLayout.PAGE_AXIS));

        // panels themes
        pThemes = new JPanel();
        pThemes.setLayout(new BoxLayout(pThemes, BoxLayout.PAGE_AXIS));

        pInThemes = new JPanel();
        pInThemes.setLayout(new BoxLayout(pInThemes, BoxLayout.LINE_AXIS));

        pInputListTheme = new JPanel();
        pInputListTheme.setLayout(new BoxLayout(pInputListTheme, BoxLayout.PAGE_AXIS));

        pConfirmButtonsTheme = new JPanel();
        pConfirmButtonsTheme.setLayout(new BoxLayout(pConfirmButtonsTheme, BoxLayout.PAGE_AXIS));

        pJustTextField = new JPanel();
        pJustTextField.setPreferredSize(new Dimension(70, 20));

        pCenter.add(pTop);
        pCenter.add(pRest);
        pRest.add(pSongs);
        pRest.add(pInstruments);
        pRest.add(pThemes);

        // Do I need to add the elements in a certain order? -> i think so

        // the text field I'm doing this with can be the specific for each type of element
        // use the specific one for song, instrument, wtv
        // what does the column number mean
        //<editor-fold desc="Song Text field (the one that should work)">
        inputField = new JTextField(5);
        validateButton = new JButton("Confirm");

        inputField.setEnabled(false);
        validateButton.setEnabled(false);
        //</editor-fold>

        //<editor-fold desc="Place Fillers">
        // PLACE FILLERS FOR WHERE THE LIST OF SONGS WILL BE
        filler1 = new JLabel("filler1");
        filler2 = new JLabel("filler2");
        filler3 = new JLabel("filler3");

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


        tagsT = new JLabel("Tags");
        pTop.add(tagsT);

        instrumentsT = new JLabel("Instruments");
        // pLabels.add(instrumentsT);

        themesT = new JLabel("Themes");
        // pLabels.add(themesT);
        //</editor-fold>

        // song search
        //<editor-fold desc="Song Add/Delete">
        BufferedImage songSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel songSearchIconLabel = new JLabel(new ImageIcon(songSearchIcon));


        songSearchBox = new JTextField();
        // pAddDeleteSongs.add(songSearchBox);

        addSongIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addSongButton = new JButton(addSongIcon);
        addSongButton.addActionListener(this::actionPerformedAddSong);
        // pCenter.add(addSongButton);

        deleteSongIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteSongButton = new JButton(deleteSongIcon);
        deleteSongButton.addActionListener(this::actionPerformedDeleteSong);
        // pCenter.add(deleteSongButton);
        //</editor-fold>

        // instrument tag search
        //<editor-fold desc="Instrument Tag Add/Delete">
        BufferedImage instrumentSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel instrumentSearchIconLabel = new JLabel(new ImageIcon(instrumentSearchIcon));
        // pTextFields.add(instrumentSearchIconLabel);

        instrumentsSearchBox = new JTextField();
        // pFitTextField2.add(instrumentsSearchBox);
        // pTextFields.add(pFitTextField2);

        addInstrumentIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addInstrumentButton = new JButton(addInstrumentIcon);
        // pInstrumentB.add(addInstrumentButton);

        deleteInstrumentIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteInstrumentButton = new JButton(deleteInstrumentIcon);
        // pInstrumentB.add(deleteInstrumentButton);
        //</editor-fold>

        // theme tag search
        //<editor-fold desc="Theme Tag Add/Delete">
        BufferedImage themeSearchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel themeSearchIconLabel = new JLabel(new ImageIcon(themeSearchIcon));
        // pTextFields.add(themeSearchIconLabel);

        themesSearchBox = new JTextField();
        // pFitTextField3.add(themesSearchBox);
        // pTextFields.add(pFitTextField3);
        // pTextFields.add(themesSearchBox);

        addThemeIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addThemeButton = new JButton(addThemeIcon);
        // pThemeB.add(addThemeButton);

        deleteThemeIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteThemeButton = new JButton(deleteThemeIcon);
        // pThemeB.add(deleteThemeButton);
        //</editor-fold>

        // I want to display all the uploaded songs
        // I can't even get the names of the files... aaaa
        String directorySongsFilePath = "src/songsFiles";

        File directorySongs = new File(directorySongsFilePath);
        File[] filesSongs = directorySongs.listFiles(File::isFile);

        // now how do I add these to the beautiful layout

        //<editor-fold desc="Adding song stuff to panels">
        pSongs.add(songsT);
        pSongs.add(pInSongs);
        pInSongs.add(songSearchIconLabel);
        pInSongs.add(pInputList);

        pJustTextField.add(inputField);
        pInputList.add(pJustTextField);
        pInputList.add(filler1);

        pInSongs.add(pConfirmButtons);

        pConfirmButtons.add(validateButton);
        pConfirmButtons.add(addSongButton);
        pConfirmButtons.add(deleteSongButton);
        //</editor-fold>

        //<editor-fold desc="adding instruments panels">
        pInstruments.add(instrumentsT);
        pInstruments.add(pInInstruments);
        pInInstruments.add(instrumentSearchIconLabel);
        pInInstruments.add(pInputListInstrument);

        pInputListInstrument.add(instrumentsSearchBox);
        pInputListInstrument.add(filler2);

        pInInstruments.add(pConfirmButtonsInstrument);

        // pConfirmButtons.add(validateButton);
        pConfirmButtonsInstrument.add(addInstrumentButton);
        pConfirmButtonsInstrument.add(deleteInstrumentButton);
        //</editor-fold>

        //<editor-fold desc="adding themes panels">
        pThemes.add(themesT);
        pThemes.add(pInInstruments);
        pInInstruments.add(instrumentSearchIconLabel);
        pInInstruments.add(pInputListInstrument);

        pInputListInstrument.add(instrumentsSearchBox);
        pInputListInstrument.add(filler3);

        pInInstruments.add(pConfirmButtonsInstrument);

        // pConfirmButtons.add(validateButton);
        pConfirmButtonsInstrument.add(addInstrumentButton);
        pConfirmButtonsInstrument.add(deleteInstrumentButton);
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