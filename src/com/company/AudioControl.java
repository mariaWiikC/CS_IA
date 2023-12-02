package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class AudioControl extends JPanel // could call it audiocontrol
{
    AudioInputStream audioInput;
    protected Clip clip;
    protected long clipTimePosition, tenSec = 10000000, clipLength;
    protected boolean paused = false, isLooped = false;
    public boolean isPlaying;
    private ArrayList<String> fileContent;
    File musicPath;
    int loopCounter = 0;

    protected JProgressBar pB;
    int songPositionSec = 0, songPositionMin = 0, songLengthMin = 0, songLengthSec = 0;
    protected JLabel timeSongNow, totalTimeSong;
    AddingDeleting addDeleteObject;

    {
        try
        {
            addDeleteObject = new AddingDeleting();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Timer timer = new Timer(1000, new ActionListener() // this is one second
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println(loopCounter);
            if (isPlaying)
            {
                clipTimePosition = clip.getMicrosecondPosition(); // - (loopCounter * clipLength);
                if (clipTimePosition < 0)
                {
                    System.out.println("why");
                    System.out.println("really, why");
                }
            }
            try
            {
                update();
            } catch (NoSuchFieldException ex)
            {
                ex.printStackTrace();
            } catch (IllegalAccessException ex)
            {
                ex.printStackTrace();
            }
        }
    });

    public AudioControl()
    {
        this.setLayout(new GridLayout(1, 0));
        pB = new JProgressBar();
        pB.setMinimum(0);
        timeSongNow = new JLabel("00:00");
        totalTimeSong = new JLabel("00:00");
    }

    Clip playMusic(String musicLocation)
    {
        try
        {
            musicPath = new File(musicLocation);
            if (musicPath.exists())
            {
                addingNumOfPlays(musicLocation);
                isPlaying = true;
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clipLength = clip.getMicrosecondLength();

                pB.setValue(0);
                pB.setPreferredSize(new Dimension(300, 20));
                pB.setMaximumSize(new Dimension(300, 20));
                pB.setMinimumSize(new Dimension(300, 20));

                timer.start();
                loopCounter = 0;
                return clip;
            }
            else
            {
                System.out.println("Can't find file");
                JOptionPane.showMessageDialog(null, "File not found");
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return clip;
    }

    private void update() throws NoSuchFieldException, IllegalAccessException
    {
        pB.setMaximum((int) clipLength / 1000000);
        String timeNow = "00:00";

        long actualSongPosition = clipTimePosition;
        if (isLooped && (clipLength - clipTimePosition < 1000) && clipLength != 0)
        {
            pB.setValue(0);
            songPositionMin = 0;
            songPositionSec = 0;
            clipTimePosition = 0;
            clip.setMicrosecondPosition(0);
            // System.out.println(clip.getMicrosecondPosition());
            loopCounter++;
        }
        else
        {
            // System.out.println(actualSongPosition);
            songPositionMin = (int) (clipTimePosition / 1000000) / 60;
            songPositionSec = (int) (clipTimePosition / 1000000) % 60;
            if (songPositionSec < 10 && songPositionMin < 10)
            {
                timeNow = "0" + songPositionMin + ":0" + songPositionSec;
            }
            if (songPositionSec >= 10 && songPositionMin < 10)
            {
                timeNow = "0" + songPositionMin + ":" + songPositionSec;
            }
            if (songPositionSec < 10 && songPositionMin >= 10)
            {
                timeNow = songPositionMin + ":0" + songPositionSec;
            }
            if (songPositionSec >= 10 && songPositionMin >= 10)
            {
                timeNow = songPositionMin + ":" + songPositionSec;
            }

            pB.setValue((int) (clipTimePosition / 1000000));
        }
        timeSongNow.setText(timeNow);
        timeSongNow.paintImmediately(timeSongNow.getVisibleRect());

        songLengthMin = (int) (clipLength / 1000000) / 60;
        songLengthSec = (int) (clipLength / 1000000) % 60;
        totalTimeSong.setText(songLengthMin + ":" + songLengthSec);
        totalTimeSong.paintImmediately(timeSongNow.getVisibleRect());

        System.out.println(clipTimePosition + " clip time position test");
    }

    public String whichPlayingNow()
    {
        System.out.println(musicPath.getName());
        return musicPath.getName();
    }

    public void addingNumOfPlays(String musicLocation) throws IOException
    {
        StringBuffer songName = new StringBuffer(musicLocation);
        songName.delete(songName.length() - 4, songName.length());
        // 15 is to delete src\songsFiles\
        songName.delete(0, 15);
        System.out.println(songName);

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

    void pauseUnpauseMusic()
    {
        if (!paused)
        {
            clipTimePosition = clip.getMicrosecondPosition(); // - (loopCounter * clipLength);
            System.out.println(clipTimePosition + " pause test");
            clip.stop();
            paused = !paused;
            isPlaying = false;
            loopCounter = 0;
        }
        else if (paused)
        {
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();
            paused = !paused;
            isPlaying = true;
            if (isLooped)
            {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }

    }

    void tenSecForward()
    {

        clipTimePosition = clip.getMicrosecondPosition(); // - (loopCounter * clipLength);
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
        timer.stop();
    }
}
