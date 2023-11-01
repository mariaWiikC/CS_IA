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
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuPlaylists, menuSongs, menuQueue, menuHome;
    private JPanel pCenter;
    private JLabel searchT, moodT, timeT, instrumentT, themeT, timeLabel;
    private JCheckBox sadBox, energeticBox, happyBox, relaxedBox, morningBox, afternoonBox, eveningBox,
            guitarBox, pianoBox, vocalBox, ChristmasBox, IndependenceBox, EasterBox, realTimeBox;
    private Timer timer;
    private boolean searching = false;
    protected File searchTagsFile;
    private ArrayList<String> fileContent;
    private ArrayList<ArrayList> allTags;
    ArrayList<JCheckBox> checkBoxesList;
    private String searchTagsFilePath;
    private JButton choosingSearchTags;

    public SearchWindow()
    {
        // make a smaller JFrame - is there a way to leave the "minimize", "x" button out of the frame?
        // DO NOT USE SET BOUNDS, I AM SUPPOSED TO USE PANELS INSIDE PANELS
        super("Search");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // make a smaller JPanel
        pCenter = new JPanel();
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

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

        menuPhotos = new JMenuItem("Photos");
        // menuPhotos.addActionListener(this::actionPerformed5);

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

        // I want this to be kinda the filters (tags here) - so it's like what should appear when
        // the user clicks the filter button
        // so like a subwindow-ish thing, not full screen, just right there in the middle

        // WHAT I WANT TO DO NOW
        // I'm gonna create a txt file. It will contain the tags selected here.
        // GREATT, NOW
        // make the search happen in the home page!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        searchT = new JLabel("Search");
        searchT.setBounds(30, 100, 100, 35);
        pCenter.add(searchT);

        //<editor-fold desc="Combo Boxes">
        String[] days = {"Day", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
                "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
                "29", "30", "31"};
        String[] months = {"Month", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] years = {"Year", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013",
                "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024",
                "2025", "2026", "2027", "2028", "2029", "2030"};
        JComboBox comboBoxDays = new JComboBox(days);

        JComboBox comboBoxMonths = new JComboBox(months);

        JComboBox comboBoxYears = new JComboBox(years);

        pCenter.add(comboBoxDays);
        pCenter.add(comboBoxMonths);
        pCenter.add(comboBoxYears);
        //</editor-fold>

        //<editor-fold desc="Real time stuff">
        realTimeBox = new JCheckBox("Real Time");
        pCenter.add(realTimeBox);

        timeLabel = new JLabel(); // not updating in real time
        showTime();
        // System.out.println(showTime());
        pCenter.add(timeLabel);
        //</editor-fold>

        timer = new Timer (1000, (ActionEvent e) -> showTime ());
        timer.start ();


        // add the bounds to everything hereeee
        // got them from here: https://www.javatpoint.com/java-jcheckbox
        //<editor-fold desc="Mood tags">
        moodT = new JLabel("Mood");
        moodT.setBounds(50, 150, 100, 35);
        pCenter.add(moodT);

        sadBox = new JCheckBox("Sad");
        sadBox.setBounds(50, 200, 100, 35);
        checkBoxesList.add(sadBox);
        pCenter.add(sadBox);

        energeticBox = new JCheckBox("Energetic");
        energeticBox.setBounds(50, 250, 100, 35);
        checkBoxesList.add(energeticBox);
        pCenter.add(energeticBox);

        happyBox = new JCheckBox("Happy");
        happyBox.setBounds(50, 300, 100, 35);
        checkBoxesList.add(happyBox);
        pCenter.add(happyBox);

        relaxedBox = new JCheckBox("Relaxed");
        relaxedBox.setBounds(50, 350, 100, 35);
        checkBoxesList.add(relaxedBox);
        pCenter.add(relaxedBox);
        //</editor-fold>

        //<editor-fold desc="Time tags">
        timeT = new JLabel("Time");
        timeT.setBounds(200, 150, 100, 35);
        pCenter.add(timeT);

        morningBox = new JCheckBox("Morning");
        morningBox.setBounds(200, 200, 100, 35);
        checkBoxesList.add(morningBox);
        pCenter.add(morningBox);

        afternoonBox = new JCheckBox("Afternoon");
        afternoonBox.setBounds(200, 250, 100, 35);
        checkBoxesList.add(afternoonBox);
        pCenter.add(afternoonBox);

        eveningBox = new JCheckBox("Evening");
        eveningBox.setBounds(200, 300, 100, 35);
        checkBoxesList.add(eveningBox);
        pCenter.add(eveningBox);
        //</editor-fold>

        //<editor-fold desc="Instruments tags">
        instrumentT = new JLabel("Instrument");
        instrumentT.setBounds(350, 150, 100, 35);
        pCenter.add(instrumentT);

        guitarBox = new JCheckBox("Guitar");
        guitarBox.setBounds(350, 200, 100, 35);
        checkBoxesList.add(guitarBox);
        pCenter.add(guitarBox);

        pianoBox = new JCheckBox("Piano");
        pianoBox.setBounds(350, 250, 100, 35);
        checkBoxesList.add(pianoBox);
        pCenter.add(pianoBox);

        vocalBox = new JCheckBox("Vocal");
        vocalBox.setBounds(350, 300, 100, 35);
        checkBoxesList.add(vocalBox);
        pCenter.add(vocalBox);
        //</editor-fold>

        //<editor-fold desc="Themes tags">
        themeT = new JLabel("Theme");
        themeT.setBounds(500, 150, 100, 35);
        pCenter.add(themeT);

        ChristmasBox = new JCheckBox("Christmas");
        ChristmasBox.setBounds(500, 200, 100, 35);
        checkBoxesList.add(ChristmasBox);
        pCenter.add(ChristmasBox);

        IndependenceBox = new JCheckBox("Independence Day");
        IndependenceBox.setBounds(500, 250, 200, 35);
        checkBoxesList.add(IndependenceBox);
        pCenter.add(IndependenceBox);

        EasterBox = new JCheckBox("Easter");
        EasterBox.setBounds(500, 300, 100, 35);
        checkBoxesList.add(EasterBox);
        pCenter.add(EasterBox);
        //</editor-fold>

        // disable check boxes
        for (JCheckBox cb : checkBoxesList)
        {
            cb.setEnabled(false);
        }

        setVisible(true);
    }

    public void showAndEditTagsSongs(ActionEvent ae)
    {
        allTags = new ArrayList<>();
        try
        {
            // reading the txt file
            Scanner scanner = new Scanner(searchTagsFile);
            ArrayList<String> searchTags = new ArrayList<>();

            while (scanner.hasNextLine())
            {
                searchTags.add(scanner.nextLine());
            }
            for (String s : searchTags)
            {
                ArrayList<String> toAdd = new ArrayList<>();
                String[] sArray = s.split(" ");
                for (String str : sArray)
                {
                    toAdd.add(str);
                }
                allTags.add(toAdd);
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        if (searching)
        {
            enablingCheckBoxes(false);
            for (ArrayList<String> a : allTags)
            {
                // checking the tags
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

        if (!searching)
        {
            // display correctly the selection of tags
            enablingCheckBoxes(true);

            for (ArrayList<String> a : allTags)
            {
                // checking the tags
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

        if (searching)
            enablingCheckBoxes(false);

        searching = !searching;
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
        // I need to create smt to delete the tag from the txt file if the box is unchecked -> slay
        if (!searching)
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
                    creatingNewStringsForTxtFile(a, nameTag);
            }
            else
            {
                if (a.contains(nameTag))
                    deletingStringsForTxtFile(a, nameTag);
            }
        }
    }

    public void creatingNewStringsForTxtFile(ArrayList<String> a, String nameTag)
    {
        String songName = a.get(0);

        StringBuffer sbO = new StringBuffer();
        for (String add : a)
        {
            sbO.append(add);
            sbO.append(" ");
        }

        System.out.println(sbO);

        StringBuffer sbA = new StringBuffer();
        sbA.append(sbO);
        sbA.append(nameTag);
        sbA.append(" ");

        String sA = String.valueOf(sbA);
        System.out.println(sA);

        editingTagTxtFile(songName, sA, searchTagsFilePath);
    }

    public void deletingStringsForTxtFile(ArrayList<String> a, String nameTag)
    {
        String songName = a.get(0);

        StringBuffer sbO = new StringBuffer();
        for (String add : a)
        {
            sbO.append(add);
            sbO.append(" ");
        }

        StringBuffer sbA = new StringBuffer();
        sbA.append(sbO);
        sbA.delete(sbA.indexOf(nameTag), sbA.indexOf(nameTag) + nameTag.length() + 1);

        String sA = String.valueOf(sbA);

        editingTagTxtFile(songName, sA, searchTagsFilePath);
    }

    public void editingTagTxtFile(String songName, String sA, String searchTagsFilePath)
    {
        try
        {
            // is there a better way to refer to the object? Or should I have made an object and just open it on the window
            // instead of placing the whole code on the window -> i don't think this way it would open another window
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(searchTagsFilePath), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++)
            {
                // the problem here is that now the element in fileContent = "nameSong tag1"
                // I need to make it just nameSong
                String[] sArray = fileContent.get(i).split(" ");
                if (sArray[0].equals(songName))
                {
                    fileContent.set(i, sA);
                    break;
                }
            }
            Files.write(Path.of(String.valueOf(searchTagsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void showTime()
    {
        LocalTime now = LocalTime.now();
        int hours = now.getHour(), minutes = now.getMinute(), seconds = now.getSecond();
        timeLabel.setText(formatInt(hours) + ":" + formatInt(minutes) + ":" + formatInt(seconds));
    }

    private String formatInt(int i)
    {
        return String.format("%02d", i);
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
        JFrame QueuePreviewWindow = new QueuePreviewWindow(); // open another JFrame
        QueuePreviewWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }
    //</editor-fold>

}