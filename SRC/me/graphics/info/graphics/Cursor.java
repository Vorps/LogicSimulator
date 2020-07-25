package me.graphics.info.graphics;


/**
    Classe pour stocker les informations du curseur
 */
public class Cursor extends Point{

    /**
     * Etat du curseur, persistant ou non sur l'écran
     */
    private boolean state;
    /**
     * @see Curve
     */
    private Curve curve;

    /**
     *
     * @param point Position sur le graphique du curseur
     * @param curve Courbe associé
     * @param state Etat du curseur
     */
    public Cursor(Point point, Curve curve, boolean state){
        super(point);
        this.curve = curve;
        this.state = state;
    }

    /**
     *
     * @param point Position sur le graphique du curseur
     * @param state Etat du curseur
     */
    public Cursor(Point point, boolean state){
        this(point, null, state);
    }

    public boolean isState() {
        return this.state;
    }

    public Curve getCurve() {
        return curve;
    }
}
