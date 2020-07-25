package me.graphics.info;

import me.logic.info.Wire;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Affichage des boutons pour le changement d'état et pour la configuration des signaux en entrée d'une porte logique
 */
public class WireJPanel extends JPanel {

    /**
     * @see WireInputConf
     * Liste les différents signaux d'entrées de la porte logique
     */
    public ArrayList<WireInputConf> wiresInputConf;

    /**
     * JPanel permettant de controler et de parametrer l'ensemble des signaux entrée de la porte logique
     * @param wireInput Ensemble des signaux en entrées de la porte logique
     */
    public WireJPanel(Wire[] wireInput){
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20,15)); // Choix d'un layout horizontal permettant d'afficher à la suite les boutons de contrôles des signaux
        this.wiresInputConf = new ArrayList<>();
        this.setBackground(COLOR.BACKGROUND_2.getColor());
        for(Wire wire : wireInput) this.wiresInputConf.add(new WireInputConf(wire));
        for(WireInputConf wireInputConf : wiresInputConf) this.add(wireInputConf.buttonWire); //Ajout des boutons dans la jPanel
    }

    /**
     * Activation de l'ensemble des signaux en entrée de la porte logique
     */
    public void run(){
        for(WireInputConf wireInputConf : wiresInputConf) wireInputConf.run();
    }

    /**
     * Stop l'ensemble des signaux en entrée de la porte logique
     */
    public void stop(){
        for(WireInputConf wireInputConf : wiresInputConf) wireInputConf.stop();

    }
}
