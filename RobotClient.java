import java.awt.*;   // Pour les composants graphiques
import java.awt.event.*;  // Pour gérer les événements
import java.io.*;  // Pour les flux de données 
import java.net.*;   // Pour la communication via sockets
import java.util.ArrayList; 
import java.util.List;
import javax.swing.*; // Pour les éléments graphiques Swing 

public class RobotClient {
    private final JFrame frame;  // Fenêtre principale 
    private final DrawingPanel panel;  // Zone de dessin 
    private final JButton sendButton;  // Bouton pour envoyer le chemin 
    private final List<Point> points = new ArrayList<>(); // Liste des points (x,y)

    public RobotClient() {
        // Configuration de la fenêtre 
        frame = new JFrame("Client - Chemin du Robot");
        frame.setSize(500, 550); // Taille de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermeture de l'application à la fermeture de la fenêtre
        frame.setLayout(new BorderLayout()); // Layout principal

        panel = new DrawingPanel();
        panel.setPreferredSize(new Dimension(500, 500));
        frame.add(panel, BorderLayout.CENTER); // Placement au centre

        // Création et ajout du bouton d'envoi
        sendButton = new JButton("Envoyer le chemin au serveur");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14)); // Style du texte du bouton
        frame.add(sendButton, BorderLayout.SOUTH); // Placement en bas

        // Action exécutée 
        sendButton.addActionListener(e -> sendPathToServer());

        // Affichage de la fenêtre
        frame.setVisible(true);
    }

    // Méthode qui envoie les points au serveur
    private void sendPathToServer() {
        try (
            // Connexion au serveur local sur le port 12345
            Socket socket = new Socket("localhost", 12345);
            // Flux de sortie vers le serveur (auto-flush activé)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Envoi de chaque point sous forme "x,y"
            for (Point p : points) {
                out.println(p.x + "," + p.y);
            }

            // Marqueur de fin de transmission
            out.println("END");

            // Message de confirmation à l'utilisateur
            JOptionPane.showMessageDialog(frame, "Chemin envoyé !");
        } catch (IOException e) {
            // Affichage d'une erreur en cas d'échec de connexion ou d'envoi
            JOptionPane.showMessageDialog(frame, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Classe interne représentant la zone de dessin
    private class DrawingPanel extends JPanel {
        public DrawingPanel() {
            // Écouteur d'événement 
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    points.add(e.getPoint());
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 1. Tracer les lignes reliant les points successifs
            g.setColor(Color.BLUE);
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            // 2. Dessiner un petit cercle pour chaque point
            for (Point p : points) {
                g.fillOval(p.x - 4, p.y - 4, 8, 8); // Cercle centré sur le point
            }
        }
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        // Exécute l'interface utilisateur dans le thread graphique de Swing
        SwingUtilities.invokeLater(RobotClient::new);
    }
}


/* fonctionnement
 */ /*  *
 * une interface graphique permettant à l'utilisateur de dessiner un chemin que devra suivre un robot. L'utilisateur clique sur une zone de dessin pour définir 
 * une série de points (coordonnées x, y), qui représentent le trajet pour le robot.
 *
 * - l'utilisateur clique sur un bouton pour envoyer ces points au serveur via laconnexion socket . 
 *
 * - L'utilisateur clique dans la zone centrale pour définir des points :
 *       - Chaque clic ajoute un point aux coordonnées cliquées.
 *       - Le panneau se redessine à chaque clic : les lignes sont tracées entre les points, 
 *         et chaque point est représenté par un petit cercle.
 *
 *  - tous les points définis, l'utilisateur clique sur le bouton
 *       "Envoyer le chemin au serveur".
 *
 * - tous les points envoyés, un message `"END"` est transmis pour 
 *      signaler la fin des données.
 *
 *  - Une boîte de dialogue informe l'utilisateur que le chemin a bien été envoyé.
 * - On reçoie bien le chemin sur notre interface serveur avec le chemin que le robot va suivre ainsi sont deplacement avec les touche droite, gauche, haut, bas
 */