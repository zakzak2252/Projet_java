import java.io.*;           // Pour PrintWriter, BufferedReader, etc.
import java.net.*;          // Pour la gestion réseau 
import java.util.Scanner;   // Pour lire les entrées clavier depuis la console

public class Client2 {     

    public static void main(String[] args) {
        String serverAddress = "localhost";  // Adresse localhost 
        int serverPort = 12345;              // Port d’écoute 

        // Affichage  pour l'utilisateur
        System.out.println("Serveur en connexion sur le port " + serverPort);
        System.out.println("Vous pouvez commencer le chat.");

        try (
            // Connexion au serveur via socket (TCP)
            Socket socket = new Socket(serverAddress, serverPort);

            // Flux de sortie : pour envoyer des messages au serveur
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  // autoFlush = true

            // Flux d’entrée : pour lire les messages envoyés par le serveur
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Scanner : pour lire ce que l'utilisateur du client tape au clavier
            Scanner scanner = new Scanner(System.in)
        ) {

            // Création d’un thread pour recevoir les messages du serveur sans bloquer l’envoi
            Thread readThread = new Thread(() -> {
                try {
                    String message;
                    // Boucle  pour lire et afficher les messages du serveur
                    while ((message = in.readLine()) != null) {//Lire en continu les messages envoyés par le serveur, ligne par ligne.
                        System.out.println("Serveur: " + message); // Affiche le message reçu
                        System.out.println("VOUS :");              // Invite à  une réponse
                    }
                } catch (IOException e) {
                    // Affiche les erreurs réseau ou de lecture
                    e.printStackTrace();
                }
            });

            // Démarrage du thread de réception
            readThread.start();

            // Boucle principale : envoie les messages saisis par l'utilisateur au serveur
            String userInput;
            while (scanner.hasNextLine()) {
                userInput = scanner.nextLine();   // Lit une ligne depuis la console
                out.println(userInput);           // Envoie la ligne au serveur
            }

        } catch (IOException e) {
            // Gestion des erreurs réseau ou d'entrée/sortie
            e.printStackTrace();
        }
    }
}

/*-fonctionnement: --------------

 * - notre client essaye se connecter à un serveur situé à l’adresse
 *   "localhost" sur le port 12345.
 * - Si le serveur est actif et écoute ce port, la connexion est établie.
 * 
 * - Pour un flux de sortie est utilisé pour envoyer des messages
 *   au serveur. 
 *
 * - le BufferedReader est utilisé pour recevoir les
 *   messages envoyés par le serveur.
 *
 * - le scanner est utilisé pour lire les messages que l’utilisateur tape
 *   dans la console du client.
 * 
 * - le thread parallèle est lancé pour écouter les messages provenant
 *   du serveur.
 * 
 * - À chaque message reçu, il est affiché sur notre terminal avec "Serveur: " ce qui permet de recevoir des messages sans bloquer l'envoi côté client.
 * - sur la boucle principale, le programme lit en continu les messages
 *   que l'utilisateur a saisit via le clavier.
 * - Chaque message est ensuite envoyé au serveur via le PrintWriter.
 * - Le try-with-resources garantit la fermeture automatique de la
 *   socket et des flux lorsque le programme se termine ou en cas d'erreur.
 */
/*pour les< problemes c'est liée au commentaire */
