package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fenêtre de dialogue affichée quand le joueur perd toutes ses vies.
 * Elle permet au joueur de relancer la partie via un bouton "Rejouer".
 */
public class GameOverDialog extends JDialog {
    public GameOverDialog(JFrame parent, Runnable onRestart) {
        super(parent, "Game Over", true);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("GAME OVER", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.RED);
        add(label, BorderLayout.CENTER);

        JButton replayBtn = new JButton("Rejouer");
        replayBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        replayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer la fenêtre
                onRestart.run(); // Appeler la fonction de redémarrage
            }
        });

        add(replayBtn, BorderLayout.SOUTH);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
