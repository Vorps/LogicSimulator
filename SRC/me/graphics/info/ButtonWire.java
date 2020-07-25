package me.graphics.info;

import javax.swing.*;
import java.awt.*;

/**
 * Gestion des bouton pour le controle des signaux
 */
public class ButtonWire extends JButton {

    private String name;

    /**
     * @param name Nom du signale
     */
    public ButtonWire(String name){
        this.name = name;
        this.setFont(new Font("Monaco", Font.PLAIN, 20));
        this.setPreferredSize(new Dimension(150,40));
        this.setToolTipText("Click droit pour paramétrer le signal");
    }
    public ButtonWire(){
        this("");
    }

    /**
     * Changement d'état du bouton lors d'un changement d'état du signal
     * @param state
     */
    public void setSignal(boolean state){
        this.setBackground(state ? Color.GREEN : Color.RED);
        this.setLabel(this.name+" "+(state ? "TRUE" : "FALSE"));

    }
}
