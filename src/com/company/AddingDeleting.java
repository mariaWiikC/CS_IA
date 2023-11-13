package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class AddingDeleting
{
    protected JList listSongs;
    private DefaultListModel listModel;
    public File songsAndTagsFile;
    private ArrayList<String> fileContent, fileContent2;
    private String nameWritten, targetPath;
    PlaylistsWindow playlistObject;


    HomePageMethods homePageMethodsObject = new HomePageMethods();

    AddingDeleting() throws IOException
    {
        // I want to display all the uploaded songs
        String directorySongsFilePath = "src/songsFiles";

        //<editor-fold desc="Setting up the list of songs">
        File directorySongs = new File(directorySongsFilePath);
        File[] filesSongs = directorySongs.listFiles(File::isFile);

        playlistObject = new PlaylistsWindow();
        playlistObject.dispose();

        listModel = new DefaultListModel();
        for (File f : filesSongs)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(f.getName());
            sb.delete(sb.length() - 4, sb.length());
            String addNow = String.valueOf(sb);
            listModel.addElement(addNow);
        }
        listSongs = new JList(listModel);
        listSongs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSongs.setLayoutOrientation(JList.VERTICAL);
        listSongs.setVisibleRowCount(-1);

        // creation of the songs and tags file
        songsAndTagsFile = new File("src\\SongsWithTags.txt");
        if (!songsAndTagsFile.exists())
            songsAndTagsFile.createNewFile();

        // FIX PLAYLIST WINDOW CLASS NEXT

        //</editor-fold>
    }
    public void addSong()
    {
        JFileChooser songUpload = new JFileChooser();
        int res2 = songUpload.showSaveDialog(null);

        // copying the file
        if (res2 == JFileChooser.APPROVE_OPTION)
        {
            File songPath = new File(songUpload.getSelectedFile().getAbsolutePath());

            Path sourcePath = Path.of(songUpload.getSelectedFile().getAbsolutePath());
            targetPath = "src/songsFiles/newSong";

            try
            {
                Files.copy(sourcePath, Path.of(targetPath), new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    void validateButton(String newNameStr)
    {
        JOptionPane.showMessageDialog(null, "File information saved");
        String newName = "src/songsFiles/" + newNameStr + ".wav";
        System.out.println(newName);
        listModel.addElement(newNameStr);

        try
        {
            Files.copy(Path.of(targetPath), Path.of(newName), new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        File toDelete = new File(targetPath);
        toDelete.delete();

        // ADDING SONG TO TXT FILE
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(songsAndTagsFile)), StandardCharsets.UTF_8));
            fileContent.add(newNameStr + " 0 ");
            Files.write(Path.of(String.valueOf(songsAndTagsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void deleteSong()
    {
        if (listSongs.getSelectedIndex() != -1)
        {
            nameWritten = (String) listSongs.getSelectedValue();
        }
        String nameFile = "src/songsFiles/" + nameWritten + ".wav";
        int index = listSongs.getSelectedIndex();
        listModel.remove(index);
        JOptionPane.showMessageDialog(null, "File deleted");

        try
        {
            Files.deleteIfExists(Path.of(nameFile));
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        deletingSong(songsAndTagsFile);
        deletingSong(homePageMethodsObject.searchResultsFile);
        deletingSong(homePageMethodsObject.queueSongsFile);

        // go through each playlist on the playlistsFile
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(playlistObject.playlistsFile)), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++)
            {
                String playlistPath = "src/" + fileContent.get(i) + ".txt";
                try
                {
                    fileContent2 = new ArrayList<>(Files.readAllLines(Path.of(playlistPath), StandardCharsets.UTF_8));
                    for (int j = 0; j < fileContent.size(); j++)
                    {
                        String[] sArray = fileContent2.get(j).split(" ");
                        if (sArray[0].equals(nameWritten))
                        {
                            fileContent2.remove(j);
                            break;
                        }
                    }
                    Files.write(Path.of(playlistPath), fileContent2, StandardCharsets.UTF_8);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void deletingSong(File fileName)
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(fileName)), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++)
            {
                String[] sArray = fileContent.get(i).split(" ");
                if (sArray[0].equals(nameWritten))
                {
                    fileContent.remove(i);
                    break;
                }
            }
            Files.write(Path.of(String.valueOf(fileName)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
