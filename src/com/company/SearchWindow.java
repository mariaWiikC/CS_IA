package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuAddDelete, menuPlaylists, menuSongs, menuHome;
    private JPanel pCenter;
    private JLabel searchT, moodT, timeT, instrumentT, themeT;
    private JCheckBox sadBox, energeticBox, happyBox, relaxedBox, morningBox, afternoonBox, eveningBox,
            guitarBox, pianoBox, vocalBox, ChristmasBox, IndependenceBox, EasterBox;
    private boolean searching = false, deleting = false;
    protected File searchTagsFile;
    private ArrayList<String> fileContent;
    private ArrayList<ArrayList> allTags;
    ArrayList<JCheckBox> checkBoxesList;
    private String searchTagsFilePath;
    private JButton choosingSearchTags;

    public SearchWindow()
    {
        super("Search");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        pCenter = new JPanel();
        pCenter.setPreferredSize(new Dimension(500, 300));
        add(pCenter, BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(pCenter);
        pCenter.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // creating searchTags file
        searchTagsFile = new File("src\\SearchTags.txt");
        if (!searchTagsFile.exists())
        {
            try
            {
                searchTagsFile.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        checkBoxesList = new ArrayList<>();
        searchTagsFilePath = String.valueOf(searchTagsFile);

        choosingSearchTags = new JButton("Select filter tags");
        choosingSearchTags.addActionListener(this::showAndEditTagsSongs);
        pCenter.add(choosingSearchTags);

        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Search page");

        menuHome = new JMenuItem("Home Page");
        menuHome.addActionListener(this::actionPerformed2);


        menuPlaylists = new JMenuItem("Playlists");
        menuPlaylists.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        fileMenu.add(menuHome);
        fileMenu.add(menuSongs);
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        searchT = new JLabel("Search");

        //<editor-fold desc="Mood tags">
        moodT = new JLabel("Mood");

        sadBox = new JCheckBox("Sad");
        checkBoxesList.add(sadBox);

        energeticBox = new JCheckBox("Energetic");
        checkBoxesList.add(energeticBox);

        happyBox = new JCheckBox("Happy");
        checkBoxesList.add(happyBox);

        relaxedBox = new JCheckBox("Relaxed");
        checkBoxesList.add(relaxedBox);
        //</editor-fold>

        //<editor-fold desc="Time tags">
        timeT = new JLabel("Time");

        morningBox = new JCheckBox("Morning");
        checkBoxesList.add(morningBox);

        afternoonBox = new JCheckBox("Afternoon");
        checkBoxesList.add(afternoonBox);

        eveningBox = new JCheckBox("Evening");
        checkBoxesList.add(eveningBox);
        //</editor-fold>

        //<editor-fold desc="Instruments tags">
        instrumentT = new JLabel("Instrument");

        guitarBox = new JCheckBox("Guitar");
        checkBoxesList.add(guitarBox);

        pianoBox = new JCheckBox("Piano");
        checkBoxesList.add(pianoBox);

        vocalBox = new JCheckBox("Vocal");
        checkBoxesList.add(vocalBox);
        //</editor-fold>

        //<editor-fold desc="Themes tags">
        themeT = new JLabel("Theme");

        ChristmasBox = new JCheckBox("Christmas");
        checkBoxesList.add(ChristmasBox);

        IndependenceBox = new JCheckBox("Independence Day");
        checkBoxesList.add(IndependenceBox);

        EasterBox = new JCheckBox("Easter");
        checkBoxesList.add(EasterBox);
        //</editor-fold>

        // disable checkboxes
        for (JCheckBox cb : checkBoxesList)
        {
            cb.setEnabled(false);
        }

        //<editor-fold desc="LAYOUT">
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(searchT)
                        .addGroup(layout.createParallelGroup()
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(moodT)
                                        .addComponent(sadBox)
                                        .addComponent(happyBox)
                                        .addComponent(energeticBox)
                                        .addComponent(relaxedBox)
                                        .addComponent(choosingSearchTags)
                                )
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
        );

        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(searchT)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(moodT)
                                        .addComponent(sadBox)
                                        .addComponent(happyBox)
                                        .addComponent(energeticBox)
                                        .addComponent(relaxedBox)
                                        .addComponent(choosingSearchTags)
                                )
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
        );
        //</editor-fold>

        setVisible(true);
    }

    public void showAndEditTagsSongs(ActionEvent ae)
    {
        allTags = new ArrayList<>();
        try
        { // reading the searchTags file
            Scanner scanner = new Scanner(searchTagsFile);
            ArrayList<String> searchTags = new ArrayList<>();

            while (scanner.hasNextLine())
                searchTags.add(scanner.nextLine());

            for (String s : searchTags)
            { // adding the tags already in the file to a list of all selected tags
                ArrayList<String> toAdd = new ArrayList<>();
                String[] sArray = s.split(" ");
                for (String tag : sArray)
                    toAdd.add(tag);
                allTags.add(toAdd);
            }
        } catch (FileNotFoundException e) {e.printStackTrace();}
        // if the user clicked the button to initiate the tag selection, proceed to this
        if (!searching)
        { // enable checkboxes so the user can select them
            enablingCheckBoxes(true);
            // method used to select checkboxes previously selected -> display saved filters
            for (ArrayList<String> a : allTags) // checking the tags
                checkingAllTags(a);
        }
        // if the user has selected the desired tags and wants to move on to their search, proceed to this
        if (searching)
        { // disable checkboxes to save the selected ones and stop user from selecting while saving
            enablingCheckBoxes(false);
            // saving selections
            for (ArrayList<String> a : allTags) // checking the tags
                checkingAllTags(a);
        }
        // change searching stage
        searching = !searching;
    }

    public void checkingAllTags(ArrayList<String> a)
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

    public void checkingTag(ArrayList<String> a, String nameTag, JCheckBox box)
    {
        // if the user has just begun the search tag selection proceed
        if (!searching)
            // if the searchTags file contains a certain tag, select it (it has been previously selected)
            if (a.contains(nameTag))
                box.setSelected(true);
        // if the user has clicked the confirm button - finished the filter process
        if(searching)
        {
            // if the tag's checkbox is selected, it means the user chose it
            if (box.isSelected())
                if (!a.contains(nameTag))
                    creatingNewStringsForTxtFile(a, nameTag);
            // if it is not selected, but is in the file, delete it from there
            if (!box.isSelected())
            {
                if (a.contains(nameTag))
                    deletingStringsForTxtFile(a, nameTag);
            }
        }
    }

    public void enablingCheckBoxes(boolean isEnabled)
    {
        for (JCheckBox cb : checkBoxesList)
            cb.setEnabled(isEnabled);
    }

    public void creatingNewStringsForTxtFile(ArrayList<String> a, String nameTag)
    {
        // tags in the file to a string buffer
        StringBuffer stringFile = new StringBuffer();
        for (String tag : a)
        {
            stringFile.append(tag);
            stringFile.append(" ");
        }
        stringFile.append(nameTag);
        stringFile.append(" ");
        a.add(nameTag);
        String sAfter = String.valueOf(stringFile);
        deleting = false;
        editingTagTxtFile(sAfter, deleting);
    }

    public void deletingStringsForTxtFile(ArrayList<String> a, String nameTag)
    {
        StringBuffer stringFile = new StringBuffer();
        for (String tag : a)
        {
            stringFile.append(tag);
            stringFile.append(" ");
        }
        stringFile.delete(stringFile.indexOf(nameTag), stringFile.indexOf(nameTag) + nameTag.length() + 1);
        String sAfter = String.valueOf(stringFile);
        a.remove(nameTag);
        deleting = true;
        editingTagTxtFile(sAfter, deleting);
    }

    public void editingTagTxtFile(String sA, boolean deleting)
    {
        try
        {  // reading the searchTags file and updating it
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(searchTagsFile)), StandardCharsets.UTF_8));
            if (!deleting)
            {
                String sToAdd = sA;
                fileContent.set(0, sToAdd);
            }
            else
                fileContent.set(0, sA);
            Files.write(Path.of(String.valueOf(searchTagsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) { e.printStackTrace();}
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
    //</editor-fold>

}