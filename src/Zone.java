/**
 * C'est une classe dérivant de la classe Point qui représente une zone (dont l'intrus doit chercher).
 * On a deux booléens en plus : "trouve" pour savoir si on a trouvé la zone et "recup" pour savoir si on a récupéré ce qui se trouve dans la zone.
 */
public class Zone extends Point{
    /**Booléen qui vaut true si cette zone a été trouvé, false sinon */
    public boolean trouve;
    /**Booléen qui vaut true si on a récupéré ce qui se trouve dans cette zone, false sinon */
    public boolean recup;
    /**
     * Constructeur de la classe Zone
     * @param x : La coordonnée x de la zone
     * @param y : La coordonnée y de la zone
     */
    public Zone(int x, int y){
        super(x,y);
        trouve = false;
        recup = false;
    }
}