package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchResultsWindow extends JFrame
{
    private JList listSongs;
    private JPanel pCenter;
    private HomePageMethods homePageObject;
    private DefaultListModel listModel;
    private JScrollPane listScroller;
    private ArrayList<ArrayList> searchResultsArr;

    public SearchResultsWindow() throws IOException
    {
        super("Search Results");
        setSize(250, 400);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        pCenter = new JPanel();
        pCenter.setPreferredSize(new Dimension(250, 400));
        add(pCenter, BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(pCenter);
        pCenter.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        homePageObject = new HomePageMethods();

        //<editor-fold desc="Reading txt file">
        // reading search results text file into array list
        searchResultsArr = new ArrayList<>();
        Scanner scanner = new Scanner(homePageObject.searchResultsFile);
        ArrayList<String> readingResults = new ArrayList<>();

        while (scanner.hasNextLine())
            readingResults.add(scanner.nextLine());

        // split the results so that each element is a single song
        for (String s : readingResults)
        {
            ArrayList<String> toAdd = new ArrayList<>();
            String[] sArray = s.split(" ");
            for (String songs : sArray)
            {
                toAdd.add(songs);
            }
            searchResultsArr.add(toAdd);
        }
        //</editor-fold>

        //<editor-fold desc="Setting up list">
        // use the search results array list as the model for the JList
        listModel = new DefaultListModel();
        for (ArrayList<String> arr : searchResultsArr)
        {
            for (String song : arr)
                listModel.addElement(song);
        }
        listSongs = new JList(listModel);
        listSongs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSongs.setLayoutOrientation(JList.VERTICAL);
        listSongs.setVisibleRowCount(-1);

        listScroller = new JScrollPane(listSongs);
        listScroller.setMaximumSize(new Dimension(250, 300));
        //</editor-fold>

        //<editor-fold desc="LAYOUT">
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(listScroller)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(listScroller)
        );
        //</editor-fold>

        setVisible(true);
    }
}
