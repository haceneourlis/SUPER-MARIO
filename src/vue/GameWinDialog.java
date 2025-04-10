package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWinDialog extends JDialog {

    public GameWinDialog(JFrame parent, Runnable onNextLevel, Runnable onQuit) {
        super(parent, "Niveau Complété", true);  // La fenêtre est modale, donc elle bloque l'accès à la fenêtre parent
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Vous avez gagné !", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.GREEN);  // Afficher le message en vert
        add(label, BorderLayout.CENTER);

        // Créer le bouton "Passer au niveau suivant"
        JButton nextLevelBtn = new JButton("Niveau Suivant");
        nextLevelBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        nextLevelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer la fenêtre
                onNextLevel.run(); // Passer au niveau suivant
            }
        });

        // Créer le bouton "Quitter"
        JButton quitBtn = new JButton("Quitter");
        quitBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer la fenêtre
                onQuit.run(); // Quitter le jeu
            }
        });

        // Ajouter les boutons dans la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));  // GridLayout pour afficher les deux boutons
        panel.add(nextLevelBtn);
        panel.add(quitBtn);

        add(panel, BorderLayout.SOUTH);

        // Configurer la fenêtre
        setSize(400, 150);  // Augmenter la taille de la fenêtre si nécessaire
        setLocationRelativeTo(parent);  // Centrer par rapport à la fenêtre parent
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
