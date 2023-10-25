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
    private HomePageWindow homePageObject;
    private DefaultListModel listModel;
    private JScrollPane listScroller;
    private String searchResults;
    private ArrayList<ArrayList> searchResultsArr;

    public SearchResultsWindow() throws IOException
    {
        super("Search Results");
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

        // CREATE A LIST WITH ALL THE SEARCH RESULTS
        homePageObject = new HomePageWindow();
        homePageObject.dispose();

        //<editor-fold desc="Reading txt file">
        searchResultsArr = new ArrayList<>();
        Scanner scanner = new Scanner(homePageObject.searchResultsFile);
        ArrayList<String> readingResults = new ArrayList<>();

        while (scanner.hasNextLine())
        {
            readingResults.add(scanner.nextLine());
        }
        for (String s : readingResults)
        {
            ArrayList<String> toAdd = new ArrayList<>();
            String[] sArray = s.split(" ");
            for (String str : sArray)
            {
                toAdd.add(str);
            }
            searchResultsArr.add(toAdd);
        }
        System.out.println(searchResultsArr);
        //</editor-fold>

        //<editor-fold desc="Setting up list">
        listModel = new DefaultListModel();
        for (ArrayList<String> arr : searchResultsArr)
        {
            for (String s : arr)
            {
                listModel.addElement(s);
            }
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
