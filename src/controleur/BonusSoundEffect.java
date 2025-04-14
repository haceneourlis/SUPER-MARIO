package controleur;

import modele.*;
import vue.*;

public class BonusSoundEffect extends Thread {
    // Instance de ScoreManager (c'est un singleton donc on peut y accéder directement)
    private static ScoreManager scoreManager = ScoreManager.getInstance();

    // Référence vers la Vue qui permet de jouer les sons
    private Affichage affichage;
    
    // Instance singleton représentant Mario (pour accéder à son état, par ex. nombre de vies)
    private Mario mario = Mario.getInstance();

    // Drapeaux pour éviter de rejouer certains sons plusieurs fois d'affilée
    private boolean game_over_sound_alreadyPlayed = false;
    private boolean jumping_sound_alreadyPlayer = false;
    private boolean mario_invincible_sound_alreadyPlayed = false;
    
    // Constructeur qui reçoit l'affichage (la vue)
    public BonusSoundEffect(Affichage affichage) {
        this.affichage = affichage;
    }

    @Override
    public void run(){
        // On récupère les valeurs initiales du ScoreManager pour comparer les changements plus tard
        int nombrePieces = ScoreManager.getCoins();
        int nombreChampignon = ScoreManager.getMushrooms();
        int nb_goobas = ScoreManager.getGoomba();
        int nb_koopas = ScoreManager.getKoopa();
        int nb_shells = ScoreManager.getShells();

        // Boucle infinie du thread pour surveiller les changements
        while (true){
            try {
                Thread.sleep(CONSTANTS.DELAY_SOUND_COIN);

                // Si certains compteurs sont revenus à zéro, on réinitialise les variables locales de suivi
                if (ScoreManager.getShells() == 0) {
                    nb_shells = 0;
                }
                if (ScoreManager.getCoins() == 0) {
                    nombrePieces = 0;
                }
                if (ScoreManager.getMushrooms() == 0) {
                    nombreChampignon = 0;
                }
                if (ScoreManager.getGoomba() == 0) {
                    nb_goobas = 0;
                }
                if (ScoreManager.getKoopa() == 0) {
                    nb_koopas = 0;
                }

                // Vérifie si le nombre de Goomba a changé (comparaison avec la variable locale)
                if (nb_goobas - ScoreManager.getGoomba() != 0) {
                    // Si changement détecté, jouer le son associé (ici "goomba")
                    affichage.playSound("goomba");
                    // Met à jour la variable locale pour ne pas rejouer le son inutilement
                    nb_goobas ++;
                }

                // Même logique pour les Koopa
                if (nb_koopas - ScoreManager.getKoopa() != 0) {
                    affichage.playSound("shell");
                    nb_koopas ++;
                }

                // Vérification pour les pièces
                if (nombrePieces - ScoreManager.getCoins() != 0) {
                    affichage.playSound("coin");
                    nombrePieces ++;
                }

                // Vérification pour les shells (quand on saute sur un Koopa et qu'il se transforme en coquille)
                if (nb_shells - ScoreManager.getShells() != 0) {
                    affichage.playSound("shell");
                    nb_shells ++;
                }

                // Vérification pour le nombre de champignons
                if (nombreChampignon - ScoreManager.getMushrooms() != 0) {
                    affichage.playSound("mushroom");
                    nombreChampignon ++;
                }

                // Gestion du Game Over : si Mario n'a plus de vies...
                if (mario.getVies() == 0) {
                    // On s'assure que le son de game over ne se joue qu'une seule fois
                    if (!game_over_sound_alreadyPlayed) {
                        affichage.playSound("gameover");
                        game_over_sound_alreadyPlayed = true;
                    }
                }
                // Si Mario a encore des vies, il faut remettre à zéro le drapeau et relancer la musique de fond
                if (mario.getVies() > 0) {
                    if (game_over_sound_alreadyPlayed) {
                        affichage.playSound("game"); // musique de fond du jeu
                    }
                    game_over_sound_alreadyPlayed = false;
                }

                // Gestion du son de saut :
                // Si Mario est autorisé à descendre et qu'il va vers le haut
                if (mario.allowedToFallDown && mario.getDirection() == "up") {
                    // On joue le son de saut s'il n'a pas déjà été joué pour ce mouvement
                    if (!jumping_sound_alreadyPlayer) {
                        affichage.playSound("jump");
                        jumping_sound_alreadyPlayer = true;
                    }
                } 
                // Une fois que Mario n'est plus en "chute" ou en phase de saut, on réinitialise le drapeau
                if (!mario.allowedToFallDown){
                    jumping_sound_alreadyPlayer = false;
                }

                // Gestion de l'invincibilité : si Mario est en mode invincible...
                if (mario.isInvincible()){
                    // On joue le son d'invincibilité une seule fois au début de cet état
                    if (!mario_invincible_sound_alreadyPlayed) {
                        affichage.playSound("invincible");
                        mario_invincible_sound_alreadyPlayed = true;
                    }
                } else {
                    // Désactivation pour pouvoir rejouer le son lors d'une nouvelle invincibilité
                    mario_invincible_sound_alreadyPlayed = false;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}
