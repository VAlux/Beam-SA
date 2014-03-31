/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

import Graphics.ViewController;

import javax.swing.*;

/**
 * User: Lux
 * Date: 27.11.13
 * Time: 16:09
 */

public class Core {
    public static void main(String [] args){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Nimbus is not available on your platform");
        }
        new ViewController();
    }
}
