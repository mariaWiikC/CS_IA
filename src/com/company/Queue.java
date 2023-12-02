package com.company;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

public class Queue
{
    Calendar cal;
    int currentMonth;
    protected ArrayList<String> fileContent, fileContentSongsTags, fileContentQueueS;
    HomePageMethods homePageMethodsObject = new HomePageMethods();
    private AddDeleteWindow addDeleteObject;
    AddingDeleting addingDeletingObject;


    Queue() throws IOException
    {
        cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH) + 1;

        try
        {
            addDeleteObject = new AddDeleteWindow();
            addDeleteObject.dispose();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        try
        {
            addingDeletingObject = new AddingDeleting();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void updateQueue()
    {
        //<editor-fold desc="Themes tags added">
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueFile)), StandardCharsets.UTF_8));
            int numReps = fileContent.size();
            for (int i = 0; i < numReps; i++)
            {
                if (i > 0)
                {
                    fileContent.remove(fileContent.get(1));
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (currentMonth > 9)
        {
            try
            {
                // if it is past September, add Christmas and Independence Day tags, as December
                // is approaching
                fileContent.add("Christmas ");
                fileContent.add("Independence ");
                Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent, StandardCharsets.UTF_8);
            } catch (IOException a)
            {
                a.printStackTrace();
            }
        }

        if (currentMonth < 3)
        {
            try
            {
                // if it is not March yet, add Easter tag, as it is approaching
                fileContent.add("Easter ");
                Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent, StandardCharsets.UTF_8);
            } catch (IOException a)
            {
                a.printStackTrace();
            }
        }
        //</editor-fold>

        //<editor-fold desc="Time of day">
        String[] timeDayArr = {"morning ", "afternoon ", "evening "};

        try
        {
            // recording real time to add it as a recommendation parameter
            LocalTime now = LocalTime.now();
            int hours = now.getHour();
            if (hours >= 6 && hours < 12)
                fileContent.add("morning ");
            if (hours >= 12 && hours < 18)
                fileContent.add("afternoon ");
            if (hours >= 18 || hours < 6)
                fileContent.add("evening ");
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
        //</editor-fold>

        // all tags that set the conditions for the recommendation are defined and recorded
        // now, the songs that fulfill the conditions will be added to the queueSongs file
        // which can be played as a regular playlist

        //<editor-fold desc="Adding songs">
        // find most and least played songs
        int min = 2147483647, max = 0, minIndex = 0, maxIndex = 0;
        try
        {
            // read file with all songs
            fileContentSongsTags = new ArrayList<>(Files.readAllLines(
                    Path.of(String.valueOf(addingDeletingObject.songsAndTagsFile)),
                    StandardCharsets.UTF_8));
            for (int i = 0; i < fileContentSongsTags.size(); i++)
            {
                String[] sArray = fileContentSongsTags.get(i).split(" ");
                // access the number of times the song has been played
                int numPlays = Integer.valueOf(sArray[sArray.length - 1]);
                // if it is smaller than the minimum, it is the least played song
                if (numPlays < min)
                {
                    min = numPlays;
                    // record the index to locate the song and later save it
                    minIndex = i;
                }
                // if it is larger than the minimum, it is the most played song
                if (numPlays > max)
                {
                    max = numPlays;
                    maxIndex = i;
                }
            }

            // read the queueSongsFile, which contains the previously recommended songs
            fileContentQueueS = new ArrayList<>(Files.readAllLines(
                    Path.of(String.valueOf(homePageMethodsObject.queueSongsFile)),
                    StandardCharsets.UTF_8));
            // delete previous recommendations
            for (int i = 0; i < fileContentQueueS.size(); i++)
                fileContentQueueS.remove(fileContentQueueS.get(0));
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueSongsFile)), fileContentQueueS, StandardCharsets.UTF_8);


            // adding the most and least played songs
            for (int i = 0; i < fileContentSongsTags.size(); i++)
            {
                String[] sArray = fileContentSongsTags.get(i).split(" ");
                // only the first element contains the name of the song, the others are tags
                if (i == minIndex)
                    fileContentQueueS.add(sArray[0]);
                if (i == maxIndex)
                    fileContentQueueS.add(sArray[0]);
            }

            // now, the songs which contain at least one parameter tag are added to the queueSongsFile
            for (int i = 0; i < fileContent.size(); i++)
            {
                // delete the placeholder "mood" on the Queue file and substitute it with the
                // user's selected mood
                StringBuffer mood = new StringBuffer(fileContent.get(i));
                mood.delete(mood.length() - 1, mood.length());
                String actualMood = String.valueOf(mood);

                boolean hasTag = false;
                for (int l = 0; l < fileContentSongsTags.size(); l++)
                {
                    // obtain the song and its tags
                    String[] sArray = fileContentSongsTags.get(l).split(" ");
                    for (String wordInLine : sArray)
                    {
                        // the mood is not in a specific index, so the array must be traversed
                        // until the selected mood is found or the array ends
                        if (wordInLine.equals(actualMood))
                        {
                            hasTag = true;
                            break;
                        }
                    }
                    // add song to file if it contains the tag and is not already in the file
                    if (hasTag && (!fileContentQueueS.contains(sArray[0])))
                        fileContentQueueS.add(sArray[0]);
                }

            }
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueSongsFile)), fileContentQueueS, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
        //</editor-fold>
    }

    //<editor-fold desc="Editing mood on queue txt file">
    // this method is performed when a mood button is clicked on the home page
    public void addFace(String mood)
    {
        try
        {
            // open the queue file and add the mood that corresponds to the clicked button
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, mood + " ");
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }
    //</editor-fold>
}