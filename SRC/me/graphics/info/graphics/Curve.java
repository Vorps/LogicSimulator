package me.graphics.info.graphics;


import java.awt.*;
import java.util.ArrayList;

/**
 * Classe pour la gestion des courbes du graphique
 */
public class Curve {

    /**
     * Non de la courbe
     * Correspond dans l'application au non des signaux observé
     */
    private String label;
    /**
     * Couleur des lignes entre les points de la courbe
     */
    private Color colorCurve;
    /**
     * Couleur des points de la courbe
     */
    private Color colorPoint;
    /**
     * @see Point
     * Ensemble des points de la courbe
     */
    private ArrayList<Point> points;
    /**
     * @see Function
     * Fonction f(x) de la courbe pour l'ajoute de point en fonction de x
     */
    private Function function;

    /**
     *
     * @param label Nom de la courbe
     * @param colorCurve Couleur des lignes de la courbe
     * @param colorPoint Couleur des points de la courbe
     * @param function Fonction f(x) de la courbe
     */
    public Curve(String label, Color colorCurve, Color colorPoint, Function function){
        this.label = label;
        this.colorCurve = colorCurve;
        this.colorPoint = colorPoint;
        this.function = function;
        this.points = new ArrayList<>();
    }

    public Function getFunction(){
        return this.function;
    }
    public String getLabel() {
        return label;
    }

    public Color getColorCurve() {
        return colorCurve;
    }

    public Color getColorPoint() {
        return colorPoint;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * Ajout de nouveau Point à la courbe
     * @param x
     */
    public void addPoint(double x){
        this.points.add(this.function.function(x));
    }

    /**
     * Efface les points de la courbe
     */
    public void clear(){
        this.points.clear();
    }


}
