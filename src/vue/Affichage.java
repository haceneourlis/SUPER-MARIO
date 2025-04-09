package vue;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import modele.*;
import modele.Tile.TileManager;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;



/**
 * Classe qui affiche le jeu
 * Elle utilise la classe Redessine pour actualiser l'affichage
 * Elle utilisera également divers classes d'animations.
 */

public class Affichage extends JPanel {

    // Variables pour les instances de Mario et de l'ennemi
    private Mario JoueurPrincipal;
    private List<Ennemi> listeEnnemis;

    // Variable pour l'animation du joueur (Mario)
    private AnimationJoueur animationJoueur;

    // Variable pour l'animation du (des) koopa 
    private List<AnimationKoopa> animationKoopa;
    private List<AnimationGoomba> animationGoomba;

    // Variable pour le gestionnaire de tuiles
    public TileManager tm;

    private Font marioFont;

    // Un score et les coins
    private Score score;
    private Coin coin;

    private int decalage = 0;

    private BufferedImage coeurImage;

    // Constantes pour l'affichage du score 
    private final int SCORE_X = 10;
    private final int SCORE_Y = 30;

    // Constante pour l'affichage du nombre de coins
    private final int COINS_X = 200;
    private final int COINS_Y = 30;


    /**
     * Constructeur de la classe Affichage.
     * On initialise la taille de la fenêtre et on crée les instances de Mario et de l'ennemi.
     * On lance également les threads de l'ennemi, de l'animation du joueur et de l'actualisation de la fenetre (redessine).
     * @throws IOException 
     * @throws FontFormatException 
     */
    public Affichage(Score score, Coin coin){
        // Initialiser la fenêtre avec les dimensions prévues.
        setPreferredSize(new Dimension(CONSTANTS.LARGEUR_VUE, CONSTANTS.HAUTEUR_VUE)); // Set window size
        this.score = score;

        this.coin = coin;

        try{
            this.marioFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PressStart2P-Regular.ttf")).deriveFont(16f);
            System.out.println("Police Mario chargée !");
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("Police Mario pas chargée !");
            
        }
        // Initialiser le joueur (classe singleton)
        this.JoueurPrincipal = Mario.getInstance(); // Get the player instance : classe singleton .
        
        // Initialiser le gestionnaire de tuiles
        tm = new TileManager();

        listeEnnemis = new ArrayList<>();
        animationKoopa = new ArrayList<>();
        animationGoomba = new ArrayList<>();

        // Ajouter plusieurs ennemis
        Koopa koopa = new Koopa(600, 5, true, tm);
        Koopa koopa1 = new Koopa(200, 5, true, tm);
        Goomba goomba = new Goomba(300, 4, true, tm);
        listeEnnemis.add(koopa);
        listeEnnemis.add(koopa1);
        listeEnnemis.add(goomba);

        // Mettre à jour l'affichage toutes les 50ms
        (new Redessine(this)).start();

        // Lancer l'animation du joueur (Mario).
        animationJoueur = new AnimationJoueur(JoueurPrincipal);
        animationJoueur.start();

        //télecharger l'image du coeur
        try {
            coeurImage = ImageIO.read(getClass().getResourceAsStream("/resources/coeur.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Démarrer les threads des ennemis et de leurs animations
        animationKoopa.add(new AnimationKoopa(koopa));
        animationKoopa.add(new AnimationKoopa(koopa1));
        animationGoomba.add(new AnimationGoomba(goomba));

        for (Ennemi ennemi : listeEnnemis) {
            ennemi.thread.start();
        }
        for (AnimationKoopa Koopa : animationKoopa) {
            Koopa.start();
        }
        for (AnimationGoomba Goomba : animationGoomba) {
            Goomba.start();
        }
    }
    
    // getter de tileManager
    public TileManager getTileManager() {
        return this.tm;
    }

    /**
     * Getter de l'objet Ennemi.
     * @return l'ennemi du jeu.
     */
//    public Ennemi getEnnemi() {
//        return ennemi;
//    }
    public List<Ennemi> getEnnemis() {
        return listeEnnemis;
    }

    public void removeEnnemi(Ennemi ennemi) {
        this.listeEnnemis.remove(ennemi);
    }
    /**
     * Méthode qui dessiner les différents éléments sur la fenetre.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // On crée un objet Graphics2D pour dessiner les éléments
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);


        // je récupère la case de mario actuelle, relative au décalage
        int case_actuelle = ((this.JoueurPrincipal.getPositionX() - decalage)/ CONSTANTS.TAILLE_CELLULE);

        // Si la case de mario dépasse la case de scrolling, on décale la fenêtre
        if (case_actuelle >= CONSTANTS.CELLULE_SCROLLING){
            // Le décalage correspond à la distance entre mario et la case de scrolling
            this.decalage = JoueurPrincipal.getPositionX() - CONSTANTS.CELLULE_SCROLLING*CONSTANTS.TAILLE_CELLULE;
        }

        // On applique le décalage du plan de jeu
        // (note que comme l'objet Graphics2D est rechargé à chaque appel, les transformations ne s'aditionnent pas)
        g2.translate(-this.decalage, 0);

        // affichons la matrice du jeu : (le terrain)
        this.tm.draw(g2);

//        // Dessiner tous les ennemis avec leur animation respective
//        for (int i = 0; i < listeEnnemis.size(); i++) {
//            Ennemi ennemi = listeEnnemis.get(i);
//            BufferedImage imageEnnemi = null;
//
//            // Sélectionner l'animation correcte en fonction du type d'ennemi
//            if (ennemi.getType().equals("koopa") && i < animationKoopa.size()) {
//                imageEnnemi = animationKoopa.get(i).getCurrentToDraw();
//            }
////            if (ennemi.getType().equals("goomba") && i < animationGoomba.size()) {
////                imageEnnemi = animationGoomba.get(i).getCurrentToDraw();
////            }
//
//            // Debug：画一个红框，看看 Goomba 是不是存在于逻辑中
////            if (ennemi.getType().equals("goomba")) {
////                g2.setColor(Color.RED);
////                g2.drawRect(ennemi.getPosition().x, ennemi.getPosition().y, ennemi.getSolidArea().width, ennemi.getSolidArea().height);
////            }
//
//            if (ennemi.getType().equals("goomba") && i < animationGoomba.size()) {
//                BufferedImage frame = animationGoomba.get(i).getCurrentToDraw();
//
//                if (frame == null) {
//                    System.out.println("❌ Goomba 第 " + i + " 帧为 null！");
//                } else {
//                    System.out.println("✅ Goomba 第 " + i + " 帧正常绘图！");
//                }
//
//                imageEnnemi = frame;
//            }
//
//
//
//
//            // Dessiner l'ennemi
//            if (imageEnnemi != null) {
//                g2.drawImage(imageEnnemi, ennemi.getPosition().x, ennemi.getPosition().y, null);
//            }
//        }


//        int goombaIndex = 0; // 为 Goomba 单独维护计数器
//        for (Ennemi ennemi : listeEnnemis) {
//            BufferedImage imageEnnemi = null;
//            if (ennemi.getType().equals("koopa") && animationKoopa.size() > 0) {
//                imageEnnemi = animationKoopa.get(0).getCurrentToDraw();
//            }
//            if (ennemi.getType().equals("goomba") && goombaIndex < animationGoomba.size()) {
//                BufferedImage frame = animationGoomba.get(goombaIndex).getCurrentToDraw();
////                if (frame == null) {
////                    System.out.println("Goomba 第 " + goombaIndex + " 帧为 null");
////                } else {
////                    System.out.println("Goomba 第 " + goombaIndex + " 帧正常绘图");
////                }
//                imageEnnemi = frame;
//                goombaIndex++;
//            }
//            if (imageEnnemi != null) {
//                g2.drawImage(imageEnnemi, ennemi.getPosition().x, ennemi.getPosition().y, null);
//            }
//        }

        // 绘制所有敌人（根据敌人类型选择对应动画）
        int goombaIndex = 0;
        for (Ennemi ennemi : listeEnnemis) {
            BufferedImage imageEnnemi = null;
            if (ennemi instanceof Koopa) {
                // 对Koopa使用 AnimationKoopa 绘制（内部已判断状态，SHELL状态下直接返回静态图）
                if (!animationKoopa.isEmpty()) {
                    imageEnnemi = animationKoopa.get(0).getCurrentToDraw();
                }
            } else if (ennemi instanceof Goomba) {
                if (goombaIndex < animationGoomba.size()) {
                    imageEnnemi = animationGoomba.get(goombaIndex).getCurrentToDraw();
                    goombaIndex++;
                }
            }
            if (imageEnnemi != null) {
                g2.drawImage(imageEnnemi, ennemi.getPosition().x, ennemi.getPosition().y, null);
            }
        }

        // FIXME:
        // （A）绘制 Mario 的碰撞盒
        g2.setColor(Color.YELLOW);
        // 如有需要可以设定线条粗细
        // g2.setStroke(new BasicStroke(2f));

        Rectangle marioHitbox = new Rectangle(
                this.JoueurPrincipal.getPosition().x + this.JoueurPrincipal.getSolidArea().x,
                this.JoueurPrincipal.getPosition().y + this.JoueurPrincipal.getSolidArea().y,
                this.JoueurPrincipal.getSolidArea().width,
                this.JoueurPrincipal.getSolidArea().height
        );
        g2.draw(marioHitbox);

        // （B）绘制每个敌人的碰撞盒
        g2.setColor(Color.RED);
        for (Ennemi ennemi : listeEnnemis) {
            Rectangle ennemiHitbox = new Rectangle(
                    ennemi.getPosition().x + ennemi.getSolidArea().x,
                    ennemi.getPosition().y + ennemi.getSolidArea().y,
                    ennemi.getSolidArea().width,
                    ennemi.getSolidArea().height
            );
            g2.draw(ennemiHitbox);
        }




        // ✅ Mario clignote uniquement s'il est invincible, sans affecter le reste du dessin
if (!JoueurPrincipal.isInvincible() || (System.currentTimeMillis() / 200) % 2 == 0) {
    g2.drawImage(this.animationJoueur.getCurrentToDraw(), JoueurPrincipal.getPositionX(), JoueurPrincipal.getPositionY(), null);
}

        // Dessiner les vies (cœurs) CENTRÉS en haut
        int vies = JoueurPrincipal.getVies();
        int coeurWidth = 30;
        int coeurHeight = 30;
        int espaceEntreCoeurs = 10;

        // Calcul de la largeur totale des cœurs à dessiner
        int largeurTotale = vies * coeurWidth + (vies - 1) * espaceEntreCoeurs;

        // Calcul du point de départ X pour centrer
        int startX = (getWidth() - largeurTotale) / 2;
        // 2. ANNULER le décalage AVANT de dessiner les cœurs
        g2.translate(this.decalage, 0);  // Remet le contexte à 0 (sans décalage)

        // Dessiner les cœurs
        for (int i = 0; i < vies; i++) {
            int x = startX + i * (coeurWidth + espaceEntreCoeurs);
            g.drawImage(coeurImage, x, 10, coeurWidth, coeurHeight, null);

        }

        // Optionnel : Afficher "Game Over" au centre si plus de vies
        if (vies <= 0) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", getWidth() / 2 - 150, getHeight() / 2);
        }
        
//        g2.dispose();
                        
        g2.setFont(marioFont);
        g2.setColor(Color.WHITE);
        g2.drawString("Score : " + score.getCurrentScore(), SCORE_X , SCORE_Y);
        g2.drawString("Coins : " + coin.getNombreDePieces(), COINS_X , COINS_Y);
    }

    
}
