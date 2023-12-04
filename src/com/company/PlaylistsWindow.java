package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class PlaylistsWindow extends JFrame
{
    private JScrollPane listScroller, listScroller2;
    private JList listSongs, listPlaylists;
    private DefaultListModel listModel, listModel2;
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuHome, menuSongs, menuQueue;
    private JPanel pCenter;
    private JLabel playlistsT;
    private ImageIcon addPlaylistIcon, deletePlaylistIcon;
    private JButton addPlaylistButton, deletePlaylistButton, editPlaylistButton, confirmButton;
    private JTextField playlistNameField;
    protected File playlistsFile, playlistFile;
    ArrayList<ArrayList> allPlaylists = new ArrayList<>();
    String directorySongsFilePath;
    boolean createOrEdit;
    private ArrayList<String> fileContent;
    private String playlistPath;
    private ArrayList<String> allSelected;
    private ArrayList<String> playlistFileContent;

    public PlaylistsWindow() throws IOException
    {
        // NOW: a way for the user to play a playlist
        // THEN: I think this will provide me with the knowledge to create a playlist based on the
        // mood button the user selects

        // MY IDEA:
        // I have a list of all the playlists, on the other side, there is a list of songs
        // If the user clicks the CREATE button, the input field is enabled, and they write its name.
        // the song list displays all the songs, they can select multiple, and then they can click
        // confirm -> it creates a txt file (and maybe a folder) with that name
        // if the user selects a playlist on the list, then clicks EDIT, then I can try to have the
        // songs in the playlist appear as selected elements on the list
        // when they click confirm, the txt file with the playlist's name is edited
        // DELETE button -> the selected playlist is deleted, as is its txt file

        // since the confirm button is for the edit and create, have a boolean control the action
        //<editor-fold desc="General Frame stuff">
        super("Playlists");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        // setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pCenter = new JPanel();
        pCenter.setLayout(null);
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(pCenter);
        pCenter.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        //</editor-fold>

        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Playlists");

        menuAddDelete = new JMenuItem("Add / Delete");
        menuAddDelete.addActionListener(this::actionPerformed2);

        menuPhotos = new JMenuItem("Photos");
        // menuPhotos.addActionListener(this::actionPerformed5);

        menuHome = new JMenuItem("Home Page");
        menuHome.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        fileMenu.add(menuAddDelete);
        fileMenu.add(menuPhotos);
        fileMenu.add(menuSongs);
        // fileMenu.add(menuHome);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        playlistsFile = new File("src\\Playlists.txt");
        if (!playlistsFile.exists())
            playlistsFile.createNewFile();

        // DON'T FORGET TO ADD THE ELEMENTS TO THE PANEL

        //<editor-fold desc="Setting up playlists list">
        listModel = new DefaultListModel();
        readingTxtFile();
        for (ArrayList<String> arr : allPlaylists)
        {
            StringBuffer sb = new StringBuffer();
            for (String s : arr)
                sb.append(s);
            String addNow = String.valueOf(sb);
            listModel.addElement(addNow);
        }
        listPlaylists = new JList(listModel);
        listPlaylists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPlaylists.setLayoutOrientation(JList.VERTICAL);
        listPlaylists.setVisibleRowCount(-1);

        listScroller = new JScrollPane(listPlaylists);
        listScroller.setMaximumSize(new Dimension(250, 300));
        //</editor-fold>

        // Set up songs list, but have it disabled. The list will be enabled and have selected songs only
        // if the create or edit buttons are clicked
        // I NEED ALL THE SONGS TO BE IN THIS LIST - i think i can copy it from the songs window

        //<editor-fold desc="Setting up the list of songs">
        directorySongsFilePath = "src/songsFiles";
        File directorySongs = new File(directorySongsFilePath);
        File[] filesSongs = directorySongs.listFiles(File::isFile);

        listModel2 = new DefaultListModel();
        for (File f : filesSongs)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(f.getName());
            sb.delete(sb.length() - 4, sb.length());
            String addNow = String.valueOf(sb);
            listModel2.addElement(addNow);
        }
        listSongs = new JList(listModel2);
        listSongs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listSongs.setLayoutOrientation(JList.VERTICAL);
        listSongs.setVisibleRowCount(-1);

        listScroller2 = new JScrollPane(listSongs);
        listScroller2.setMaximumSize(new Dimension(250, 300));
        listSongs.setEnabled(false);
        //</editor-fold>

        //<editor-fold desc="Add/Delete">
        playlistsT = new JLabel("Playlists");
        playlistsT.setBounds(30, 150, 100, 35);

        addPlaylistIcon = new ImageIcon("src/add-delete/addIcon.jpg");
        addPlaylistButton = new JButton(addPlaylistIcon);
        addPlaylistButton.addActionListener(this::createPlaylist);

        editPlaylistButton = new JButton("Edit Playlist");
        editPlaylistButton.addActionListener(this::editPlaylist);

        deletePlaylistIcon = new ImageIcon("src/add-delete/deleteIcon.jpg");
        deletePlaylistButton = new JButton(deletePlaylistIcon);
        deletePlaylistButton.addActionListener(this::deletePlaylist);

        playlistNameField = new JTextField();
        playlistNameField.setMaximumSize(new Dimension(200, 30));
        playlistNameField.setEnabled(false);

        confirmButton = new JButton("Confirm");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(this::confirmActions);
        //</editor-fold>

        //<editor-fold desc="LAYOUT">
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(playlistsT)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(listScroller)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(addPlaylistButton)
                                        .addComponent(editPlaylistButton)
                                        .addComponent(deletePlaylistButton)
                                )
                                .addComponent(playlistNameField)
                                .addComponent(confirmButton)
                                .addComponent(listScroller2)
                        )

        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(playlistsT)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(listScroller)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(addPlaylistButton)
                                        .addComponent(editPlaylistButton)
                                        .addComponent(deletePlaylistButton)
                                )
                                .addComponent(playlistNameField)
                                .addComponent(confirmButton)
                                .addComponent(listScroller2)
                        )
        );
        //</editor-fold>

        setVisible(true);
    }

    public void createPlaylist(ActionEvent e)
    {
        playlistNameField.setEnabled(true);
        listSongs.setEnabled(true);
        createOrEdit = true;
        confirmButton.setEnabled(true);
    }

    public void editPlaylist(ActionEvent e)
    {
        createOrEdit = false;
        listSongs.setEnabled(true);
        confirmButton.setEnabled(true);
        // display the selected songs
        // reading the txt file
        String namePlaylist = String.valueOf(listPlaylists.getSelectedValue());
        String playlistPath = "src\\" + namePlaylist + ".txt";
        try
        {
            playlistFileContent = new ArrayList<>(Files.readAllLines(Path.of(playlistPath), StandardCharsets.UTF_8));
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        ArrayList<Integer> indicesSongs = new ArrayList<>();
        for (String str : playlistFileContent)
            indicesSongs.add(listModel2.indexOf(str));

        int[] indicesSongsArr = new int[indicesSongs.size()];
        for (int i = 0; i < indicesSongsArr.length; i++)
            indicesSongsArr[i] = indicesSongs.get(i);

        listSongs.setSelectedIndices(indicesSongsArr);
    }

    public void confirmActions(ActionEvent e)
    {
        playlistNameField.setEnabled(false);
        listSongs.setEnabled(false);
        String playlistPartPath;
        ArrayList<String> listOfSelectedSongs;

        if (createOrEdit)
        {
            playlistPartPath = playlistNameField.getText();
            playlistFile = new File("src\\" + playlistPartPath + ".txt");
            if (!playlistFile.exists())
            {
                try
                {
                    playlistFile.createNewFile();
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }

            if (!listSongs.isSelectionEmpty())
            {
                // get names of all selected songs and plop them in the txt file
                listOfSelectedSongs = new ArrayList<>(listSongs.getSelectedValuesList());
                try (FileWriter fw = new FileWriter(playlistFile); BufferedWriter bw = new BufferedWriter(fw))
                {
                    for (String str : listOfSelectedSongs)
                    {
                        bw.write(str);
                        bw.newLine();
                    }
                } catch (IOException exc)
                {
                    exc.printStackTrace();
                    System.out.println("Got exception: " + exc);
                    System.exit(1);
                }

                // add playlist name to the Playlist txt file
                try
                {
                    ArrayList<String> fileContent = new ArrayList<>(Files.readAllLines(
                            Path.of(String.valueOf(playlistsFile)), StandardCharsets.UTF_8));
                    fileContent.add(playlistPartPath);
                    Files.write(Path.of(String.valueOf(playlistsFile)), fileContent, StandardCharsets.UTF_8);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                listModel.addElement(playlistPartPath);
            }
        }

        else
        {
            playlistPartPath = String.valueOf(listPlaylists.getSelectedValue());

            StringBuffer sb = new StringBuffer("src\\.txt");
            sb.insert(4, playlistPartPath);
            playlistPath = String.valueOf(sb);
            listOfSelectedSongs = new ArrayList<>(listSongs.getSelectedValuesList());
            editingPlaylistFile(listOfSelectedSongs);
        }

        confirmButton.setEnabled(false);
    }

    public void editingPlaylistFile(ArrayList<String> listOfSongs)
    {
        try
        {
            Files.write(Path.of(playlistPath), listOfSongs, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void deletePlaylist(ActionEvent e)
    {
        // deleting the playlist from the text file
        try
        {
            playlistFileContent = new ArrayList<>(Files.readAllLines(Path.of("src\\Playlists.txt"), StandardCharsets.UTF_8));
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        playlistFileContent.remove(listPlaylists.getSelectedValue());
        try
        {
            Files.write(Path.of("src\\Playlists.txt"), playlistFileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
        // deleting the playlist from the list
        try
        {
            Files.deleteIfExists(Path.of("src/" + listPlaylists.getSelectedValue() + ".txt"));
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        listModel.removeElement(listPlaylists.getSelectedValue());
    }

    public void readingTxtFile()
    {
        allPlaylists.clear();
        try
        {
            // reading the txt file
            Scanner scanner = new Scanner(playlistsFile);
            ArrayList<String> playlistsArr = new ArrayList<>();

            while (scanner.hasNextLine())
            {
                playlistsArr.add(scanner.nextLine());
            }
            for (String s : playlistsArr)
            {
                ArrayList<String> toAdd = new ArrayList<>();
                String[] sArray = s.split(" ");
                for (String str : sArray)
                {
                    toAdd.add(str);
                }
                allPlaylists.add(toAdd);
            }
        } catch (FileNotFoundException c)
        {
            c.printStackTrace();
        }
    }

    //<editor-fold desc="Menu bar actions">
    public void actionPerformed2(ActionEvent e)
    {
        JFrame AddDeleteWindow = null; // open another JFrame
        try
        {
            AddDeleteWindow = new AddDeleteWindow();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        AddDeleteWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed3(ActionEvent e)
    {
        JFrame SongsWindow = new SongsWindow(); // open another JFrame
        SongsWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed4(ActionEvent e)
    {
        JFrame HomePageWindow = null; // open another JFrame
        try
        {
            HomePageWindow = new HomePageWindow();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        HomePageWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    /*
    public void actionPerformed5(ActionEvent e)
    {
        JFrame PhotosWindow = new PhotosWindow(); // open another JFrame
        PhotosWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

     */
    //</editor-fold>
}