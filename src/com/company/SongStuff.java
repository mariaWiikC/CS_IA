package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SongStuff
{
    AudioInputStream audioInput;
    protected Clip clip;
    protected long clipTimePosition, tenSec = 10000000, clipLength;
    protected boolean paused = false, isLooped = false;
    public boolean isPlaying = false;
    private ArrayList<String> fileContent;

    public SongStuff()
    {

    }

    Clip playMusic(String musicLocation)
    {
        try
        {
            File musicPath = new File(musicLocation);
            if (musicPath.exists())
            {
                addingNumOfPlays(musicLocation);
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
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return clip;
    }

    public void addingNumOfPlays(String musicLocation) // IS THIS NOT BEING EXECUTED??????
    {
        StringBuffer songName = new StringBuffer(musicLocation);
        songName.delete(songName.length() - 4, songName.length());
        songName.delete(0, songName.lastIndexOf("/") + 1);
        System.out.println(songName);

        AddDeleteWindow addDeleteObject = null;
        try
        {
            addDeleteObject = new AddDeleteWindow();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        addDeleteObject.dispose();

        // open the songsWithTags file, find the line with the song, get the last element
        // add one to the last element, and substitute it
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(addDeleteObject.songsAndTagsFile)), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++)
            {
                String[] sArray = fileContent.get(i).split(" ");
                if (sArray[0].equals(String.valueOf(songName)))
                {
                    int newNum = Integer.parseInt(sArray[sArray.length - 1]) + 1;
                    sArray[sArray.length - 1] = String.valueOf(newNum);
                    fileContent.remove(i);
                    // build a string with all the elements from the array
                    StringBuffer sb = new StringBuffer();
                    for (String s : sArray)
                    {
                        sb.append(s + " ");
                    }
                    fileContent.add(String.valueOf(sb));
                    break;
                }
            }
            Files.write(Path.of(String.valueOf(addDeleteObject.songsAndTagsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
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
        } catch (Exception e)
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
        if (!isLooped)
        {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            isLooped = !isLooped;
        }
        else if (isLooped)
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
