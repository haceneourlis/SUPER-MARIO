import controleur.*;
import javax.swing.*;
import java.awt.*;
import modele.*;
import modele.Tile.TileManager;
import vue.*;

public class Main {
    public static void main(String[] args) {
        JFrame fenetre = new JFrame("Super Mario");
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setSize(CONSTANTS.LARGEUR_VUE, CONSTANTS.HAUTEUR_VUE);
        fenetre.setLocationRelativeTo(null); // Centrer la fenêtre

        // === PANEL DE DÉMARRAGE ===
        JPanel startPanel = new JPanel(new BorderLayout());
        JLabel titre = new JLabel("Super Mario", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 36));
        JButton startBtn = new JButton("Jouer");
        startBtn.setFont(new Font("Arial", Font.PLAIN, 24));

        startPanel.add(titre, BorderLayout.NORTH);
        startPanel.add(startBtn, BorderLayout.CENTER);
        fenetre.setContentPane(startPanel);
        fenetre.setVisible(true);

        // === ACTION SUR "JOUER" ===
        startBtn.addActionListener(e -> {
            // Nettoyer et créer les composants
            TileManager tilemanager = TileManager.getInstance();
            Mario mario = Mario.getInstance();
            Affichage gamePanel = new Affichage();

            Descente descente = new Descente();
            Jumping jumping = new Jumping(descente);
            MouvementJoueur mv = new MouvementJoueur();
            DeplacementListener dl = new DeplacementListener(mv, mario, jumping);
            Collision collision = new Collision(jumping, descente);

            descente.start();
            dl.start();
            collision.start();

            for (Ennemi ennemi : tilemanager.getListeEnnemis()) {
                ennemi.thread.start();
            }

            mario.reset(mv);

            // Remplacer le contenu de la fenêtre par le jeu
            fenetre.setContentPane(gamePanel);
            fenetre.revalidate();
            fenetre.repaint();

            // === Focus pour capter les touches ===
            gamePanel.addKeyListener(mv);
            gamePanel.setFocusable(true);
            gamePanel.requestFocusInWindow();
        });
    }
}
