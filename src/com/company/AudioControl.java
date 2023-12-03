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

    public Timer timer = new Timer(1000, new ActionListener() // updating every second
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (isPlaying)
                clipTimePosition = clip.getMicrosecondPosition();
            try
            {
                update();
            } catch (NoSuchFieldException ex) {ex.printStackTrace();}
            catch (IllegalAccessException ex) {ex.printStackTrace();}
        }
    });

    public AudioControl()
    {
        this.setLayout(new GridLayout(1, 0));
        pB = new JProgressBar();
        pB.setMinimum(0);
        pB.setPreferredSize(new Dimension(300, 20));
        pB.setMaximumSize(new Dimension(300, 20));
        pB.setMinimumSize(new Dimension(300, 20));
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
                // adding one to the number of times this song has been played
                addingNumOfPlays(musicLocation);
                isPlaying = true;

                // initializing clip
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clipLength = clip.getMicrosecondLength();

                pB.setValue(0);
                pB.setMaximum((int) clipLength);
                // total song duration labels
                songLengthMin = (int) (clipLength / 1000000) / 60;
                songLengthSec = (int) (clipLength / 1000000) % 60;
                totalTimeSong.setText(songLengthMin + ":" + songLengthSec);

                timer.start();
                return clip;
            }
            else
            {
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
        String timeNow = "00:00";

        // playing song from the start if it is looped and has ended
        if (isLooped && (clipLength - clipTimePosition < 1000) && clipLength != 0)
        {
            pB.setValue(0);
            songPositionMin = 0;
            songPositionSec = 0;
            clipTimePosition = 0;
            clip.setMicrosecondPosition(0);
        }
        // just playing song
        else
        {
            // managing progress labels
            songPositionMin = (int) (clipTimePosition / 1000000) / 60;
            songPositionSec = (int) (clipTimePosition / 1000000) % 60;
            // correct label display
            if (songPositionSec < 10 && songPositionMin < 10)
                timeNow = "0" + songPositionMin + ":0" + songPositionSec;
            if (songPositionSec >= 10 && songPositionMin < 10)
                timeNow = "0" + songPositionMin + ":" + songPositionSec;
            if (songPositionSec < 10 && songPositionMin >= 10)
                timeNow = songPositionMin + ":0" + songPositionSec;
            if (songPositionSec >= 10 && songPositionMin >= 10)
                timeNow = songPositionMin + ":" + songPositionSec;

            // update progress bar
            pB.setValue((int) (clipTimePosition));
        }
        timeSongNow.setText(timeNow);
    }

    public String whichPlayingNow()
    {
        return musicPath.getName();
    }

    public void addingNumOfPlays(String musicLocation) throws IOException
    {
        StringBuffer songName = new StringBuffer(musicLocation);
        // deleting .wav
        songName.delete(songName.length() - 4, songName.length());
        // 15 is to delete src\songsFiles\
        // only the actual name is important because that is what is present in the songs and tags file
        songName.delete(0, 15);
        // open the songsWithTags file, find the line with the song, get the last element (num of plays)
        // add one to it -> substitute it
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(
                    Path.of(String.valueOf(addDeleteObject.songsAndTagsFile)), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++)
            {
                String[] sArray = fileContent.get(i).split(" ");
                // the first element of the array is always th name of the song
                if (sArray[0].equals(String.valueOf(songName)))
                {
                    // the new number is equal to the previous number of plays (the last element) plus one
                    int newNum = Integer.parseInt(sArray[sArray.length - 1]) + 1;
                    sArray[sArray.length - 1] = String.valueOf(newNum);
                    // remove the line
                    fileContent.remove(i);
                    // build a string with all the elements from the array
                    StringBuffer stringFile = new StringBuffer();
                    for (String s : sArray)
                        stringFile.append(s + " ");
                    // add the altered text back to the file
                    fileContent.add(i, String.valueOf(stringFile));
                    break;
                }
            }
            Files.write(Path.of(String.valueOf(addDeleteObject.songsAndTagsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {e.printStackTrace();}
    }

    void pauseUnpauseMusic()
    {
        if (!paused)
        {
            // record where the song was paused
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
            paused = !paused;
            isPlaying = false;
        }
        else if (paused)
        {
            // return to where the song was paused
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();
            paused = !paused;
            isPlaying = true;
            // if the song was looped maintain the condition
            if (isLooped)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
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
        { // 0 stops loop
            clip.loop(0);
            isLooped = !isLooped;
        }
    }

    void stopPlaying()
    {
        isPlaying = false;
        clip.stop();
        // stop updates
        timer.stop();
    }
}
