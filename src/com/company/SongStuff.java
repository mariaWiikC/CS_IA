package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

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

    void playMusic(String musicLocation)
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
            }
            else
                System.out.println("Can't find file");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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

    // need to add a button to stop playing the song!!! like, a square thing you know
    void stopPlaying()
    {
        isPlaying = false;
    }
}
