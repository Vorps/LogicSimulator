package me.graphics.info.graphics;

/**
    Objet qui contient des couples de valeurs
 */
public class Point {

    private double x;
    private double y;

    /**
     * Couble de valeur double
     * @param x double
     * @param y double
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Fonction de copy de points
     * @param point
     */
    public Point(Point point){
        this(point.x, point.y);
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

}
