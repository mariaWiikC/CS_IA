package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Photo
{
    Photo()
    {

    }
    public void addingPhotos()
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

    public void displayPhotos()
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
}