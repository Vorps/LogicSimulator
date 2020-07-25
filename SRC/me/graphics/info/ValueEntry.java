package me.graphics.info;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Gestion générique des entrées de valeurs numériques pour la configuration de la simulation
 */
public class ValueEntry extends JTextField implements KeyListener {

    private int value;

    public int getValue(){
        return this.value;
    }

    public void valid(){
        this.setBackground(Color.GRAY);
    }
    public ValueEntry(int value){
        super(value+"");
        this.value = value;
        this.setPreferredSize(new Dimension(70,30));
        this.setFont(new Font("Monaco", Font.PLAIN, 18));
        this.setBackground(Color.GRAY);
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {


    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        try{
            int value = Integer.parseInt(ValueEntry.this.getText());
            ValueEntry.this.setBackground(Color.WHITE);
            this.value = value;
        } catch (Exception err){
            ValueEntry.this.setBackground(Color.RED);
        }
    }
}
