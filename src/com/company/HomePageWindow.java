package com.company;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.util.Calendar;

public class HomePageWindow extends JFrame implements ActionListener
{
    private JMenuBar menuBar;
    private JPanel p;
    private JPanel pCenter;
    private JButton toSelectPlayWindow, toAddDeleteWindow, toPhotosWindow, toPlaylistsWindow,
            toQueuePreviewWindow, toSongsWindow, toSelectPlaylistWindow, addPhotosButton;
    private JButton sadFaceButton, chillFaceButton, happyFaceButton, veryHappyButton;
    private JLabel searchT, selectYourMoodT;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuPlaylists, menuSongs, menuQueue;
    private ImageIcon sadIcon, chillIcon, happyIcon, veryHappyIcon;
    private JButton filterButton, loopButton, nextSongButton, pauseButton, playButton, previousSongButton,
            randomButton, tenBackButton, tenForwardButton, searchButton, stopButton;
    // Do I need a search button? Or will the search happen when the user presses enter
    private ImageIcon filterIcon, loopIcon, nextSongIcon, pauseIcon, playIcon, previousSongIcon,
            randomIcon, tenBackIcon, tenForwardIcon, searchIcon;
    private JTextField searchBox;
    private JProgressBar pB;
    private SongStuff songObject = new SongStuff();
    boolean isPlaying;
    private SearchWindow searchObject;
    ArrayList<ArrayList> allTags, allSongsAndTags;
    private AddDeleteWindow addDeleteObject;
    private boolean allMatch;
    protected ArrayList<String> allSearchResults = new ArrayList<>(), fileContent, fileContent3;
    JFrame SearchResultsWindow;
    protected File searchResultsFile, queueFile, queueSongsFile;
    PlaylistMethods playlistsObject;
    HomePageMethods homePageObject;

    protected JScrollPane listScroller;
    protected JList listPlaylists;
    protected DefaultListModel listModel;
    PlaylistsWindow playlistsObjectWindow;

    protected ArrayList<String> playlistFileContent, fileContent2, songsPaths = new ArrayList<>();
    Calendar cal;
    int currentMonth;

    boolean display = true;
    PhotosWindow photosObject;
    JFrame PhotosWindow;

    // nameOfPlaylistT, nameOfSongT -> these two change according to what is playingggg

    public HomePageWindow() throws IOException
    {
        super("Audio Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //<editor-fold desc="Menu Bar">
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Home Page");

        menuSelect = new JMenuItem("Select What to Play");
        menuSelect.addActionListener(e ->
        {
            try
            {
                actionPerformedSelectSongPlay(e);
            } catch (UnsupportedAudioFileException ex)
            {
                ex.printStackTrace();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            } catch (LineUnavailableException ex)
            {
                ex.printStackTrace();
            }
        });

        menuAddDelete = new JMenuItem("Add / Delete");
        menuAddDelete.addActionListener(this::actionPerformed2);

        menuPhotos = new JMenuItem("Photos");
        menuPhotos.addActionListener(this::displayPhotos);

        menuPlaylists = new JMenuItem("Playlists");
        menuPlaylists.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        menuQueue = new JMenuItem("Queue");
        menuQueue.addActionListener(this::actionPerformed6);

        fileMenu.add(menuAddDelete);
        fileMenu.add(menuPhotos);
        fileMenu.add(menuQueue);
        fileMenu.add(menuSongs);
        fileMenu.add(menuSelect);
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        // adding a panel so I can add buttons here, buttons weren't moving without this :/
        // i think the layout is too small and not positioned correctly
        // i need to move it so my buttons show up

        cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH) + 1;
        System.out.println(currentMonth);

        //<editor-fold desc="Main Stuff">
        // is it possible to make the panel full screen?
        pCenter = new JPanel();
        pCenter.setLayout(null);
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        // get the positions right
        // this is not actually to the window, it just opens the file chooser
        toSelectPlayWindow = new JButton("Select a Song or Playlist");
        toSelectPlayWindow.setBounds(550, 50, 225, 35);
        toSelectPlayWindow.addActionListener(e ->
        {
            try
            {
                actionPerformedSelectSongPlay(e);
            } catch (UnsupportedAudioFileException ex)
            {
                ex.printStackTrace();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            } catch (LineUnavailableException ex)
            {
                ex.printStackTrace();
            }
        });
        pCenter.add(toSelectPlayWindow);

        toSelectPlaylistWindow = new JButton("Select Playlist");
        toSelectPlaylistWindow.setBounds(700, 50, 205, 35);
        toSelectPlaylistWindow.addActionListener(this::selectPlaylist);
        pCenter.add(toSelectPlaylistWindow);

        //<editor-fold desc="Setting up playlists list">
        try
        {
            playlistsObjectWindow = new PlaylistsWindow();
            playlistsObjectWindow.dispose();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        listModel = new DefaultListModel();
        playlistsObjectWindow.readingTxtFile();
        listModel.addElement("Queue");
        for (ArrayList<String> arr : playlistsObjectWindow.allPlaylists)
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
        listScroller.setBounds(850, 50, 200, 35);
        pCenter.add(listScroller);
        //</editor-fold>

        toAddDeleteWindow = new JButton("Add / Delete");
        toAddDeleteWindow.setBounds(30, 90, 150, 35);
        toAddDeleteWindow.addActionListener(this::actionPerformed2);
        pCenter.add(toAddDeleteWindow);

        toSongsWindow = new JButton("Songs");
        toSongsWindow.setBounds(30, 400, 100, 35);
        toSongsWindow.addActionListener(this::actionPerformed3);
        pCenter.add(toSongsWindow);

        toPlaylistsWindow = new JButton("Playlists");
        toPlaylistsWindow.setBounds(30, 500, 100, 35);
        toPlaylistsWindow.addActionListener(this::actionPerformed4);
        pCenter.add(toPlaylistsWindow);

        toQueuePreviewWindow = new JButton("Queue");
        toQueuePreviewWindow.setBounds(1100, 150, 100, 35);
        toQueuePreviewWindow.addActionListener(this::updateQueue);
        pCenter.add(toQueuePreviewWindow);

        toPhotosWindow = new JButton("Photos");
        toPhotosWindow.setBounds(1100, 90, 100, 35);
        toPhotosWindow.addActionListener(this::displayPhotos);
        pCenter.add(toPhotosWindow);
        //</editor-fold>

        addPhotosButton = new JButton("Add Photo");
        addPhotosButton.setBounds(1100, 50, 100, 35);
        addPhotosButton.addActionListener(this::addingPhotos);
        pCenter.add(addPhotosButton);

        //<editor-fold desc="Search Stuff">
        searchT = new JLabel("Search");
        searchT.setBounds(30, 150, 100, 35);
        pCenter.add(searchT);

        filterIcon = new ImageIcon("src/middleSectionHP/filterIcon.png");
        filterButton = new JButton(filterIcon);
        filterButton.setBounds(110, 200, 50, 30);
        filterButton.addActionListener(this::actionPerformed7);
        pCenter.add(filterButton);

        searchBox = new JTextField();
        searchBox.setBounds(50, 200, 100, 30);
        pCenter.add(searchBox);

        searchIcon = new ImageIcon("src/middleSectionHP/searchIcon.png");
        searchButton = new JButton(searchIcon);
        searchButton.setBounds(30, 200, 30, 30);
        searchButton.addActionListener(this::search);
        pCenter.add(searchButton);
        //</editor-fold>

        //<editor-fold desc="Mood Stuff">
        selectYourMoodT = new JLabel("Select Your Mood");
        selectYourMoodT.setBounds(950, 400, 150, 35);
        pCenter.add(selectYourMoodT);

        sadIcon = new ImageIcon("src/moodIcons/sadIcon.png");
        sadFaceButton = new JButton(sadIcon);
        sadFaceButton.addActionListener(this::addSadFace);
        sadFaceButton.setBounds(800, 520, 100, 90);
        pCenter.add(sadFaceButton);

        chillIcon = new ImageIcon("src/moodIcons/chillIcon.png");
        chillFaceButton = new JButton(chillIcon);
        chillFaceButton.addActionListener(this::addRelaxedFace);
        chillFaceButton.setBounds(1100, 520, 100, 90);
        pCenter.add(chillFaceButton);

        happyIcon = new ImageIcon("src/moodIcons/happyIcon.png");
        happyFaceButton = new JButton(happyIcon);
        happyFaceButton.addActionListener(this::addHappyFace);
        happyFaceButton.setBounds(900, 520, 100, 90);
        pCenter.add(happyFaceButton);

        veryHappyIcon = new ImageIcon("src/moodIcons/veryHappyIcon.png");
        veryHappyButton = new JButton(veryHappyIcon);
        veryHappyButton.addActionListener(this::addEnergeticFace);
        veryHappyButton.setBounds(1000, 520, 100, 90);
        pCenter.add(veryHappyButton);
        //</editor-fold>

        // middle section -> pause play stuff
        // create a play button -> change to this when pause is clicked, and vice versa
        // adjust the coordinates cause this looks weird

        //<editor-fold desc="Middle Stuff">
        pauseIcon = new ImageIcon("src/middleSectionHP/pauseIcon.jpg");
        pauseButton = new JButton(pauseIcon);
        pauseButton.setBounds(600, 300, 90, 70);
        pauseButton.addActionListener(this::actionPerformedPauseSong);
        pCenter.add(pauseButton);

        // i have to somehow leave this hidden until the pause button is clicked
        playIcon = new ImageIcon("src/middleSectionHP/playIcon.jpg");
        playButton = new JButton(playIcon);
        playButton.setBounds(700, 300, 90, 70);

        stopButton = new JButton("Stop");
        stopButton.setBounds(730, 300, 75, 60);
        stopButton.addActionListener(this::stopMusic);
        pCenter.add(stopButton);

        previousSongIcon = new ImageIcon("src/middleSectionHP/previousSongIcon.jpg");
        previousSongButton = new JButton(previousSongIcon);
        previousSongButton.setBounds(400, 300, 75, 60);
        pCenter.add(previousSongButton);

        nextSongIcon = new ImageIcon("src/middleSectionHP/nextSongIcon.jpg");
        nextSongButton = new JButton(nextSongIcon);
        nextSongButton.setBounds(800, 300, 75, 60);
        pCenter.add(nextSongButton);

        loopIcon = new ImageIcon("src/middleSectionHP/loopIcon.jpg");
        loopButton = new JButton(loopIcon);
        loopButton.setBounds(460, 400, 80, 70);
        loopButton.addActionListener(this::actionPerformedLoopSong);
        pCenter.add(loopButton);

        randomIcon = new ImageIcon("src/middleSectionHP/randomIcon.jpg");
        randomButton = new JButton(randomIcon);
        randomButton.setBounds(560, 400, 80, 70);
        pCenter.add(randomButton);

        tenBackIcon = new ImageIcon("src/middleSectionHP/tenBackIcon.jpg");
        tenBackButton = new JButton(tenBackIcon);
        tenBackButton.setBounds(310, 400, 50, 30);
        tenBackButton.addActionListener(this::actionPerformedTenSecBack);
        pCenter.add(tenBackButton);

        tenForwardIcon = new ImageIcon("src/middleSectionHP/tenForwardIcon.jpg");
        tenForwardButton = new JButton(tenForwardIcon);
        tenForwardButton.setBounds(700, 400, 50, 30);
        tenForwardButton.addActionListener(this::actionPerformedTenSecForward);
        pCenter.add(tenForwardButton);

        // progress bar that moves with music
        pB = new JProgressBar();
        pB.setValue(0);
        pB.setStringPainted(true);
        pB.setBounds(400, 500, 300, 20);
        pCenter.add(pB);
        // isPlaying = songObject.isPlaying;
        // PROGRESS BAR STUFF IS NOT WORKING
        // use a timer!!!!!!!!!!!!!
        System.out.println("is Playing: " + isPlaying);
        while (isPlaying)
        {
            pB.setValue((int) (100 * songObject.clipTimePosition / songObject.clipLength));
            pB.setString((int) (songObject.clipTimePosition / (1e6 * 60)) + ":" +
                    (int) ((songObject.clipTimePosition / 1e6) % 60) + " / " +
                    (int) (songObject.clipLength / (1e6 * 60)) + ":" + (int) ((songObject.clipLength / 1e6) % 60));
        }
        //</editor-fold>

        searchObject = new SearchWindow();
        searchObject.dispose();

        try
        {
            addDeleteObject = new AddDeleteWindow();
            addDeleteObject.dispose();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
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

        setVisible(true);
    }

    public void displayPhotos(ActionEvent e)
    {
        if (display)
        {
            PhotosWindow = new PhotosWindow();
        }
        else
        {
            PhotosWindow.dispose();
        }
        display = !display;
    }

    //<editor-fold desc="Editing mood on queue txt file">
    public void addSadFace(ActionEvent e)
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, "sad ");
            Files.write(Path.of(String.valueOf(queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }

    public void addEnergeticFace(ActionEvent e)
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, "energetic ");
            Files.write(Path.of(String.valueOf(queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }

    public void addHappyFace(ActionEvent e)
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, "happy ");
            Files.write(Path.of(String.valueOf(queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }

    public void addRelaxedFace(ActionEvent e)
    {
        try
        {
            fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(queueFile)), StandardCharsets.UTF_8));
            fileContent.set(0, "relaxed ");
            Files.write(Path.of(String.valueOf(queueFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
    }
    //</editor-fold>

    public void updateQueue(ActionEvent e)
    {
        //<editor-fold desc="Themes tags added">
        // MAKE SURE I DELETE THE TEXT WHEN IT'S OUT OF SEASON
        // ALSO, CHECK IF THE TAG IS THERE ALREADY
        // clear the entire FILE CONTENT after the first element
        if (currentMonth > 9)
        {
            try
            {
                fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(queueFile)), StandardCharsets.UTF_8));
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
                Files.write(Path.of(String.valueOf(queueFile)), fileContent, StandardCharsets.UTF_8);
            } catch (IOException a)
            {
                a.printStackTrace();
            }
        }

        if (currentMonth < 3)
        {
            try
            {
                fileContent = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(queueFile)), StandardCharsets.UTF_8));
                for (int i = 0; i < fileContent.size(); i++)
                {
                    if (i > 0)
                    {
                        fileContent.remove(fileContent.get(1));
                    }
                }
                fileContent.add("Easter ");
                Files.write(Path.of(String.valueOf(queueFile)), fileContent, StandardCharsets.UTF_8);
            } catch (IOException a)
            {
                a.printStackTrace();
            }
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
            fileContent2 = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(addDeleteObject.songsAndTagsFile)), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent2.size(); i++)
            {
                String[] sArray = fileContent2.get(i).split(" ");
                if (Integer.valueOf(sArray[sArray.length - 1]) < min)
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

            fileContent3 = new ArrayList<>(Files.readAllLines(Path.of(String.valueOf(queueSongsFile)), StandardCharsets.UTF_8));
            int numReps = fileContent3.size();
            for (int i = 0; i < numReps; i++)
            {
                fileContent3.remove(fileContent3.get(0));
            }
            Files.write(Path.of(String.valueOf(queueSongsFile)), fileContent3, StandardCharsets.UTF_8);


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
            for (int i = 0; i < fileContent.size(); i++)
            {
                StringBuffer sb = new StringBuffer(fileContent.get(i));
                sb.delete(sb.length() - 1, sb.length());
                String actualMood = String.valueOf(sb);

                boolean hasTag = false;
                String[] sArray = fileContent2.get(i).split(" ");
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

            Files.write(Path.of(String.valueOf(queueSongsFile)), fileContent3, StandardCharsets.UTF_8);
        } catch (IOException a)
        {
            a.printStackTrace();
        }
        //</editor-fold>
    }

    public void search(ActionEvent e)
    {
        // first I want to be able to search just with tags, then I'll add the song name to it
        // I'm gonna add the songs to it now
        // first I read the search tags file
        allSearchResults.clear();

        allTags = new ArrayList<>();
        try
        {
            // reading the txt file
            Scanner scanner = new Scanner(searchObject.searchTagsFile);
            ArrayList<String> searchTags = new ArrayList<>();

            while (scanner.hasNextLine())
            {
                searchTags.add(scanner.nextLine());
            }
            for (String s : searchTags)
            {
                ArrayList<String> toAdd = new ArrayList<>();
                String[] sArray = s.split(" ");
                for (String str : sArray)
                {
                    toAdd.add(str);
                }
                allTags.add(toAdd);
            }
        } catch (FileNotFoundException c)
        {
            c.printStackTrace();
        }

        // adding searchBox text to the search tags array
        if (searchBox.getText().length() > 0)
            allTags.get(0).add(searchBox.getText());

        // now, I'll read the songs with tags file
        allSongsAndTags = new ArrayList<>();
        try
        {
            Scanner scanner = new Scanner(addDeleteObject.songsAndTagsFile);
            ArrayList<String> songsAndTags = new ArrayList<>();

            while (scanner.hasNextLine())
            {
                songsAndTags.add(scanner.nextLine());
            }
            for (String s : songsAndTags)
            {
                ArrayList<String> toAdd = new ArrayList<>();
                String[] sArray = s.split(" ");
                for (String str : sArray)
                {
                    toAdd.add(str);
                }
                allSongsAndTags.add(toAdd);
            }
        } catch (FileNotFoundException c)
        {
            c.printStackTrace();
        }

        // now I need to see which lines match - e.g. the tags file has "sad" and "morning"
        // i checks which lines of the songs file has those tags
        int numMatches = 0;
        allMatch = false;
        for (ArrayList<String> arrTags : allTags)
        {
            for (ArrayList<String> arrSongs : allSongsAndTags)
            {
                // I just realized this might not work when I have more lines on the songs file
                // fixing ittttt
                for (String tag : arrTags)
                {
                    if (arrSongs.contains(tag))
                        numMatches++;
                }
                if (numMatches == arrTags.size())
                    allMatch = true;
                // add the song name to an array, so I can display it (as a list?) in the SearchResultsWindow
                if (allMatch)
                {
                    System.out.println(arrSongs.get(0));
                    allSearchResults.add(arrSongs.get(0));
                }
            }

        }

        editingSearchTxtFile(allSearchResults);

        try
        {
            SearchResultsWindow = new SearchResultsWindow();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        SearchResultsWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
        // now I need to display all the songs that match the search -> use the SearchResultsWindow

    }

    public void editingSearchTxtFile(ArrayList<String> allSearchResults)
    {
        StringBuffer sb = new StringBuffer();
        for (String s : allSearchResults)
        {
            sb.append(s);
            sb.append(" ");
        }
        String sA = String.valueOf(sb);

        try
        {
            fileContent = new ArrayList<>();
            fileContent.add(sA);
            Files.write(Path.of(String.valueOf(searchResultsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void actionPerformedSelectSongPlay(ActionEvent e) throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        JFileChooser songUpload = new JFileChooser(new File("src/songsFiles"));
        int res2 = songUpload.showSaveDialog(null);

        if (res2 == JFileChooser.APPROVE_OPTION)
        {
            File song = new File(songUpload.getSelectedFile().getAbsolutePath());
            // ok, so I only want the last word from the path (so the name of the song)
            // and then I just add the two strings
            String songPath = song.getAbsolutePath();
            System.out.println(songPath);
            String actualPath = songPath.substring(songPath.lastIndexOf("songsFiles") + 11);
            System.out.println(actualPath);
            String realPath = "src/songsFiles/" + actualPath;
            System.out.println(realPath);
            songObject.playMusic(realPath);
            isPlaying = songObject.isPlaying;
        }
    }

    public void stopMusic(ActionEvent e)
    {
        songObject.stopPlaying();
    }

    public void actionPerformedPauseSong(ActionEvent e)
    {
        // I should combine it into one button, the pause and unpause methods
        songObject.pauseUnpauseMusic();
    }

    public void actionPerformedLoopSong(ActionEvent e)
    {
        songObject.loopMusic();
    }

    public void actionPerformedTenSecForward(ActionEvent e)
    {
        songObject.tenSecForward();
    }

    public void actionPerformedTenSecBack(ActionEvent e)
    {
        songObject.tenSecBack();
    }

    public void selectPlaylist(ActionEvent e) // FIX THIS
    {
        if (listPlaylists.getSelectedValue() != null)
        {
            String namePlaylist = String.valueOf(listPlaylists.getSelectedValue());
            String playlistPath;
            if (namePlaylist.equals("Queue"))
            {
                playlistPath = "src\\QueueSongs.txt";
            }
            else
            {
                playlistPath = "src\\" + namePlaylist + ".txt";
            }
            songsPaths.clear();
            try
            {
                playlistFileContent = new ArrayList<>(Files.readAllLines(Path.of(playlistPath), StandardCharsets.UTF_8));
                for (String songName : playlistFileContent)
                {
                    songsPaths.add("src\\songsFiles\\" + songName + ".wav");
                }
                System.out.println(songsPaths);
                songObject.playPlaylist(songsPaths);

                // THE BUTTONS ARE NOT WORKING
                // THE PROGRAM IS BASICALLY PAUSE HERE WHILE THE PLAYLIST IS PLAYING
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void addingPhotos(ActionEvent e)
    {
        JFileChooser imageUpload = new JFileChooser();
        int res2 = imageUpload.showSaveDialog(null);

        // copying the file
        if (res2 == JFileChooser.APPROVE_OPTION)
        {
            File imagePath = new File(imageUpload.getSelectedFile().getAbsolutePath());

            Path sourcePath = Path.of(imageUpload.getSelectedFile().getAbsolutePath());
            StringBuffer sb = new StringBuffer(imageUpload.getSelectedFile().getAbsolutePath());
            System.out.println(sb);
            sb.delete(0, sb.length() - 7);
            String targetPath = "src/Photos/" + sb;

            try
            {
                Files.copy(sourcePath, Path.of(targetPath), new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    // i found this here: https://stackoverflow.com/questions/9569700/java-call-method-via-jbutton

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
        SongsWindow.setVisible(true); // display
        dispose(); // close home page
    }

    public void actionPerformed4(ActionEvent e)
    {
        JFrame PlaylistsWindow = null; // open another JFrame
        try
        {
            PlaylistsWindow = new PlaylistsWindow();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        PlaylistsWindow.setVisible(true); // display
        dispose(); // close home page
    }

    public void actionPerformed6(ActionEvent e)
    {
        JFrame QueuePreviewWindow = new QueuePreviewWindow(); // open another JFrame
        QueuePreviewWindow.setVisible(true); // display
        dispose(); // close home page
    }

    public void actionPerformed7(ActionEvent e)
    {
        JFrame SearchWindow = new SearchWindow(); // open another JFrame
        SearchWindow.setVisible(true); // display
        dispose(); // close home page
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }
    //</editor-fold>
}