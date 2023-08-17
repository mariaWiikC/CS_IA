package com.company;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
        {
            try
            {
                new HomePageWindow();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }
}
