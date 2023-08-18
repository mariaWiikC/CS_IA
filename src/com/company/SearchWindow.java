package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SearchWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuPlaylists, menuSongs, menuQueue;

    public SearchWindow()
    {
        super("Search");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());

        // I want this to be kinda the filters (tags here) - so it's like what should appear when
        // te user clicks the filter button
        // so like a subwindow-ish thing, not full screen, just right there in the middle

        setVisible(true);
    }

}