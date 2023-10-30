package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class SongStuff
{
    AudioInputStream audioInput;
    protected Clip clip;
    protected long clipTimePosition, tenSec = 10000000, clipLength;
    protected boolean paused = false, isLooped = false;
    public boolean isPlaying = false;

    public SongStuff()
    {

    }

    Clip playMusic(String musicLocation)
    {
        try
        {
            File musicPath = new File(musicLocation);
            if(musicPath.exists())
            {
                isPlaying = true;
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clipLength = clip.getMicrosecondLength();
                return clip;
            }
            else
                System.out.println("Can't find file");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return clip;
    }

    void playPlaylist(ArrayList<String> playlistName)
    {
        // THE BUTTONS ARE NOT WORKING
        // THE PROGRAM IS BASICALLY PAUSED HERE WHILE THE PLAYLIST IS PLAYING
        try
        {
            for (int i = 0; i < playlistName.size(); i++)
            {
                Clip currentClip = playMusic(playlistName.get(i));
                while (currentClip.getMicrosecondLength() != currentClip.getMicrosecondPosition())
                {
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    void pauseUnpauseMusic()
    {
        if (!paused)
        {
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
            paused = !paused;
        }
        else if (paused)
        {
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();
            paused = !paused;
        }

    }

    void tenSecForward()
    {
        clipTimePosition = clip.getMicrosecondPosition();
        clip.setMicrosecondPosition(clipTimePosition + tenSec);
    }

    void tenSecBack()
    {
        clipTimePosition = clip.getMicrosecondPosition();
        clip.setMicrosecondPosition(clipTimePosition - tenSec);
    }

    void loopMusic()
    {
        if(!isLooped)
        {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            isLooped = !isLooped;
        }
        else if(isLooped)
        {
            clip.loop(0);
            isLooped = !isLooped;
        }
    }

    void stopPlaying()
    {
        isPlaying = false;
        clip.stop();
    }
}
