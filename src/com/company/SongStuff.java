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
    protected long clipTimePosition, tenSec = 10000000;

    void playMusic(String musicLocation)
    {
        try
        {
            File musicPath = new File(musicLocation);

            if(musicPath.exists())
            {
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
            else
                System.out.println("Can't find file");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    void pauseMusic()
    {
        clipTimePosition = clip.getMicrosecondPosition();
        clip.stop();
    }

    void unpauseMusic()
    {
        clip.setMicrosecondPosition(clipTimePosition);
        clip.start();
    }

    void tenSecForward()
    {
        // it's only doing this once. After I click it again, it goes back to the time when I first clicked the button
        clip.setMicrosecondPosition(clipTimePosition + tenSec);
    }

    void tenSecBack()
    {
        clip.setMicrosecondPosition(clipTimePosition - tenSec);
    }

    // I need to create a method to break the loop
    void loopMusic()
    {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    void stopLoopMusic()
    {
        clip.loop(0);
    }
}
