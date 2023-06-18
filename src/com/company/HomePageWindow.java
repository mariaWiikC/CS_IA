package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageWindow extends JFrame implements ActionListener
{
    private JMenuBar menuBar;
    private JPanel p;
    private JButton toSelectPlayWindow, toAddDeleteWindow, toPhotosWindow, toPlaylistsWindow,
    toQueuePreviewWindow, toSongsWindow;


    public HomePageWindow()
    {
        super("Audio Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        /*
        p = new JPanel();
        p.setLayout(null);

        toSelectPlayWindow = new JButton("Select song, playlist, or queue");
        toSelectPlayWindow.setFocusable(false);
        toSelectPlayWindow.setBounds(0, 30, 195, 30);
        p.add(toSelectPlayWindow);

         */

        /*
        toAddDeleteWindow = new JButton("Add/Delete");
        toAddDeleteWindow.setFocusable(false);
        //toAddDeleteWindow.setBounds(30, 0, 54, 30);
        toPhotosWindow = new JButton("Photos");
        toPhotosWindow.setFocusable(false);

        toPlaylistsWindow = new JButton("Playlists");
        toPlaylistsWindow.setFocusable(false);

        toQueuePreviewWindow = new JButton("Queue Preview");
        toQueuePreviewWindow.setFocusable(false);

        toSongsWindow = new JButton("Songs");
        toSongsWindow.setFocusable(false);

         */

        // toSelectPlayWindow.addActionListener(this);
        /*
        toAddDeleteWindow.addActionListener(this::actionPerformed2);
        toSongsWindow.addActionListener(this::actionPerformed3);
        toPlaylistsWindow.addActionListener(this::actionPerformed4);
        toPhotosWindow.addActionListener(this::actionPerformed5);
        toQueuePreviewWindow.addActionListener(this::actionPerformed6);
        addButtons();

         */

        // add(p);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton jbutton = new JButton("Click");
        jbutton.setBounds(10, 10, 75, 25);  // x, y, width, height
        panel.add(jbutton);
        add(panel);
        setVisible(true);
    }
    public void addButtons()
    {
        p.add(toSelectPlayWindow, BorderLayout.PAGE_START);
        p.add(toAddDeleteWindow, BorderLayout.LINE_START);
        p.add(toPhotosWindow, BorderLayout.LINE_END);
        p.add(toPlaylistsWindow, BorderLayout.PAGE_END);
        p.add(toSongsWindow, BorderLayout.LINE_START);
        p.add(toQueuePreviewWindow, BorderLayout.LINE_END);
    }

    @Override
    // i found this here: https://stackoverflow.com/questions/9569700/java-call-method-via-jbutton
    public void actionPerformed(ActionEvent e)
    {
        JFrame SelectPlayWindow = new SelectPlayWindow(); // open another JFrame
        SelectPlayWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }
    public void actionPerformed2(ActionEvent e)
    {
        JFrame AddDeleteWindow = new AddDeleteWindow(); // open another JFrame
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
        JFrame PlaylistsWindow = new PlaylistsWindow(); // open another JFrame
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
}