import java.util.ArrayList;

/**
 * C'est une classe pour représenter un point dans la scène ayant à deux coordonnées x et y.
 */
public class Point {
    /**Coordonnées x du point*/
    public int x;
    /**Coordonnées y du point*/
    public int y;
    /**
     * Constructeur de la classe Point
     * @param x : La coordonnée x du point
     * @param y : La coordonnée y du point
     */
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }
    /**
     * Getter du paramètre x
     * @return : on retourne la coordonnée x du point
     */
    public int getX(){
        return x;
    }
    /**
     * Getter du paramètre y
     * @return : on retourne la coordonnée y du point
     */
    public int getY(){
        return y;
    }
    /**
     * Setter du paramètre x
     * @param x : La coordonnée x du point
     */
    public void setX(int x){
        this.x=x;
    }
    /**
     * Setter du paramètre y
     * @param y : La coordonnée y du point
     */
    public void setY(int y){
        this.y=y;
    }
    /**
     * Méthode pour savoir si le point actuel est égal au point donné en paramètre
     * @param p : Point dont on veut savoir s'il est égal au point actuel
     * @return : on retourne 1 si les deux points sont égaux, 0 sinon
     */
    public int equals(Point p){
        if((this.x == p.x)&&(this.y == p.y)){
            return 1;
        }
        return 0;
    }
    /**
     * Méthode pour savoir si le point donné en paramètre est à gauche du point actuel
     * @param p : Point dont on vaut savoir s'il est à une case à gauche de ce point
     * @return : on retourne 1 si le point donné en paramètre est à gauche du point actuel, 0 sinon
     */
    public int equalsG(Point p){
        if((this.x == p.x-1)&&(this.y == p.y)){
            return 1;
        }
        return 0;
    }
    /**
     * Méthode pour savoir si le point donné en paramètre est à droite du point actuel
     * @param p : Point dont on vaut savoir s'il est à une case à droite de ce point
     * @return : on retourne 1 si le point donné en paramètre est à droite du point actuel, 0 sinon
     */
    public int equalsD(Point p){
        if((this.x == p.x+1)&&(this.y == p.y)){
            return 1;
        }
        return 0;
    }
    /**
     * Méthode pour savoir si le point donné en paramètre est en haut du point actuel
     * @param p : Point dont on vaut savoir s'il est à une case en haut de ce point
     * @return : on retourne 1 si le point donné en paramètre est en haut du point actuel, 0 sinon
     */
    public int equalsH(Point p){
        if((this.x == p.x)&&(this.y == p.y-1)){
            return 1;
        }
        return 0;
    }
    /**
     * Méthode pour savoir si le point donné en paramètre est en bas du point actuel
     * @param p : Point dont on vaut savoir s'il est à une case en bas de ce point
     * @return : on retourne 1 si le point donné en paramètre est en bas du point actuel, 0 sinon
     */
    public int equalsB(Point p){
        if((this.x == p.x)&&(this.y == p.y+1)){
            return 1;
        }
        return 0;
    }
    /**
     * Méthode pour savoir si on a trouvé la zone particulière
     * @param p : point où se trouve l'intrus
     * @return : on retourne 1 si on a bien trouvé la zone particulière, 0 sinon
     */
    public int zoneParticuliere(Point p){
        //2 cases à gauche
        if((this.x == p.x-2)&&(this.y == p.y)){
            return 1;
        }
        //2 cases à droite
        if((this.x == p.x+2)&&(this.y == p.y)){
            return 1;
        }
        //2 cases en haut
        if((this.x == p.x)&&(this.y == p.y-2)){
            return 1;
        }
        //2 cases en bas
        if((this.x == p.x)&&(this.y == p.y+2)){
            return 1;
        }
        //En haut à gauche
        if((this.x == p.x-1)&&(this.y == p.y-1)){
            return 1;
        }
        //En haut à droite
        if((this.x == p.x+1)&&(this.y == p.y-1)){
            return 1;
        }
        //En bas à gauche
        if((this.x == p.x-1)&&(this.y == p.y+1)){
            return 1;
        }
        //En bas à droite
        if((this.x == p.x+1)&&(this.y == p.y+1)){
            return 1;
        }
        //Les autres cases
        if(this.equalsG(p)==1){return 1;}
        if(this.equalsD(p)==1){return 1;}
        if(this.equalsH(p)==1){return 1;}
        if(this.equalsB(p)==1){return 1;}
        if(this.equals(p)==1){return 1;}
        return 0;
    }
    /**
     * Méthode pour savoir si l'intrus se trouve dans le champ de vision du drone.
     * @param intrus : point où se trouve l'intrus
     * @param perception : valeur du champ de vision du drone
     * @param arbre : liste des arbres de la scène
     * @return : on retourne true si l'intrus est dans le champ de vision du drone, 0 sinon
     */
    public boolean visionDrone(Point intrus, int perception, ArrayList<Arbre> arbre){
        Point p;
        boolean cache = false;
        for(int i=0;i<arbre.size();i++){
            /*
            if(arbre.get(i).equals(intrus)==1){
                cache = true;
                break;
            }*/
            if(this.x==intrus.x && this.y>intrus.y){
                /*
                if(arbre.get(i).equalsB(intrus)==1){
                    cache = true;
                    break;
                }*/
                for(int j=intrus.y+1;j<this.y;j++){
                    p = new Point(this.x,j);
                    if(arbre.get(i).equals(p)==1){
                        cache = true;
                        break;
                    }
                }
            }
            if(this.x==intrus.x && this.y<intrus.y){
                /*
                if(arbre.get(i).equalsH(intrus)==1){
                    cache = true;
                    break;
                }*/
                for(int j=this.y+1;j<intrus.y;j++){
                    p = new Point(this.x,j);
                    if(arbre.get(i).equals(p)==1){
                        cache = true;
                        break;
                    }
                }
            }
            if(this.x>intrus.x && this.y==intrus.y){
                /*
                if(arbre.get(i).equalsD(intrus)==1){
                    cache = true;
                    break;
                }*/
                for(int j=intrus.x+1;j<this.x;j++){
                    p = new Point(j,this.y);
                    if(arbre.get(i).equals(p)==1){
                        cache = true;
                        break;
                    }
                }
            }
            if(this.x<intrus.x && this.y==intrus.y){
                /*
                if(arbre.get(i).equalsG(intrus)==1){
                    cache = true;
                    break;
                }*/
                for(int j=this.x+1;j<intrus.x;j++){
                    p = new Point(j,this.y);
                    if(arbre.get(i).equals(p)==1){
                        cache = true;
                        break;
                    }
                }
            }
        }
        if(!cache){
            for(int i=0;i<=perception;i++){
                for(int j=0;j<=perception-i;j++){
                    p=new Point(this.x+i,this.y+j);
                    if(p.equals(intrus)==1){
                        return true;
                    }
                    if(i!=0){
                        p=new Point(this.x-i,this.y+j);
                        if(p.equals(intrus)==1){
                            return true;
                        }
                    }
                    if(j!=0){
                        p=new Point(this.x+i,this.y-j);
                        if(p.equals(intrus)==1){
                            return true;
                        }
                    }
                    if(i!=0 && j!=0){
                        p=new Point(this.x-i,this.y-j);
                        if(p.equals(intrus)==1){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}