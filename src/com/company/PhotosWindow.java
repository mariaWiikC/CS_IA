package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PhotosWindow extends JFrame
{
    private JMenuBar menuBar;
    private JMenuItem menuSelect, menuAddDelete, menuHome, menuPlaylists, menuSongs, menuQueue;

    public PhotosWindow()
    {
        // I CAN'T CLICK ON ANYTHING ELSE WHILE THIS IS BEING EXECUTED
        // IT IS ONLY SHOWING ONE IMAGE, AND ONLY AFTER IT EXECUTES THE BLOCK
        super("Photos");
        setLayout(new FlowLayout());
        setSize(200, 300);

        setVisible(true);

        String directoryPhotosFilePath = "src/Photos";
        File directoryPhotos = new File(directoryPhotosFilePath);
        File[] filesPhotos = directoryPhotos.listFiles(File::isFile);

        for (File f : filesPhotos)
        {
            BufferedImage img = null;
            try
            {
                System.out.println(f.getName());
                String actualPath = directoryPhotosFilePath + "/" + f.getName();
                System.out.println(actualPath);
                img = ImageIO.read(new File(actualPath));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            ImageIcon icon = new ImageIcon(img);
            JLabel lbl = new JLabel();
            lbl.setIcon(icon);
            add(lbl);
            try
            {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}