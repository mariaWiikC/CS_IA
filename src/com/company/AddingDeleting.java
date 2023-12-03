package com.company;

import javax.swing.*;
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
    private ArrayList<String> fileContent, fileContentPlaylist;
    private String nameWritten, targetPath;
    PlaylistsWindow playlistObject;
    HomePageMethods homePageMethodsObject = new HomePageMethods();

    AddingDeleting() throws IOException
    {
        //<editor-fold desc="Setting up the list of songs">
        String directorySongsFilePath = "src/songsFiles";
        // Gather all files (songs) in the folder songsFiles
        File directorySongs = new File(directorySongsFilePath);
        File[] filesSongs = directorySongs.listFiles(File::isFile);

        playlistObject = new PlaylistsWindow();
        playlistObject.dispose();

        listModel = new DefaultListModel();
        // JList created with songs in the songsFiles
        for (File f : filesSongs)
        {
            // removing ".wav" from the name of the file to obtain only the
            // name of the song
            StringBuffer songName = new StringBuffer();
            songName.append(f.getName());
            songName.delete(songName.length() - 4, songName.length());
            String songAdd = String.valueOf(songName);

            listModel.addElement(songAdd);
        }
        listSongs = new JList(listModel);
        listSongs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSongs.setLayoutOrientation(JList.VERTICAL);
        listSongs.setVisibleRowCount(-1);

        // creation of the songs and tags file
        songsAndTagsFile = new File("src\\SongsWithTags.txt");
        if (!songsAndTagsFile.exists())
            songsAndTagsFile.createNewFile();

        //</editor-fold>
    }

    public boolean addSong()
    {
        // opening the file chooser
        JFileChooser songUpload = new JFileChooser();
        int res2 = songUpload.showSaveDialog(null);
        // Confirming action
        if (res2 == JFileChooser.APPROVE_OPTION)
        {
            Path sourcePath = Path.of(songUpload.getSelectedFile().getAbsolutePath());
            // is it a WAV file?
            StringBuffer typeOfFile = new StringBuffer(String.valueOf(sourcePath));
            typeOfFile.delete(0, typeOfFile.length() - 4);
            if (String.valueOf(typeOfFile).equals(".wav"))
            {
                targetPath = "src/songsFiles/newSong";
                try
                {
                    // copying the file
                    Files.copy(sourcePath, Path.of(targetPath),
                            new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Invalid file format");
                return false;
            }
        }
        return true;
    }

    void validateButton(String newNameStr)
    {
        // creating the path for the renamed new song
        String newName = "src/songsFiles/" + newNameStr + ".wav";
        // adding the song to the list of all songs in this window
        listModel.addElement(newNameStr);
        try
        {
            // creating the new file with the correct name
            Files.copy(Path.of(targetPath), Path.of(newName),
                    new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            JOptionPane.showMessageDialog(null, "File information saved");
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        // delete the file named newSong
        File toDelete = new File(targetPath);
        toDelete.delete();
        // ADDING SONG TO TXT FILE
        try
        {
            // reading the file that contains all songs and their tags
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(songsAndTagsFile)),
                    StandardCharsets.UTF_8));
            // adding the song to that file. "0" indicates the number of times this song has been played
            fileContent.add(newNameStr + " 0 ");
            Files.write(Path.of(String.valueOf(songsAndTagsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void deleteSong()
    {
        // deleting the selected song from the list of all songs
        if (listSongs.getSelectedIndex() != -1)
        {
            nameWritten = (String) listSongs.getSelectedValue();
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

            deletingFromPlaylist();
        }
    }

    public void deletingFromPlaylist()
    { // going through each playlist on the playlistsFile to delete the song
        try
        { // reading names of all playlists
            fileContent = new ArrayList<>(Files.readAllLines(
                    Path.of(String.valueOf(playlistObject.playlistsFile)), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++)
            {
                String playlistPath = "src/" + fileContent.get(i) + ".txt";
                try
                { // open specific playlist file
                    fileContentPlaylist = new ArrayList<>(Files.readAllLines(Path.of(playlistPath),
                            StandardCharsets.UTF_8));
                    for (int j = 0; j < fileContent.size(); j++)
                    {
                        String[] sArray = fileContentPlaylist.get(j).split(" ");
                        // the first element of the array is the name of the song, the others are tags
                        if (sArray[0].equals(nameWritten))
                        { // delete the song and tags associated with it from the file
                            fileContentPlaylist.remove(j);
                            break;
                        }
                    }
                    Files.write(Path.of(playlistPath), fileContentPlaylist, StandardCharsets.UTF_8);
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
            // open specific file
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(fileName)),
                    StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++)
            {
                String[] sArray = fileContent.get(i).split(" ");
                // the first element of the array is the name of the song
                if (sArray[0].equals(nameWritten))
                {
                    // delete the song from the file
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
