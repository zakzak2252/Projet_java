import java.io.*;          // gestion des d'entrée/sortie.
import java.net.*;         //  gestion des connexions réseau via sockets
import java.util.Scanner;  //  lire les entrées clavier de l'utilisateur 

public class ChatServer {  

    public static void main(String[] args) {
        int port = 12345;  // Port  du serveur

        try ( 
            // Création d’un socket serveur qui écoute sur le port
            ServerSocket serverSocket = new ServerSocket(port);
            // Attend la connexion d’un client (accept)
            Socket clientSocket = serverSocket.accept();
            // Flux de sortie pour envoyer des messages au client (auto-flush activé)
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Flux d’entrée pour recevoir les messages du client (ligne par ligne)
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Scanner pour lire les messages tapés par l'utilisateur du serveur
            Scanner scanner = new Scanner(System.in)
        ) {

            // Thread secondaire pour recevoir et afficher les messages du client
            Thread readThread = new Thread(() -> {
                try {
                    String message;  // Variable pour stocker les messages reçus

                    // Boucle de lecture continue des messages du client
                    while ((message = in.readLine()) != null) { //Lire en continu les messages envoyés par le client, ligne par ligne.
                        // Affichage du message reçu
                        System.out.println("Client: " + message);
                        // Réinvite l'utilisateur à taper une réponse
                        System.out.println("VOUS : ");
                    }
                } catch (IOException e) {
                    // En cas d'erreur de réception, affiche la trace de l'erreur
                    e.printStackTrace();
                }
            });

            // Démarrage du thread de réception des messages du client
            readThread.start();

            //lit ce que tape l'utilisateur du serveur et l’envoie au client
            String userInput;
            while (scanner.hasNextLine()) {
                userInput = scanner.nextLine();   // Lit une ligne de la console
                out.println(userInput);           // Envoie le message au client via le flux de sortie
            }

        } catch (IOException e) {
            // Gère les erreur 
            e.printStackTrace();
        }
    }
}
//-----------------------------------//fonctionnement//----------------------------------
// Lancement du serveur
// programme démarre et crée un ServerSocket sur le port 12345.
// attend  (accept()) qu’un client se connecte
// Connexion d’un client
//Dès qu’un client se connecte  la connexion est établie.
//Le serveur crée alors deux flux :out : pour envoyer des messages au client,
//in : pour recevoir les messages du clien
//Réception des messages du client (thread parallèle)
//Un thread secondaire est lancé pour 
//écouter en permanence ce que le client envoie (readLine()),
//afficher chaque message reçu dans la console avec System.out.println("Client: ...").

//-----------------------------------//explication//-----------------------------------------------*
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
// on a un auto flach= true ca veut dire que des quand tap entree notre texte est envoyer sans decalage 
// instentanement 


/*pour les problemes c'est liée au commentaire */