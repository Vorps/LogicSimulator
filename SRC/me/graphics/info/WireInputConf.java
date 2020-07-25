package me.graphics.info;

import me.logic.info.Wire;

import javax.swing.*;
import java.awt.*;

/**
 * Classe de configuration des signaux
 * Permettant de changer l'état des signaux pendant la simulation ou bien de paramètrer le changement de cette état pendant la simulation
 */
public class WireInputConf {

    /**
     * @see Wire
     */
    public Wire wire;

    /**
     * @see ButtonWire
     */
    public ButtonWire buttonWire;


    private boolean state;
    public boolean signal1;
    public boolean signal2 = true;
    public int time1;
    public int time2;
    public boolean active;
    private boolean inSimulation;
    private WireDialog wireDialog;

    public WireInputConf(Wire wire){
        this.wire = wire;
        this.buttonWire = new ButtonWire(wire.getName());
        this.wireDialog = new WireDialog(this);

        this.buttonWire.addActionListener((e) -> {
            setSignal(!this.state);
        });

        this.buttonWire.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getButton()==3){
                    wireDialog.setLocation(buttonWire.getLocationOnScreen().x-50, (buttonWire.getLocationOnScreen().y + 73));
                    wireDialog.setVisible(true);
                }
            }});
        buttonWire.setToolTipText("Click droit pour paramétrer le signal");
        this.setSignal(false);
    }

    /**
     * Changement d'état du signal
     * @param state
     */
    public void setSignal(boolean state){
        this.state = state;
        if(this.inSimulation) wire.setSignal(this.state); //Changement d'état directement en simulation
        else this.buttonWire.setSignal(state);
    }

    /**
     * Activé au début de la simulation
     */
    public void run(){
        this.inSimulation = true;
        this.buttonWire.setLabel("Running");
        this.buttonWire.setBackground(Color.GRAY);
        if(this.active){ //Utilisation du paramètrage du signal
            new Thread(() -> {
                while(inSimulation){
                    this.setSignal(signal1);
                    wire.setSignal(signal1);
                    try {
                        Thread.sleep(time1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.setSignal(signal2);
                    wire.setSignal(signal2);
                    try {
                        Thread.sleep(time2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            this.setSignal(this.state);
            wire.setSignal(this.state);
        }
    }

    public void stop(){
        this.inSimulation = false;
        this.setSignal(this.state);
    }



}
