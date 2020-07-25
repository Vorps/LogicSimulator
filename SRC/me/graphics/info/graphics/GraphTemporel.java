package me.graphics.info.graphics;


import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Implémentation d'un graphe en mode temporel avec défilement de l'axe X en fontion du temps
 */
public class GraphTemporel extends Graph {

    private Point intervalX;
    private double x;


    public GraphTemporel(Axes axeX, Axes axeY, String title, Curve[] curves, double te, Point intervalX){
        super(axeX, axeY, title, curves, te);
        this.intervalX = intervalX;
        this.x = this.intervalX.getX();
    }

    public void setAxeYMax(int max){
        this.axesY.setMax(max);
    }

    public void reset(){
        for(Curve curve : this.curves)
            curve.clear();
        this.x = this.intervalX.getX();
    }



    @Override
    public void refreshGraph(){
        if(this.x < this.intervalX.getY()){
            for(Curve curve : this.curves){
                if(curve.getFunction() != null) curve.addPoint(this.x);
            }
            this.x+= super.te;
        }
    }

    @Override
    public void function(){
        boolean stateX = false;
        if(this.x < this.intervalX.getY()){
            if (!curves.isEmpty()) {
                Point point = curves.get(0).getPoints().get(curves.get(0).getPoints().size() - 1);
                for (Curve curve : curves) {
                    if (curve.getPoints().size() > 1 && curve.getPoints().get(curve.getPoints().size() - 2).getX() <= this.axesX.getInterval().getY()) {
                        if (point.getX() < this.axesX.getInterval().getX()) this.axesX.setMin(point.getX());
                        if (point.getX() > this.axesX.getInterval().getY()) this.axesX.setMax(point.getX());
                        stateX = true;
                    }
                    if (stateX || curve.getPoints().size() > 1 && curve.getPoints().get(curve.getPoints().size() - 2).getY() <= this.axesY.getInterval().getY() && curve.getPoints().get(curve.getPoints().size() - 2).getY() >= this.axesY.getInterval().getX()) {
                        if (point.getY() < this.axesY.getInterval().getX()) this.axesY.setMin(point.getY());
                        if (point.getY() > this.axesY.getInterval().getY()) this.axesY.setMax(point.getY());
                    }
                }
                if (curves.get(0).getPoints().size() > 2 && point.getX() >= this.axesX.getInterval().getY() && curves.get(0).getPoints().get(curves.get(0).getPoints().size() - 2).getX() <= this.axesX.getInterval().getY())
                    this.axesX.setMin(this.axesX.getInterval().getX() + te);
            }
        }

    }

    @Override
    public void paintInfo(Graphics graphics){
        graphics.setColor(Color.red);
        graphics.drawString(1/te+" FPS", this.getSize().width - (marginRight+subMarginRight+50), marginTop+subMarginTop-10);
        graphics.drawString("TE : "+te, this.getSize().width - (marginRight+subMarginRight+120), marginTop+subMarginTop-10);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }


    @Override
    public void setTimeMax(int time) {
        this.intervalX =  new Point(this.intervalX.getX(), time);
    }

    @Override
    public void start() {
        this.reset();
        super.start();
    }




}
