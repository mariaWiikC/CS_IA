package com.company;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
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
    private ImageIcon filterIcon, loopIcon, nextSongIcon, pauseIcon, playIcon, previousSongIcon,
            randomIcon, tenBackIcon, tenForwardIcon, searchIcon, loopingIcon, stopIcon;
    private JTextField searchBox;
    private AudioControl songObject = new AudioControl();
    boolean isPlaying;
    private SearchWindow searchObject;
    ArrayList<ArrayList> allTags, allSongsAndTags;
    private AddDeleteWindow addDeleteObject;
    private boolean allMatch;
    protected ArrayList<String> allSearchResults = new ArrayList<>(), fileContent, fileContent3;
    JFrame SearchResultsWindow;
    Playlist playlistsObject;
    HomePageMethods homePageObject;

    protected JScrollPane listScroller;
    protected JList listPlaylists;
    protected DefaultListModel listModel;
    PlaylistsWindow playlistsObjectWindow;

    protected ArrayList<String> playlistFileContent, fileContent2, fileContent4, songsPaths = new ArrayList<>();
    Calendar cal;
    int currentMonth;

    boolean display = true;
    PhotosWindow photosObject;
    JFrame PhotosWindow;
    boolean playingPlaylist, random = false;
    String playlistNamePlaying;
    ArrayList<Integer> numberOfSongsInPlaylist = new ArrayList<>(), alreadyPlayed = new ArrayList<>();
    HomePageMethods homePageMethodsObject = new HomePageMethods();

    boolean isPaused = false, isLooping = false;
    // ProgressBarMethods progressBarObject = new ProgressBarMethods();
    // nameOfPlaylistT, nameOfSongT -> these two change according to what is playingggg

    JButton progressBarButton;

    Photo photoObject = new Photo();
    Queue queueObject = new Queue();
    AddingDeleting addingDeletingObject;

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

        cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH) + 1;
        System.out.println(currentMonth);

        try
        {
            addingDeletingObject = new AddingDeleting();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //<editor-fold desc="Main Stuff">
        // is it possible to make the panel full screen?
        pCenter = new JPanel();
        pCenter.setLayout(null);
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        // get the positions right
        // this is not actually to the window, it just opens the file chooser
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

        //<editor-fold desc="Search Stuff">
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

        //<editor-fold desc="Mood Stuff">
        selectYourMoodT = new JLabel("Select Your Mood");
        selectYourMoodT.setBounds(950, 400, 150, 35);
        pCenter.add(selectYourMoodT);

        sadIcon = new ImageIcon("src/moodIcons/sadIcon.png");
        sadFaceButton = new JButton(sadIcon);
        sadFaceButton.addActionListener((ActionEvent e) -> queueObject.addSadFace());
        sadFaceButton.setBounds(800, 450, 100, 90);
        pCenter.add(sadFaceButton);

        chillIcon = new ImageIcon("src/moodIcons/chillIcon.png");
        chillFaceButton = new JButton(chillIcon);
        chillFaceButton.addActionListener((ActionEvent e) -> queueObject.addRelaxedFace());
        chillFaceButton.setBounds(1100, 450, 100, 90);
        pCenter.add(chillFaceButton);

        happyIcon = new ImageIcon("src/moodIcons/happyIcon.png");
        happyFaceButton = new JButton(happyIcon);
        happyFaceButton.addActionListener((ActionEvent e) -> queueObject.addHappyFace());
        happyFaceButton.setBounds(900, 450, 100, 90);
        pCenter.add(happyFaceButton);

        veryHappyIcon = new ImageIcon("src/moodIcons/veryHappyIcon.png");
        veryHappyButton = new JButton(veryHappyIcon);
        veryHappyButton.addActionListener((ActionEvent e) -> queueObject.addEnergeticFace());
        veryHappyButton.setBounds(1000, 450, 100, 90);
        pCenter.add(veryHappyButton);
        //</editor-fold>

        // middle section -> pause play stuff
        // create a play button -> change to this when pause is clicked, and vice versa
        // adjust the coordinates cause this looks weird

        //<editor-fold desc="Middle Stuff">
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
        tenBackButton.addActionListener(this::actionPerformedTenSecBack);
        pCenter.add(tenBackButton);

        tenForwardIcon = new ImageIcon("src/middleSectionHP/tenForwardIcon.jpg");
        tenForwardButton = new JButton(tenForwardIcon);
        tenForwardButton.setBounds(800, 300, 50, 30);
        tenForwardButton.addActionListener(this::actionPerformedTenSecForward);
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

        /*
        try
        {
            addDeleteObject = new AddDeleteWindow();
            addDeleteObject.dispose();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

         */

        setVisible(true);
    }

    public void search(ActionEvent e)
    {
        // can I have a search thing that if the user types only one letter, or a few, it shows all the
        // songs that start like that

        // i think I have to split the content of the text field, and each song name into individual char
        // then i compare those one by one, if one char is different, break
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

        ArrayList<Character> charsTxtFieldList = new ArrayList<>();
        // adding searchBox text to the search tags array
        if (searchBox.getText().length() > 0)
        {
            // dividing the textField content into chars
            char[] charsTextField = searchBox.getText().toCharArray();
            for (char c : charsTextField)
            {
                System.out.println(c);
                charsTxtFieldList.add(c);
                allTags.get(0).add(c);
            }
        }

        // now, I'll read the songs with tags file
        allSongsAndTags = new ArrayList<>();
        try
        {
            Scanner scanner = new Scanner(addingDeletingObject.songsAndTagsFile);
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
        // I want a new search mechanism -> show all the songs with all the tags, or at least one of them
        for (ArrayList<String> arrTags : allTags)
        {
            for (ArrayList<String> arrSong : allSongsAndTags)
            {
                boolean theSame = true;
                char[] songInChars = arrSong.get(0).toCharArray();
                for (int i = 0; i < charsTxtFieldList.size(); i++)
                {
                    if (Character.compare(songInChars[i], charsTxtFieldList.get(i)) != 0)
                    {
                        theSame = false;
                        break;
                    }
                }
                if (theSame)
                {
                    allSearchResults.add(arrSong.get(0));
                }
            }
            // don't add the song if it's already on the array
            for (ArrayList<String> arrSong : allSongsAndTags)
            {
                for (String tag : arrTags)
                {
                    if (arrSong.contains(tag))
                    {
                        // add the song name to an array, so I can display it in the SearchResultsWindow
                        System.out.println(arrSong.get(0));
                        if (!allSearchResults.contains(arrSong.get(0)))
                            allSearchResults.add(arrSong.get(0));
                        break;
                    }
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
            Files.write(Path.of(String.valueOf(homePageMethodsObject.searchResultsFile)), fileContent, StandardCharsets.UTF_8);
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
        }
    }

    public void stopMusic(ActionEvent e)
    {
        songObject.stopPlaying();
        playingPlaylist = false;
        pauseButton.setIcon(pauseIcon);
        //progressBarObject.timer.stop();
    }

    public void actionPerformedPauseSong(ActionEvent e)
    {
        if (!isPaused)
        {
            pauseButton.setIcon(playIcon);
        }
        else
        {
            pauseButton.setIcon(pauseIcon);
        }
        isPaused = !isPaused;

        songObject.pauseUnpauseMusic();
    }

    public void actionPerformedLoopSong(ActionEvent e)
    {
        if (!isLooping)
        {
            loopButton.setIcon(loopingIcon);
        }
        else
        {
            loopButton.setIcon(loopIcon);
        }
        isLooping = !isLooping;

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

    public void randomizePlaylist(ActionEvent e) // HAVE SMT INDICATE RANDOM IS ACTIVATED
    {
        random = !random;
    }

    public void selectPlaylist(ActionEvent e)
    {
        if (listPlaylists.getSelectedValue() != null)
        {
            alreadyPlayed.clear();
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
                songObject.playMusic(songsPaths.get(0));
                alreadyPlayed.add(0);
                playingPlaylist = true;

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void goPreviousSong(ActionEvent e)
    {
        if (playingPlaylist)
        {
            String songPlayingNow = "src\\songsFiles\\" + songObject.whichPlayingNow();
            songObject.stopPlaying();
            if (songsPaths.indexOf(songPlayingNow) - 1 >= 0)
            {
                songObject.playMusic(songsPaths.get(songsPaths.indexOf(songPlayingNow) - 1));
            }
            else
            {
                songObject.playMusic(songsPaths.get(songsPaths.size() - 1));
            }
        }
    }

    public void goNextSong(ActionEvent e)
    {
        if (playingPlaylist)
        {
            if (!random)
            {
                String songPlayingNow = "src\\songsFiles\\" + songObject.whichPlayingNow();
                songObject.stopPlaying();
                if (songsPaths.indexOf(songPlayingNow) + 1 < songsPaths.size())
                {
                    songObject.playMusic(songsPaths.get(songsPaths.indexOf(songPlayingNow) + 1));
                }
                else
                {
                    songObject.playMusic(songsPaths.get(0));
                }
            }
            else
            {
                int rnd = 0;
                while (alreadyPlayed.contains(rnd))
                {
                    rnd = new Random().nextInt(songsPaths.size());
                }
                System.out.println(rnd);
                alreadyPlayed.add(rnd);
                songObject.playMusic(songsPaths.get(rnd));
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
        // setVisible(false);
        AddDeleteWindow.setVisible(true); // display SelectPlayWindow
        AddDeleteWindow.setAlwaysOnTop(true);
        // setVisible(true);
        // dispose(); // close home page
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