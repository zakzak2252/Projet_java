import java.awt.*;            // Pour les composants graphiques
import java.awt.event.*;      // Pour gérer les événements clavier
import java.io.*;             // Pour la gestion des flux réseau
import java.net.*;            // Pour la communication via sockets
import java.util.ArrayList; 
import java.util.List;
import javax.swing.*;         // Pour les éléments graphiques Swing

public class RobotPathServer {

    private final JFrame frame;           // Fenêtre principale
    private final DrawingPanel panel;     // Panneau pour afficher le chemin et le robot
    private final List<Point> path = new ArrayList<>(); // Liste des points formant le chemin
    private int targetIndex = 0;          // Index du point vers lequel le robot doit se diriger

    private double robotX, robotY;        // Coordonnées actuelles du robot
    private Timer moveTimer;              // Timer pour animer le mouvement du robot

    public RobotPathServer() {
        // Configuration de la fenêtre
        frame = new JFrame("Serveur - Robot animé");
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ajout du panneau graphique dans la fenêtre
        panel = new DrawingPanel();
        frame.add(panel);
        frame.setVisible(true);

        // Gestion du déplacement du robot avec les flèches du clavier
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Si le chemin est vide ou si une animation est déjà en cours, on ne fait rien
                if (path.isEmpty() || (moveTimer != null && moveTimer.isRunning())) return;

                // Déplacement vers le point suivant avec flèche droite ou bas
                if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_DOWN)
                    && targetIndex < path.size() - 1) {
                    animateToPoint(targetIndex + 1);
                }
                // Déplacement vers le point précédent avec flèche gauche ou haut
                else if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_UP)
                    && targetIndex > 0) {
                    animateToPoint(targetIndex - 1);
                }
            }
        });

        // Permet de capter les touches clavier
        SwingUtilities.invokeLater(() -> frame.requestFocusInWindow());

        // Démarrage du serveur pour attendre les données du client
        startServer();
    }

    /**
     * Méthode pour animer le déplacement du robot vers un point cible
     */
    private void animateToPoint(int newIndex) {
        Point target = path.get(newIndex);  // Point d’arrivée
        double dx = target.x - robotX;      // Distance horizontale à parcourir
        double dy = target.y - robotY;      // Distance verticale à parcourir
        double distance = Math.sqrt(dx * dx + dy * dy); // Distance totale
        double steps = distance / 2.0;      // Nombre de pas pour une animation fluide
        double stepX = dx / steps;          // Avancement horizontal à chaque étape
        double stepY = dy / steps;          // Avancement vertical à chaque étape

        moveTimer = new Timer(10, null);    // Crée un timer qui déclenche toutes les 10 ms
        final int[] count = {0};            // Compteur d'étapes

        moveTimer.addActionListener(e -> {
            if (count[0] >= steps) {
                // Animation terminée : on place le robot exactement sur la cible
                robotX = target.x;
                robotY = target.y;
                targetIndex = newIndex;
                moveTimer.stop();
            } else {
                // Déplacement progressif du robot
                robotX += stepX;
                robotY += stepY;
                count[0]++;
            }
            panel.repaint(); // Redessine la scène avec la nouvelle position
        });

        moveTimer.start(); // Démarre l’animation
    }

    /**
     * Méthode pour démarrer le serveur TCP et recevoir les coordonnées du chemin
     */
    private void startServer() {
        new Thread(() -> {
            try (
                // Création du socket serveur (attend une connexion)
                ServerSocket serverSocket = new ServerSocket(12345);
                Socket socket = serverSocket.accept(); // Connexion entrante
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                String line;
                path.clear(); // Vide l’ancien chemin

                // Lecture des points envoyés par le client
                while ((line = in.readLine()) != null) {
                    if (line.equals("END")) break; // Fin de la transmission
                    String[] parts = line.split(","); // Séparation des coordonnées "x,y"
                    path.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                } // Initialisation du robot à la position du premier point
                if (!path.isEmpty()) {
                    Point start = path.get(0);
                    robotX = start.x;
                    robotY = start.y;
                    targetIndex = 0;
                    panel.repaint(); // Affiche le chemin et la position initiale
                }

            } catch (IOException e) {
                e.printStackTrace(); // En cas d'erreur de connexion
            }
        }).start(); // Démarre le serveur dans un thread séparé
    }

    /**
     * Classe interne qui gère le dessin graphique (chemin et robot)
     */
    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Dessine les lignes entre les points du chemin
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < path.size() - 1; i++) {
                Point p1 = path.get(i);
                Point p2 = path.get(i + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            // Dessine chaque point du chemin
            g.setColor(Color.BLUE);
            for (Point p : path) {
                g.fillOval(p.x - 4, p.y - 4, 8, 8);
            }

            // Dessine le robot sous forme de cercle vert
            g.setColor(Color.GREEN);
            g.fillOval((int) robotX - 10, (int) robotY - 10, 20, 20);
        }
    }

    // Méthode principale pour lancer l'application serveur
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RobotPathServer::new);
    }
}


/*  
fonctionnement
 

 - le serveur crée une interface graphique pour afficher le chemin du robot.
 - démarre un serveur sur le port 12345 et attend que notre client se connecte.
 - notre client connecté, met les points pour le trajet du robot 
lit les points du trajet envoyés par le client sous forme de texte "x,y".
    
le message "chemin envoyée" au client indiquenat que les donnes sont transferer au robot
 - le robot est positionné au premier point du trajet.

  Le chemin est affiché graphiquement :
    - Les lignes entre les points sont tracées en gris.
    - Les points eux-mêmes sont affichés en bleu.
    - Le robot est représenté par un cercle vert.

 -  on peut  contrôler le robot avec le clavier 
  */