import java.awt.*;                     // Pour la mise en page et les composants graphiques
import java.awt.event.ActionEvent;    // Pour les actions
import java.awt.event.ActionListener;
import java.io.*;                      // Pour les flux d'entrée/sortie (réseau)
import java.net.*;                     // Pour la communication réseau via Socket
import javax.swing.*;                 // Pour créer l'interface graphique avec Swing

public class ChatClientGUI {

    private final JFrame frame;         // Fenêtre principale de l'application
    private final JTextArea chatArea;   // Zone d'affichage des messages reçus/envoyés
    private final JTextField inputField;// Champ pour saisir un message
    private PrintWriter out;            // Flux permettant d'envoyer les messages au serveur

    public ChatClientGUI() {
        // Création de la fenêtre
        frame = new JFrame("Client de Chat");
        frame.setSize(400, 500); // Définition de la taille de la fenêtre (largeur x hauteur)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme le programme quand la fenêtre est fermée

        // Initialisation de la zone de chat (affichage)
        chatArea = new JTextArea();
        chatArea.setEditable(false); //  pas de modification de zone directement
        JScrollPane scrollPane = new JScrollPane(chatArea); //  faire défiler le texte si besoin

        // Initialisation du champ de saisie
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override /*  @Override est utilisée pour indiquer que la méthode qui suit est une réécriture d'une méthode héritée*/
            public void actionPerformed(ActionEvent e) {
                // Récupère le message de l'utilisateur
                String message = inputField.getText();

                // Si un message est tapé ET que la connexion est bien établie
                if (!message.isEmpty() && out != null) {
                    // Affiche le message du client
                    chatArea.append("Client: " + message + "\n");
                    // Envoie du message au serveur
                    out.println(message);
                    
                    inputField.setText("");
                } else {
                    // Si le client n'est pas connecté au serveur, afficher un message d'erreur
                    chatArea.append("Erreur : Non connecté au serveur\n");
                }
            }
        });

        // Ajout des composants
        frame.add(scrollPane, BorderLayout.CENTER); // La zone de chat au centre
        frame.add(inputField, BorderLayout.SOUTH);  // Le champ de saisie en bas
        frame.setVisible(true); // Affiche de la fenêtre à l'écran

        connectToServer();
    }

    // Méthode pour se connecter au serveur
    public void connectToServer() {
        String serverAddress = "localhost"; // Adresse IP
        int serverPort = 12345;             // Port 

        // lance un thread pour établir la connexion 
        new Thread(() -> {
            try (
                // la Création de la socket pour la connection au serveur
                Socket socket = new Socket(serverAddress, serverPort);
                // la Création d'un flux pour lire les messages envoyés par le serveur
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                // Création du flux pour envoyer les messages au serveur
                out = new PrintWriter(socket.getOutputStream(), true);
                // Affiche un message dans le chat si la connexion est réussie
                chatArea.append("Connecté au serveur.\n");

                String message;
                // lire les messages envoyés du serveur en infinie
                while ((message = in.readLine()) != null) {
                    // Affiche les messages du serveur 
                    chatArea.append("Serveur: " + message + "\n");
                }

            } catch (IOException e) {
                // Si une erreur réseau
                chatArea.append("Erreur: " + e.getMessage() + "\n");
            }
        }).start(); // lance le thread
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClientGUI::new);
    }
}

/*---------------//Fonctionnement//------------------------------------

L’utilisateur ouvre l’application : une fenêtre s’affiche.
Le client tente de se connecter au serveur (localhost:12345).
Si la connexion réussit, un message "Connecté au serveur" apparaît.
L’utilisateur écrit un message et appuie sur Entrée.
Le message est envoyé au serveur via PrintWriter.
Tous les messages reçus du serveur sont affichés dans la zone de texte. */
/*
 * 
 */