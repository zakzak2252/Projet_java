# APPLICATION-JAVA

Dans le cadre de mon Projet en Java, j'ai mis en oeuvre une application client-serveur qui réalise les interactions suivantes 
- Transfert des fichiers (Serveur.java / Pub.java).
- Système de chat en ligne / Interface graphique (ChatServer, Client2, ChatServerGUI, ChatClientGUI).
- Transmission et animation d’un chemin pour un robot (RobotClient, RobotPathServer).

Ce projet permet d’explorer plusieurs aspects des communications en Java, notamment les sockets TCP, la gestion de flux, et l'interface graphique avec Swing.

Fonctionnement de l’application 
1/ Transfert des fichiers du client vers serveur:


Client (Pub.java) : lit un fichier local ( fichier.txt) et l’envoie par socket.
Serveur (Serveur.java) : reçoit le fichier et le sauvegarde sous received_file.txt

String filename = "fichier.txt";

2/ Application de chat (mode texte):

Le serveur (ChatServer.java) démarre en premier :
Il ouvre un port TCP (12345).
Il attend qu’un client se connecte.
Une fois connecté, il peut envoyer et recevoir des messages.

Le client (Client2.java) démarre ensuite :
Il se connecte à l’adresse IP du serveur (souvent localhost en local).
Il peut envoyer des messages au serveur et en recevoir également.


3/Version graphique du chat:
Créer une application de chat en interface graphique (GUI) entre un client et un serveur, en utilisant Java Swing.
 Fichiers impliqués :
ChatServerGUI.java : côté serveur graphique
ChatClientGUI.java : côté client graphique
 Technologies utilisées :
Java Swing : pour créer des interfaces (fenêtres, zones de texte, champs de saisie, etc.)
Sockets TCP : pour la communication réseau entre le client et le serveur
Threads (multithreading) : pour gérer la réception des messages sans bloquer l’interface
 Fonctionnement de l’interface:
1. Champs de saisie (JTextField)

Permet à l’utilisateur de taper un message.
Dès qu’on appuie sur Entrée, le message est envoyé.

2. Zone d’affichage des messages (JTextArea)

Affiche tous les messages précédents :

Ceux envoyés par l’utilisateur local (serveur ou client).
Ceux reçus depuis l'autre côté de la communication.

 3. Barre de défilement (JScrollPane)

Permet de faire défiler les messages si la fenêtre est remplie.

4/ Simulation de robot et transmission de chemin
 Côté Client : RobotClient.java
 Interface graphique intuitive :

Une fenêtre graphique (créée avec Swing) permet à l’utilisateur de cliquer sur l’écran.
À chaque clic, un point est ajouté à une liste de coordonnées (List<Point>).
Ces points représentent les étapes du trajet du robot.

 Envoi du chemin :
En appuyant sur le bouton "Envoyer le chemin au serveur", le programme :
Transforme chaque point en une ligne de texte "x,y".
Envoie ces lignes via un socket TCP vers le serveur.
Termine l'envoi avec une ligne "END" pour signaler la fin du trajet.
 Côté Serveur : RobotPathServer.java
   Réception des données :
Le serveur démarre et attend une connexion du client.
Une fois connecté, il :
Lit chaque ligne reçue (coordonnées "x,y"),
Les convertit en Point(x, y),
Les stocke dans une liste de points représentant le chemin.

 Animation du robot :
Un robot (représenté par un cercle vert) est affiché à l’écran.
Le robot suit progressivement le trajet point par point.
L’animation est fluide grâce à un Timer qui déplace le robot par petits pas.


 Contrôle du déplacement :
L’utilisateur peut appuyer sur les flèches du clavier (← ↑ ↓ →) pour naviguer entre les points du chemin :
Flèche droite / bas → avance vers le point suivant.
Flèche gauche / haut → revient au point précédent.


Résultats obtenues:
Dans un premier temps il trace des points aléatoire par le client et les envoie au serveur qu’il les reçoit et représente le robot par le point vert et on appuie sur les flèches du clavier (← ↑ ↓ →) pour naviguer entre les points du chemin :
Flèche droite / bas → avance vers le point suivant.
Flèche gauche / haut → revient au point précédent.

Utilisation de l’application : 
Lancement des serveurs :
Exécuter les classes Serveur, ChatServer ou RobotPathServer selon le besoin.
Utilisation des clients :
Envoyer un fichier avec Pub.
Chatter en console (Client2) ou en interface (ChatClientGUI).
Dessiner un trajet sur RobotClient puis le visualiser animé côté serveur.
