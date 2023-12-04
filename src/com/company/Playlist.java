package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Playlist extends Thread
{
    protected JScrollPane listScroller;
    protected JList listPlaylists;
    protected DefaultListModel listModel;
    PlaylistsWindow playlistsObject;
    protected JButton playButton;
    protected ArrayList<String> playlistFileContent, songsPaths = new ArrayList<>();
    AudioControl songObject;

    public Playlist()
    {
        try
        {
            playlistsObject = new PlaylistsWindow();
            playlistsObject.dispose();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //<editor-fold desc="Setting up playlists list">
        listModel = new DefaultListModel();
        playlistsObject.readingTxtFile();
        for (ArrayList<String> arr : playlistsObject.allPlaylists)
        {
            StringBuffer sb = new StringBuffer();
            for (String s : arr)
                sb.append(s);
            String addNow = String.valueOf(sb);
            listModel.addElement(addNow);
        }
        listPlaylists = new JList(listModel);
        listPlaylists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPlaylists.setLayoutOrientation(JList.VERTICAL);
        listPlaylists.setVisibleRowCount(-1);

        listScroller = new JScrollPane(listPlaylists);
        listScroller.setMaximumSize(new Dimension(250, 300));
        //</editor-fold>

        playButton = new JButton("Play");
        playButton.addActionListener(this::playPlaylist);

        songObject = new AudioControl();
    }

    public void playPlaylist(ActionEvent e)
    {
        if (listPlaylists.getSelectedValue() != null)
        {
            String namePlaylist = String.valueOf(listPlaylists.getSelectedValue());
            String playlistPath = "src\\" + namePlaylist + ".txt";
            songsPaths.clear();
            try
            {
                playlistFileContent = new ArrayList<>(Files.readAllLines(Path.of(playlistPath), StandardCharsets.UTF_8));
                for (String songName : playlistFileContent)
                {
                    songsPaths.add("src\\songsFiles\\" + songName + ".wav");
                }
                System.out.println(songsPaths);
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}