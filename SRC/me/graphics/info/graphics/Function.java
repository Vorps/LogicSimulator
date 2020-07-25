package me.graphics.info.graphics;

/**
 * Interface f(x) des courbes
 */
@FunctionalInterface
public interface Function {

    /**
     * Definit les nouveaux points de la courbe à l'instant x
     * @param x Temps courrant de simulation
     * @return Point
     */
    Point function(double x);

}
