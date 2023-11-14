package com.company;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HomePageMethods extends Thread
{
    File searchResultsFile, queueFile, queueSongsFile;

    public HomePageMethods() throws IOException
    {
        searchResultsFile = new File("src\\SearchResults.txt");
        if (!searchResultsFile.exists())
            searchResultsFile.createNewFile();

        queueFile = new File("src\\Queue.txt");
        if (!queueFile.exists())
        {
            queueFile.createNewFile();
            try (FileWriter fw = new FileWriter(queueFile.getAbsoluteFile()); BufferedWriter bw = new BufferedWriter(fw))
            {
                bw.write("mood");
                bw.write(" ");
                bw.newLine();
            } catch (IOException exc)
            {
                exc.printStackTrace();
                System.out.println("Got exception: " + exc);
                System.exit(1);
            }
        }

        queueSongsFile = new File("src\\QueueSongs.txt");
        if (!queueSongsFile.exists())
            queueSongsFile.createNewFile();
    }
}
