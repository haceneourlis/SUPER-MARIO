import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * Classe qui va gérer les logs de chaque classe et les mettre dans un fichier
 */
public class LoggingConfig {
    static {
        try {
            // Supprime le fichier logs.txt s'il existe déjà
            File logFile = new File("logs.txt");
            if (logFile.exists()) {
                if (logFile.delete()) {
                    System.out.println("Ancien fichier logs.txt supprimé.");
                } else {
                    System.err.println("Impossible de supprimer logs.txt.");
                }
            }
        
            // Crée un handler pour écrire dans le fichier "logs.txt"
            FileHandler fileHandler = new FileHandler("logs.txt", true); // 'true' pour append
            fileHandler.setFormatter(new SimpleFormatter()); // Format simple des logs

            // Ajoute ce handler à tous les loggers dans l'application
            Logger rootLogger = Logger.getLogger(""); // Root logger (global)
            rootLogger.addHandler(fileHandler);

            // Définit le niveau de log global (mettre à OFF, si on ne veut plus de msg)
            rootLogger.setLevel(Level.WARNING);
        } catch (IOException e) {
            System.err.println("Erreur lors de la configuration des logs : " + e.getMessage());
        }
    }
}
