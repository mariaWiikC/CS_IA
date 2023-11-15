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
    String numOfPlays;

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

    public void creatingNewStringsForTxtFile(ArrayList<String> a, String nameTag)
    {
        String songName = a.get(0);

        StringBuffer sbO = new StringBuffer();
        for (String add : a)
        {
            sbO.append(add);
            sbO.append(" ");
        }

        boolean integerLast = false;
        // checking if last item is an int
        // checking if the line has more than just the name of the song there
        if (sbO.lastIndexOf(" ", sbO.lastIndexOf(" ") - 1) > -1)
        {
            StringBuffer sb = new StringBuffer(sbO);
            System.out.println(sb.lastIndexOf(" "));
            System.out.println("Length: " + sb.length());
            sb.delete(0, sb.lastIndexOf(" ", sb.lastIndexOf(" ") - 1));
            System.out.println(sb);
            sb.delete(sb.length() - 1, sb.length());
            sb.delete(0, 1);
            System.out.println(sb);

            // deleting the integer
            if (Integer.parseInt(String.valueOf(sb)) % 1 == 0)
            {
                numOfPlays = String.valueOf(sb);
                sbO.delete(sbO.lastIndexOf(numOfPlays) - numOfPlays.length() + 1, sbO.lastIndexOf(numOfPlays) + numOfPlays.length());
                System.out.println(sbO);
                integerLast = true;
            }
        }

        StringBuffer sbA = new StringBuffer();
        sbA.append(sbO);
        sbA.append(nameTag);
        a.add(a.size() - 1, nameTag);
        sbA.append(" ");
        if (integerLast)
        {
            sbA.append(numOfPlays + " ");
        }

        String sA = String.valueOf(sbA);

        editingTagTxtFile(songName, sA, String.valueOf(addingDeletingObject.songsAndTagsFile));
    }

    public void deletingStringsForTxtFile(ArrayList<String> a, String nameTag)
    {
        String songName = a.get(0);

        StringBuffer sbO = new StringBuffer();
        for (String add : a)
        {
            sbO.append(add);
            sbO.append(" ");
        }

        StringBuffer sbA = new StringBuffer();
        sbA.append(sbO);
        sbA.delete(sbA.indexOf(nameTag), sbA.indexOf(nameTag) + nameTag.length() + 1);
        a.remove(nameTag);
        String sA = String.valueOf(sbA);

        editingTagTxtFile(songName, sA, String.valueOf(addingDeletingObject.songsAndTagsFile));
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
