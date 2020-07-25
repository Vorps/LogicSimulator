package me.graphics.info.graphics;


import me.graphics.info.COLOR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe mère d'un graph qui permet la gestion :
 *  - Zoom
 *  - Curseurs
 *  - Affichage de courbes
 *  - Défilement des échelles sur l'axe X et Y
 */
public abstract class Graph extends JPanel implements MouseMotionListener,MouseListener,MouseWheelListener,KeyListener, Runnable, Controle{

    /**
     * @see Axes Axe des X
     */
    protected Axes axesX;
    /**
     * @see Axes Axe des Y
     */
    protected Axes axesY;


    private Color colorBackGround;
    private Color colorAxe;
    private Color colorGrid;
    private Color colorAxes0;

    /**
     * Periode d'update en milisecondes des courbe et des fonctionnalités du graphique
     */
    protected double te;

    private String title;
    /**
     * @see Curve Ensemble des courbes à afficher
     */
    protected ArrayList<Curve> curves;


    protected int marginLeft;
    protected int marginRight;
    protected int marginTop;
    protected int marginBottom;
    private int size;
    protected int subMarginLeft;
    protected int subMarginRight;
    protected int subMarginTop;
    protected int subMarginBottom;

    /**
     * @see Cursor
     * Curseur sur l'axe Y à un temps donnée
     */
    private ArrayList<Cursor> cursorY = new ArrayList<>();

    /**
     * @see Point
     */
    private Point cursorPos;
    /**
     * @see Point
     * Curseur de la souris sur le graphe
     */
    private Point currentCursor;

    /**
     * @see Cursor
     * Ensemble des curseurs ajouter aux graphique par clique gauche
     */
    private ArrayList<Cursor> cursor = new ArrayList<>();

    private Color colorCursor;
    private boolean state;

    private int currentX;
    private int currentY;

    private String formatValue;
    private int button;
    private int action = 0;

    /**
     * @see Point
     * Paramètre de zoom donne le scope en X et Y
     */
    private Point zoom;

    /**
     * Etat de controle du graphique (Start, Stop)
     */
    private boolean interupt;
    /**
        Etat de la simulation (Start, Stop)
     */
    private boolean pause;


    private AffineTransform affineTransform = new AffineTransform();


    /**
     * Fonction destiné à la classe fille pour la modification du comportement du graphique pour l'ajout de points aux courbes
     */
    public abstract void refreshGraph();


    /**
     * Méthode permettant l'update de l'affichage
     */
    public void run(){
        while(!this.interupt){
            try {
                if(!this.pause)
                    this.refreshGraph(); //En mode pause seul les ajouts de points son stopé
                this.function();
                this.repaint();
                Thread.sleep((int) (1000*this.te));
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }



    protected abstract void reset();

    @Override
    public void keyTyped(KeyEvent e){
    }

    @Override
    public void keyPressed(KeyEvent e){
        if(this.state){
            switch (e.getKeyChar()){
                case '+':
                    this.zoom((int) this.cursorPos.getX(), (int) this.cursorPos.getY(), true);
                    break;
                case '-':
                    this.zoom((int) this.cursorPos.getX(), (int) this.cursorPos.getY(), false);
                    break;
                case ' ':
                    this.cursor.addAll(this.cursorY);
                    break;
                default:
                    break;
            }
            switch (e.getKeyCode()){
                case 37:
                    this.axesX.setMin(this.axesX.getInterval().getX()- ((this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/10));
                    this.axesX.setMax(this.axesX.getInterval().getY()- ((this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/10));
                    break;
                case 38:
                    this.axesY.setMin(this.axesY.getInterval().getX()+ ((this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/10));
                    this.axesY.setMax(this.axesY.getInterval().getY()+ ((this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/10));
                    break;
                case 39:
                    this.axesX.setMin(this.axesX.getInterval().getX()+ ((this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/10));
                    this.axesX.setMax(this.axesX.getInterval().getY()+ ((this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/10));
                    break;
                case 40:
                    this.axesY.setMin(this.axesY.getInterval().getX()- ((this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/10));
                    this.axesY.setMax(this.axesY.getInterval().getY()- ((this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/10));
                    break;
                case 10:
                    this.cursor.add(new Cursor(this.currentCursor, false));
                    break;
                case 8:
                    if(!this.cursor.isEmpty()){
                        boolean state = this.cursor.get(this.cursor.size()-1).isState();
                        for(int i = 0; i < (state ? this.curves.size() : 1); i++) this.cursor.remove(this.cursor.size()-1);
                    }
                default:
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        int x = e.getX();
        int y = e.getY();
        if(x > this.marginLeft+this.subMarginLeft && x < getWidth()-(this.marginRight+this.subMarginRight) && y > this.marginTop+this.subMarginTop+20 && y < getHeight()-(this.marginBottom+this.subMarginBottom)) this.zoom(x, y, e.getWheelRotation() < 0);
    }

    public void zoom(int x, int y, boolean state){
        double xValue = ((x-(marginLeft+subMarginRight))/((double)(getWidth()-(marginLeft+subMarginLeft+marginRight+subMarginRight))));
        double yValue = (y-(marginTop+subMarginTop+20))/((double)(getHeight()-(marginTop+subMarginTop+20+marginBottom+subMarginBottom)));
        double valueX = (this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/10;
        double valueY = (this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/10;
        if(state){
            this.axesX.setMin(this.axesX.getInterval().getX()+(xValue*valueX)*this.zoom.getX());
            this.axesX.setMax(this.axesX.getInterval().getY()-((1-xValue)*valueX)*this.zoom.getX());
            this.axesY.setMin(this.axesY.getInterval().getX()+((1-yValue)*valueY)*this.zoom.getY());
            this.axesY.setMax(this.axesY.getInterval().getY()-(yValue*valueY)*this.zoom.getY());
        } else {
            this.axesX.setMin(this.axesX.getInterval().getX()-(xValue*valueX)*this.zoom.getX());
            this.axesX.setMax(this.axesX.getInterval().getY()+((1-xValue)*valueX)*this.zoom.getX());
            this.axesY.setMin(this.axesY.getInterval().getX()-((1-yValue)*valueY)*this.zoom.getY());
            this.axesY.setMax(this.axesY.getInterval().getY()+(yValue*valueY)*this.zoom.getY());
        }

    }


    public void mousePressed(MouseEvent e){
        this.currentX = e.getX()-3;
        this.currentY = e.getY()+25;
        this.button = e.getButton();
        this.action = 3;
    }

    public void mouseReleased(MouseEvent e){
        if(this.action != 1){
            this.action = 2;
            switch (e.getButton()){
                case 1:
                    this.cursor.addAll(this.cursorY);
                    break;
                case 2:
                    if(!this.cursor.isEmpty()){
                        boolean state = this.cursor.get(this.cursor.size()-1).isState();
                        for(int i = 0; i < (state ? this.curves.size() : 1); i++) this.cursor.remove(this.cursor.size()-1);
                    }
                    break;
                case 3:
                    this.cursor.add(new Cursor(this.currentCursor, false));
                    break;
                default:
                    break;
            }
        }
    }

    public void mouseEntered(MouseEvent e){
    }

    public void mouseExited(MouseEvent e){
    }


    @Override
    public void mouseDragged(MouseEvent e){
        if(this.button == 1){
            action = 1;
            state = false;
            int x = e.getX()-3;
            int y = e.getY()+25;
            if(x > marginLeft+subMarginLeft && x < getWidth()-(marginRight+subMarginRight) && y > marginTop+subMarginTop+20 && y < getHeight()-(marginBottom+subMarginBottom)){
                double x1 = this.axesX.getInterval().getX() + ((x - getX()-(this.marginLeft+this.subMarginLeft)) / ((double) getWidth()-(this.marginLeft+this.subMarginLeft+this.marginRight+this.subMarginRight))) * (this.axesX.getInterval().getY() - this.axesX.getInterval().getX());
                double x2 = this.axesX.getInterval().getX() + ((x-(Math.abs(this.currentX-x)) - getX()-(this.marginLeft+this.subMarginLeft)) / ((double) getWidth()-(this.marginLeft+this.subMarginLeft+this.marginRight+this.subMarginRight))) * (this.axesX.getInterval().getY() - this.axesX.getInterval().getX());
                if(this.currentX > x){
                    this.axesX.setMin(this.axesX.getInterval().getX()+(x1-x2));
                    this.axesX.setMax(this.axesX.getInterval().getY()+(x1-x2));
                } else if(this.currentX < x){
                    this.axesX.setMax(this.axesX.getInterval().getY()-(x1-x2));
                    this.axesX.setMin(this.axesX.getInterval().getX()-(x1-x2));
                }
                double y1 = this.axesX.getInterval().getX() + ((y - getX()-(this.marginTop+this.subMarginTop+20)) / ((double) getHeight()-(this.marginTop+this.subMarginTop+20+this.marginBottom+this.subMarginBottom))) * (this.axesY.getInterval().getY() - this.axesY.getInterval().getX());
                double y2 = this.axesX.getInterval().getX() + ((y-(Math.abs(this.currentY-y))- getX()-(this.marginTop+this.subMarginTop+20)) / ((double) getHeight()-(this.marginTop+this.subMarginTop+20+this.marginBottom+this.subMarginBottom))) * (this.axesY.getInterval().getY() - this.axesY.getInterval().getX());
                if(this.currentY < y){
                    this.axesY.setMin(this.axesY.getInterval().getX()+(y1-y2));
                    this.axesY.setMax(this.axesY.getInterval().getY()+(y1-y2));
                } else if(this.currentY > y){
                    this.axesY.setMax(this.axesY.getInterval().getY()-(y1-y2));
                    this.axesY.setMin(this.axesY.getInterval().getX()-(y1-y2));
                }
            }
            this.currentX = x;
            this.currentY = y;
        }
    }

    public void mouseMoved(MouseEvent e){
        int x = e.getX()-3;
        int y = e.getY()-25;
        if(x > marginLeft+subMarginLeft && x < getWidth()-(marginRight+subMarginRight) && y > marginTop+subMarginTop+20 && y < getHeight()-(marginBottom+subMarginBottom)){
            this.cursorPos = new Point(x, y);
            this.state = true;
        } else state = false;
    }


    /**
     * Parametrage initial du graphique
     * @param axeX
     * @param axeY
     * @param title
     * @param curves
     * @param te
     */
    public Graph(Axes axeX, Axes axeY, String title, Curve curves[], double te){
        this.title = title;
        this.te =te;
        this.axesX = axeX;
        this.axesY = axeY;
        this.curves = new ArrayList<>();
        this.curves.addAll(Arrays.asList(curves));
        this.setLayout(null);
        this.setMinimumSize(new Dimension(100, 100));
        this.interupt = true;
        this.defaultSettings();
        this.setLayout(null);
        this.zoom = new Point(1,1);
        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);
    }


    /**
     * Modification des courbes du graphique à la voler reset du graphique
     * Utilisé pour le changement de gate dans l'application
     * @param curves Curve
     */
    public void setCurves(Curve curves[]){
        if(!interupt) this.stop();
        this.curves.clear();
        this.reset();
        this.curves.addAll(Arrays.asList(curves));
    }


    /**
     * Paramètre par default
     */
    private void defaultSettings(){
        this.setMargin(0, 0, 0, 0);
        this.setSizePoint(5);
        this.subMarginRight = 60;
        this.subMarginTop = 60;
        this.subMarginBottom = 30;
        this.colorBackGround = Color.BLACK;
        this.colorAxe = Color.GREEN;
        this.colorGrid = Color.GREEN;
        this.colorAxes0 = Color.RED;
        this.colorCursor = Color.CYAN;
        this.formatValue = "0.00";
        this.setFormatValue(0);
    }

    /**
     * Dimenstionnement des points des courbes
     * @param size
     */
    public void setSizePoint(int size){
        this.size = size;
    }

    /**
     * Formatage des valeur des axes
     * @param nb valeur à afficher
     * @return Formatage
     */
    private NumberFormat setFormatValue(double nb){
        NumberFormat formatter;
        if(Math.abs(nb) > 100000){
            formatter = new DecimalFormat(formatValue+"E0");
        } else if(Math.abs(nb) < 0.01){
            if(nb == 0){
                formatter = new DecimalFormat("#"+formatValue);
            } else formatter = new DecimalFormat(formatValue+"E0");
        } else {
            formatter = new DecimalFormat("#"+formatValue);
        }
        return formatter;
    }

    /**
     * Settup des marge du graphique
     * @param marginLeft Marge à gauche
     * @param marginRight Marge à droite
     * @param marginTop Marge en haut
     * @param marginBottom Marge en bas
     */
    public void setMargin(int marginLeft, int marginRight, int marginTop, int marginBottom){
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }

    /**
     * Gestion du graphique : Affichage des axe XY
     * @param graphics
     */
    private void graph(Graphics graphics){

        graphics.setColor(COLOR.BACKGROUND_1.getColor());
        graphics.drawRect(0,0,this.getSize().width, this.getSize().height);
        graphics.setColor(this.colorBackGround);
        graphics.fillRoundRect(marginLeft, marginTop, this.getSize().width-(marginLeft+marginRight), this.getSize().height-(marginBottom+marginTop), 20, 20);
        graphics.setColor(this.axesY.getColor());
        graphics.drawString(this.axesY.getTitle(), marginLeft+subMarginLeft-graphics.getFontMetrics().stringWidth(this.axesY.getTitle())/2, marginTop+20+subMarginTop-40);
        graphics.setColor(this.colorAxe);
        graphics.drawLine(marginLeft+subMarginLeft, marginTop+20+subMarginTop-30, marginLeft+subMarginLeft, this.getSize().height-marginBottom-subMarginBottom);
        graphics.drawLine(marginLeft+subMarginLeft-5,marginTop+20+subMarginTop-18,marginLeft+subMarginLeft,marginTop+20+subMarginTop-30);
        graphics.drawLine(marginLeft+subMarginLeft,marginTop+20+subMarginTop-30,marginLeft+subMarginLeft+5,marginTop+20+subMarginTop-18);
        graphics.drawLine(marginLeft+subMarginLeft, this.getSize().height-marginBottom-subMarginBottom, this.getSize().width-marginRight-subMarginRight+30, this.getSize().height-marginBottom-subMarginBottom);
        graphics.drawLine(this.getSize().width-marginRight-subMarginRight+30,this.getSize().height-marginBottom-subMarginBottom,this.getSize().width-marginRight-subMarginRight+18,this.getSize().height-marginBottom-subMarginBottom+5);
        graphics.drawLine(this.getSize().width-marginRight-subMarginRight+30,this.getSize().height-marginBottom-subMarginBottom,this.getSize().width-marginRight-subMarginRight+18,this.getSize().height-marginBottom-subMarginBottom-5);
        affineTransform.setToRotation(Math.PI/2);
        ((Graphics2D)(graphics)).setTransform(affineTransform);
        graphics.drawString(this.axesX.getTitle(), this.getSize().height-marginBottom-subMarginBottom-graphics.getFontMetrics().stringWidth(this.axesX.getTitle())/2, -(this.getSize().width-marginRight-subMarginRight+40));
        affineTransform.setToRotation(0);
        ((Graphics2D)(graphics)).setTransform(affineTransform);
        double tye =  (this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/10;
        double tyi = (this.getSize().height-(marginTop+20+subMarginTop+marginBottom+subMarginBottom))/10.0;
        for(int y = 0; y <= 10; y++){
            graphics.setColor(this.axesY.getColor());
            //graphics.drawString(this.setFormatValue(this.axesY.getInterval().getY()-this.axesY.getInterval().getX()).format(this.axesY.getInterval().getX()+y * tye), marginLeft+subMarginLeft-8 - graphics.getFontMetrics().stringWidth(this.setFormatValue(this.axesY.getInterval().getY()-this.axesY.getInterval().getX()).format(this.axesY.getInterval().getX()+y * tye)), (int) (this.getSize().height - marginBottom-subMarginBottom+5 - y * tyi));
            graphics.setColor(this.colorGrid);
            ((Graphics2D)graphics).setStroke(new BasicStroke(1F));
            graphics.drawLine(marginLeft+subMarginLeft+2,(int) (this.getSize().height - (marginBottom+subMarginBottom) - y * tyi) , this.getSize().width-(marginRight+subMarginRight), (int) (this.getSize().height - (marginBottom+subMarginBottom) - y * tyi));
            ((Graphics2D)graphics).setStroke(new BasicStroke(3F));
        }
        graphics.setColor(this.colorAxes0);
        if(this.axesX.getInterval().getX()< 0 && this.axesX.getInterval().getY() > 0) graphics.drawLine(getPosX(0), getPosY(this.axesY.getInterval().getX()), getPosX(0), getPosY(this.axesY.getInterval().getY()));
        if(this.axesY.getInterval().getX()< 0 && this.axesY.getInterval().getY() > 0) graphics.drawLine(getPosX(this.axesX.getInterval().getX()), getPosY(0), getPosX(this.axesX.getInterval().getY()), getPosY(0));

        double txe = (this.axesX.getInterval().getY() - this.axesX.getInterval().getX()) / 10;
        double txi = (this.getSize().width-(marginLeft+subMarginLeft+marginRight+subMarginRight)) / 10.0;
        for(int x = 0; x <= 10; x++){
            graphics.setColor(this.axesX.getColor());
            graphics.drawString( this.setFormatValue(this.axesX.getInterval().getY()-this.axesX.getInterval().getX()).format(this.axesX.getInterval().getX()+(x*txe)), (int) (marginLeft+subMarginLeft+x*txi-graphics.getFontMetrics().stringWidth( this.setFormatValue(this.axesX.getInterval().getY()-this.axesX.getInterval().getX()).format(this.axesX.getInterval().getX()+(x*txe)))/2), this.getSize().height-(marginBottom+subMarginBottom)+20);
            graphics.setColor(this.colorGrid);
            ((Graphics2D)graphics).setStroke(new BasicStroke(1F));
            graphics.drawLine((int)(marginLeft+subMarginLeft+x*txi),this.getSize().height-(marginBottom+subMarginBottom)-2 , (int)(marginLeft+subMarginLeft+x*txi), marginTop+20+subMarginTop);
            ((Graphics2D)graphics).setStroke(new BasicStroke(3F));
        }
        int i = 100;
        Font font = graphics.getFont();
        graphics.setFont(new Font(font.getName(), font.getStyle(), font.getSize()+10));
        for(Curve curve : this.curves){
            graphics.setColor(curve.getColorCurve());
            graphics.drawString(curve.getLabel(), i, 20);
            i+= graphics.getFontMetrics().stringWidth(curve.getLabel())+10;
        }
        graphics.setFont(font);
    }

    /**
     * Gestion des courbes du graphique
     * @param graphics
     */
    private void curve(Graphics graphics){
        for(Curve curve : this.curves){
            for(int i = 0; i < curve.getPoints().size(); i++){
                if(curve.getPoints().get(i).getX() > this.axesX.getInterval().getX() && curve.getPoints().get(i).getX() < this.axesX.getInterval().getY() && curve.getPoints().get(i).getY() > this.axesY.getInterval().getX() && curve.getPoints().get(i).getY() < this.axesY.getInterval().getY()){
                    graphics.setColor(curve.getColorCurve());
                    if(i > 0) graphics.drawLine(getPosX(curve.getPoints().get(i-1).getX()), getPosY(curve.getPoints().get(i-1).getY()), getPosX(curve.getPoints().get(i).getX()), getPosY(curve.getPoints().get(i).getY()));
                    graphics.setColor(curve.getColorPoint());
                    graphics.fillOval(getPosX(curve.getPoints().get(i).getX())-size/2,getPosY(curve.getPoints().get(i).getY())-size/2, size, size);
                }
            }
        }
    }

    /**
     * Gestion des curseurs programmé
     * @param graphics
     */
    private void cursor(Graphics graphics){
        ((Graphics2D)graphics).setStroke(new BasicStroke(1F));
        if(this.state){
            graphics.setColor(this.colorCursor);
            graphics.drawLine((int) this.cursorPos.getX(), this.marginTop+this.subMarginTop+20, (int) this.cursorPos.getX(),this.getHeight()-(this.marginBottom+this.subMarginBottom));
             double x = this.axesX.getInterval().getX() + (((int) this.cursorPos.getX() - getX()-(this.marginLeft+this.subMarginLeft)) / ((double) getWidth()-(this.marginLeft+this.subMarginLeft+this.marginRight+this.subMarginRight))) * (this.axesX.getInterval().getY() - this.axesX.getInterval().getX());
            graphics.drawString( this.setFormatValue(x).format(x),(int) this.cursorPos.getX()-graphics.getFontMetrics().stringWidth( this.setFormatValue(x).format(x))/2,this.marginTop+this.subMarginTop+5);
            double y =  this.axesY.getInterval().getX()+(this.axesY.getInterval().getY() - (this.axesY.getInterval().getX() + (((int) this.cursorPos.getY() - getY()-(this.marginTop+this.subMarginTop+20)) / ((double)getHeight()-(this.marginTop+this.subMarginTop+20+this.marginBottom+this.subMarginBottom))) * (this.axesY.getInterval().getY() - this.axesY.getInterval().getX())));

            this.currentCursor = new Point(x, y);
            this.cursorY = new ArrayList<>();

            for(Curve curve : this.curves){
                for(int i = 0; i < curve.getPoints().size()-1; i++){
                    if(curve.getPoints().get(i).getX() <= x && x <= curve.getPoints().get(i+1).getX()){
                        double yValue  = curve.getPoints().get(i).getY() < curve.getPoints().get(i+1).getY() ? curve.getPoints().get(i).getY()+(Math.abs(curve.getPoints().get(i+1).getY()-curve.getPoints().get(i).getY()))*((x-curve.getPoints().get(i).getX())/(curve.getPoints().get(i+1).getX()-curve.getPoints().get(i).getX())) : curve.getPoints().get(i+1).getY()+(Math.abs(curve.getPoints().get(i+1).getY()-curve.getPoints().get(i).getY()))*((curve.getPoints().get(i+1).getX()-x)/(curve.getPoints().get(i+1).getX()-curve.getPoints().get(i).getX()));
                        graphics.setColor(curve.getColorCurve());
                        graphics.drawLine(this.marginLeft+this.subMarginLeft, getPosY(yValue),this.getWidth()-(this.marginRight+this.subMarginRight), getPosY(yValue));
                        graphics.drawString(yValue % 2 != 0 ? "TRUE" : "FALSE", this.getWidth()-(this.marginRight+this.subMarginRight)+5,getPosY(yValue)+5);
                        this.cursorY.add(new Cursor(new Point(x, yValue), curve, true));
                    }
                }
            }
        }
        for(Cursor cursor : this.cursor){
            if((cursor.getY() <= this.axesY.getInterval().getY() && cursor.getY() >= this.axesY.getInterval().getX()) && (cursor.getX() <= this.axesX.getInterval().getY() && cursor.getX() >= this.axesX.getInterval().getX())){
                graphics.setColor(cursor.isState() ? cursor.getCurve().getColorCurve() : this.colorCursor);
                graphics.drawLine(this.marginLeft+this.subMarginLeft, getPosY(cursor.getY()),this.getWidth()-(this.marginRight+this.subMarginRight), getPosY(cursor.getY()));
                graphics.drawString(cursor.getY() % 2 != 0 ? "TRUE" : "FALSE", this.getWidth()-(this.marginRight+this.subMarginRight)+5,getPosY(cursor.getY())+5);
                graphics.setColor(this.colorCursor);
                graphics.drawLine(getPosX(cursor.getX()), this.marginTop+this.subMarginTop+20, getPosX(cursor.getX()),this.getHeight()-(this.marginBottom+this.subMarginBottom));
                graphics.drawString(this.setFormatValue(cursor.getX()).format(cursor.getX()), getPosX(cursor.getX())-graphics.getFontMetrics().stringWidth(this.setFormatValue(cursor.getX()).format(cursor.getX()))/2,this.marginTop+this.subMarginTop+5);
            }
        }
    }

    /**
     * Affichage des informations complémentaire de la classe fille
     * @param graphics
     */
    public abstract void paintInfo(Graphics graphics);


    /**
     * Affichage des élément graphique du panel
     * @param graphics
     */
    @Override
    public void paintComponent(Graphics graphics){
        this.subMarginLeft = (graphics.getFontMetrics().stringWidth(this.setFormatValue(this.axesY.getInterval().getY()).format(this.axesY.getInterval().getY())) >  graphics.getFontMetrics().stringWidth(this.setFormatValue(this.axesY.getInterval().getX()).format(this.axesY.getInterval().getX())) ? graphics.getFontMetrics().stringWidth(this.setFormatValue(this.axesY.getInterval().getY()).format(this.axesY.getInterval().getY())) : graphics.getFontMetrics().stringWidth(this.setFormatValue(this.axesY.getInterval().getX()).format(this.axesY.getInterval().getX())))+15;
        ((Graphics2D)graphics).setStroke(new BasicStroke(3F));
        graphics.drawString(this.title, (this.marginLeft+(this.getWidth()-(this.marginLeft+this.marginRight)) / 2) - graphics.getFontMetrics().stringWidth(this.title) /2, this.marginTop+12);
        graph(graphics);
        curve(graphics);
        cursor(graphics);
        graphics.setColor(Color.red);
        this.setFormatValue((this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/10);
        Font font = graphics.getFont();
        graphics.setFont(new Font(font.getName(), font.getStyle(), font.getSize()-2));
        graphics.drawString("Zoom : x = "+this.setFormatValue((this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/10).format( (this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/10)+" y = "+this.setFormatValue((this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/10).format((this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/10)+" "+this.setFormatValue((this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/(this.axesY.getInterval().getY()-this.axesY.getInterval().getX())).format((this.axesX.getInterval().getY()-this.axesX.getInterval().getX())/(this.axesY.getInterval().getY()-this.axesY.getInterval().getX()))+":"+this.setFormatValue((this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/(this.axesX.getInterval().getY()-this.axesX.getInterval().getX())).format((this.axesY.getInterval().getY()-this.axesY.getInterval().getX())/(this.axesX.getInterval().getY()-this.axesX.getInterval().getX())),this.getSize().width - (marginRight+subMarginRight+500), marginTop+subMarginTop-10);
        paintInfo(graphics);
    }


    /**
     * Transformation des X de l'écran vers les X du graphique fonction de l'axe X
     * @param value Valeur des X relatif à l'écran
     * @return Valeur des X relatif à l'axe X
     */
    private int getPosX(double value){
        value-=this.axesX.getInterval().getX();
        return (int) (marginLeft+subMarginLeft + ((this.getSize().width - (marginLeft+subMarginLeft+marginRight+subMarginRight))*value) / (this.axesX.getInterval().getY() - (this.axesX.getInterval().getX())));
    }

    /**
     * Transformation des Y de l'écran vers les Y du graphique fonction de l'axe Y
     * @param value Valeur des Y relatif à l'écran
     * @return Valeur des Y relatif à l'axe Y
     */
    private int getPosY(double value){
        value-=this.axesY.getInterval().getX();
        return (int) (this.getSize().height - (marginBottom+subMarginBottom + ((this.getSize().height - (marginBottom+subMarginBottom+marginTop+20+subMarginTop))*value) / (this.axesY.getInterval().getY() - (this.axesY.getInterval().getX()))));
    }

    /**
     * @see Controle
     */
    @Override
    public void start(){
        this.interupt = false;
        if(!this.pause) new Thread(this).start();
        this.pause = false;
    }

    /**
     * @see Controle
     */
    @Override
    public void stop(){
        this.interupt = true;
        this.pause = false;
    }

    /**
     * @see Controle
     */
    @Override
    public void pause(){
        this.pause = true;
    }

    /**
     * @see Controle
     */
    @Override
    public void resume() {
        this.pause = false;
    }

    /**
     * Fonction d'update des courbe et de l'affichage
     */
    abstract void function();
}
