package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Song
{
    private ArrayList<String> fileContent;
    AddingDeleting addingDeletingObject;


    Song()
    {
        try
        {
            addingDeletingObject = new AddingDeleting();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void editingTagTxtFile(String songName, String sA, String songsAndTagsFilePath)
    {
        try
        {
            // is there a better way to refer to the object? Or should I have made an object and just open it on the window
            // instead of placing the whole code on the window -> i don't think this way it would open another window
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(songsAndTagsFilePath), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++)
            {
                String[] sArray = fileContent.get(i).split(" ");
                if (sArray[0].equals(songName))
                {
                    fileContent.set(i, sA);
                    break;
                }
            }
            Files.write(Path.of(String.valueOf(addingDeletingObject.songsAndTagsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
