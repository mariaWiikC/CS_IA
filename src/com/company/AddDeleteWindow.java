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
    private JMenuItem menuSelect, menuPhotos, menuPlaylists, menuSongs, menuQueue, menuHome;
    private JPanel pCenter;
    private JLabel songsT, tagsT, instrumentsT, themesT;
    private JTextField songSearchBox, instrumentsSearchBox, themesSearchBox;
    private ImageIcon addSongIcon, deleteSongIcon, addInstrumentIcon, deleteInstrumentIcon,
            addThemeIcon, deleteThemeIcon;
    private JButton addSongButton, deleteSongButton, addInstrumentButton, deleteInstrumentButton,
            addThemeButton, deleteThemeButton, validateButton;
    private JTextField inputField;
    private String[] newNameArr = new String[1];
    private File chosenFile;
    private JList listSongs;
    private DefaultListModel listModel;
    private JScrollPane listScroller;
    public File songsAndTagsFile;
    private ArrayList<String> fileContent;

    public AddDeleteWindow() throws IOException
    {
        // position the labels better
        // if i add three songs in a row, it adds the same to the list three times
        // If I add more than one song, the following ones are not added as wav files -> whenever there
        // is this newSong file, why is it not goneeeee
        // WHAT IS GOING ON WITH THE AUDIO FILES????????????????????????????????
        // now Wrecked isn't working? And when I try to add songs they are added weirdly??
        // clean the input field after using it
        super("Add/Delete");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pCenter = new JPanel();
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(pCenter);
        pCenter.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // the text field I'm doing this with can be the specific for each type of element
        // use the specific one for song, instrument, wtv
        inputField = new JTextField(5);
        inputField.setMaximumSize(new Dimension(200, 30));
        validateButton = new JButton("Confirm");

        inputField.setEnabled(false);
        validateButton.setEnabled(false);


        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Add / Delete Page");

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
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        // all locations are wronggggggggg
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

        deleteSongIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deleteSongButton = new JButton(deleteSongIcon);
        deleteSongButton.addActionListener(this::actionPerformedDeleteSong);
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

        // I want to display all the uploaded songs
        String directorySongsFilePath = "src/songsFiles";

        //<editor-fold desc="Setting up the list of songs">
        File directorySongs = new File(directorySongsFilePath);
        File[] filesSongs = directorySongs.listFiles(File::isFile);

        listModel = new DefaultListModel();
        for (File f : filesSongs)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(f.getName());
            sb.delete(sb.length() - 4, sb.length());
            String addNow = String.valueOf(sb);
            listModel.addElement(addNow);
        }
        listSongs = new JList(listModel);
        listSongs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSongs.setLayoutOrientation(JList.VERTICAL);
        listSongs.setVisibleRowCount(-1);

        listScroller = new JScrollPane(listSongs);
        listScroller.setMaximumSize(new Dimension(250, 300));
        //</editor-fold>

        // creation of the songs and tags file
        songsAndTagsFile = new File("src\\SongsWithTags.txt");
        if (!songsAndTagsFile.exists())
            songsAndTagsFile.createNewFile();

        //<editor-fold desc="LAYOUT">
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(tagsT) // move to left
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(songsT)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(instrumentsT)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(themesT))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(songSearchIconLabel)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(inputField)
                                        .addComponent(listScroller))

                                .addComponent(validateButton)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(addSongButton)
                                        .addComponent(deleteSongButton))

                                .addComponent(instrumentSearchIconLabel)
                                .addComponent(instrumentsSearchBox)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(addInstrumentButton)
                                        .addComponent(deleteInstrumentButton))

                                .addComponent(themeSearchIconLabel)
                                .addComponent(themesSearchBox)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(addThemeButton)
                                        .addComponent(deleteThemeButton)))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(tagsT)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(songsT)
                                .addComponent(instrumentsT)
                                .addComponent(themesT))
                        .addGroup(layout.createParallelGroup()
                                .addComponent(songSearchIconLabel)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(inputField)
                                        .addComponent(listScroller)
                                )
                                .addComponent(validateButton)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(addSongButton)
                                        .addComponent(deleteSongButton))


                                .addComponent(instrumentSearchIconLabel)
                                .addComponent(instrumentsSearchBox)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(addInstrumentButton)
                                        .addComponent(deleteInstrumentButton))

                                .addComponent(themeSearchIconLabel)
                                .addComponent(themesSearchBox)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(addThemeButton)
                                        .addComponent(deleteThemeButton)))
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
    //</editor-fold>

    public void actionPerformedAddSong(ActionEvent e)
    {
        JFileChooser songUpload = new JFileChooser();
        int res2 = songUpload.showSaveDialog(null);

        // copying the file
        if (res2 == JFileChooser.APPROVE_OPTION)
        {
            File songPath = new File(songUpload.getSelectedFile().getAbsolutePath());

            Path sourcePath = Path.of(songUpload.getSelectedFile().getAbsolutePath());
            String targetPath = "src/songsFiles/newSong";

            try
            {
                Files.copy(sourcePath, Path.of(targetPath), new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }

            // I need to get the user's input as a string and substitute in place of "NewName"
            inputField.setEnabled(true);
            validateButton.setEnabled(true);

            validateButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String newNameStr = inputField.getText();
                    JOptionPane.showMessageDialog(null, "File information saved");
                    String newName = "src/songsFiles/" + newNameStr + ".wav";
                    System.out.println(newName);
                    listModel.addElement(newNameStr);

                    try
                    {
                        Files.copy(Path.of(targetPath), Path.of(newName), new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
                    } catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }

                    File toDelete = new File(targetPath);
                    toDelete.delete();

                    // ADDING SONG TO TXT FILE
                    try
                    {
                        fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(songsAndTagsFile)), StandardCharsets.UTF_8));
                        fileContent.add(newNameStr + " ");
                        Files.write(Path.of(String.valueOf(songsAndTagsFile)), fileContent, StandardCharsets.UTF_8);
                    } catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                    inputField.setEnabled(false);
                    validateButton.setEnabled(false);
                }
            });

            // WHY IS IT ADDING TO SONGS AT ONCE???????????
        }

    }

    // WHEN I DELETE THE SONG, I MUST ALSO DELETE IT FROM THE TEXT FILE
    public void actionPerformedDeleteSong(ActionEvent e)
    {
        String nameWritten = null;
        if (listSongs.getSelectedIndex() != -1)
        {
            nameWritten = (String) listSongs.getSelectedValue();
        }
        String nameFile = "src/songsFiles/" + nameWritten + ".wav";
        int index = listSongs.getSelectedIndex();
        listModel.remove(index);
        JOptionPane.showMessageDialog(null, "File deleted");

        try
        {
            Files.deleteIfExists(Path.of(nameFile));
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}