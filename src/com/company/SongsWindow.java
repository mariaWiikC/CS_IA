package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class SongsWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuAddDelete, menuPhotos, menuPlaylists, menuSongs, menuHome;
    private JPanel pCenter;
    private JList listSongs;
    private DefaultListModel listModel;
    private JScrollPane listScroller;
    private JLabel moodT, timeT, instrumentT, themeT;
    private JCheckBox sadBox, energeticBox, happyBox, relaxedBox, morningBox, afternoonBox, eveningBox,
            guitarBox, pianoBox, vocalBox, ChristmasBox, IndependenceBox, EasterBox;
    ArrayList<ArrayList> allSongsAndTags;
    Tags tagsObject;
    boolean isEnabled = false, editing = false;
    ArrayList<JCheckBox> checkBoxesList;
    String directorySongsFilePath;
    String nameWritten, songsAndTagsFilePath;
    private JButton editButton;
    Song songObject = new Song();
    AddingDeleting addingDeletingObject;

    public SongsWindow()
    {
        super("Songs");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        // setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pCenter = new JPanel();
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(pCenter);
        pCenter.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        tagsObject = new Tags();

        editButton = new JButton("Edit Information / Confirm");
        editButton.addActionListener(this::showAndEditTagsSongs);


        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Songs");

        menuAddDelete = new JMenuItem("Add / Delete");
        menuAddDelete.addActionListener(this::actionPerformed2);

        menuPhotos = new JMenuItem("Photos");
        // menuPhotos.addActionListener(this::actionPerformed5);

        menuPlaylists = new JMenuItem("Playlists");
        menuPlaylists.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        menuHome = new JMenuItem("Home Page");
        menuHome.addActionListener(this::actionPerformed6);

        fileMenu.add(menuAddDelete);
        fileMenu.add(menuPhotos);
        // fileMenu.add(menuHome);
        fileMenu.add(menuSongs);
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        directorySongsFilePath = "src/songsFiles";

        //LIST OF ALL CHECK BOXES
        checkBoxesList = new ArrayList<>();

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

        //<editor-fold desc="Mood tags">
        moodT = new JLabel("Mood");
        moodT.setBounds(50, 150, 100, 35);
        pCenter.add(moodT);

        sadBox = new JCheckBox("Sad");
        sadBox.setBounds(50, 200, 100, 35);
        sadBox.setEnabled(isEnabled);
        checkBoxesList.add(sadBox);
        pCenter.add(sadBox);

        energeticBox = new JCheckBox("Energetic");
        energeticBox.setBounds(50, 250, 100, 35);
        energeticBox.setEnabled(isEnabled);
        checkBoxesList.add(energeticBox);
        pCenter.add(energeticBox);

        happyBox = new JCheckBox("Happy");
        happyBox.setBounds(50, 300, 100, 35);
        happyBox.setEnabled(isEnabled);
        checkBoxesList.add(happyBox);
        pCenter.add(happyBox);

        relaxedBox = new JCheckBox("Relaxed");
        relaxedBox.setBounds(50, 350, 100, 35);
        relaxedBox.setEnabled(isEnabled);
        checkBoxesList.add(relaxedBox);
        pCenter.add(relaxedBox);
        //</editor-fold>

        //<editor-fold desc="Time tags">
        timeT = new JLabel("Time");
        timeT.setBounds(200, 150, 100, 35);
        pCenter.add(timeT);

        morningBox = new JCheckBox("Morning");
        morningBox.setBounds(200, 200, 100, 35);
        morningBox.setEnabled(isEnabled);
        checkBoxesList.add(morningBox);
        pCenter.add(morningBox);

        afternoonBox = new JCheckBox("Afternoon");
        afternoonBox.setBounds(200, 250, 100, 35);
        afternoonBox.setEnabled(isEnabled);
        checkBoxesList.add(afternoonBox);
        pCenter.add(afternoonBox);

        eveningBox = new JCheckBox("Evening");
        eveningBox.setBounds(200, 300, 100, 35);
        eveningBox.setEnabled(isEnabled);
        checkBoxesList.add(eveningBox);
        pCenter.add(eveningBox);
        //</editor-fold>

        //<editor-fold desc="Instruments tags">
        instrumentT = new JLabel("Instrument");
        instrumentT.setBounds(350, 150, 100, 35);
        pCenter.add(instrumentT);

        guitarBox = new JCheckBox("Guitar");
        guitarBox.setBounds(350, 200, 100, 35);
        guitarBox.setEnabled(isEnabled);
        checkBoxesList.add(guitarBox);
        pCenter.add(guitarBox);

        pianoBox = new JCheckBox("Piano");
        pianoBox.setBounds(350, 250, 100, 35);
        pianoBox.setEnabled(isEnabled);
        checkBoxesList.add(pianoBox);
        pCenter.add(pianoBox);

        vocalBox = new JCheckBox("Vocal");
        vocalBox.setBounds(350, 300, 100, 35);
        vocalBox.setEnabled(isEnabled);
        checkBoxesList.add(vocalBox);
        pCenter.add(vocalBox);
        //</editor-fold>

        //<editor-fold desc="Themes tags">
        themeT = new JLabel("Theme");
        themeT.setBounds(500, 150, 100, 35);
        pCenter.add(themeT);

        ChristmasBox = new JCheckBox("Christmas");
        ChristmasBox.setBounds(500, 200, 100, 35);
        ChristmasBox.setEnabled(isEnabled);
        checkBoxesList.add(ChristmasBox);
        pCenter.add(ChristmasBox);

        IndependenceBox = new JCheckBox("Independence Day");
        IndependenceBox.setBounds(500, 250, 200, 35);
        IndependenceBox.setEnabled(isEnabled);
        checkBoxesList.add(IndependenceBox);
        pCenter.add(IndependenceBox);

        EasterBox = new JCheckBox("Easter");
        EasterBox.setBounds(500, 300, 100, 35);
        EasterBox.setEnabled(isEnabled);
        checkBoxesList.add(EasterBox);
        pCenter.add(EasterBox);
        //</editor-fold>

        try
        {
            addingDeletingObject = new AddingDeleting();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        songsAndTagsFilePath = String.valueOf(addingDeletingObject.songsAndTagsFile);

        //<editor-fold desc="LAYOUT">
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(listScroller)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(moodT)
                                        .addComponent(sadBox)
                                        .addComponent(happyBox)
                                        .addComponent(energeticBox)
                                        .addComponent(relaxedBox))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(timeT)
                                        .addComponent(morningBox)
                                        .addComponent(afternoonBox)
                                        .addComponent(eveningBox))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(instrumentT)
                                        .addComponent(vocalBox)
                                        .addComponent(guitarBox)
                                        .addComponent(pianoBox))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(themeT)
                                        .addComponent(EasterBox)
                                        .addComponent(IndependenceBox)
                                        .addComponent(ChristmasBox))
                        )
                        .addComponent(editButton)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(listScroller)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(moodT)
                                        .addComponent(sadBox)
                                        .addComponent(happyBox)
                                        .addComponent(energeticBox)
                                        .addComponent(relaxedBox))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(timeT)
                                        .addComponent(morningBox)
                                        .addComponent(afternoonBox)
                                        .addComponent(eveningBox))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(instrumentT)
                                        .addComponent(vocalBox)
                                        .addComponent(guitarBox)
                                        .addComponent(pianoBox))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(themeT)
                                        .addComponent(EasterBox)
                                        .addComponent(IndependenceBox)
                                        .addComponent(ChristmasBox))
                        )
                        .addComponent(editButton)
        );
        //</editor-fold>

        setVisible(true);
    }

    public void showAndEditTagsSongs(ActionEvent ae)
    {
        allSongsAndTags = new ArrayList<>();
        try
        {
            Scanner scanner = new Scanner(addingDeletingObject.songsAndTagsFile);
            ArrayList<String> songsAndTags = new ArrayList<>();

            while (scanner.hasNextLine())
            {
                songsAndTags.add(scanner.nextLine());
            }
            for (String s : songsAndTags)
            {
                ArrayList<String> toAdd = new ArrayList<>();
                String[] sArray = s.split(" ");
                for (String str : sArray)
                {
                    toAdd.add(str);
                }
                allSongsAndTags.add(toAdd);
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        if (listSongs.getSelectedIndex() == -1)
        {
            enablingCheckBoxes(false);
        }

        if (listSongs.getSelectedIndex() != -1)
        {
            // display correctly the selection of tags
            nameWritten = (String) listSongs.getSelectedValue();
            enablingCheckBoxes(true);

            for (ArrayList<String> a : allSongsAndTags)
            {
                // checking the tags
                if (a.contains(nameWritten))
                {
                    checkingTag(a, "sad", sadBox);
                    checkingTag(a, "energetic", energeticBox);
                    checkingTag(a, "happy", happyBox);
                    checkingTag(a, "relaxed", relaxedBox);
                    checkingTag(a, "morning", morningBox);
                    checkingTag(a, "afternoon", afternoonBox);
                    checkingTag(a, "evening", eveningBox);
                    checkingTag(a, "guitar", guitarBox);
                    checkingTag(a, "piano", pianoBox);
                    checkingTag(a, "vocal", vocalBox);
                    checkingTag(a, "Independence", IndependenceBox);
                    checkingTag(a, "Easter", EasterBox);
                    checkingTag(a, "Christmas", ChristmasBox);
                }
            }
        }

        if (editing)
            enablingCheckBoxes(false);

        editing = !editing;
    }

    public void enablingCheckBoxes(boolean isEnabled)
    {
        for (JCheckBox cb : checkBoxesList)
        {
            cb.setEnabled(isEnabled);
        }
    }

    public void checkingTag(ArrayList<String> a, String nameTag, JCheckBox box)
    {
        // I need to create smt to delete the tag from the txt file if the box is unchecked
        // WHY AM I ONLY ABLE TO ADD ONE TAG AT A TIME
        if (!editing)
        {
            if (a.contains(nameTag))
            {
                box.setSelected(true);
            }
        }
        else
        {
            if (box.isSelected())
            {
                if (!a.contains(nameTag))
                    songObject.creatingNewStringsForTxtFile(a, nameTag);
            }
            else
            {
                if (a.contains(nameTag))
                    songObject.deletingStringsForTxtFile(a, nameTag);
            }
            box.setSelected(false);
        }
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
    //</editor-fold>
}