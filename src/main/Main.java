package Main;

import javax.swing.JFrame;

import View.Affichage;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Super Mario Ground");
        Affichage affichage = new Affichage();

        frame.add(affichage);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}