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
    protected ArrayList<String> fileContent, fileContent2, fileContent3, fileContent4;
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
        // MAKE SURE I DELETE THE TEXT WHEN IT'S OUT OF SEASON
        // ALSO, CHECK IF THE TAG IS THERE ALREADY
        // clear the entire FILE CONTENT after the first element
        if (currentMonth > 9)
        {
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
                fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueFile)), StandardCharsets.UTF_8));
                for (int i = 0; i < fileContent.size(); i++)
                {
                    if (i > 0)
                    {
                        fileContent.remove(fileContent.get(1));
                    }
                }
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
            fileContent4 = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueFile)), StandardCharsets.UTF_8));
            for (String str : timeDayArr)
            {
                if (fileContent4.contains(str))
                {
                    fileContent4.remove(str);
                }
            }
            LocalTime now = LocalTime.now();
            int hours = now.getHour();
            if (hours >= 6 && hours < 12)
                fileContent4.add("morning ");
            if (hours >= 12 && hours < 18)
                fileContent4.add("afternoon ");
            if (hours >= 18 || hours < 6)
                fileContent4.add("evening ");
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent4, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
        //</editor-fold>

        // adding songs to queueSongs txt file
        //<editor-fold desc="Adding songs">
        // most and least selected songs
        int min = 100000000, max = 0, minIndex = 0, maxIndex = 0;
        try
        {
            // is there a better way to refer to the object? Or should I have made an object and just open it on the window
            // instead of placing the whole code on the window -> i don't think this way it would open another window
            fileContent2 = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(addingDeletingObject.songsAndTagsFile)), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent2.size(); i++)
            {
                String[] sArray = fileContent2.get(i).split(" ");
                if (Integer.valueOf(sArray[sArray.length - 1]) < min && Integer.valueOf(sArray[sArray.length - 1]) > 0)
                {
                    min = Integer.valueOf(sArray[sArray.length - 1]);
                    minIndex = i;
                }
                if (Integer.valueOf(sArray[sArray.length - 1]) > max)
                {
                    max = Integer.valueOf(sArray[sArray.length - 1]);
                    maxIndex = i;
                }
            }

            fileContent3 = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueSongsFile)), StandardCharsets.UTF_8));
            int numReps = fileContent3.size();
            for (int i = 0; i < numReps; i++)
            {
                fileContent3.remove(fileContent3.get(0));
            }
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueSongsFile)), fileContent3, StandardCharsets.UTF_8);


            // adding the most and least played songs
            for (int i = 0; i < fileContent2.size(); i++)
            {
                String[] sArray = fileContent2.get(i).split(" ");
                if (i == minIndex)
                {
                    fileContent3.add(sArray[0]);
                }
                if (i == maxIndex)
                {
                    fileContent3.add(sArray[0]);
                }
            }

            // NOW I GOTTA ADD THE SONGS THAT HAVE THE TAGS
            for (int i = 0; i < fileContent4.size(); i++)
            {
                StringBuffer sb = new StringBuffer(fileContent4.get(i));
                sb.delete(sb.length() - 1, sb.length());
                String actualMood = String.valueOf(sb);

                boolean hasTag = false;
                for (int l = 0; l < fileContent2.size(); l++)
                {
                    String[] sArray = fileContent2.get(l).split(" ");
                    for (String wordInLine : sArray)
                    {
                        if (wordInLine.equals(actualMood))
                        {
                            hasTag = true;
                            break;
                        }
                    }
                    if (hasTag && (!fileContent3.contains(sArray[0])))
                    {
                        fileContent3.add(sArray[0]);
                    }
                }

            }

            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueSongsFile)), fileContent3, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
        //</editor-fold>
    }

    //<editor-fold desc="Editing mood on queue txt file">
    public void addSadFace()
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, "sad ");
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }

    public void addEnergeticFace()
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, "energetic ");
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }

    public void addHappyFace()
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, "happy ");
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }

    public void addRelaxedFace()
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(homePageMethodsObject.queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, "relaxed ");
            Files.write(Path.of(String.valueOf(homePageMethodsObject.queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }
    //</editor-fold>
}