package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalTime;

public class SearchWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuAddDelete, menuPhotos, menuPlaylists, menuSongs, menuQueue;
    private JPanel pCenter;
    private JLabel searchT, moodT, timeT, instrumentT, themeT, timeLabel;
    private JCheckBox sadBox, energeticBox, happyBox, relaxedBox, morningBox, afternoonBox, eveningBox,
            guitarBox, pianoBox, vocalBox, christmasBox, independenceDayBox, easterBox, realTimeBox;

    public SearchWindow()
    {
        // make a smaller JFrame - is there a way to leave the "minimize", "x" button out of the frame?
        // DO NOT USE SET BOUNDS, I AM SUPPOSED TO USE PANELS INSIDE PANELS
        super("Search");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // make a smaller JPanel
        pCenter = new JPanel();
        pCenter.setPreferredSize(new Dimension(1260, 650));
        // pCenter.setBorder(BorderFactory.createLineBorder(Color.black));
        add(pCenter, BorderLayout.CENTER);

        // I want this to be kinda the filters (tags here) - so it's like what should appear when
        // the user clicks the filter button
        // so like a subwindow-ish thing, not full screen, just right there in the middle


        searchT = new JLabel("Search");
        searchT.setBounds(30, 100, 100, 35);
        pCenter.add(searchT);

        //<editor-fold desc="Combo Boxes">
        String[] days = {"Day", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
                "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
                "29", "30", "31"};
        String[] months = {"Month", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] years = {"Year", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013",
                "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024",
                "2025", "2026", "2027", "2028", "2029", "2030"};
        JComboBox comboBoxDays = new JComboBox(days);

        JComboBox comboBoxMonths = new JComboBox(months);

        JComboBox comboBoxYears = new JComboBox(years);

        pCenter.add(comboBoxDays);
        pCenter.add(comboBoxMonths);
        pCenter.add(comboBoxYears);
        //</editor-fold>

        //<editor-fold desc="Real time stuff">
        realTimeBox = new JCheckBox("Real Time");
        pCenter.add(realTimeBox);

        timeLabel = new JLabel();
        showTime();
        pCenter.add(timeLabel);
        //</editor-fold>
        
        // add the bounds to everything hereeee
        // got them from here: https://www.javatpoint.com/java-jcheckbox
        //<editor-fold desc="Mood tags">
        moodT = new JLabel("Mood");
        moodT.setBounds(50, 150, 100, 35);
        pCenter.add(moodT);

        sadBox = new JCheckBox("Sad");
        sadBox.setBounds(50, 200, 100, 35);
        pCenter.add(sadBox);

        energeticBox = new JCheckBox("Energetic");
        energeticBox.setBounds(50, 250, 100, 35);
        pCenter.add(energeticBox);

        happyBox = new JCheckBox("Happy");
        happyBox.setBounds(50, 300, 100, 35);
        pCenter.add(happyBox);

        relaxedBox = new JCheckBox("Relaxed");
        relaxedBox.setBounds(50, 350, 100, 35);
        pCenter.add(relaxedBox);
        //</editor-fold>

        //<editor-fold desc="Time tags">
        timeT = new JLabel("Time");
        timeT.setBounds(200, 150, 100, 35);
        pCenter.add(timeT);

        morningBox = new JCheckBox("Morning");
        morningBox.setBounds(200, 200, 100, 35);
        pCenter.add(morningBox);

        afternoonBox = new JCheckBox("Afternoon");
        afternoonBox.setBounds(200, 250, 100, 35);
        pCenter.add(afternoonBox);

        eveningBox = new JCheckBox("Evening");
        eveningBox.setBounds(200, 300, 100, 35);
        pCenter.add(eveningBox);
        //</editor-fold>

        //<editor-fold desc="Instruments tags">
        instrumentT = new JLabel("Instrument");
        instrumentT.setBounds(350, 150, 100, 35);
        pCenter.add(instrumentT);

        guitarBox = new JCheckBox("Guitar");
        guitarBox.setBounds(350, 200, 100, 35);
        pCenter.add(guitarBox);

        pianoBox = new JCheckBox("Piano");
        pianoBox.setBounds(350, 250, 100, 35);
        pCenter.add(pianoBox);

        vocalBox = new JCheckBox("Vocal");
        vocalBox.setBounds(350, 300, 100, 35);
        pCenter.add(vocalBox);
        //</editor-fold>

        //<editor-fold desc="Themes tags">
        themeT = new JLabel("Theme");
        themeT.setBounds(500, 150, 100, 35);
        pCenter.add(themeT);

        christmasBox = new JCheckBox("Christmas");
        christmasBox.setBounds(500, 200, 100, 35);
        pCenter.add(christmasBox);

        independenceDayBox = new JCheckBox("Independence Day");
        independenceDayBox.setBounds(500, 250, 200, 35);
        pCenter.add(independenceDayBox);

        easterBox = new JCheckBox("Easter");
        easterBox.setBounds(500, 300, 100, 35);
        pCenter.add(easterBox);
        //</editor-fold>

        setVisible(true);
    }

    public void showTime()
    {
        LocalTime now = LocalTime.now();
        int hours = now.getHour(), minutes = now.getMinute(), seconds = now.getSecond();
        timeLabel.setText(formatInt(hours) + ":" + formatInt(minutes) + ":" + formatInt(seconds));
    }

    private String formatInt(int i)
    {
        return String.format("%02d", i);
    }

}