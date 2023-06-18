package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SelectPlayWindow extends JFrame
{
    private JMenuBar menuBar;

    public SelectPlayWindow()
    {
        super("Select Play");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());

        menuBar = new JMenuBar();
        JMenuItem quitMenu = new JMenuItem("Quit");
        quitMenu.addActionListener((ActionEvent e) -> System.exit(0));
        menuBar.add(quitMenu);
        setJMenuBar(menuBar);


        setVisible(true);
    }
}