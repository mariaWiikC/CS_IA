package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PhotosStuff
{
    JLabel lbl;
    String directoryPhotosFilePath = "src/Photos";
    File directoryPhotos = new File(directoryPhotosFilePath);
    File[] filesPhotos = directoryPhotos.listFiles(File::isFile);
    BufferedImage img = null, img2 = null;

    public PhotosStuff()
    {
        try
        {
            System.out.println(filesPhotos[0].getName());
            String actualPath = directoryPhotosFilePath + "/" + filesPhotos[0].getName();
            System.out.println(actualPath);
            img = ImageIO.read(new File(actualPath));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(img);
        lbl = new JLabel();
        lbl.setIcon(icon);
    }

}
