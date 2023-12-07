import java.util.ArrayList;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.lang.Thread;

import javafx.concurrent.Task;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

/**
 * C'est la classe principale de l'application, c'est ici qu'on crée la scène du jeu par exemple.
 */
public class AppliDrones extends Application {
    //Déclaration de variable
    /**Thread pour que la tâche qui permet de faire avancer les drones tourne en même temps que la tâche principale où l'intrus peut se déplacer*/
    public Thread t;
    Rectangle rectangle;
    Circle cercle;
    /**Cercle représentant l'intrus */
    public Circle intrusCercle;
    /**Liste des rectangles représentant les sorties */
    public ArrayList<Rectangle> sortieRectangle;
    /**Liste des rectangles représentant les arbres */
    public ArrayList<Rectangle> arbreRectangle;
    /**Liste des rectangles représentant les zones */
    public ArrayList<Rectangle> zoneRectangle;
    /**Liste des cercles représentant les drones */
    public ArrayList<Circle> droneCercle;
    /**Liste des rectangles représentant le vide */
    public ArrayList<Rectangle> videRectangle;
    /**Valeur de la vitesse de l'intrus */
    public double vitesseIntrus;
    /**Valeur de la vitesse des drones */
    public double vitesseDrone;
    /**Valeur du champ de vision des drones */
    public int perceptionDrone;
    /**Valeur du champ de vision de l'intrus */
    public int perceptionIntrus;
    /**Emplacement de l'intrus dans la map */
    public Point intrus;
    /**Longueur de la map */
    public int n;
    /**Largeur de la map */
    public int m;
    /**Nombre de zones à chercher */
    public int nbZones;
    int nbTrouve = 0;
    int nbRecup = 0;
    /**Liste des sorties */
    public ArrayList<Point> sortie;
    /**Liste des arbres */
    public ArrayList<Arbre> arbre;
    /**Liste des zones à trouver */
    public ArrayList<Zone> zone;
    /**Liste des drones */
    public ArrayList<Drone> drone;
    /**Liste des cases de vide */
    public ArrayList<Point> vide;
    /**Système utile pour le comportement des drones */
    public Systeme systeme;
    /**Temps auquel l'intrus est repéré par les drones*/
    public int tempsRepere;
    long crescendo;
    /**Scène principale */
    public Stage primaryStage;
    Pane root;
    /**
     * Constructeur par défaut de la classe
     */
    public AppliDrones(){}
    @Override
    /**
     * Méthode exécutée après le démarrage de l'application.
     * Elle affiche une fenêtre pour mettre les différents paramètres du jeu.
     * @param primaryStage : la scène principale
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Créer des éléments graphiques
        Label label1 = new Label("Longueur :");
        TextField nField = new TextField("50");

        Label label2 = new Label("Largeur :");
        TextField mField = new TextField("50");

        Label label3 = new Label("Densité :");
        TextField dField = new TextField("10");

        CheckBox cb1 = new CheckBox("Modifier la position de l'intrus ?");

        Label label4 = new Label("Nombre de sorties :");
        TextField nbsField = new TextField("4");

        Label label5 = new Label("Nombre de drones :");
        TextField nbdField = new TextField("10");

        CheckBox cb2 = new CheckBox("Modifier la position des drones ?");

        Label label6 = new Label("Nombre de zones :");
        TextField nbZonesField = new TextField("1");

        Label label7 = new Label("Vitesse des drones :");
        TextField vitesseDroneField = new TextField("1");

        Label label8 = new Label("Vitesse de l'intrus :");
        TextField vitesseIntrusField = new TextField("1");

        Label label9 = new Label("Zone de perception des drones :");
        TextField perceptionDroneField = new TextField("3");

        Label label10 = new Label("Zone de perception de l'intrus :");
        TextField perceptionIntrusField = new TextField("4");

        Label label11 = new Label("Temps de repérage pour se faire capturer :");
        TextField tempsRepereField = new TextField("5");

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            // Récupérer les valeurs entrées par l'utilisateur
            boolean good = true;
            n = Integer.parseInt(nField.getText());
            if(n<5){
                System.out.println("Erreur : La longueur est trop petite !");
                good=false;
                start(primaryStage);
            }
            m = Integer.parseInt(mField.getText());
            if(m<5){
                System.out.println("Erreur : La largeur est trop petite !");
                good=false;
                start(primaryStage);
            }
            int d = Integer.parseInt(dField.getText());
            if(d<0){
                System.out.println("Erreur : Densité impossible !");
                good=false;
                start(primaryStage);
            }
            if(d>95){
                System.out.println("Erreur : Trop dense !");
                good=false;
                start(primaryStage);
            }
            int nbs = Integer.parseInt(nbsField.getText());
            if(nbs<=0){
                System.out.println("Erreur : Nombre de sorties impossible !");
                good=false;
                start(primaryStage);
            }
            if(nbs>4){
                System.out.println("Erreur : Trop de sorties !");
                good=false;
                start(primaryStage);
            }
            int nbd = Integer.parseInt(nbdField.getText());
            if(nbd<0){
                System.out.println("Erreur : Nombre de drones impossible !");
                good=false;
                start(primaryStage);
            }
            nbZones = Integer.parseInt(nbZonesField.getText());
            if(nbZones<=0){
                System.out.println("Erreur : Nombre de zones impossible !");
                good=false;
                start(primaryStage);
            }
            vitesseDrone = Double.parseDouble(vitesseDroneField.getText());
            if(vitesseDrone<=0){
                System.out.println("Erreur : Vitesse des drones impossible !");
                good=false;
                start(primaryStage);
            }
            if(vitesseDrone>5){
                System.out.println("Erreur : Vitesse des drones trop haute !");
                good=false;
                start(primaryStage);
            }
            vitesseIntrus = Double.parseDouble(vitesseIntrusField.getText());
            if(vitesseIntrus<0.5){
                System.out.println("Erreur : Vitesse de l'intrus impossible !");
                good=false;
                start(primaryStage);
            }
            perceptionDrone = Integer.parseInt(perceptionDroneField.getText());
            if(perceptionDrone<0){
                System.out.println("Erreur : Perception des drones impossible !");
                good=false;
                start(primaryStage);
            }
            perceptionIntrus = Integer.parseInt(perceptionIntrusField.getText());
            if(perceptionIntrus<0){
                System.out.println("Erreur : Perception de l'intrus impossible !");
                good=false;
                start(primaryStage);
            }
            tempsRepere = Integer.parseInt(tempsRepereField.getText())*1000;
            if(tempsRepere<=0){
                System.out.println("Erreur : Temps de repérage de l'intrus avant qu'il se fasse capturé impossible!");
                good=false;
                start(primaryStage);
            }
            boolean positionIntrus = cb1.isSelected();
            boolean positionDrone = cb2.isSelected();

            // Fermer la fenêtre
            if(good) construireScenesPourDronesPartie1(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
        });

        // Organiser les éléments dans une mise en page VBox
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label1, nField, label2, mField, label3, dField, cb1, label4, nbsField, label5, nbdField, cb2, label6, nbZonesField, label7, vitesseDroneField, label8, vitesseIntrusField, label9, perceptionDroneField, label10, perceptionIntrusField, label11, tempsRepereField, okButton);

        // Créer une scène et l'ajouter à la fenêtre principale
        Scene scene = new Scene(vbox, 450, 775);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Paramètres");
        primaryStage.setOnCloseRequest(event -> stopThread()); // Arrêter le thread lorsque la fenêtre est fermée
        primaryStage.show();
    }
    /**
     * Méthode utilisée pour construire la scène pour le jeu.
     * Dans cette partie, on crée l'obscurité, les cases vides et les sorties.
     * @param primaryStage : la scène principale
     * @param n : la longueur de l'environnement
     * @param m : la largeur de l'environnement
     * @param d : la densité des arbres dans l'environnement
     * @param nbs : le nombre de sorties
     * @param nbd : le nombre de drones
     * @param nbZones : le nombre de zones à trouver
     * @param vitesseDrone : la vitesse des drones
     * @param vitesseIntrus : la vitesse de l'intrus
     * @param perceptionDrone : la valeur du champ de vision des drones
     * @param perceptionIntrus : la valeur du champ de vision de l'intrus
     * @param positionIntrus : booléen qui vaut true si on veut modifier la position de l'intrus, false si on veut que ça soit aléatoire
     * @param positionDrone : booléen qui vaut true si on veut modifier la position des drones, false si on veut que ça soit aléatoire
     * @param tempsRepere : si l'intrus est repéré pendant plus de temps que ce temps-ci, il se fait capturé par les drones
     */
    public void construireScenesPourDronesPartie1(Stage primaryStage, int n, int m, int d, int nbs, int nbd, int nbZones, double vitesseDrone, double vitesseIntrus, int perceptionDrone, int perceptionIntrus, boolean positionIntrus, boolean positionDrone, int tempsRepere){
        /*
        //Pour choisir les paramètres
        System.out.println("Longueur : " + n);
        System.out.println("Largeur : " + m);
        System.out.println("Densité : " + d);
        System.out.println("Nombre de sorties : " + nbs);
        System.out.println("Nombre de drones : " + nbd);
        System.out.println("Nombre de zones : " + nbZones);
        System.out.println("Vitesse des drones : " + vitesseDrone);
        System.out.println("Vitesse de l'intrus : " + vitesseIntrus);
        System.out.println("Zone de perception des drones : " + perceptionDrone);
        System.out.println("Zone de perception de l'intrus : " + perceptionIntrus);
        System.out.println("Choix de la position de l'intrus : "+positionIntrus);
        System.out.println("Choix de la position des drones : "+positionDrone);
        */
        // Configurer la fenêtre principale
        primaryStage.setTitle("Jeu des drones");
        primaryStage.show();

        //Pour contenir les coordonnées de points
        int a,b;

        // Initialisation des ArrayList
        sortie = new ArrayList<Point>();
        sortieRectangle = new ArrayList<Rectangle>();
        arbre = new ArrayList<Arbre>();
        arbreRectangle = new ArrayList<Rectangle>();
        drone = new ArrayList<Drone>();
        droneCercle = new ArrayList<Circle>();
        zone = new ArrayList<Zone>();
        zoneRectangle = new ArrayList<Rectangle>();
        vide = new ArrayList<Point>();
        videRectangle = new ArrayList<Rectangle>();

        // Créer un conteneur Pane
        root = new Pane();

        // Créer l'obscurité
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                rectangle = new Rectangle(i*10,j*10,10,10);
                rectangle.setFill(Color.BLACK); // Définir la couleur de remplissage du rectangle
                root.getChildren().add(rectangle);
            }
        }

        //Déclaration d'un point
        Point p;

        // Créer les cases vides (où il n'y a rien)
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                rectangle = new Rectangle(i*10,j*10,10,10);
                rectangle.setFill(Color.GRAY); // Définir la couleur de remplissage du rectangle
                rectangle.setOpacity(0);
                p=new Point(i, j);
                vide.add(p);
                root.getChildren().add(rectangle);
                videRectangle.add(rectangle);
            }
        }

        // Créer les sorties
        for(int i=0;i<nbs;i++){
            switch (i) {
                case 0:
                    a=0;
                    b=m/2;
                    rectangle = new Rectangle(a*10,b*10,10,10);
                    rectangle.setFill(Color.ORANGE); // Définir la couleur de remplissage du rectangle
                    rectangle.setOpacity(0);
                    root.getChildren().add(rectangle);
                    p=new Point(a,b);
                    sortie.add(p);
                    sortieRectangle.add(rectangle);
                    break;
                case 1:
                    a=n-1;
                    b=m/2;
                    rectangle = new Rectangle(a*10,b*10,10,10);
                    rectangle.setFill(Color.ORANGE); // Définir la couleur de remplissage du rectangle
                    rectangle.setOpacity(0);
                    root.getChildren().add(rectangle);
                    p=new Point(a,b);
                    sortie.add(p);
                    sortieRectangle.add(rectangle);
                    break;
                case 2:
                    a=n/2;
                    b=0;
                    rectangle = new Rectangle(a*10,b*10,10,10);
                    rectangle.setFill(Color.ORANGE); // Définir la couleur de remplissage du rectangle
                    rectangle.setOpacity(0);
                    root.getChildren().add(rectangle);
                    p=new Point(a,b);
                    sortie.add(p);
                    sortieRectangle.add(rectangle);
                    break;
                case 3:
                    a=n/2;
                    b=m-1;
                    rectangle = new Rectangle(a*10,b*10,10,10);
                    rectangle.setFill(Color.ORANGE); // Définir la couleur de remplissage du rectangle
                    rectangle.setOpacity(0);
                    root.getChildren().add(rectangle);
                    p=new Point(a,b);
                    sortie.add(p);
                    sortieRectangle.add(rectangle);
                    break;
                default:
                    break;
            }
        }
        construireScenesPourDronesPartie2(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
    }

    /**
     * Méthode utilisée pour construire la scène pour le jeu.
     * Dans cette partie, on crée les drones.
     * @param primaryStage : la scène principale
     * @param n : la longueur de l'environnement
     * @param m : la largeur de l'environnement
     * @param d : la densité des arbres dans l'environnement
     * @param nbs : le nombre de sorties
     * @param nbd : le nombre de drones
     * @param nbZones : le nombre de zones à trouver
     * @param vitesseDrone : la vitesse des drones
     * @param vitesseIntrus : la vitesse de l'intrus
     * @param perceptionDrone : la valeur du champ de vision des drones
     * @param perceptionIntrus : la valeur du champ de vision de l'intrus
     * @param positionIntrus : booléen qui vaut true si on veut modifier la position de l'intrus, false si on veut que ça soit aléatoire
     * @param positionDrone : booléen qui vaut true si on veut modifier la position des drones, false si on veut que ça soit aléatoire
     * @param tempsRepere : si l'intrus est repéré pendant plus de temps que ce temps-ci, il se fait capturé par les drones
     */
    public void construireScenesPourDronesPartie2(Stage primaryStage, int n, int m, int d, int nbs, int nbd, int nbZones, double vitesseDrone, double vitesseIntrus, int perceptionDrone, int perceptionIntrus, boolean positionIntrus, boolean positionDrone, int tempsRepere){
        int a,b;
        Random rand = new Random();
        Drone dr;
        // Créer les drones
        if(!positionDrone){
            for(int i=0;i<nbd;i++){
                a=rand.nextInt(n);
                b=rand.nextInt(m);
                dr=new Drone(a,b);
                int egal = 0;
                for(int j=0;j<drone.size();j++){
                    egal+=drone.get(j).equals(dr);
                }
                while (egal!=0) {
                    a=rand.nextInt(n);
                    b=rand.nextInt(m);
                    dr=new Drone(a,b);
                    egal = 0;
                    for(int j=0;j<drone.size();j++){
                        egal+=drone.get(j).equals(dr);
                    }
                }
                drone.add(dr);
                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                cercle.toFront();
                droneCercle.add(cercle);
            }
            construireScenesPourDronesPartie3(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
        }
        else{
            VBox vbox = new VBox(10);
            ArrayList<Label> Labels = new ArrayList<Label>();
            ArrayList<TextField> TextFields = new ArrayList<TextField>();
            Label label;
            TextField texte;
            for(int i=0;i<nbd;i++){
                label = new Label("Inserer la position du drone "+(i+1)+" : ");
                Labels.add(label);
                vbox.getChildren().add(label);
                texte = new TextField();
                TextFields.add(texte);
                vbox.getChildren().add(texte);
                texte = new TextField();
                TextFields.add(texte);
                vbox.getChildren().add(texte);
            }
            Button okButton = new Button("OK");
            okButton.setOnAction(e -> {
                drone.clear();
                droneCercle.clear();
                int good = 0;
                for(int i=0;i<nbd;i++){
                    Drone dr2 = new Drone(Integer.parseInt(TextFields.get(i*2).getText())-1, Integer.parseInt(TextFields.get(i*2+1).getText())-1);
                    int egal=0;
                    for(int j=0;j<drone.size();j++){
                        if(drone.get(j).equals(dr2)==1){
                            egal++;
                        }
                    }
                    if(egal==0){
                        good++;
                        if(dr2.x >= 0 && dr2.x <= n-1 && dr2.y >= 0 && dr2.y <= m-1) good++;
                        if(good%2 != 0){
                            System.out.println("Erreur : un drone n'est pas sur la map !");
                            construireScenesPourDronesPartie2(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
                            break;
                        }
                        drone.add(dr2);
                        if(dr2.mega) {cercle = new Circle(dr2.x*10+5,dr2.y*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                        else {cercle = new Circle(dr2.x*10+5,dr2.y*10+5,5);cercle.setFill(Color.RED);}
                        cercle.toFront();
                        droneCercle.add(cercle);
                    }
                    else{
                        System.out.println("Erreur : un drone à la même position qu'un autre drone !");
                        construireScenesPourDronesPartie2(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
                    }
                }
                if(good==2*nbd) construireScenesPourDronesPartie3(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
            });
            vbox.getChildren().add(okButton);
            ScrollPane scroll = new ScrollPane(vbox);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            Scene scene = new Scene(scroll, 200, 300);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Paramètres : Position des drones");
            primaryStage.setOnCloseRequest(event -> stopThread()); // Arrêter le thread lorsque la fenêtre est fermée
            primaryStage.show();
        }
    }

    /**
     * Méthode utilisée pour construire la scène pour le jeu.
     * Dans cette partie, on crée l'intrus.
     * @param primaryStage : la scène principale
     * @param n : la longueur de l'environnement
     * @param m : la largeur de l'environnement
     * @param d : la densité des arbres dans l'environnement
     * @param nbs : le nombre de sorties
     * @param nbd : le nombre de drones
     * @param nbZones : le nombre de zones à trouver
     * @param vitesseDrone : la vitesse des drones
     * @param vitesseIntrus : la vitesse de l'intrus
     * @param perceptionDrone : la valeur du champ de vision des drones
     * @param perceptionIntrus : la valeur du champ de vision de l'intrus
     * @param positionIntrus : booléen qui vaut true si on veut modifier la position de l'intrus, false si on veut que ça soit aléatoire
     * @param positionDrone : booléen qui vaut true si on veut modifier la position des drones, false si on veut que ça soit aléatoire
     * @param tempsRepere : si l'intrus est repéré pendant plus de temps que ce temps-ci, il se fait capturé par les drones
     */
    public void construireScenesPourDronesPartie3(Stage primaryStage, int n, int m, int d, int nbs, int nbd, int nbZones, double vitesseDrone, double vitesseIntrus, int perceptionDrone, int perceptionIntrus, boolean positionIntrus, boolean positionDrone, int tempsRepere){
        int a,b;
        Random rand = new Random();
        Point p;
        int cote;
        //Créer l'intrus
        if(!positionIntrus){
            a=rand.nextInt(n);
            b=rand.nextInt(m);
            cote=rand.nextInt(4);
            if(cote==0) a=rand.nextInt(3);
            if(cote==1) a=n-1-rand.nextInt(3);
            if(cote==2) b=rand.nextInt(3);
            if(cote==3) b=m-1-rand.nextInt(3);
            p=new Point(a,b);
            int egal = 0;
            for(int j=0;j<drone.size();j++){
                egal+=drone.get(j).equals(p);
            }
            while (egal!=0) {
                a=rand.nextInt(n);
                b=rand.nextInt(m);
                cote=rand.nextInt(4);
                if(cote==0) a=rand.nextInt(3);
                if(cote==1) a=n-1-rand.nextInt(3);
                if(cote==2) b=rand.nextInt(3);
                if(cote==3) b=m-1-rand.nextInt(3);
                p=new Point(a,b);
                egal = 0;
                for(int j=0;j<drone.size();j++){
                    egal+=drone.get(j).equals(p);
                }
            }
            intrus = p;
            cercle = new Circle(a*10+5,b*10+5,5);
            cercle.setFill(Color.TURQUOISE); // Définir la couleur de remplissage du cercle
            cercle.toFront();
            intrusCercle = cercle;
            construireScenesPourDronesPartie4(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
        }
        else{
            VBox vbox = new VBox(10);
            vbox.getChildren().add(new Label("Inserer la position de l'intrus : "));
            TextField x = new TextField();
            TextField y = new TextField();
            vbox.getChildren().add(x);
            vbox.getChildren().add(y);
            Button okButton = new Button("OK");
            okButton.setOnAction(e -> {
                Point p3=new Point(Integer.parseInt(x.getText())-1,Integer.parseInt(y.getText())-1);
                int good=0;
                int egal = 0;
                for(int j=0;j<drone.size();j++){
                    egal+=drone.get(j).equals(p3);
                }
                if(egal==0){
                    good++;
                    if((p3.x >= 0 && p3.x <= 2 && p3.y>=0 && p3.y<=m-1) || (p3.x <= n-1 && p3.x >= n-3 && p3.y>=0 && p3.y<=m-1) || (p3.x>=0 && p3.x <= n-1 && p3.y >= 0 && p3.y <= 2) || (p3.x>=0 && p3.x <= n-1 && p3.y <= m-1 && p3.y >= m-3)) good++;
                    intrus = p3;
                    cercle = new Circle(p3.x*10+5,p3.y*10+5,5);
                    cercle.setFill(Color.TURQUOISE); // Définir la couleur de remplissage du cercle
                    cercle.toFront();
                    intrusCercle = cercle;
                }
                else{
                    System.out.println("Erreur : l'intrus à la même position qu'un drone !");
                    construireScenesPourDronesPartie3(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
                }
                if(good==2) construireScenesPourDronesPartie4(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
                else if (good==1) {
                    System.out.println("Erreur : l'intrus n'est pas au bord de la map !");
                    construireScenesPourDronesPartie3(primaryStage,n,m,d,nbs,nbd,nbZones,vitesseDrone,vitesseIntrus,perceptionDrone,perceptionIntrus,positionIntrus,positionDrone,tempsRepere);
                }
            });
            vbox.getChildren().add(okButton);
            Scene scene = new Scene(vbox, 200, 125);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Paramètres : Position de l'intrus");
            primaryStage.setOnCloseRequest(event -> stopThread()); // Arrêter le thread lorsque la fenêtre est fermée
            primaryStage.show();
        }
    }

    /**
     * Méthode utilisée pour construire la scène pour le jeu.
     * Dans cette partie, on crée les arbres, les zones à trouver et le système des drones et on lance enfin le jeu/
     * @param primaryStage : la scène principale
     * @param n : la longueur de l'environnement
     * @param m : la largeur de l'environnement
     * @param d : la densité des arbres dans l'environnement
     * @param nbs : le nombre de sorties
     * @param nbd : le nombre de drones
     * @param nbZones : le nombre de zones à trouver
     * @param vitesseDrone : la vitesse des drones
     * @param vitesseIntrus : la vitesse de l'intrus
     * @param perceptionDrone : la valeur du champ de vision des drones
     * @param perceptionIntrus : la valeur du champ de vision de l'intrus
     * @param positionIntrus : booléen qui vaut true si on veut modifier la position de l'intrus, false si on veut que ça soit aléatoire
     * @param positionDrone : booléen qui vaut true si on veut modifier la position des drones, false si on veut que ça soit aléatoire
     * @param tempsRepere : si l'intrus est repéré pendant plus de temps que ce temps-ci, il se fait capturé par les drones
     */
    public void construireScenesPourDronesPartie4(Stage primaryStage, int n, int m, int d, int nbs, int nbd, int nbZones, double vitesseDrone, double vitesseIntrus, int perceptionDrone, int perceptionIntrus, boolean positionIntrus, boolean positionDrone, int tempsRepere){
        //on ajoute l'intrus
        root.getChildren().add(intrusCercle);

        int a,b;
        Random rand = new Random();
        Arbre ar;
        // Créer les arbres
        for(int i=0;i<(d*(n/10)*(m/10)*100)/100;i++){
            a=rand.nextInt(n);
            b=rand.nextInt(m);
            ar=new Arbre(a,b);
            int egal = 0;
            for(int j=0;j<sortie.size();j++){
                egal+=sortie.get(j).equals(ar);
            }
            for(int j=0;j<drone.size();j++){
                egal+=drone.get(j).equals(ar);
            }
            egal+=intrus.equals(ar);
            egal+=intrus.equalsG(ar);
            egal+=intrus.equalsD(ar);
            egal+=intrus.equalsH(ar);
            egal+=intrus.equalsB(ar);
            for(int j=0;j<arbre.size();j++){
                egal+=arbre.get(j).equals(ar);
            }
            while (egal!=0) {
                a=rand.nextInt(n);
                b=rand.nextInt(m);
                ar=new Arbre(a,b);
                egal = 0;
                for(int j=0;j<sortie.size();j++){
                    egal+=sortie.get(j).equals(ar);
                }
                for(int j=0;j<drone.size();j++){
                    egal+=drone.get(j).equals(ar);
                }
                egal+=intrus.equals(ar);
                egal+=intrus.equalsG(ar);
                egal+=intrus.equalsD(ar);
                egal+=intrus.equalsH(ar);
                egal+=intrus.equalsB(ar);
                for(int j=0;j<arbre.size();j++){
                    egal+=arbre.get(j).equals(ar);
                }
            }
            rectangle = new Rectangle(a*10,b*10,10,10);
            if(!ar.petit) rectangle.setFill(Color.DARKGREEN);
            else rectangle.setFill(Color.SEAGREEN);
            rectangle.setOpacity(0);
            root.getChildren().add(rectangle);
            arbre.add(ar);
            arbreRectangle.add(rectangle);
        }

        //Déclaration d'une zone
        Zone z;

        //Créer les zones particulières
        for(int i=0;i<nbZones;i++){
            a=rand.nextInt(n);
            b=rand.nextInt(m);
            z=new Zone(a,b);
            int egal = 0;
            for(int j=0;j<sortie.size();j++){
                egal+=sortie.get(j).equals(z);
            }
            for(int j=0;j<arbre.size();j++){
                egal+=arbre.get(j).equals(z);
            }
            for(int j=0;j<drone.size();j++){
                egal+=drone.get(j).equals(z);
            }
            for(int j=0;j<zone.size();j++){
                egal+=zone.get(j).equals(z);
            }
            egal+=intrus.equals(z);
            while (egal!=0) {
                a=rand.nextInt(n);
                b=rand.nextInt(m);
                z=new Zone(a,b);
                egal = 0;
                for(int j=0;j<sortie.size();j++){
                    egal+=sortie.get(j).equals(z);
                }
                for(int j=0;j<arbre.size();j++){
                    egal+=arbre.get(j).equals(z);
                }
                for(int j=0;j<drone.size();j++){
                    egal+=drone.get(j).equals(z);
                }
                for(int j=0;j<zone.size();j++){
                    egal+=zone.get(j).equals(z);
                }
                egal+=intrus.equals(z);
            }
            rectangle = new Rectangle(a*10,b*10,10,10);
            rectangle.setFill(Color.PURPLE); // Définir la couleur de remplissage du rectangle
            rectangle.setOpacity(0);
            root.getChildren().add(rectangle);
            zone.add(z);
            zoneRectangle.add(rectangle);
        }

        // On rajoute les drones à la fin
        for(int i=0;i<droneCercle.size();i++){
            root.getChildren().add(droneCercle.get(i));
        }

        //Premier repérage
        reperage();

        //Créer le système de l'ensemble des drones
        systeme = new Systeme(drone,intrus);
        systeme.repererIntrus(perceptionDrone,arbre);

        // Créer une scène
        Scene scene = new Scene(root, n*10, m*10);

        // Ajouter un écouteur d'événements de clavier à la scène
        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));

        // Configurer et afficher la fenêtre principale
        primaryStage.setTitle("Poursuite Drones");
        primaryStage.setScene(scene);
        primaryStage.show();
        crescendo = System.currentTimeMillis();
        System.out.println(LocalTime.now().format(
            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Création de l'environnement"
        );
        for(int i=0;i<drone.size();i++){
            System.out.println(LocalTime.now().format(
                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
            );
        }
        System.out.println(LocalTime.now().format(
            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position de l'intrus : "+(intrus.getX()+1)+" "+(intrus.getY()+1)
        );

        // Créer une tâche qui sera exécutée dans un autre thread pour gérer le déplacement des drones
        Task<Void> task = new Task<Void>() {
            @Override
            /**Boucle répétée à l'infini tant que l'intrus ne sort pas de la map ou ne se fasse capturé. Elle permet de faire avancer les drones */
            public Void call() throws Exception {
                int direct;
                int egal;
                double incremente = 0;
                int a,b;
                Drone dr;
                while(!Thread.interrupted()){
                    if(systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere){
                        System.out.println(LocalTime.now().format(
                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                        );
                        Platform.exit();
                        primaryStage.close();t.interrupt();
                    }
                    if(!systeme.trouve){
                        for(int i=0;i<drone.size();i++){
                            drone.get(i).directDeblock.clear();
                            if(drone.get(i).active){
                                direct = rand.nextInt(4);
                                switch (direct) {
                                    case 0:
                                        egal=0;
                                        if(drone.get(i).getY()!=0){
                                            for(int j=0;j<arbre.size();j++){
                                                if(!drone.get(i).mega) egal+=arbre.get(j).equalsH(drone.get(i));
                                                else if(!arbre.get(j).petit) egal+=arbre.get(j).equalsH(drone.get(i));
                                            }
                                            for(int j=0;j<drone.size();j++){
                                                if(drone.get(j).active){
                                                    egal+=drone.get(j).equalsH(drone.get(i));
                                                    if(drone.get(i).mega==drone.get(j).mega && drone.get(j).equalsH(drone.get(i))==1){
                                                        drone.get(i).active = false;
                                                        drone.get(j).active = false;
                                                        droneCercle.get(i).setOpacity(0);
                                                        droneCercle.get(j).setOpacity(0);
                                                        System.out.println(LocalTime.now().format(
                                                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Le drone "+(i+1)+" et le drone "+(j+1)+" se sont percutés"
                                                        );
                                                    }
                                                }
                                            }
                                            if(egal>0){break;}
                                            droneCercle.get(i).setCenterY(droneCercle.get(i).getCenterY() - 10);
                                            systeme.repererIntrus(perceptionDrone,arbre);
                                            try {
                                                Thread.sleep((int)(1000/((vitesseDrone+incremente)*drone.size())));
                                            } catch(InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            drone.get(i).setY(drone.get(i).y-1);
                                            System.out.println(LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
                                            );
                                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                                System.out.println(LocalTime.now().format(
                                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                                );
                                                Platform.exit();
                                                primaryStage.close();t.interrupt();
                                            }
                                            if(System.currentTimeMillis()-crescendo >= 60000){
                                                crescendo = System.currentTimeMillis();
                                                if((vitesseDrone+incremente)<5){
                                                    incremente+=0.5;
                                                    if((vitesseDrone+incremente)>=5) incremente = 5 - vitesseDrone;
                                                }
                                                a=rand.nextInt(n);
                                                b=rand.nextInt(m);
                                                dr=new Drone(a,b);
                                                egal = 0;
                                                for(int j=0;j<drone.size();j++){
                                                    egal+=drone.get(j).equals(dr);
                                                }
                                                for(int j=0;j<arbre.size();j++){
                                                    egal+=arbre.get(j).equals(dr);
                                                }
                                                egal+=intrus.equals(dr);
                                                egal+=intrus.equalsG(dr);
                                                egal+=intrus.equalsD(dr);
                                                egal+=intrus.equalsH(dr);
                                                egal+=intrus.equalsB(dr);
                                                while (egal!=0) {
                                                    a=rand.nextInt(n);
                                                    b=rand.nextInt(m);
                                                    dr=new Drone(a,b);
                                                    egal = 0;
                                                    for(int j=0;j<drone.size();j++){
                                                        egal+=drone.get(j).equals(dr);
                                                    }
                                                    for(int j=0;j<arbre.size();j++){
                                                        egal+=arbre.get(j).equals(dr);
                                                    }
                                                    egal+=intrus.equals(dr);
                                                    egal+=intrus.equalsG(dr);
                                                    egal+=intrus.equalsD(dr);
                                                    egal+=intrus.equalsH(dr);
                                                    egal+=intrus.equalsB(dr);
                                                }
                                                drone.add(dr);
                                                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                                                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                                                cercle.toFront();
                                                droneCercle.add(cercle);
                                                javafx.application.Platform.runLater(() -> ajouterCercles(root,cercle));
                                            }
                                        }
                                        break;
                                    case 1:
                                        egal=0;
                                        if(drone.get(i).getY()!=m-1){
                                            for(int j=0;j<arbre.size();j++){
                                                if(!drone.get(i).mega) egal+=arbre.get(j).equalsB(drone.get(i));
                                                else if(!arbre.get(j).petit) egal+=arbre.get(j).equalsB(drone.get(i));
                                            }
                                            for(int j=0;j<drone.size();j++){
                                                if(drone.get(j).active){
                                                    egal+=drone.get(j).equalsB(drone.get(i));
                                                    if(drone.get(i).mega==drone.get(j).mega && drone.get(j).equalsB(drone.get(i))==1){
                                                        drone.get(i).active = false;
                                                        drone.get(j).active = false;
                                                        droneCercle.get(i).setOpacity(0);
                                                        droneCercle.get(j).setOpacity(0);
                                                        System.out.println(LocalTime.now().format(
                                                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Le drone "+(i+1)+" et le drone "+(j+1)+" se sont percutés"
                                                        );
                                                    }
                                                }
                                            }
                                            if(egal>0){break;}
                                            droneCercle.get(i).setCenterY(droneCercle.get(i).getCenterY() + 10);
                                            systeme.repererIntrus(perceptionDrone,arbre);
                                            try {
                                                Thread.sleep((int)(1000/((vitesseDrone+incremente)*drone.size())));
                                            } catch(InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            drone.get(i).setY(drone.get(i).y+1);
                                            System.out.println(LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
                                            );
                                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                                System.out.println(LocalTime.now().format(
                                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                                );
                                                Platform.exit();
                                                primaryStage.close();t.interrupt();
                                            }
                                            if(System.currentTimeMillis()-crescendo >= 60000){
                                                crescendo = System.currentTimeMillis();
                                                if((vitesseDrone+incremente)<5){
                                                    incremente+=0.5;
                                                    if((vitesseDrone+incremente)>=5) incremente = 5 - vitesseDrone;
                                                }
                                                a=rand.nextInt(n);
                                                b=rand.nextInt(m);
                                                dr=new Drone(a,b);
                                                egal = 0;
                                                for(int j=0;j<drone.size();j++){
                                                    egal+=drone.get(j).equals(dr);
                                                }
                                                for(int j=0;j<arbre.size();j++){
                                                    egal+=arbre.get(j).equals(dr);
                                                }
                                                egal+=intrus.equals(dr);
                                                egal+=intrus.equalsG(dr);
                                                egal+=intrus.equalsD(dr);
                                                egal+=intrus.equalsH(dr);
                                                egal+=intrus.equalsB(dr);
                                                while (egal!=0) {
                                                    a=rand.nextInt(n);
                                                    b=rand.nextInt(m);
                                                    dr=new Drone(a,b);
                                                    egal = 0;
                                                    for(int j=0;j<drone.size();j++){
                                                        egal+=drone.get(j).equals(dr);
                                                    }
                                                    for(int j=0;j<arbre.size();j++){
                                                        egal+=arbre.get(j).equals(dr);
                                                    }
                                                    egal+=intrus.equals(dr);
                                                    egal+=intrus.equalsG(dr);
                                                    egal+=intrus.equalsD(dr);
                                                    egal+=intrus.equalsH(dr);
                                                    egal+=intrus.equalsB(dr);
                                                }
                                                drone.add(dr);
                                                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                                                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                                                cercle.toFront();
                                                droneCercle.add(cercle);
                                                javafx.application.Platform.runLater(() -> ajouterCercles(root,cercle));
                                            }
                                        }
                                        break;
                                    case 2:
                                        egal=0;
                                        if(drone.get(i).getX()!=0){
                                            for(int j=0;j<arbre.size();j++){
                                                if(!drone.get(i).mega) egal+=arbre.get(j).equalsG(drone.get(i));
                                                else if(!arbre.get(j).petit) egal+=arbre.get(j).equalsG(drone.get(i));
                                            }
                                            for(int j=0;j<drone.size();j++){
                                                if(drone.get(j).active){
                                                    egal+=drone.get(j).equalsG(drone.get(i));
                                                    if(drone.get(i).mega==drone.get(j).mega && drone.get(j).equalsG(drone.get(i))==1){
                                                        drone.get(i).active = false;
                                                        drone.get(j).active = false;
                                                        droneCercle.get(i).setOpacity(0);
                                                        droneCercle.get(j).setOpacity(0);
                                                        System.out.println(LocalTime.now().format(
                                                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Le drone "+(i+1)+" et le drone "+(j+1)+" se sont percutés"
                                                        );
                                                    }
                                                }
                                            }
                                            if(egal>0){break;}
                                            droneCercle.get(i).setCenterX(droneCercle.get(i).getCenterX() - 10);
                                            systeme.repererIntrus(perceptionDrone,arbre);
                                            try {
                                                Thread.sleep((int)(1000/((vitesseDrone+incremente)*drone.size())));
                                            } catch(InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            drone.get(i).setX(drone.get(i).x-1);
                                            System.out.println(LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
                                            );
                                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                                System.out.println(LocalTime.now().format(
                                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                                );
                                                Platform.exit();
                                                primaryStage.close();t.interrupt();
                                            }
                                            if(System.currentTimeMillis()-crescendo >= 60000){
                                                crescendo = System.currentTimeMillis();
                                                if((vitesseDrone+incremente)<5){
                                                    incremente+=0.5;
                                                    if((vitesseDrone+incremente)>=5) incremente = 5 - vitesseDrone;
                                                }
                                                a=rand.nextInt(n);
                                                b=rand.nextInt(m);
                                                dr=new Drone(a,b);
                                                egal = 0;
                                                for(int j=0;j<arbre.size();j++){
                                                    egal+=arbre.get(j).equals(dr);
                                                }
                                                egal+=intrus.equals(dr);
                                                egal+=intrus.equalsG(dr);
                                                egal+=intrus.equalsD(dr);
                                                egal+=intrus.equalsH(dr);
                                                egal+=intrus.equalsB(dr);
                                                for(int j=0;j<drone.size();j++){
                                                    egal+=drone.get(j).equals(dr);
                                                }
                                                while (egal!=0) {
                                                    a=rand.nextInt(n);
                                                    b=rand.nextInt(m);
                                                    dr=new Drone(a,b);
                                                    egal = 0;
                                                    for(int j=0;j<drone.size();j++){
                                                        egal+=drone.get(j).equals(dr);
                                                    }
                                                    for(int j=0;j<arbre.size();j++){
                                                        egal+=arbre.get(j).equals(dr);
                                                    }
                                                    egal+=intrus.equals(dr);
                                                    egal+=intrus.equalsG(dr);
                                                    egal+=intrus.equalsD(dr);
                                                    egal+=intrus.equalsH(dr);
                                                    egal+=intrus.equalsB(dr);
                                                }
                                                drone.add(dr);
                                                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                                                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                                                cercle.toFront();
                                                droneCercle.add(cercle);
                                                javafx.application.Platform.runLater(() -> ajouterCercles(root,cercle));
                                            }
                                        }
                                        break;
                                    case 3:
                                        egal=0;
                                        if(drone.get(i).getX()!=n-1){
                                            for(int j=0;j<arbre.size();j++){
                                                if(!drone.get(i).mega) egal+=arbre.get(j).equalsD(drone.get(i));
                                                else if(!arbre.get(j).petit) egal+=arbre.get(j).equalsD(drone.get(i));
                                            }
                                            for(int j=0;j<drone.size();j++){
                                                if(drone.get(j).active){
                                                    egal+=drone.get(j).equalsD(drone.get(i));
                                                    if(drone.get(i).mega==drone.get(j).mega && drone.get(j).equalsD(drone.get(i))==1){
                                                        drone.get(i).active = false;
                                                        drone.get(j).active = false;
                                                        droneCercle.get(i).setOpacity(0);
                                                        droneCercle.get(j).setOpacity(0);
                                                        System.out.println(LocalTime.now().format(
                                                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Le drone "+(i+1)+" et le drone "+(j+1)+" se sont percutés"
                                                        );
                                                    }
                                                }
                                            }
                                            if(egal>0){break;}
                                            droneCercle.get(i).setCenterX(droneCercle.get(i).getCenterX() + 10);
                                            systeme.repererIntrus(perceptionDrone,arbre);
                                            try {
                                                Thread.sleep((int)(1000/((vitesseDrone+incremente)*drone.size())));
                                            } catch(InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            drone.get(i).setX(drone.get(i).x+1);
                                            System.out.println(LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
                                            );
                                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                                System.out.println(LocalTime.now().format(
                                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                                );
                                                Platform.exit();
                                                primaryStage.close();t.interrupt();
                                            }
                                            if(System.currentTimeMillis()-crescendo >= 60000){
                                                crescendo = System.currentTimeMillis();
                                                if((vitesseDrone+incremente)<5){
                                                    incremente+=0.5;
                                                    if((vitesseDrone+incremente)>=5) incremente = 5 - vitesseDrone;
                                                }
                                                a=rand.nextInt(n);
                                                b=rand.nextInt(m);
                                                dr=new Drone(a,b);
                                                egal = 0;
                                                for(int j=0;j<drone.size();j++){
                                                    egal+=drone.get(j).equals(dr);
                                                }
                                                for(int j=0;j<arbre.size();j++){
                                                    egal+=arbre.get(j).equals(dr);
                                                }
                                                egal+=intrus.equals(dr);
                                                egal+=intrus.equalsG(dr);
                                                egal+=intrus.equalsD(dr);
                                                egal+=intrus.equalsH(dr);
                                                egal+=intrus.equalsB(dr);
                                                while (egal!=0) {
                                                    a=rand.nextInt(n);
                                                    b=rand.nextInt(m);
                                                    dr=new Drone(a,b);
                                                    egal = 0;
                                                    for(int j=0;j<drone.size();j++){
                                                        egal+=drone.get(j).equals(dr);
                                                    }
                                                    for(int j=0;j<arbre.size();j++){
                                                        egal+=arbre.get(j).equals(dr);
                                                    }
                                                    egal+=intrus.equals(dr);
                                                    egal+=intrus.equalsG(dr);
                                                    egal+=intrus.equalsD(dr);
                                                    egal+=intrus.equalsH(dr);
                                                    egal+=intrus.equalsB(dr);
                                                }
                                                drone.add(dr);
                                                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                                                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                                                cercle.toFront();
                                                droneCercle.add(cercle);
                                                javafx.application.Platform.runLater(() -> ajouterCercles(root,cercle));
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    else{
                        int direct1,direct2;
                        systeme.intrus = intrus;
                        systeme.deblockDrones(arbre);
                        for(int i=0;i<drone.size();i++){
                            if(drone.get(i).active){
                                if(drone.get(i).directDeblock.isEmpty()){
                                    if(drone.get(i).y != systeme.intrus.y){
                                        if(drone.get(i).y > systeme.intrus.y){
                                            direct1 = 0;
                                        }
                                        else{
                                            direct1 = 1;
                                        }
                                    }
                                    else{
                                        direct1=-1;
                                    }
                                    if(drone.get(i).x != systeme.intrus.x){
                                        if(drone.get(i).x > systeme.intrus.x){
                                            direct2 = 2;
                                        }
                                        else{
                                            direct2 = 3;
                                        }
                                    }
                                    else{
                                        direct2 = -1;
                                    }
                                    if(direct1==-1) direct=direct2;
                                    else if(direct2==-1) direct=direct1;
                                    else{
                                        direct = -1;
                                        if(direct1==0){
                                            if(drone.get(i).equalsB(arbre)){
                                                direct = direct2;
                                            }
                                        }
                                        if(direct1==1){
                                            if(drone.get(i).equalsH(arbre)){
                                                direct = direct2;
                                            }
                                        }
                                        if(direct2==2){
                                            if(drone.get(i).equalsD(arbre)){
                                                direct = direct1;
                                            }
                                        }
                                        if(direct2==3){
                                            if(drone.get(i).equalsG(arbre)){
                                                direct = direct1;
                                            }
                                        }
                                        if(direct != direct1 && direct != direct2){
                                            int choix = rand.nextInt(2)+1;
                                            if(choix==1) direct=direct1;
                                            else direct=direct2;
                                        }
                                    }
                                }
                                else{
                                    direct = drone.get(i).directDeblock.remove(0);
                                }
                                switch (direct) {
                                    case 0:
                                        egal=0;
                                        if(drone.get(i).getY()!=0){
                                            for(int j=0;j<arbre.size();j++){
                                                if(!drone.get(i).mega) egal+=arbre.get(j).equalsH(drone.get(i));
                                                else if(!arbre.get(j).petit) egal+=arbre.get(j).equalsH(drone.get(i));
                                            }
                                            for(int j=0;j<drone.size();j++){
                                                if(drone.get(j).active){
                                                    egal+=drone.get(j).equalsH(drone.get(i));
                                                    if(drone.get(i).mega==drone.get(j).mega && drone.get(j).equalsH(drone.get(i))==1){
                                                        drone.get(i).active = false;
                                                        drone.get(j).active = false;
                                                        droneCercle.get(i).setOpacity(0);
                                                        droneCercle.get(j).setOpacity(0);
                                                        System.out.println(LocalTime.now().format(
                                                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Le drone "+(i+1)+" et le drone "+(j+1)+" se sont percutés"
                                                        );
                                                    }
                                                }
                                            }
                                            if(egal>0){break;}
                                            droneCercle.get(i).setCenterY(droneCercle.get(i).getCenterY() - 10);
                                            systeme.repererIntrus(perceptionDrone,arbre);
                                            try {
                                                Thread.sleep((int)(1000/((vitesseDrone+incremente)*drone.size())));
                                            } catch(InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            drone.get(i).setY(drone.get(i).y-1);
                                            System.out.println(LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
                                            );
                                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                                System.out.println(LocalTime.now().format(
                                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                                );
                                                Platform.exit();
                                                primaryStage.close();t.interrupt();
                                            }
                                            if(System.currentTimeMillis()-crescendo >= 60000){
                                                crescendo = System.currentTimeMillis();
                                                if((vitesseDrone+incremente)<5){
                                                    incremente+=0.5;
                                                    if((vitesseDrone+incremente)>=5) incremente = 5 - vitesseDrone;
                                                }
                                                a=rand.nextInt(n);
                                                b=rand.nextInt(m);
                                                dr=new Drone(a,b);
                                                egal = 0;
                                                for(int j=0;j<drone.size();j++){
                                                    egal+=drone.get(j).equals(dr);
                                                }
                                                for(int j=0;j<arbre.size();j++){
                                                    egal+=arbre.get(j).equals(dr);
                                                }
                                                egal+=intrus.equals(dr);
                                                egal+=intrus.equalsG(dr);
                                                egal+=intrus.equalsD(dr);
                                                egal+=intrus.equalsH(dr);
                                                egal+=intrus.equalsB(dr);
                                                while (egal!=0) {
                                                    a=rand.nextInt(n);
                                                    b=rand.nextInt(m);
                                                    dr=new Drone(a,b);
                                                    egal = 0;
                                                    for(int j=0;j<drone.size();j++){
                                                        egal+=drone.get(j).equals(dr);
                                                    }
                                                    for(int j=0;j<arbre.size();j++){
                                                        egal+=arbre.get(j).equals(dr);
                                                    }
                                                    egal+=intrus.equals(dr);
                                                    egal+=intrus.equalsG(dr);
                                                    egal+=intrus.equalsD(dr);
                                                    egal+=intrus.equalsH(dr);
                                                    egal+=intrus.equalsB(dr);
                                                }
                                                drone.add(dr);
                                                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                                                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                                                cercle.toFront();
                                                droneCercle.add(cercle);
                                                javafx.application.Platform.runLater(() -> ajouterCercles(root,cercle));
                                            }
                                        }
                                        break;
                                    case 1:
                                        egal=0;
                                        if(drone.get(i).getY()!=m-1){
                                            for(int j=0;j<arbre.size();j++){
                                                if(!drone.get(i).mega) egal+=arbre.get(j).equalsB(drone.get(i));
                                                else if(!arbre.get(j).petit) egal+=arbre.get(j).equalsB(drone.get(i));
                                            }
                                            for(int j=0;j<drone.size();j++){
                                                if(drone.get(j).active){
                                                    egal+=drone.get(j).equalsB(drone.get(i));
                                                    if(drone.get(i).mega==drone.get(j).mega && drone.get(j).equalsB(drone.get(i))==1){
                                                        drone.get(i).active = false;
                                                        drone.get(j).active = false;
                                                        droneCercle.get(i).setOpacity(0);
                                                        droneCercle.get(j).setOpacity(0);
                                                        System.out.println(LocalTime.now().format(
                                                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Le drone "+(i+1)+" et le drone "+(j+1)+" se sont percutés"
                                                        );
                                                    }
                                                }
                                            }
                                            if(egal>0){break;}
                                            droneCercle.get(i).setCenterY(droneCercle.get(i).getCenterY() + 10);
                                            systeme.repererIntrus(perceptionDrone,arbre);
                                            try {
                                                Thread.sleep((int)(1000/((vitesseDrone+incremente)*drone.size())));
                                            } catch(InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            drone.get(i).setY(drone.get(i).y+1);
                                            System.out.println(LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
                                            );
                                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                                System.out.println(LocalTime.now().format(
                                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                                );
                                                Platform.exit();
                                                primaryStage.close();t.interrupt();
                                            }
                                            if(System.currentTimeMillis()-crescendo >= 60000){
                                                crescendo = System.currentTimeMillis();
                                                if((vitesseDrone+incremente)<5){
                                                    incremente+=0.5;
                                                    if((vitesseDrone+incremente)>=5) incremente = 5 - vitesseDrone;
                                                }
                                                a=rand.nextInt(n);
                                                b=rand.nextInt(m);
                                                dr=new Drone(a,b);
                                                egal = 0;
                                                for(int j=0;j<drone.size();j++){
                                                    egal+=drone.get(j).equals(dr);
                                                }
                                                for(int j=0;j<arbre.size();j++){
                                                    egal+=arbre.get(j).equals(dr);
                                                }
                                                egal+=intrus.equals(dr);
                                                egal+=intrus.equalsG(dr);
                                                egal+=intrus.equalsD(dr);
                                                egal+=intrus.equalsH(dr);
                                                egal+=intrus.equalsB(dr);
                                                while (egal!=0) {
                                                    a=rand.nextInt(n);
                                                    b=rand.nextInt(m);
                                                    dr=new Drone(a,b);
                                                    egal = 0;
                                                    for(int j=0;j<drone.size();j++){
                                                        egal+=drone.get(j).equals(dr);
                                                    }
                                                    for(int j=0;j<arbre.size();j++){
                                                        egal+=arbre.get(j).equals(dr);
                                                    }
                                                    egal+=intrus.equals(dr);
                                                    egal+=intrus.equalsG(dr);
                                                    egal+=intrus.equalsD(dr);
                                                    egal+=intrus.equalsH(dr);
                                                    egal+=intrus.equalsB(dr);
                                                }
                                                drone.add(dr);
                                                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                                                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                                                cercle.toFront();
                                                droneCercle.add(cercle);
                                                javafx.application.Platform.runLater(() -> ajouterCercles(root,cercle));
                                            }
                                        }
                                        break;
                                    case 2:
                                        egal=0;
                                        if(drone.get(i).getX()!=0){
                                            for(int j=0;j<arbre.size();j++){
                                                if(!drone.get(i).mega) egal+=arbre.get(j).equalsG(drone.get(i));
                                                else if(!arbre.get(j).petit) egal+=arbre.get(j).equalsG(drone.get(i));
                                            }
                                            for(int j=0;j<drone.size();j++){
                                                if(drone.get(j).active){
                                                    egal+=drone.get(j).equalsG(drone.get(i));
                                                    if(drone.get(i).mega==drone.get(j).mega && drone.get(j).equalsG(drone.get(i))==1){
                                                        drone.get(i).active = false;
                                                        drone.get(j).active = false;
                                                        droneCercle.get(i).setOpacity(0);
                                                        droneCercle.get(j).setOpacity(0);
                                                        System.out.println(LocalTime.now().format(
                                                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Le drone "+(i+1)+" et le drone "+(j+1)+" se sont percutés"
                                                        );
                                                    }
                                                }
                                            }
                                            if(egal>0){break;}
                                            droneCercle.get(i).setCenterX(droneCercle.get(i).getCenterX() - 10);
                                            systeme.repererIntrus(perceptionDrone,arbre);
                                            try {
                                                Thread.sleep((int)(1000/((vitesseDrone+incremente)*drone.size())));
                                            } catch(InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            drone.get(i).setX(drone.get(i).x-1);
                                            System.out.println(LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
                                            );
                                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                                System.out.println(LocalTime.now().format(
                                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                                );
                                                Platform.exit();
                                                primaryStage.close();t.interrupt();
                                            }
                                            if(System.currentTimeMillis()-crescendo >= 60000){
                                                crescendo = System.currentTimeMillis();
                                                if((vitesseDrone+incremente)<5){
                                                    incremente+=0.5;
                                                    if((vitesseDrone+incremente)>=5) incremente = 5 - vitesseDrone;
                                                }
                                                a=rand.nextInt(n);
                                                b=rand.nextInt(m);
                                                dr=new Drone(a,b);
                                                egal = 0;
                                                for(int j=0;j<drone.size();j++){
                                                    egal+=drone.get(j).equals(dr);
                                                }
                                                for(int j=0;j<arbre.size();j++){
                                                    egal+=arbre.get(j).equals(dr);
                                                }
                                                egal+=intrus.equals(dr);
                                                egal+=intrus.equalsG(dr);
                                                egal+=intrus.equalsD(dr);
                                                egal+=intrus.equalsH(dr);
                                                egal+=intrus.equalsB(dr);
                                                while (egal!=0) {
                                                    a=rand.nextInt(n);
                                                    b=rand.nextInt(m);
                                                    dr=new Drone(a,b);
                                                    egal = 0;
                                                    for(int j=0;j<drone.size();j++){
                                                        egal+=drone.get(j).equals(dr);
                                                    }
                                                    for(int j=0;j<arbre.size();j++){
                                                        egal+=arbre.get(j).equals(dr);
                                                    }
                                                    egal+=intrus.equals(dr);
                                                    egal+=intrus.equalsG(dr);
                                                    egal+=intrus.equalsD(dr);
                                                    egal+=intrus.equalsH(dr);
                                                    egal+=intrus.equalsB(dr);
                                                }
                                                drone.add(dr);
                                                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                                                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                                                cercle.toFront();
                                                droneCercle.add(cercle);
                                                javafx.application.Platform.runLater(() -> ajouterCercles(root,cercle));
                                            }
                                        }
                                        break;
                                    case 3:
                                        egal=0;
                                        if(drone.get(i).getX()!=n-1){
                                            for(int j=0;j<arbre.size();j++){
                                                if(!drone.get(i).mega) egal+=arbre.get(j).equalsD(drone.get(i));
                                                else if(!arbre.get(j).petit) egal+=arbre.get(j).equalsD(drone.get(i));
                                            }
                                            for(int j=0;j<drone.size();j++){
                                                if(drone.get(j).active){
                                                    egal+=drone.get(j).equalsD(drone.get(i));
                                                    if(drone.get(i).mega==drone.get(j).mega && drone.get(j).equalsD(drone.get(i))==1){
                                                        drone.get(i).active = false;
                                                        drone.get(j).active = false;
                                                        droneCercle.get(i).setOpacity(0);
                                                        droneCercle.get(j).setOpacity(0);
                                                        System.out.println(LocalTime.now().format(
                                                            DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Le drone "+(i+1)+" et le drone "+(j+1)+" se sont percutés"
                                                        );
                                                    }
                                                }
                                            }
                                            if(egal>0){break;}
                                            droneCercle.get(i).setCenterX(droneCercle.get(i).getCenterX() + 10);
                                            systeme.repererIntrus(perceptionDrone,arbre);
                                            try {
                                                Thread.sleep((int)(1000/((vitesseDrone+incremente)*drone.size())));
                                            } catch(InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            drone.get(i).setX(drone.get(i).x+1);
                                            System.out.println(LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position du drone "+(i+1)+" : "+(drone.get(i).getX()+1)+" "+(drone.get(i).getY()+1)
                                            );
                                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                                System.out.println(LocalTime.now().format(
                                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                                );
                                                Platform.exit();
                                                primaryStage.close();t.interrupt();
                                            }
                                            if(System.currentTimeMillis()-crescendo >= 60000){
                                                crescendo = System.currentTimeMillis();
                                                if((vitesseDrone+incremente)<5){
                                                    incremente+=0.5;
                                                    if((vitesseDrone+incremente)>=5) incremente = 5 - vitesseDrone;
                                                }
                                                a=rand.nextInt(n);
                                                b=rand.nextInt(m);
                                                dr=new Drone(a,b);
                                                egal = 0;
                                                for(int j=0;j<drone.size();j++){
                                                    egal+=drone.get(j).equals(dr);
                                                }
                                                for(int j=0;j<arbre.size();j++){
                                                    egal+=arbre.get(j).equals(dr);
                                                }
                                                egal+=intrus.equals(dr);
                                                egal+=intrus.equalsG(dr);
                                                egal+=intrus.equalsD(dr);
                                                egal+=intrus.equalsH(dr);
                                                egal+=intrus.equalsB(dr);
                                                while (egal!=0) {
                                                    a=rand.nextInt(n);
                                                    b=rand.nextInt(m);
                                                    dr=new Drone(a,b);
                                                    egal = 0;
                                                    for(int j=0;j<drone.size();j++){
                                                        egal+=drone.get(j).equals(dr);
                                                    }
                                                    for(int j=0;j<arbre.size();j++){
                                                        egal+=arbre.get(j).equals(dr);
                                                    }
                                                    egal+=intrus.equals(dr);
                                                    egal+=intrus.equalsG(dr);
                                                    egal+=intrus.equalsD(dr);
                                                    egal+=intrus.equalsH(dr);
                                                    egal+=intrus.equalsB(dr);
                                                }
                                                drone.add(dr);
                                                if(dr.mega) {cercle = new Circle(a*10+5,b*10+5,7.5);cercle.setFill(Color.DARKRED);} // Définir la couleur de remplissage du cercle
                                                else {cercle = new Circle(a*10+5,b*10+5,5);cercle.setFill(Color.RED);}
                                                cercle.toFront();
                                                droneCercle.add(cercle);
                                                javafx.application.Platform.runLater(() -> ajouterCercles(root,cercle));
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    Thread.yield();
                }
                return null;
            }
        };
        t = new Thread(task);
        t.start();
    }

    /**
     * Méthode pour gérer le déplacement de l'intrus.
     * @param code : touche du clavier appuyé
     */
    public void handleKeyPress(KeyCode code) {
        int egal;
        switch (code) {
            case UP:
                egal=0;
                if(intrus.getY()!=0){
                    for(int i=0;i<arbre.size();i++){
                        egal+=arbre.get(i).equalsH(intrus);
                    }
                    if(egal>0){break;}
                    intrusCercle.setCenterY(intrusCercle.getCenterY() - 10);
                    systeme.repererIntrus(perceptionDrone,arbre);
                    try {
                        Thread.sleep((int)(100/vitesseIntrus));
                    } catch(InterruptedException e) {
                        System.out.println("got interrupted!");
                    }
                    intrus.setY(intrus.y-1);
                    System.out.println(LocalTime.now().format(
                        DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position de l'intrus : "+(intrus.getX()+1)+" "+(intrus.getY()+1)
                    );
                    for(int i=0;i<zone.size();i++){
                        if((zone.get(i).zoneParticuliere(intrus)==1)&&(!zone.get(i).trouve)){
                            nbTrouve++;
                            zone.get(i).trouve = true;
                            if(nbTrouve==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbTrouve+" caisse trouvée"
                                );
                            }
                            else{
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbTrouve+" caisses trouvées"
                                );
                            }
                        }
                        if((zone.get(i).equals(intrus)==1)&&(!zone.get(i).recup)){
                            nbRecup++;
                            zone.get(i).recup = true;
                            if(nbRecup==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbRecup+" caisse récupérée"
                                );
                            }
                            else{
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbRecup+" caisses récupérées"
                                );
                            }
                            if(nbRecup==nbZones){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Votre mission est terminée, sortez d'ici"
                                );
                            }
                            zoneRectangle.get(i).setOpacity(0);
                        }
                    }
                    if(nbRecup==nbZones){
                        for(int i=0;i<sortie.size();i++){
                            if(sortie.get(i).equals(intrus)==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Bravo, vous avez réussi parfaitement votre mission !"
                                );
                                t.interrupt();primaryStage.close();
                            }
                        }
                    }
                    for(int i=0;i<drone.size();i++){
                        if(drone.get(i).active){
                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                );
                                t.interrupt();primaryStage.close();
                            }
                        }
                    }
                    for(int i=0;i<arbre.size();i++){
                        if(arbre.get(i).equals(intrus)==1){
                            if(systeme.trouve){
                                systeme.trouve = false;
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> L'intrus a disparu !"
                                );
                            }
                        }
                    }
                    reperage();
                }
                break;
            case DOWN:
                egal=0;
                if(intrus.getY()!=m-1){
                    for(int i=0;i<arbre.size();i++){
                        egal+=arbre.get(i).equalsB(intrus);
                    }
                    if(egal>0){break;}
                    intrusCercle.setCenterY(intrusCercle.getCenterY() + 10);
                    systeme.repererIntrus(perceptionDrone,arbre);
                    try {
                        Thread.sleep((int)(100/vitesseIntrus));
                    } catch(InterruptedException e) {
                        System.out.println("got interrupted!");
                    }
                    intrus.setY(intrus.y+1);
                    System.out.println(LocalTime.now().format(
                        DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position de l'intrus : "+(intrus.getX()+1)+" "+(intrus.getY()+1)
                    );
                    for(int i=0;i<zone.size();i++){
                        if((zone.get(i).zoneParticuliere(intrus)==1)&&(!zone.get(i).trouve)){
                            nbTrouve++;
                            zone.get(i).trouve = true;
                            if(nbTrouve==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbTrouve+" caisse trouvée"
                                );
                            }
                            else{
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbTrouve+" caisses trouvées"
                                );
                            }
                        }
                        if((zone.get(i).equals(intrus)==1)&&(!zone.get(i).recup)){
                            nbRecup++;
                            zone.get(i).recup = true;
                            if(nbRecup==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbRecup+" caisse récupérée"
                                );
                            }
                            else{
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbRecup+" caisses récupérées"
                                );
                            }
                            if(nbRecup==nbZones){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Votre mission est terminée, sortez d'ici"
                                );
                            }
                            zoneRectangle.get(i).setOpacity(0);
                        }
                    }
                    if(nbRecup==nbZones){
                        for(int i=0;i<sortie.size();i++){
                            if(sortie.get(i).equals(intrus)==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Bravo, vous avez réussi parfaitement votre mission !"
                                );
                                t.interrupt();primaryStage.close();
                            }
                        }
                    }
                    for(int i=0;i<drone.size();i++){
                        if(drone.get(i).active){
                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                );
                                t.interrupt();primaryStage.close();
                            }
                        }
                    }
                    for(int i=0;i<arbre.size();i++){
                        if(arbre.get(i).equals(intrus)==1){
                            if(systeme.trouve){
                                systeme.trouve = false;
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> L'intrus a disparu !"
                                );
                            }
                        }
                    }
                    reperage();
                }
                break;
            case LEFT:
                egal=0;
                if(intrus.getX()!=0){
                    for(int i=0;i<arbre.size();i++){
                        egal+=arbre.get(i).equalsG(intrus);
                    }
                    if(egal>0){break;}
                    intrusCercle.setCenterX(intrusCercle.getCenterX() - 10);
                    systeme.repererIntrus(perceptionDrone,arbre);
                    try {
                        Thread.sleep((int)(100/vitesseIntrus));
                    } catch(InterruptedException e) {
                        System.out.println("got interrupted!");
                    }
                    intrus.setX(intrus.x-1);
                    System.out.println(LocalTime.now().format(
                        DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position de l'intrus : "+(intrus.getX()+1)+" "+(intrus.getY()+1)
                    );
                    for(int i=0;i<zone.size();i++){
                        if((zone.get(i).zoneParticuliere(intrus)==1)&&(!zone.get(i).trouve)){
                            nbTrouve++;
                            zone.get(i).trouve = true;
                            if(nbTrouve==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbTrouve+" caisse trouvée"
                                );
                            }
                            else{
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbTrouve+" caisses trouvées"
                                );
                            }
                        }
                        if((zone.get(i).equals(intrus)==1)&&(!zone.get(i).recup)){
                            nbRecup++;
                            zone.get(i).recup = true;
                            if(nbRecup==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbRecup+" caisse récupérée"
                                );
                            }
                            else{
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbRecup+" caisses récupérées"
                                );
                            }
                            if(nbRecup==nbZones){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Votre mission est terminée, sortez d'ici"
                                );
                            }
                            zoneRectangle.get(i).setOpacity(0);
                        }
                    }
                    if(nbRecup==nbZones){
                        for(int i=0;i<sortie.size();i++){
                            if(sortie.get(i).equals(intrus)==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Bravo, vous avez réussi parfaitement votre mission !"
                                );
                                t.interrupt();primaryStage.close();
                            }
                        }
                    }
                    for(int i=0;i<drone.size();i++){
                        if(drone.get(i).active){
                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                );
                                t.interrupt();primaryStage.close();
                            }
                        }
                    }
                    for(int i=0;i<arbre.size();i++){
                        if(arbre.get(i).equals(intrus)==1){
                            if(systeme.trouve){
                                systeme.trouve = false;
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> L'intrus a disparu !"
                                );
                            }
                        }
                    }
                    reperage();
                }
                break;
            case RIGHT:
                egal=0;
                if(intrus.getX()!=n-1){
                    for(int i=0;i<arbre.size();i++){
                        egal+=arbre.get(i).equalsD(intrus);
                    }
                    if(egal>0){break;}
                    intrusCercle.setCenterX(intrusCercle.getCenterX() + 10);
                    systeme.repererIntrus(perceptionDrone,arbre);
                    try {
                        Thread.sleep((int)(100/vitesseIntrus));
                    } catch(InterruptedException e) {
                        System.out.println("got interrupted!");
                    }
                    intrus.setX(intrus.x+1);
                    System.out.println(LocalTime.now().format(
                        DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Position de l'intrus : "+(intrus.getX()+1)+" "+(intrus.getY()+1)
                    );
                    for(int i=0;i<zone.size();i++){
                        if((zone.get(i).zoneParticuliere(intrus)==1)&&(!zone.get(i).trouve)){
                            nbTrouve++;
                            zone.get(i).trouve = true;
                            if(nbTrouve==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbTrouve+" caisse trouvée"
                                );
                            }
                            else{
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbTrouve+" caisses trouvées"
                                );
                            }
                        }
                        if((zone.get(i).equals(intrus)==1)&&(!zone.get(i).recup)){
                            nbRecup++;
                            zone.get(i).recup = true;
                            if(nbRecup==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbRecup+" caisse récupérée"
                                );
                            }
                            else{
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> "+nbRecup+" caisses récupérées"
                                );
                            }
                            if(nbRecup==nbZones){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Votre mission est terminée, sortez d'ici"
                                );
                            }
                            zoneRectangle.get(i).setOpacity(0);
                        }
                    }
                    if(nbRecup==nbZones){
                        for(int i=0;i<sortie.size();i++){
                            if(sortie.get(i).equals(intrus)==1){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Bravo, vous avez réussi parfaitement votre mission !"
                                );
                                t.interrupt();primaryStage.close();
                            }
                        }
                    }
                    for(int i=0;i<drone.size();i++){
                        if(drone.get(i).active){
                            if(drone.get(i).equals(intrus)==1 || (systeme.trouve && (System.currentTimeMillis()-systeme.startRepere)>tempsRepere)){
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Vous vous êtes fait capturer, vous avez échoué votre mission"
                                );
                                t.interrupt();primaryStage.close();
                            }
                        }
                    }
                    for(int i=0;i<arbre.size();i++){
                        if(arbre.get(i).equals(intrus)==1){
                            if(systeme.trouve){
                                systeme.trouve = false;
                                System.out.println(LocalTime.now().format(
                                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> L'intrus a disparu !"
                                );
                            }
                        }
                    }
                    reperage();
                }
                break;
            default:
                // Ne rien faire pour les autres touches
        }
    }

    /**
     * Méthode qui permet d'éclairer tout ce qui se trouve dans le champ de vision de l'intrus.
     */
    public void reperage(){
        int a=intrus.x;
        int b=intrus.y;
        Point p;
        Point p2;
        float flou = (float) 1/10;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                p = new Point(i, j);
                for(int k=0;k<vide.size();k++){
                    if(vide.get(k).equals(p)==1){
                        videRectangle.get(k).setOpacity(videRectangle.get(k).getOpacity()-flou);
                    }
                }
                for(int k=0;k<sortie.size();k++){
                    if(sortie.get(k).equals(p)==1){
                        sortieRectangle.get(k).setOpacity(sortieRectangle.get(k).getOpacity()-flou);
                    }
                }
                for(int k=0;k<arbre.size();k++){
                    if(arbre.get(k).equals(p)==1){
                        arbreRectangle.get(k).setOpacity(arbreRectangle.get(k).getOpacity()-flou);
                    }
                }
                for(int k=0;k<zone.size();k++){
                    if(zone.get(k).equals(p)==1 && !zone.get(k).recup){
                        zoneRectangle.get(k).setOpacity(zoneRectangle.get(k).getOpacity()-flou);
                    }
                }
            }
        }
        for(int i=-perceptionIntrus;i<=perceptionIntrus;i++){
            p = new Point(a+i,b);
            for(int k=0;k<vide.size();k++){
                if(vide.get(k).equals(p)==1){
                    videRectangle.get(k).setOpacity(1);
                }
            }
            for(int k=0;k<sortie.size();k++){
                if(sortie.get(k).equals(p)==1){
                    sortieRectangle.get(k).setOpacity(1);
                }
            }
            for(int k=0;k<arbre.size();k++){
                if(arbre.get(k).equals(p)==1){
                    arbreRectangle.get(k).setOpacity(1);
                }
            }
            for(int k=0;k<zone.size();k++){
                if(zone.get(k).equals(p)==1 && !zone.get(k).recup){
                    zoneRectangle.get(k).setOpacity(1);
                }
            }
        }
        for(int j=1;j<=perceptionIntrus;j++){
            for(int i=j-perceptionIntrus;i<=perceptionIntrus-j;i++){
                p = new Point(a+i,b+j);
                p2 = new Point(a+i,b-j);
                for(int k=0;k<vide.size();k++){
                    if(vide.get(k).equals(p)==1){
                        videRectangle.get(k).setOpacity(1);
                    }
                    if(vide.get(k).equals(p2)==1){
                        videRectangle.get(k).setOpacity(1);
                    }
                }
                for(int k=0;k<sortie.size();k++){
                    if(sortie.get(k).equals(p)==1){
                        sortieRectangle.get(k).setOpacity(1);
                    }
                    if(sortie.get(k).equals(p2)==1){
                        sortieRectangle.get(k).setOpacity(1);
                    }
                }
                for(int k=0;k<arbre.size();k++){
                    if(arbre.get(k).equals(p)==1){
                        arbreRectangle.get(k).setOpacity(1);
                    }
                    if(arbre.get(k).equals(p2)==1){
                        arbreRectangle.get(k).setOpacity(1);
                    }
                }
                for(int k=0;k<zone.size();k++){
                    if(zone.get(k).equals(p)==1 && !zone.get(k).recup){
                        zoneRectangle.get(k).setOpacity(1);
                    }
                    if(zone.get(k).equals(p2)==1 && !zone.get(k).recup){
                        zoneRectangle.get(k).setOpacity(1);
                    }
                }
            }
        }
    }

    /**
     * Méthode pour arrêter le thread.
     */
    public void stopThread() {
        if (t != null && t.isAlive()) {
            t.interrupt();
        }
    }

    /**
     * Méthode pour ajouter des drones visuellement dans le jeu
     * @param root : Contient tous les éléments graphiques (rectangle, cercles, ...)
     * @param cercle : Le cercle qu'on veut ajouter visuellement dans le jeu
     */
    public void ajouterCercles(Pane root, Circle cercle) {
        root.getChildren().add(cercle);
    }
    
    /**
     * Méthode pour démarrer l'application.
     * @param args : des arguments (on n'en a pas besoin)
     */
    public static void main(String[] args) {
        launch(args);
    }
}