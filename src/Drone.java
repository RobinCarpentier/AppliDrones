import java.util.ArrayList;
import java.util.Random;

/**
 * C'est une classe dérivant de la classe Point qui représente un drone.
 * On a deux booléen en plus : "mega" pour savoir si c'est un méga drone et "active" pour savoir si le drone est activé ou non.
 */
public class Drone extends Point{
    Random rand = new Random();
    /**Booléen qui vaut true si c'est un méga drone, false sinon */
    public boolean mega;
    /**Booléen qui vaut true si le drone est en marche, false si le drone est cassé (par exemple, s'il percute un drone) */
    public boolean active;
    /**Liste de directions servant à débloquer un drone */
    public ArrayList<Integer> directDeblock;
    /**
     * Constructeur de la classe Drone
     * @param x : La coordonnée x du drone
     * @param y : La coordonnée y du drone
     */
    public Drone(int x, int y){
        super(x,y);
        int t = rand.nextInt(2);
        if(t==1){
            mega = true;
        }
        else{
            mega = false;
        }
        active = true;
        directDeblock = new ArrayList<Integer>();
    }
    /**
     * Méthode pour savoir si un arbre se trouve en haut du drone
     * @param arbre : Liste des arbres
     * @return : On retourne true s'il y a bien un arbre en haut du drone, false sinon
     */
    public boolean equalsH(ArrayList<Arbre> arbre){
        int egal = 0;
        for(int i = 0;i<arbre.size();i++){
            if(!(this.mega && arbre.get(i).petit)){
                egal += this.equalsB(arbre.get(i));
            }
        }
        if(egal == 0) return false;
        else return true;
    }
    /**
     * Méthode pour savoir si un arbre se trouve en bas du drone
     * @param arbre : Liste des arbres
     * @return : On retourne true s'il y a bien un arbre en bas du drone, false sinon
     */
    public boolean equalsB(ArrayList<Arbre> arbre){
        int egal = 0;
        for(int i = 0;i<arbre.size();i++){
            if(!(this.mega && arbre.get(i).petit)){
                egal += this.equalsH(arbre.get(i));
            }
        }
        if(egal == 0) return false;
        else return true;
    }
    /**
     * Méthode pour savoir si un arbre se trouve à gauche du drone
     * @param arbre : Liste des arbres
     * @return : On retourne true s'il y a bien un arbre à gauche du drone, false sinon
     */
    public boolean equalsG(ArrayList<Arbre> arbre){
        int egal = 0;
        for(int i = 0;i<arbre.size();i++){
            if(!(this.mega && arbre.get(i).petit)){
                egal += this.equalsD(arbre.get(i));
            }
        }
        if(egal == 0) return false;
        else return true;
    }
    /**
     * Méthode pour savoir si un arbre se trouve à droite du drone
     * @param arbre : Liste des arbres
     * @return : On retourne true s'il y a bien un arbre à droite du drone, false sinon
     */
    public boolean equalsD(ArrayList<Arbre> arbre){
        int egal = 0;
        for(int i = 0;i<arbre.size();i++){
            if(!(this.mega && arbre.get(i).petit)){
                egal += this.equalsG(arbre.get(i));
            }
        }
        if(egal == 0) return false;
        else return true;
    }
    /**
     * Méthode pour débloquer le drone en le déplaçant en haut puis à gauche
     * @param arbre : Liste des arbres
     * @param virtual : Position du drone pour le calcul de la trajectoire
     */
    public void hautGauche(ArrayList<Arbre> arbre, Drone virtual){
        while(virtual.equalsG(arbre) && !virtual.equalsH(arbre)){
            directDeblock.add(0);
            virtual.y--;
        }
        if(virtual.equalsH(arbre)){
            droiteHaut(arbre,virtual);
            hautGauche(arbre,virtual);
        }
        else{
            directDeblock.add(2);
            virtual.x--;
        }
    }
    /**
     * Méthode pour débloquer le drone en le déplaçant en haut puis à gauche
     * @param arbre : Liste des arbres
     * @param virtual : Position du drone pour le calcul de la trajectoire
     */
    public void hautDroite(ArrayList<Arbre> arbre, Drone virtual){
        while(virtual.equalsD(arbre) && !virtual.equalsH(arbre)){
            directDeblock.add(0);
            virtual.y--;
        }
        if(virtual.equalsH(arbre)){
            gaucheHaut(arbre,virtual);
            hautDroite(arbre,virtual);
        }
        else{
            directDeblock.add(3);
            virtual.x++;
        }
    }
    /**
     * Méthode pour débloquer le drone en le déplaçant en haut puis à gauche
     * @param arbre : Liste des arbres
     * @param virtual : Position du drone pour le calcul de la trajectoire
     */
    public void basGauche(ArrayList<Arbre> arbre, Drone virtual){
        while(virtual.equalsG(arbre) && !virtual.equalsB(arbre)){
            directDeblock.add(1);
            virtual.y++;
        }
        if(virtual.equalsB(arbre)){
            droiteBas(arbre,virtual);
            basGauche(arbre,virtual);
        }
        else{
            directDeblock.add(2);
            virtual.x--;
        }
    }
    /**
     * Méthode pour débloquer le drone en le déplaçant en haut puis à gauche
     * @param arbre : Liste des arbres
     * @param virtual : Position du drone pour le calcul de la trajectoire
     */
    public void basDroite(ArrayList<Arbre> arbre, Drone virtual){
        while(virtual.equalsD(arbre) && !virtual.equalsB(arbre)){
            directDeblock.add(1);
            virtual.y++;
        }
        if(virtual.equalsH(arbre)){
            gaucheBas(arbre,virtual);
            basDroite(arbre,virtual);
        }
        else{
            directDeblock.add(3);
            virtual.x++;
        }
    }
    /**
     * Méthode pour débloquer le drone en le déplaçant en haut puis à gauche
     * @param arbre : Liste des arbres
     * @param virtual : Position du drone pour le calcul de la trajectoire
     */
    public void gaucheHaut(ArrayList<Arbre> arbre, Drone virtual){
        while(virtual.equalsH(arbre) && !virtual.equalsG(arbre)){
            directDeblock.add(2);
            virtual.x--;
        }
        if(virtual.equalsG(arbre)){
            basGauche(arbre,virtual);
            gaucheHaut(arbre,virtual);
        }
        else{
            directDeblock.add(0);
            virtual.y--;
        }
    }
    /**
     * Méthode pour débloquer le drone en le déplaçant en haut puis à gauche
     * @param arbre : Liste des arbres
     * @param virtual : Position du drone pour le calcul de la trajectoire
     */
    public void gaucheBas(ArrayList<Arbre> arbre, Drone virtual){
        while(virtual.equalsB(arbre) && !virtual.equalsG(arbre)){
            directDeblock.add(2);
            virtual.x--;
        }
        if(virtual.equalsD(arbre)){
            hautGauche(arbre,virtual);
            gaucheBas(arbre,virtual);
        }
        else{
            directDeblock.add(1);
            virtual.y++;
        }
    }
    /**
     * Méthode pour débloquer le drone en le déplaçant en haut puis à gauche
     * @param arbre : Liste des arbres
     * @param virtual : Position du drone pour le calcul de la trajectoire
     */
    public void droiteHaut(ArrayList<Arbre> arbre, Drone virtual){
        while(virtual.equalsH(arbre) && !virtual.equalsD(arbre)){
            directDeblock.add(3);
            virtual.x++;
        }
        if(virtual.equalsD(arbre)){
            basDroite(arbre,virtual);
            droiteHaut(arbre,virtual);
        }
        else{
            directDeblock.add(0);
            virtual.y--;
        }
    }
    /**
     * Méthode pour débloquer le drone en le déplaçant en haut puis à gauche
     * @param arbre : Liste des arbres
     * @param virtual : Position du drone pour le calcul de la trajectoire
     */
    public void droiteBas(ArrayList<Arbre> arbre, Drone virtual){
        while(virtual.equalsB(arbre) && !virtual.equalsD(arbre)){
            directDeblock.add(3);
            virtual.x++;
        }
        if(virtual.equalsD(arbre)){
            hautDroite(arbre,virtual);
            droiteBas(arbre,virtual);
        }
        else{
            directDeblock.add(1);
            virtual.y++;
        }
    }
    /**
     * Méthode permettant de mettre les directions nécessaires pour débloquer le drone
     * @param arbre : Liste des arbres
     * @param intrus : Emplacement de l'intrus dans la map
     */
    public void deblock(ArrayList<Arbre> arbre, Point intrus){
        if(directDeblock.isEmpty()){
            Random rand = new Random();
            Drone virtual = new Drone(x, y);
            virtual.mega = this.mega;
            //Cas du drone au nord de l'intrus
            if(this.x==intrus.x && this.y<intrus.y && !(this.equalsG(arbre) && this.equalsD(arbre))){
                int direct;
                if(this.equalsG(arbre)) direct = 3;
                else if(this.equalsD(arbre)) direct = 2;
                else{
                    direct = rand.nextInt(2);
                    if(direct==0) direct = 2;
                    else direct = 3;
                }
                if(virtual.equalsB(arbre)){
                    //Cas de la direction à gauche
                    if(direct==2){
                        while(virtual.equalsB(arbre) && !virtual.equalsG(arbre)){
                            directDeblock.add(2);
                            virtual.x--;
                        }
                        if(!virtual.equalsB(arbre)){
                            directDeblock.add(1);
                            virtual.y++;
                        }
                        else{
                            while(virtual.equalsG(arbre) && !virtual.equalsH(arbre)){
                                directDeblock.add(0);
                                virtual.y--;
                            }
                            if(!virtual.equalsG(arbre)){
                                directDeblock.add(2);
                                virtual.x--;
                            }
                            else{
                                while(virtual.equalsH(arbre) && !virtual.equalsD(arbre)){
                                    directDeblock.add(3);
                                    virtual.x++;
                                }
                                if(!virtual.equalsH(arbre)){
                                    directDeblock.add(0);
                                    virtual.y--;
                                }
                                else{
                                    while(virtual.equalsD(arbre) && !virtual.equalsB(arbre)){
                                        directDeblock.add(1);
                                        virtual.y++;
                                    }
                                    if(!virtual.equalsD(arbre)){
                                        directDeblock.add(3);
                                        virtual.x++;
                                    }
                                    else{
                                        while(virtual.equalsB(arbre) && !virtual.equalsG(arbre)){
                                            directDeblock.add(2);
                                            virtual.x--;
                                        }
                                        if(!virtual.equalsB(arbre)){
                                            directDeblock.add(1);
                                            virtual.y++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //Cas de la direction à droite
                    else{
                        while(virtual.equalsB(arbre) && !virtual.equalsD(arbre)){
                            directDeblock.add(3);
                            virtual.x++;
                        }
                        if(!virtual.equalsB(arbre)){
                            directDeblock.add(1);
                            virtual.y++;
                        }
                        else{
                            while(virtual.equalsD(arbre) && !virtual.equalsH(arbre)){
                                directDeblock.add(0);
                                virtual.y--;
                            }
                            if(!virtual.equalsD(arbre)){
                                directDeblock.add(3);
                                virtual.x++;
                            }
                            else{
                                while(virtual.equalsH(arbre) && !virtual.equalsG(arbre)){
                                    directDeblock.add(2);
                                    virtual.x--;
                                }
                                if(!virtual.equalsH(arbre)){
                                    directDeblock.add(0);
                                    virtual.y--;
                                }
                                else{
                                    while(virtual.equalsG(arbre) && !virtual.equalsB(arbre)){
                                        directDeblock.add(1);
                                        virtual.y++;
                                    }
                                    if(!virtual.equalsG(arbre)){
                                        directDeblock.add(2);
                                        virtual.x--;
                                    }
                                    else{
                                        while(virtual.equalsB(arbre) && !virtual.equalsD(arbre)){
                                            directDeblock.add(3);
                                            virtual.x++;
                                        }
                                        if(!virtual.equalsB(arbre)){
                                            directDeblock.add(1);
                                            virtual.y++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Cas du drone au sud de l'intrus
            if(this.x==intrus.x && this.y>intrus.y && !(this.equalsG(arbre) && this.equalsD(arbre))){
                int direct;
                if(this.equalsG(arbre)) direct = 3;
                else if(this.equalsD(arbre)) direct = 2;
                else{
                    direct = rand.nextInt(2);
                    if(direct==0) direct = 2;
                    else direct = 3;
                }
                if(virtual.equalsH(arbre)){
                    //Cas de la direction à gauche
                    if(direct==2){
                        while(virtual.equalsH(arbre) && !virtual.equalsG(arbre)){
                            directDeblock.add(2);
                            virtual.x--;
                        }
                        if(!virtual.equalsH(arbre)){
                            directDeblock.add(0);
                            virtual.y--;
                        }
                        else{
                            while(virtual.equalsG(arbre) && !virtual.equalsB(arbre)){
                                directDeblock.add(1);
                                virtual.y++;
                            }
                            if(!virtual.equalsG(arbre)){
                                directDeblock.add(2);
                                virtual.x--;
                            }
                            else{
                                while(virtual.equalsB(arbre) && !virtual.equalsD(arbre)){
                                    directDeblock.add(3);
                                    virtual.x++;
                                }
                                if(!virtual.equalsB(arbre)){
                                    directDeblock.add(1);
                                    virtual.y++;
                                }
                                else{
                                    while(virtual.equalsD(arbre) && !virtual.equalsH(arbre)){
                                        directDeblock.add(0);
                                        virtual.y--;
                                    }
                                    if(!virtual.equalsD(arbre)){
                                        directDeblock.add(3);
                                        virtual.x++;
                                    }
                                    else{
                                        while(virtual.equalsH(arbre) && !virtual.equalsG(arbre)){
                                            directDeblock.add(2);
                                            virtual.x--;
                                        }
                                        if(!virtual.equalsH(arbre)){
                                            directDeblock.add(0);
                                            virtual.y--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //Cas de la direction à droite
                    else{
                        while(virtual.equalsH(arbre) && !virtual.equalsD(arbre)){
                            directDeblock.add(3);
                            virtual.x++;
                        }
                        if(!virtual.equalsH(arbre)){
                            directDeblock.add(0);
                            virtual.y--;
                        }
                        else{
                            while(virtual.equalsD(arbre) && !virtual.equalsB(arbre)){
                                directDeblock.add(1);
                                virtual.y++;
                            }
                            if(!virtual.equalsD(arbre)){
                                directDeblock.add(3);
                                virtual.x++;
                            }
                            else{
                                while(virtual.equalsB(arbre) && !virtual.equalsG(arbre)){
                                    directDeblock.add(2);
                                    virtual.x--;
                                }
                                if(!virtual.equalsB(arbre)){
                                    directDeblock.add(1);
                                    virtual.y++;
                                }
                                else{
                                    while(virtual.equalsG(arbre) && !virtual.equalsH(arbre)){
                                        directDeblock.add(0);
                                        virtual.y--;
                                    }
                                    if(!virtual.equalsG(arbre)){
                                        directDeblock.add(2);
                                        virtual.x--;
                                    }
                                    else{
                                        while(virtual.equalsH(arbre) && !virtual.equalsD(arbre)){
                                            directDeblock.add(3);
                                            virtual.x++;
                                        }
                                        if(!virtual.equalsH(arbre)){
                                            directDeblock.add(0);
                                            virtual.y--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Cas du drone à l'ouest de l'intrus
            if(this.x<intrus.x && this.y==intrus.y && !(this.equalsH(arbre) && this.equalsB(arbre))){
                int direct;
                if(this.equalsH(arbre)) direct = 1;
                else if(this.equalsB(arbre)) direct = 0;
                else{
                    direct = rand.nextInt(2);
                }
                if(virtual.equalsD(arbre)){
                    //Cas de la direction en haut
                    if(direct==0){
                        while(virtual.equalsD(arbre) && !virtual.equalsH(arbre)){
                            directDeblock.add(0);
                            virtual.y--;
                        }
                        if(!virtual.equalsD(arbre)){
                            directDeblock.add(3);
                            virtual.x++;
                        }
                        else{
                            while(virtual.equalsH(arbre) && !virtual.equalsG(arbre)){
                                directDeblock.add(2);
                                virtual.x--;
                            }
                            if(!virtual.equalsH(arbre)){
                                directDeblock.add(0);
                                virtual.y--;
                            }
                            else{
                                while(virtual.equalsG(arbre) && !virtual.equalsB(arbre)){
                                    directDeblock.add(1);
                                    virtual.y++;
                                }
                                if(!virtual.equalsG(arbre)){
                                    directDeblock.add(2);
                                    virtual.x--;
                                }
                                else{
                                    while(virtual.equalsB(arbre) && !virtual.equalsD(arbre)){
                                        directDeblock.add(3);
                                        virtual.x++;
                                    }
                                    if(!virtual.equalsB(arbre)){
                                        directDeblock.add(1);
                                        virtual.y++;
                                    }
                                    else{
                                        while(virtual.equalsD(arbre) && !virtual.equalsH(arbre)){
                                            directDeblock.add(0);
                                            virtual.y--;
                                        }
                                        if(!virtual.equalsD(arbre)){
                                            directDeblock.add(3);
                                            virtual.x++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //Cas de la direction en bas
                    else{
                        while(virtual.equalsD(arbre) && !virtual.equalsB(arbre)){
                            directDeblock.add(1);
                            virtual.y++;
                        }
                        if(!virtual.equalsD(arbre)){
                            directDeblock.add(3);
                            virtual.x++;
                        }
                        else{
                            while(virtual.equalsB(arbre) && !virtual.equalsG(arbre)){
                                directDeblock.add(2);
                                virtual.x--;
                            }
                            if(!virtual.equalsB(arbre)){
                                directDeblock.add(1);
                                virtual.y++;
                            }
                            else{
                                while(virtual.equalsG(arbre) && !virtual.equalsH(arbre)){
                                    directDeblock.add(0);
                                    virtual.y--;
                                }
                                if(!virtual.equalsG(arbre)){
                                    directDeblock.add(2);
                                    virtual.x--;
                                }
                                else{
                                    while(virtual.equalsH(arbre) && !virtual.equalsD(arbre)){
                                        directDeblock.add(3);
                                        virtual.x++;
                                    }
                                    if(!virtual.equalsH(arbre)){
                                        directDeblock.add(0);
                                        virtual.y--;
                                    }
                                    else{
                                        while(virtual.equalsD(arbre) && !virtual.equalsB(arbre)){
                                            directDeblock.add(1);
                                            virtual.y++;
                                        }
                                        if(!virtual.equalsD(arbre)){
                                            directDeblock.add(3);
                                            virtual.x++;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
            //Cas du drone à l'est de l'intrus
            if(this.x>intrus.x && this.y==intrus.y && !(this.equalsH(arbre) && this.equalsB(arbre))){
                int direct;
                if(this.equalsH(arbre)) direct = 1;
                else if(this.equalsB(arbre)) direct = 0;
                else{
                    direct = rand.nextInt(2);
                }
                if(virtual.equalsG(arbre)){
                    //Cas de la direction en haut
                    if(direct==0){
                        while(virtual.equalsG(arbre) && !virtual.equalsH(arbre)){
                            directDeblock.add(0);
                            virtual.y--;
                        }
                        if(!virtual.equalsG(arbre)){
                            directDeblock.add(2);
                            virtual.x--;
                        }
                        else{
                            while(virtual.equalsH(arbre) && !virtual.equalsD(arbre)){
                                directDeblock.add(3);
                                virtual.x++;
                            }
                            if(!virtual.equalsH(arbre)){
                                directDeblock.add(0);
                                virtual.y--;
                            }
                            else{
                                while(virtual.equalsD(arbre) && !virtual.equalsB(arbre)){
                                    directDeblock.add(1);
                                    virtual.y++;
                                }
                                if(!virtual.equalsD(arbre)){
                                    directDeblock.add(3);
                                    virtual.x++;
                                }
                                else{
                                    while(virtual.equalsB(arbre) && !virtual.equalsG(arbre)){
                                        directDeblock.add(2);
                                        virtual.x--;
                                    }
                                    if(!virtual.equalsB(arbre)){
                                        directDeblock.add(1);
                                        virtual.y++;
                                    }
                                    else{
                                        while(virtual.equalsG(arbre) && !virtual.equalsH(arbre)){
                                            directDeblock.add(0);
                                            virtual.y--;
                                        }
                                        if(!virtual.equalsG(arbre)){
                                            directDeblock.add(2);
                                            virtual.x--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //Cas de la direction en bas
                    else{
                        while(virtual.equalsG(arbre) && !virtual.equalsB(arbre)){
                            directDeblock.add(1);
                            virtual.y++;
                        }
                        if(!virtual.equalsG(arbre)){
                            directDeblock.add(2);
                            virtual.x--;
                        }
                        else{
                            while(virtual.equalsB(arbre) && !virtual.equalsD(arbre)){
                                directDeblock.add(3);
                                virtual.x++;
                            }
                            if(!virtual.equalsB(arbre)){
                                directDeblock.add(1);
                                virtual.y++;
                            }
                            else{
                                while(virtual.equalsD(arbre) && !virtual.equalsH(arbre)){
                                    directDeblock.add(0);
                                    virtual.y--;
                                }
                                if(!virtual.equalsD(arbre)){
                                    directDeblock.add(3);
                                    virtual.x++;
                                }
                                else{
                                    while(virtual.equalsH(arbre) && !virtual.equalsG(arbre)){
                                        directDeblock.add(2);
                                        virtual.x--;
                                    }
                                    if(!virtual.equalsH(arbre)){
                                        directDeblock.add(0);
                                        virtual.y--;
                                    }
                                    else{
                                        while(virtual.equalsG(arbre) && !virtual.equalsB(arbre)){
                                            directDeblock.add(1);
                                            virtual.y++;
                                        }
                                        if(!virtual.equalsG(arbre)){
                                            directDeblock.add(2);
                                            virtual.x--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Cas du drone au nord-ouest de l'intrus
            if(x<=intrus.x && y<intrus.y){
                int direct;
                if(this.equalsH(arbre)) direct = 2;
                else if(this.equalsG(arbre)) direct = 0;
                else{
                    direct = rand.nextInt(2);
                    if(direct==1) direct = 2;
                }
                if(virtual.equalsB(arbre) && virtual.equalsD(arbre)){
                    //Cas de la direction en haut
                    if(direct==0){
                        hautDroite(arbre, virtual);
                        droiteBas(arbre, virtual);
                    }
                    //Cas de la direction à gauche
                    else{
                        gaucheBas(arbre, virtual);
                        basDroite(arbre, virtual);
                    }
                }
            }
            //Cas du drone au nord-est de l'intrus
            if(x>intrus.x && y<=intrus.y){
                int direct;
                if(this.equalsH(arbre)) direct = 3;
                else if(this.equalsD(arbre)) direct = 0;
                else{
                    direct = rand.nextInt(2);
                    if(direct==1) direct = 3;
                }
                if(virtual.equalsB(arbre) && virtual.equalsG(arbre)){
                    //Cas de la direction en haut
                    if(direct==0){
                        hautGauche(arbre, virtual);
                        gaucheBas(arbre, virtual);
                    }
                    //Cas de la direction à droite
                    else{
                        droiteBas(arbre, virtual);
                        basGauche(arbre, virtual);
                    }
                }
            }
            //Cas du drone au sud-ouest de l'intrus
            if(x<intrus.x && y>=intrus.y){
                int direct;
                if(this.equalsB(arbre)) direct = 2;
                else if(this.equalsG(arbre)) direct = 1;
                else{
                    direct = rand.nextInt(2);
                    if(direct==0) direct = 1;
                    else direct = 2;
                }
                if(virtual.equalsH(arbre) && virtual.equalsD(arbre)){
                    //Cas de la direction en bas
                    if(direct==1){
                        basDroite(arbre, virtual);
                        droiteHaut(arbre, virtual);
                    }
                    //Cas de la direction à gauche
                    else{
                        gaucheHaut(arbre, virtual);
                        hautDroite(arbre, virtual);
                    }
                }
            }
            //Cas du drone au sud-est de l'intrus
            if(x>=intrus.x && y>intrus.y){
                int direct;
                if(this.equalsB(arbre)) direct = 3;
                else if(this.equalsD(arbre)) direct = 1;
                else{
                    direct = rand.nextInt(2);
                    if(direct==0) direct = 1;
                    else direct = 3;
                }
                if(virtual.equalsH(arbre) && virtual.equalsG(arbre)){
                    //Cas de la direction en bas
                    if(direct==1){
                        basGauche(arbre, virtual);
                        gaucheHaut(arbre, virtual);
                    }
                    //Cas de la direction à droite
                    else{
                        droiteHaut(arbre, virtual);
                        hautGauche(arbre, virtual);
                    }
                }
            }
        }
    }
}