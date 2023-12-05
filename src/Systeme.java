import java.util.ArrayList;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * C'est la classe pour générer le comportement de l'ensemble des drones.
 */
public class Systeme{
    /**Liste des drones */
    public ArrayList<Drone> drone;
    /**Booléen qui vaut true si l'intrus a été trouvé par l'un des drones, false sinon */
    public boolean trouve;
    /**Emplacement de l'intrus dans la map */
    public Point intrus;
    /**L'heure à laquelle on a trouvé l'intrus */
    public long startRepere;
    /**
     * Constructeur de la classe Système
     * @param drone : liste des drones
     * @param intrus : point où se trouve l'intrus
     */
    public Systeme(ArrayList<Drone> drone, Point intrus){
        this.drone = drone;
        trouve = false;
        this.intrus = intrus;
    }
    /**
     * Méthode pour savoir si l'intrus est repéré ou s'il y a disparu des champs de vision des drones.
     * @param perception : valeur du champ de vision du drone
     * @param arbre : liste des arbres (l'intrus peut se cacher derrière un arbre)
     */
    public void repererIntrus(int perception, ArrayList<Arbre> arbre){
        if(!trouve){
            for(int i=0;i<drone.size();i++){
                if(drone.get(i).visionDrone(intrus,perception,arbre) && drone.get(i).active){
                    startRepere = System.currentTimeMillis();
                    trouve = true;
                    break;
                }
            }
            if(trouve){
                System.out.println(LocalTime.now().format(
                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> Intrus repéré !"
                );
            }
        }
        else{
            int nbNonRepere=0;
            for(int i=0;i<drone.size();i++){
                if(!drone.get(i).active) nbNonRepere++;
                else if(!drone.get(i).visionDrone(intrus,perception,arbre)) nbNonRepere++;
            }
            if(nbNonRepere==drone.size()){
                trouve = false;
                System.out.println(LocalTime.now().format(
                    DateTimeFormatter.ofPattern("hh:mm:ss"))+" -> L'intrus a disparu !"
                );
            }
        }
    }
    /**
     * Méthode pour débloquer les drones lors de la poursuite de l'intrus
     * @param arbre : liste des arbres
     */
    public void deblockDrones(ArrayList<Arbre> arbre){
        if(trouve){
            for(int i=0;i<drone.size();i++){
                if(drone.get(i).active){
                    drone.get(i).deblock(arbre,intrus);
                }
            }
        }
    }
}