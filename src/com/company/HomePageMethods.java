package com.company;

import java.io.IOException;
import java.util.ArrayList;

public class HomePageMethods extends Thread
{
    private SongStuff songObject = new SongStuff();

    public HomePageMethods()
    {
    }

    public void smtElse()
    {
        PlaylistMethods selectPlaylistObject = null;
        selectPlaylistObject = new PlaylistMethods();
        ArrayList<String> songsToPlay = new ArrayList<>(selectPlaylistObject.returningSongsPaths());

        System.out.println("Printing the thingy now");
        System.out.println(songsToPlay);
    }
}
