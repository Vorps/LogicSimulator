package me.graphics.info.graphics;

/**
 * Interface de controle du graphics et de la trace de la simulation à partir des bouttons de contrôles
 * Permet de communiquer des actions entre JPanel
 */
public interface Controle {
    /**
     * Controle de start
     */
    void start();
    /**
     * Controle de reprise après pause
     */
    void resume();
    /**
     * Controle de pause
     */
    void pause();
    /**
     * Controle de stop
     */
    void stop();

    /**
     * Controle de temps de simulation
     */
    void setTimeMax(int time);
}
