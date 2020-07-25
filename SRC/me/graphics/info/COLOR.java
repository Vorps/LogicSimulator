package me.graphics.info;

import java.awt.*;

/**
 * Enumeration des couleurs utilisées pour l'application
 */
public enum COLOR {

    BACKGROUND_1(new Color(60,63,65)),
    BACKGROUND_2(new Color(43,43,43)),
    BACKGROUND_3(new Color(13,41,62)),
    FOREGROUND_1(new Color(196, 146, 97));

    private Color color;

    COLOR(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }
}
