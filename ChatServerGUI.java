import javax.swing.*;               // Bibliothèque pour interface graphique
import java.awt.*;                  //  gérer les zones (layout) et positionnement des composants
import java.awt.event.ActionEvent; //  gérer les événements comme un clic ou la touche Entrée
import java.awt.event.ActionListener;
import java.io.*;                   //  lire/écrire via les sockets
import java.net.*;                  //  gérer les connexions réseau 

public class ChatServerGUI {

    private final JFrame frame;         // Fenêtre principale 
    private final JTextArea chatArea;   // Zone d'affichage des messages
    private final JTextField inputField;// Champ de saisie des messages
    private PrintWriter out;            // Flux pour envoyer les messages au client

    public ChatServerGUI() {
        // Initialisation de la fenêtre principale
        frame = new JFrame("Serveur de Chat");
        frame.setSize(400, 500); // Taille de la fenêtre   hauteur et largeur
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Quitter l'application ferme le programme

        // Zone d'affichage des messages reçus/envoyés
        chatArea = new JTextArea();
        chatArea.setEditable(false); // L'utilisateur ne peut pas écrire dedans
        JScrollPane scrollPane = new JScrollPane(chatArea);  //permet de garder visible la zone de chat

        // Champ de saisie des messages (en bas)
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupère le texte tapé par l'utilisateur
                String message = inputField.getText();
                // Si un client est connecté et le message n’est pas vide
                if (!message.isEmpty() && out != null) {
                    // Affiche le message dans la zone de chat côté serveur
                    chatArea.append("Serveur: " + message + "\n");
                    // Envoie le message au client
                    out.println(message);
                    // Vide le champ de saisie
                    inputField.setText("");
                } else {
                    // Si aucun client n’est connecté, afficher un message d’erreur
                    chatArea.append("Erreur : Client non connecté\n");
                }
            }
        });

        // Ajout des éléments graphiques dans la fenêtre
        frame.add(scrollPane, BorderLayout.CENTER); // zone de chat au centre
        frame.add(inputField, BorderLayout.SOUTH);  // champ de saisie en bas
        frame.setVisible(true); // Affiche la fenêtre

        startServer(); // Lance le serveur (en arrière-plan)
    }

    public void startServer() {
        int port = 12345; // Port d’écoute du serveur

        // Démarre un thread pour ne pas bloquer l’interface graphique
        new Thread(() -> {
            try (
                // Création du serveur sur le port
                ServerSocket serverSocket = new ServerSocket(port);
                // Attend qu’un client se connecte (appel bloquant)
                Socket clientSocket = serverSocket.accept();
                // Flux de lecture pour recevoir les messages du client
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()))
            ) {
                // Flux de sortie pour envoyer des messages au client
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                // Affiche que le client est connecté
                chatArea.append("Client connecté.\n");

                String message;
                // Boucle de réception des messages tant que le client est connecté
                while ((message = in.readLine()) != null) {
                    // Affiche chaque message reçu du client
                    chatArea.append("Client: " + message + "\n");
                }

            } catch (IOException e) {
                // Affiche les erreurs dans la zone de chat
                chatArea.append("Erreur: " + e.getMessage() + "\n");
            }
        }).start(); // Lance le thread
    }

    public static void main(String[] args) {
        // Lance l’interface graphique sur le thread Swing
        SwingUtilities.invokeLater(ChatServerGUI::new);
    }
}


/*-----------//Fonctionnement//--------------------------------------------------------------
 * Il permet à un utilisateur (le serveur) :
 * - de communiquer en temps réel avec un client TCP,
 * - d’afficher les messages reçus,
 * - d’écrire et d’envoyer des réponses via une interface visuelle simple.
 */

/* -----------//explication//------------------------------------
 * - private signifie que ces attributs sont accessibles uniquement dans la classe ChatServerGUI
 */

/*/*-----------//Fonctionnement en detail//--------------------------------------------------------------
 * une interface s'ouvre avec :
 * - une zone de texte qui affiche les messages échangés avec le client,
 * - et un champ de saisie qui permet au serveur d'envoyer des messages.
 * Le serveur démarre automatiquement en grâce à la méthode `startServer()`.
 * Il attend qu’un client se connecte sur le port 12345. Ce processus se fait dans un thread séparé,
 * afin de ne pas bloquer l’interface graphique pendant l’attente de connexion.
 * Une fois que le client est connecté :
 * - le serveur lit tous les messages envoyés par ce client via un flux d'entrée (`BufferedReader`),
 * - et les affiche en temps réel dans la zone de chat.
 * Lorsque l'utilisateur du serveur écrit un message dans le champ de saisie et appuie sur Entrée :
 * - ce message est affiché localement dans l'interface (avec le préfixe "Serveur:"),
 * - et immédiatement transmis au client grâce à un flux de sortie (`PrintWriter`).
 * Si le serveur tente d’envoyer un message alors qu’aucun client n’est connecté,
 * un message d’erreur s’affiche dans l’interface pour en informer l’utilisateur.
 * La communication gère qu’un seul client à la fois, mais offre une interface simple, 
 *//*pour les< problemes c'est liée au commentaire */
