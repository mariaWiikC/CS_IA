package com.company;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.util.*;
// create classes for the objects and stuff
// one for each of the following: song, (playlist), (photo), (queue)

// what if I never dispose the home page? then the user will never see the ugly thing
// then to close the program they have to close the home page -> like I'm doing with the PhotosFrame!!!
// now I have to make the new frame be on top of the homePage
public class HomePageWindow extends JFrame implements ActionListener
{
    private JMenuBar menuBar;
    private JPanel pCenter;
    private JButton toSelectPlayWindow, toAddDeleteWindow, toPhotosWindow, toPlaylistsWindow,
            toQueuePreviewWindow, toSongsWindow, toSelectPlaylistWindow, addPhotosButton;
    private JButton sadFaceButton, chillFaceButton, happyFaceButton, veryHappyButton;
    private JLabel searchT, selectYourMoodT;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuPlaylists, menuSongs;
    private ImageIcon sadIcon, chillIcon, happyIcon, veryHappyIcon;
    private JButton filterButton, loopButton, nextSongButton, pauseButton, previousSongButton,
            randomButton, tenBackButton, tenForwardButton, searchButton, stopButton;
    private ImageIcon filterIcon, loopIcon, nextSongIcon, pauseIcon, playIcon, previousSongIcon,
            randomIcon, tenBackIcon, tenForwardIcon, searchIcon, loopingIcon, stopIcon;
    private JTextField searchBox;
    private AudioControl songObject = new AudioControl();
    private SearchWindow searchObject;
    ArrayList<ArrayList> allTags, allSongsAndTags;
    protected ArrayList<String> allSearchResults = new ArrayList<>(), fileContent;
    JFrame SearchResultsWindow;

    protected JScrollPane listScroller;
    protected JList listPlaylists;
    protected DefaultListModel listModel;
    PlaylistsWindow playlistsObjectWindow;

    protected ArrayList<String> playlistFileContent, songsPaths = new ArrayList<>();

    boolean playingPlaylist, random = false;
    ArrayList<Integer> alreadyPlayed = new ArrayList<>();
    HomePageMethods homePageMethodsObject = new HomePageMethods();

    boolean isPaused = false, isLooping = false, isPlaying = false;

    Photo photoObject = new Photo();
    Queue queueObject = new Queue();
    AddingDeleting addingDeletingObject;

    public HomePageWindow() throws IOException
    {
        super("Audio Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        timer.start();

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
        menuPhotos.addActionListener((ActionEvent e) -> photoObject.displayPhotos());
        menuPlaylists = new JMenuItem("Playlists");
        menuPlaylists.addActionListener(this::actionPerformed4);

        menuSongs = new JMenuItem("Songs");
        menuSongs.addActionListener(this::actionPerformed3);

        fileMenu.add(menuAddDelete);
        fileMenu.add(menuPhotos);
        fileMenu.add(menuSongs);
        fileMenu.add(menuSelect);
        fileMenu.add(menuPlaylists);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //</editor-fold>

        // adding a panel so I can add buttons here, buttons weren't moving without this :/
        // i think the layout is too small and not positioned correctly
        // i need to move it so my buttons show up

        try
        {
            addingDeletingObject = new AddingDeleting();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //<editor-fold desc="Main Section">
        pCenter = new JPanel();
        pCenter.setLayout(null);
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        toSelectPlayWindow = new JButton("Select a Song");
        toSelectPlayWindow.setBounds(300, 50, 225, 35);
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
        toSelectPlaylistWindow.setBounds(565, 50, 205, 35);
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

        Playlist playlistMethodsObject = new Playlist();

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
        listScroller.setBounds(800, 50, 200, 35);
        pCenter.add(listScroller);
        //</editor-fold>

        toAddDeleteWindow = new JButton("Add / Delete");
        toAddDeleteWindow.setBounds(50, 90, 150, 35);
        toAddDeleteWindow.addActionListener(this::actionPerformed2);
        pCenter.add(toAddDeleteWindow);

        toSongsWindow = new JButton("Songs");
        toSongsWindow.setBounds(50, 400, 100, 35);
        toSongsWindow.addActionListener(this::actionPerformed3);
        pCenter.add(toSongsWindow);

        toPlaylistsWindow = new JButton("Playlists");
        toPlaylistsWindow.setBounds(50, 500, 100, 35);
        toPlaylistsWindow.addActionListener(this::actionPerformed4);
        pCenter.add(toPlaylistsWindow);

        toQueuePreviewWindow = new JButton("Queue");
        toQueuePreviewWindow.setBounds(1100, 300, 100, 35);
        toQueuePreviewWindow.addActionListener((ActionEvent e) -> queueObject.updateQueue());
        pCenter.add(toQueuePreviewWindow);

        toPhotosWindow = new JButton("Photos");
        toPhotosWindow.setBounds(1100, 150, 100, 35);
        toPhotosWindow.addActionListener((ActionEvent e) -> photoObject.displayPhotos());
        pCenter.add(toPhotosWindow);
        //</editor-fold>

        addPhotosButton = new JButton("Add Photo");
        addPhotosButton.setBounds(1100, 100, 100, 35);
        addPhotosButton.addActionListener((ActionEvent e) -> photoObject.addingPhotos());
        pCenter.add(addPhotosButton);

        //<editor-fold desc="Search Section">
        searchT = new JLabel("Search");
        searchT.setBounds(50, 150, 100, 35);
        pCenter.add(searchT);

        filterIcon = new ImageIcon("src/middleSectionHP/filterIcon.png");
        filterButton = new JButton(filterIcon);
        filterButton.setBounds(250, 200, 50, 30);
        filterButton.addActionListener(this::actionPerformed7);
        pCenter.add(filterButton);

        searchBox = new JTextField();
        searchBox.setBounds(100, 200, 100, 30);
        pCenter.add(searchBox);

        searchIcon = new ImageIcon("src/middleSectionHP/searchIcon.png");
        searchButton = new JButton(searchIcon);
        searchButton.setBounds(50, 200, 30, 30);
        searchButton.addActionListener(this::search);
        pCenter.add(searchButton);
        //</editor-fold>

        //<editor-fold desc="Mood Section">
        selectYourMoodT = new JLabel("Select Your Mood");
        selectYourMoodT.setBounds(950, 400, 150, 35);
        pCenter.add(selectYourMoodT);

        sadIcon = new ImageIcon("src/moodIcons/sadIcon.png");
        sadFaceButton = new JButton(sadIcon);
        sadFaceButton.addActionListener((ActionEvent e) -> queueObject.addFace("sad"));
        sadFaceButton.setBounds(800, 450, 100, 90);
        pCenter.add(sadFaceButton);

        chillIcon = new ImageIcon("src/moodIcons/chillIcon.png");
        chillFaceButton = new JButton(chillIcon);
        chillFaceButton.addActionListener((ActionEvent e) -> queueObject.addFace("relaxed"));
        chillFaceButton.setBounds(1100, 450, 100, 90);
        pCenter.add(chillFaceButton);

        happyIcon = new ImageIcon("src/moodIcons/happyIcon.png");
        happyFaceButton = new JButton(happyIcon);
        happyFaceButton.addActionListener((ActionEvent e) -> queueObject.addFace("happy"));
        happyFaceButton.setBounds(900, 450, 100, 90);
        pCenter.add(happyFaceButton);

        veryHappyIcon = new ImageIcon("src/moodIcons/veryHappyIcon.png");
        veryHappyButton = new JButton(veryHappyIcon);
        veryHappyButton.addActionListener((ActionEvent e) -> queueObject.addFace("energetic"));
        veryHappyButton.setBounds(1000, 450, 100, 90);
        pCenter.add(veryHappyButton);
        //</editor-fold>

        //<editor-fold desc="Middle Section">
        pauseIcon = new ImageIcon("src/middleSectionHP/pauseIcon.jpg");
        playIcon = new ImageIcon("src/middleSectionHP/playIcon.jpg");
        pauseButton = new JButton(pauseIcon);
        pauseButton.setBounds(550, 200, 90, 70);
        pauseButton.addActionListener(this::actionPerformedPauseSong);
        pCenter.add(pauseButton);

        stopIcon = new ImageIcon("src/middleSectionHP/stopIcon.jpg");
        stopButton = new JButton(stopIcon);
        stopButton.setBounds(680, 200, 75, 60);
        stopButton.addActionListener(this::stopMusic);
        pCenter.add(stopButton);

        previousSongIcon = new ImageIcon("src/middleSectionHP/previousSongIcon.jpg");
        previousSongButton = new JButton(previousSongIcon);
        previousSongButton.addActionListener(this::goPreviousSong);
        previousSongButton.setBounds(400, 200, 75, 60);
        pCenter.add(previousSongButton);

        nextSongIcon = new ImageIcon("src/middleSectionHP/nextSongIcon.jpg");
        nextSongButton = new JButton(nextSongIcon);
        nextSongButton.addActionListener(this::goNextSong);
        nextSongButton.setBounds(800, 200, 75, 60);
        pCenter.add(nextSongButton);

        loopIcon = new ImageIcon("src/middleSectionHP/loopIcon.jpg");
        loopingIcon = new ImageIcon("src/middleSectionHP/loopingIcon.jpg");
        loopButton = new JButton(loopIcon);
        loopButton.setBounds(550, 300, 80, 70);
        loopButton.addActionListener(this::actionPerformedLoopSong);
        pCenter.add(loopButton);

        randomIcon = new ImageIcon("src/middleSectionHP/randomIcon.jpg");
        randomButton = new JButton(randomIcon);
        randomButton.addActionListener(this::randomizePlaylist);
        randomButton.setBounds(650, 300, 80, 70);
        pCenter.add(randomButton);

        tenBackIcon = new ImageIcon("src/middleSectionHP/tenBackIcon.jpg");
        tenBackButton = new JButton(tenBackIcon);
        tenBackButton.setBounds(410, 300, 50, 30);
        tenBackButton.addActionListener((ActionEvent e) -> songObject.tenSecBack());
        pCenter.add(tenBackButton);

        tenForwardIcon = new ImageIcon("src/middleSectionHP/tenForwardIcon.jpg");
        tenForwardButton = new JButton(tenForwardIcon);
        tenForwardButton.setBounds(800, 300, 50, 30);
        tenForwardButton.addActionListener((ActionEvent e) -> songObject.tenSecForward());
        pCenter.add(tenForwardButton);
        //</editor-fold>

        // progress bar
        songObject.pB.setBounds(480, 400, 300, 20);
        songObject.timeSongNow.setBounds(430, 400, 40, 20);
        songObject.totalTimeSong.setBounds(800, 400, 40, 20);
        pCenter.add(songObject.timeSongNow);
        pCenter.add(songObject.totalTimeSong);
        pCenter.add(songObject.pB);

        searchObject = new SearchWindow();
        searchObject.dispose();

        setVisible(true);
    }


    public Timer timer = new Timer(5000, new ActionListener() // updating every second
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            updatePlaylists();
        }
    });


    public void updatePlaylists()
    {
        listModel.clear();
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
    }

    // SEARCH RESULTS ARE WRONG!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void search(ActionEvent e)
    { // clear previous search results to begin search
        allSearchResults.clear();
        allTags = new ArrayList<>();
        try
        { // reading the txt file
            Scanner scanner = new Scanner(searchObject.searchTagsFile);
            ArrayList<String> searchTags = new ArrayList<>();

            while (scanner.hasNextLine())
                searchTags.add(scanner.nextLine());

            // adding all tags from the searchTags file to the allTags list to then compare with the songs
            for (String s : searchTags)
            {
                ArrayList<String> toAdd = new ArrayList<>();
                String[] sArray = s.split(" ");
                for (String tag : sArray)
                    toAdd.add(tag);
                allTags.add(toAdd);
            }
        } catch (FileNotFoundException c) {c.printStackTrace();}

        ArrayList<Character> charsTxtFieldList = new ArrayList<>();
        // adding searchBox text to the search tags array
        if (searchBox.getText().length() > 0)
        { // dividing the textField content into chars
            char[] charsTextField = searchBox.getText().toCharArray();
            for (char c : charsTextField)
            { // adding the chars as conditions for search results
                System.out.println(c);
                charsTxtFieldList.add(c);
                allTags.get(0).add(c);
            }
        }
        // now, read the songs with tags file
        allSongsAndTags = new ArrayList<>();
        try
        {
            Scanner scanner = new Scanner(addingDeletingObject.songsAndTagsFile);
            ArrayList<String> songsAndTags = new ArrayList<>();

            while (scanner.hasNextLine())
                songsAndTags.add(scanner.nextLine());

            for (String s : songsAndTags)
            {
                ArrayList<String> toAdd = new ArrayList<>();
                String[] sArray = s.split(" ");
                for (String contents : sArray)
                {
                    toAdd.add(contents);
                }
                allSongsAndTags.add(toAdd);
            }
        } catch (FileNotFoundException c) {c.printStackTrace();}

        // compare text input with song name
        for (ArrayList<String> arrTags : allTags)
        {
            for (ArrayList<String> arrSong : allSongsAndTags)
            {
                boolean theSame = true;
                // transform the name of the song into chars to compare it with the text field input
                char[] songInChars = arrSong.get(0).toCharArray();
                for (int i = 0; i < charsTxtFieldList.size(); i++)
                {
                    // if the char does not match, the song does not match the text field's input
                    if (Character.compare(songInChars[i], charsTxtFieldList.get(i)) != 0)
                    {
                        theSame = false;
                        break;
                    }
                }
                if (charsTxtFieldList.size() == 0)
                    theSame = false;
                // if the texts match, add the song to the search results
                if (theSame)
                    allSearchResults.add(String.valueOf(arrSong.get(0)));
            }

            // delete the text field input from the search conditions as it has been dealt with
            for(int i = 0; i < charsTxtFieldList.size(); i++)
                arrTags.remove(arrTags.size()-1);

            // check which lines match the search conditions
            // goal: display all songs with at least one of the filter tags
            for (ArrayList<String> arrSong : allSongsAndTags)
            {
                for (String tag : arrTags)
                {
                    if (arrSong.contains(tag))
                    {
                        // add the song name to array, so it can be displayed in the SearchResultsWindow
                        if (!allSearchResults.contains(arrSong.get(0)))
                            allSearchResults.add(arrSong.get(0));
                        break;
                    }
                }
            }
        }
        editingSearchTxtFile(allSearchResults);

        // create the search results window to display the search results
        try
        {
            SearchResultsWindow = new SearchResultsWindow();
        } catch (IOException ex) {ex.printStackTrace();}
        SearchResultsWindow.setVisible(true); // display SelectPlayWindow
        // now, display all the songs that match the search -> SearchResultsWindow
    }

    public void editingSearchTxtFile(ArrayList<String> allSearchResults)
    {
        StringBuffer stringFile = new StringBuffer();
        // adding search results to a single string
        for (String s : allSearchResults)
        {
            stringFile.append(s);
            stringFile.append(" ");
        }
        String sA = String.valueOf(stringFile);

        try
        {
            // adding search results to the search results file
            fileContent = new ArrayList<>();
            fileContent.add(sA);
            Files.write(Path.of(String.valueOf(homePageMethodsObject.searchResultsFile)), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void actionPerformedSelectSongPlay(ActionEvent e) throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        // if no song is being played, open file chooser so user can choose a song
        if (!isPlaying)
        {
            // open the folder with all the songs in the program
            JFileChooser songUpload = new JFileChooser(new File("src/songsFiles"));
            int res2 = songUpload.showSaveDialog(null);

            if (res2 == JFileChooser.APPROVE_OPTION)
            {
                File song = new File(songUpload.getSelectedFile().getAbsolutePath());
                // only the last word from the path (so the name of the song) is needed
                String songPath = song.getAbsolutePath();
                // record just the name of the song, what comes after songsFiles\ in the path
                String actualPath = songPath.substring(songPath.lastIndexOf("songsFiles") + 11);
                // the path below is the one used as input to play the song
                String realPath = "src/songsFiles/" + actualPath;
                songObject.playMusic(realPath);

                // buttons to default
                pauseButton.setIcon(pauseIcon);
                loopButton.setIcon(loopIcon);
                isPlaying = true;
            }
        }
        // Do not allow another song to be selected while one is playing
        else
        {
            JOptionPane.showMessageDialog(null, "Already playing song. " +
                    "Press stop button and select again.");
        }
    }

    public void stopMusic(ActionEvent e)
    {
        // stop the music and set buttons to default icons
        songObject.stopPlaying();
        playingPlaylist = false;
        pauseButton.setIcon(pauseIcon);
        loopButton.setIcon(loopIcon);
        isPlaying = false;
    }

    public void actionPerformedPauseSong(ActionEvent e)
    {
        if (!isPaused)
            pauseButton.setIcon(playIcon);
        else
            pauseButton.setIcon(pauseIcon);
        isPaused = !isPaused;
        songObject.pauseUnpauseMusic();
    }

    public void actionPerformedLoopSong(ActionEvent e)
    {
        if (!isLooping)
            loopButton.setIcon(loopingIcon);
        else
            loopButton.setIcon(loopIcon);
        isLooping = !isLooping;
        songObject.loopMusic();
    }

    public void randomizePlaylist(ActionEvent e)
    {
        random = !random;
    }

    public void selectPlaylist(ActionEvent e)
    {
        // only allow a playlist to be selected if nothing is playing
        if (!isPlaying)
        {
            if (listPlaylists.getSelectedValue() != null)
            {
                // new playlist has been chosen, no songs from it have been played (for the random feature)
                alreadyPlayed.clear();
                String namePlaylist = String.valueOf(listPlaylists.getSelectedValue());
                String playlistPath;
                // decide if the playlist is the queue or a generic playlist
                if (namePlaylist.equals("Queue"))
                    playlistPath = "src\\QueueSongs.txt";
                else
                    playlistPath = "src\\" + namePlaylist + ".txt";
                songsPaths.clear();
                try
                {
                    // read file to find songs included in the playlist
                    playlistFileContent = new ArrayList<>(Files.readAllLines(
                            Path.of(playlistPath), StandardCharsets.UTF_8));
                    for (String songName : playlistFileContent)
                        songsPaths.add("src\\songsFiles\\" + songName + ".wav");

                    pauseButton.setIcon(pauseIcon);
                    songObject.playMusic(songsPaths.get(0));
                    // add the first song to list of the already played ones
                    alreadyPlayed.add(0);
                    playingPlaylist = true;
                    isPlaying = true;
                } catch (IOException ex) {ex.printStackTrace();}
            }
        }
        else
            JOptionPane.showMessageDialog(null, "Already playing song. " + "Press stop button and select again.");
    }

    public void goPreviousSong(ActionEvent e)
    { // pause button to default
        pauseButton.setIcon(pauseIcon);

        if (playingPlaylist)
        {
            String songPlayingNow = "src\\songsFiles\\" + songObject.whichPlayingNow();
            // stop current song
            songObject.stopPlaying();
            // play previous song on the list
            // (does not matter if it is random or not, the previous does not change)
            if (songsPaths.indexOf(songPlayingNow) - 1 >= 0)
                songObject.playMusic(songsPaths.get(songsPaths.indexOf(songPlayingNow) - 1));
            else
                songObject.playMusic(songsPaths.get(songsPaths.size() - 1));
        }
    }

    public void goNextSong(ActionEvent e)
    {
        pauseButton.setIcon(pauseIcon);
        if (playingPlaylist)
        {
            // if the playlist is not set to random, proceed here
            if (!random)
            {
                // move sequentially in the list
                String songPlayingNow = "src\\songsFiles\\" + songObject.whichPlayingNow();
                songObject.stopPlaying();
                if (songsPaths.indexOf(songPlayingNow) + 1 < songsPaths.size())
                    songObject.playMusic(songsPaths.get(songsPaths.indexOf(songPlayingNow) + 1));
                else
                    songObject.playMusic(songsPaths.get(0));
            }
            // if it is random
            else
            {
                int rnd = 0;
                // if the list with the index of songs that have been played contains this index,
                // generate new random number
                while (alreadyPlayed.contains(rnd))
                    rnd = new Random().nextInt(songsPaths.size());

                songObject.stopPlaying();
                alreadyPlayed.add(rnd);
                songObject.playMusic(songsPaths.get(rnd));
            }
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
        // setVisible(false);
        AddDeleteWindow.setVisible(true); // display SelectPlayWindow
        // AddDeleteWindow.setAlwaysOnTop(true);
        // setVisible(true);
        dispose(); // close home page
    }

    public void actionPerformed3(ActionEvent e)
    {
        JFrame SongsWindow = new SongsWindow(); // open another JFrame
        SongsWindow.setVisible(true); // display
        SongsWindow.setAlwaysOnTop(true);
        // dispose(); // close home page
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
        PlaylistsWindow.setAlwaysOnTop(true);
        // dispose(); // close home page
    }

    public void actionPerformed7(ActionEvent e)
    {
        JFrame SearchWindow = new SearchWindow(); // open another JFrame
        SearchWindow.setVisible(true); // display
        SearchWindow.setAlwaysOnTop(true);
        // dispose(); // close home page
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }
    //</editor-fold>
}