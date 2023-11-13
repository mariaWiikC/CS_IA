package com.company;

import javax.imageio.ImageIO;
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
import java.util.ArrayList;
import java.util.Collections;

public class Photo
{
    Photo()
    {

    }
    public void addingPhotos() // FIX THIS
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

    public void displayPhotos() // FIX THIS
    {
        EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                new PhotosWindow().display();
            }
        });
    }

    protected Timer timer = new Timer(5000, new ActionListener() // this is five seconds
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            update();
        }
    });

    protected void buildingPhotoWindow()
    {
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
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        label = new JLabel(list.get(0));

    }

    private void update()
    {
        Collections.shuffle(list);
        label.setIcon(list.get(0));
    }

    private ArrayList<Icon> list = new ArrayList<>();
    JLabel label;
}