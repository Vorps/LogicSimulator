package me.graphics.info;


import me.graphics.info.graphics.*;
import me.graphics.info.graphics.Point;
import me.logic.info.Wire;

import javax.swing.JFrame;
import java.awt.*;
import java.util.Random;

/**
 * Positionnement des éléments de l'application dans une fenètre
 */
public class Window extends JFrame {

    public Window(String title) {
       super(title);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dimension.width,dimension.height);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setBackground(COLOR.BACKGROUND_1.getColor());
        this.setLayout(new GridBagLayout());
        GraphTemporel graph = new GraphTemporel(new Axes("Temps(Secondes)", new Point(0, 20), Color.orange), new Axes("Niveaux", new Point(-1, 0), Color.CYAN), "Chronogramme", new Curve[] {}, 0.01, new Point(0, 100));
        TraceJPanel traceJPanel = new TraceJPanel();
     ListComponentsJPanel listComponentsJPanel = new ListComponentsJPanel();

        ComponentJPanel jPanelComp = new ComponentJPanel();
        jPanelComp.setControle(graph, traceJPanel);
        listComponentsJPanel.setGateSelect((gate) -> {
         jPanelComp.setGate(gate);
         Curve[] curves = new Curve[gate.getSignals().wiresOutput.length+gate.getSignals().wiresInput.length];
         int i = 0;

         for(Wire wire : gate.getSignals().wiresInput){
             Color color =  new Color(new Random().nextInt()*255+255*255);
             int finalI = i+1;
          curves[i++] = new Curve(wire.getName(), color, color, (x) -> new Point(x,  wire.getSignal() ? (finalI*2 -1) : finalI*2-2));
         }
         for(Wire wire : gate.getSignals().wiresOutput){
                Color color =  new Color(new Random().nextInt()*255+255*255);
                int finalI = i+1;
             curves[i++] = new Curve(wire.getName(),  color, color, (x) ->  new Point(x,  wire.getSignal() ? (finalI*2 -1) : finalI*2-2));
            }
            graph.setCurves(curves);
            graph.setAxeYMax(curves.length*2);
            traceJPanel.start();
        });

     listComponentsJPanel.jList.setSelectedIndex(0);

     this.add(graph, new GridBagConstraints(0,0,3,1,0.5,0.6, GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
     this.add(listComponentsJPanel, new GridBagConstraints(0,1,1,1,0.3,0.2,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
     this.add(jPanelComp, new GridBagConstraints(1,1,1,1,0.4,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
     this.add(traceJPanel, new GridBagConstraints(2,1,1,1,0.3,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
    }
}
