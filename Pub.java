import java.io.*;         // Importe classes gestion des fichiers et flux
import java.net.*;        // Importe classes communication réseau (Socket)

public class Pub {
    public static void main(String[] args) {
        String filename = "fichier.txt";        // Nom du fichier à envoyer
        String serverAddress = "localhost";     // Adresse du serveur dans notre cas notre ordi 
        int serverPort = 12345;                 // Port du serveur

        try (
            // Création socket pour se connecter au serveur sur l’adresse et le port
            Socket socket = new Socket(serverAddress, serverPort);
            //lire le fichier à envoyer
            FileInputStream fileInputStream = new FileInputStream(filename);
            //  envoyer les données au serveur via la socket
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())
        ) {

            byte[] buffer = new byte[1024];  // Création d’un tampon pour la lecture des données par blocs de 1024 octets
            int bytesRead;
            // Écrit ces données dans le fichier jusqu’à la fin de la transmission
            // Lecture du fichier  par bloc et envoi au serveur
            while ((bytesRead = fileInputStream.read(buffer)) != -1) { //Condition != -1 :
                dataOutputStream.write(buffer, 0, bytesRead);  // Envoie uniquement les octets lus
            }
  //Tant que le client continue à envoyer des données, read() renvoie un nombre positif.
            // Message de confirmation affiché côté client
            System.out.println("Fichier envoyé avec succès.");

        } catch (IOException e) {
            // Affichage de l’erreur en cas de problème.
            e.printStackTrace();
        }
    }
}

//-----------------------//fonctionnement//-------------------------------------------
// un client qui se connecte à un serveur TCP, lit un fichier local, et l'envoie au serveur via le réseau.
//----------------------// explication//-----------------------------------------------
//pour avoir l'adresse ip du pc  (command Prompt /cd ipconfig)

/*pour les problemes c'est liée au commentaire */