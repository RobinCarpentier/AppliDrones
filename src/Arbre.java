import java.util.Random;

/**
 * C'est une classe dérivant de la classe Point qui représente un arbre.
 * On a un booléen en plus : "petit" pour savoir si l'arbre est petit ou non (les méga drones peuvent passés au dessus des petits arbres).
 */
public class Arbre extends Point{
    Random rand = new Random();
    /**Booléen qui vaut true si l'arbre est petit, false sinon */
    public boolean petit;
    /**
     * Constructeur de la classe Arbre
     * @param x : La coordonnée x de l'arbre
     * @param y : La coordonnée y de l'arbre
     */
    public Arbre(int x, int y){
        super(x,y);
        int t = rand.nextInt(2);
        if(t==0){
            petit = true;
        }
        else{
            petit = false;
        }
    }
}