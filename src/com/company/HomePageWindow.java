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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class HomePageWindow extends JFrame implements ActionListener
{
    private JMenuBar menuBar;
    private JPanel p;
    private JPanel pCenter;
    private JButton toSelectPlayWindow, toAddDeleteWindow, toPhotosWindow, toPlaylistsWindow,
            toQueuePreviewWindow, toSongsWindow;
    private JButton sadFaceButton, chillFaceButton, happyFaceButton, veryHappyButton;
    private JLabel searchT, selectYourMoodT;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuPlaylists, menuSongs, menuQueue;
    private ImageIcon sadIcon, chillIcon, happyIcon, veryHappyIcon;
    private JButton filterButton, loopButton, nextSongButton, pauseButton, playButton, previousSongButton,
            randomButton, tenBackButton, tenForwardButton;
    // Do I need a search button? Or will the search happen when the user presses enter
    private ImageIcon filterIcon, loopIcon, nextSongIcon, pauseIcon, playIcon, previousSongIcon,
            randomIcon, tenBackIcon, tenForwardIcon;
    private JTextField searchBox;
    private JProgressBar pB;
    private AudioInputStream audioInputStream;
    private SongStuff songObject;
    private boolean isPlaying;

    // nameOfPlaylistT, nameOfSongT -> these two change according to what is playingggg
    // DECREASE THE SAMPLE RATE OF THE SONGS AND TRY AGAIN -> IT WORKEDDDDDDD

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
        menuPhotos.addActionListener(this::actionPerformed5);

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
        toQueuePreviewWindow.addActionListener(this::actionPerformed6);
        pCenter.add(toQueuePreviewWindow);

        toPhotosWindow = new JButton("Photos");
        toPhotosWindow.setBounds(1100, 90, 100, 35);
        toPhotosWindow.addActionListener(this::actionPerformed5);
        pCenter.add(toPhotosWindow);
        //</editor-fold>

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

        BufferedImage searchIcon = ImageIO.read(new File("src/middleSectionHP/searchIcon.png"));
        JLabel searchIconLabel = new JLabel(new ImageIcon(searchIcon));
        searchIconLabel.setBounds(30, 200, 30, 30);
        pCenter.add(searchIconLabel);
        //</editor-fold>

        //<editor-fold desc="Mood Stuff">
        selectYourMoodT = new JLabel("Select Your Mood");
        selectYourMoodT.setBounds(950, 400, 150, 35);
        pCenter.add(selectYourMoodT);

        sadIcon = new ImageIcon("src/moodIcons/sadIcon.png");
        sadFaceButton = new JButton(sadIcon);
        sadFaceButton.setBounds(800, 520, 100, 90);
        pCenter.add(sadFaceButton);

        chillIcon = new ImageIcon("src/moodIcons/chillIcon.png");
        chillFaceButton = new JButton(chillIcon);
        chillFaceButton.setBounds(1100, 520, 100, 90);
        pCenter.add(chillFaceButton);

        happyIcon = new ImageIcon("src/moodIcons/happyIcon.png");
        happyFaceButton = new JButton(happyIcon);
        happyFaceButton.setBounds(900, 520, 100, 90);
        pCenter.add(happyFaceButton);

        veryHappyIcon = new ImageIcon("src/moodIcons/veryHappyIcon.png");
        veryHappyButton = new JButton(veryHappyIcon);
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
        playButton.addActionListener(this::actionPerformedUnpauseSong);
        pCenter.add(playButton);

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
        //while (isPlaying)
        //{
            // I FORGOT HOW THIS IS DONE :/ HOW DO I GET A VALUE FROM ANOTHER CLASS?

            //pB.setValue((int) (100 * songObject.clip.getMicrosecondPosition() / songObject.clip.getMicrosecondLength()));
            // pB.setString((int)(clip.getMicrosecondPosition()/(1e6*60)) + ":" +
            // (int)((clip.getMicrosecondPosition()/1e6)%60) + " / " +
            // (int)(clip.getMicrosecondLength()/(1e6*60)) + ":" + (int)((clip.getMicrosecondLength()/1e6)%60));
        //}
        //</editor-fold>

        // is the search thingy where the user writes a "text field"?
        // for the song selection part, my idea is the following:
        // I open the file chooser on the folder songsFiles (the one here) and I get it to play it


        setVisible(true);
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
            songObject = new SongStuff();
            songObject.playMusic(realPath);
            isPlaying = true;
        }
    }

    public void actionPerformedPauseSong(ActionEvent e)
    {
        // I should combine it into one button, the pause and unpause methods
        songObject.pauseMusic();
    }

    public void actionPerformedUnpauseSong(ActionEvent e)
    {
        songObject.unpauseMusic();
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

    @Override
    // i found this here: https://stackoverflow.com/questions/9569700/java-call-method-via-jbutton
    public void actionPerformed(ActionEvent e)
    {
        JFrame SelectPlayWindow = new SelectPlayWindow(); // open another JFrame
        SelectPlayWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

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
        JFrame PlaylistsWindow = null; // open another JFrame
        try
        {
            PlaylistsWindow = new PlaylistsWindow();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        PlaylistsWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed5(ActionEvent e)
    {
        JFrame PhotosWindow = new PhotosWindow(); // open another JFrame
        PhotosWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed6(ActionEvent e)
    {
        JFrame QueuePreviewWindow = new QueuePreviewWindow(); // open another JFrame
        QueuePreviewWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }

    public void actionPerformed7(ActionEvent e)
    {
        JFrame SearchWindow = new SearchWindow(); // open another JFrame
        SearchWindow.setVisible(true); // display SelectPlayWindow
        dispose(); // close home page
    }
}