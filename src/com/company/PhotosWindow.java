package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class PhotosWindow extends JPanel
{
    private ArrayList<Icon> list = new ArrayList<>();
    JLabel label;

    private Timer timer = new Timer(5000, new ActionListener() // this is five seconds
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            update();
        }
    });

    public PhotosWindow()
    {
        this.setLayout(new GridLayout(1, 0));
        String directoryPhotosFilePath = "src/Photos";
        File directoryPhotos = new File(directoryPhotosFilePath);
        File[] filesPhotos = directoryPhotos.listFiles(File::isFile);
        for (File file : filesPhotos)
        {
            String actualPath = directoryPhotosFilePath + "/" + file.getName();
            try
            {
                BufferedImage img = ImageIO.read(new File(actualPath));
                ImageIcon icon = new ImageIcon(img);
                list.add(icon);
                System.out.println(actualPath);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        label = new JLabel(list.get(0));
        this.add(label);

        timer.start();
    }

    private void update()
    {
        Collections.shuffle(list);
        label.setIcon(list.get(0));
    }

    public void display()
    {
        JFrame f = new JFrame("Photos");
        // i don't want to exit the program, just the JFrame
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.add(this);
        //f.setSize(new Dimension(400, 400));
        f.pack(); // not this, make it full screen
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

}