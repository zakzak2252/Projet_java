import java.io.*;           // Importe les classes (entrées/sorties) 
import java.net.*;          // Importe les classes (communication réseau)

public class Serveur {      
        public static void main(String[] args) {  
        int port = 12345;    // Numéro de port sur lequel le serveur va écouter les connexions

        try ( //pour exécuter une liste d’instructions 
        // crée un socket serveur qui écoute le port 12345 et attend qu’un client se connecte via TCP
            ServerSocket serverSocket = new ServerSocket(port); 
            // la méthode accept() est bloquante
            // Crée un socket de communication une fois la connexion établie
            Socket clientSocket = serverSocket.accept(); 
            //renvoie un Socket représentant la connexion établie avec ce client.
            //Créer un fichier vide où il va écrire les données reçues dans un fichier nommé "received_file.txt",
            FileOutputStream fileOutputStream = new FileOutputStream("received_file.txt");
            // Lire les données envoyées par le client
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream())
        ) {

            byte[] buffer = new byte[1024]; // Création d’un tampon pour la lecture des données par blocs de 1024 octets
            int bytesRead;                  // Variable pour stocker le nombre d'octets lus à chaque lecture
            // Écrit ces données dans le fichier jusqu’à la fin de la transmission
            while ((bytesRead = dataInputStream.read(buffer)) != -1) { //Condition != -1 :
            //Tant que le client continue à envoyer des données, read() renvoie un nombre positif.
            // Écriture dans le fichier uniquement des octets réellement lus
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            // Message de confirmation 
            System.out.println("Fichier reçu avec succès.");

        } catch (IOException e) {  // Capture les erreurs entre les entrees et les zorties 
            e.printStackTrace();   // Affiche la trace de l’erreur dans la console
        }
    }
}


//----------------------------//Fonctionnement//-------------------------------------------
//Le serveur s’installe sur le port 12345 et attend une connexion du client.
//- Quand le client se connecte, le serveur accepte la connexion et prépare à recevoir des données.
// la réception du fichier par le serveur (lit les données envoyées par le client bloc par bloc).
//-Chaque bloc reçu est écrit dans un fichier local nommé "received_file.txt".
//-Quand le client a terminé d’envoyer, le serveur ferme tout et affiche "Fichier reçu avec succès.".

//---------------------------//explication//-------------------------------------------------
//  //Condition != -1 :
//Tant que le client continue à envoyer des données, read() renvoie un nombre positif.
// sur While // // Quand le client a fini d'envoyer, read() retourne -1 (fin du flux).
// fileOutputStream.write(buffer, 0, bytesRead); buuffer fait  1024 mais read lit que 120 octect il yua un risque de pas lire les donnes ecrite 

/*pour les problemes c'est liée au commentaire */