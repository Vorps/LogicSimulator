package me.graphics.info.graphics;

import java.awt.*;

/**
 * Classe pour la définition des axes du graphique
 */
public class Axes {

    /**
     * Nom de l'axe
     */
    private String title;
    /**
     * @see Point
     * Affichage de l'axe dans cette intervale X correspond à la valeur minimal et Y la valeur maximal
     */
    private Point interval;

    /**
     * Couleur de l'axes
     */
    private Color color;

    /**
     * @param title Titre de l'axe
     * @param interval Interval de l'axe
     * @param color Couleur de l'axe
     */
    public Axes(String title, Point interval, Color color){
        this.title = title;
        this.interval = interval;
        this.color = color;
    }

    public String getTitle(){
        return this.title;
    }

    public void setMin(double min){
        this.interval = new Point(min,this.interval.getY());
    }

    public void setMax(double max){
        this.interval = new Point(this.interval.getX(),max);
    }

    public Color getColor() {
         return this.color;
    }

    public Point getInterval(){
        return this.interval;
    }
}
