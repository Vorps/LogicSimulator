package me.graphics.info;

import me.graphics.info.graphics.Controle;
import me.logic.info.Scheduler;
import me.logic.info.Trace;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Affichage de la trace de la simulation provenant des portes logiques
 */
public class TraceJPanel extends JPanel implements Controle {
    private JList jList;
    private  long dateStart;
    public TraceJPanel(){
        this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.setBackground(COLOR.BACKGROUND_1.getColor());
        this.setLayout( new GridBagLayout());
        JLabel label = new JLabel("Trace de la simulation",JLabel.CENTER);
        label.setForeground(COLOR.FOREGROUND_1.getColor());
        label.setFont(new Font("Monaco", Font.PLAIN, 20));
        JButton reload = new JButton();
        reload.setPreferredSize(new Dimension(60,60));
        reload.setIcon(new ImageIcon("resources/reload.png"));
        reload.addActionListener((e) -> start());
        reload.setBackground(COLOR.BACKGROUND_1.getColor());
        this.jList = new JList(Scheduler.getScheduler().traces);
        jList.setBackground(COLOR.BACKGROUND_2.getColor());
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setCellRenderer(new TraceJPanel.ListRenderer());

        jList.setBorder(new EmptyBorder(0,0,0,0));
        JScrollPane scrollPane = new JScrollPane(jList);

        scrollPane.setPreferredSize(new Dimension(500, 200));
        scrollPane.setBackground(COLOR.BACKGROUND_2.getColor());
        jList.setLayoutOrientation(JList.VERTICAL);
        this.add(label,new GridBagConstraints(0,0,1,1,0.8,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
        this.add(reload,new GridBagConstraints(1,0,1,1,0.2,0,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(10,10,10,10),0,0));
        this.add(scrollPane, new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
    }

    @Override
    public void start() {
        Scheduler.getScheduler().traces.clear();
        this.jList.updateUI();
        this.dateStart =System.currentTimeMillis();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setTimeMax(int time) {

    }


    class ListRenderer extends JPanel
            implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list,
                                                      Object value, // valeur à afficher
                                                      int index, // indice d'item
                                                      boolean isSelected, // l'item est-il sélectionné
                                                      boolean cellHasFocus) // La liste a-t-elle le focus
        {
            this.removeAll();
            Trace s = (Trace) value;
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            if (isSelected) {
                setForeground(COLOR.FOREGROUND_1.getColor());
                setBackground(COLOR.BACKGROUND_3.getColor());
                setBorder(new LineBorder(COLOR.FOREGROUND_1.getColor(),2,true));
            }else{
                setForeground(Color.GRAY);
                setBackground(COLOR.BACKGROUND_2.getColor());
                setBorder(new LineBorder(COLOR.BACKGROUND_2.getColor()));
            }
            JLabel labelName = new JLabel(s.getGate(), JLabel.LEFT);
            labelName.setForeground(COLOR.FOREGROUND_1.getColor());
            JLabel labelInput = new JLabel(s.getInput(), JLabel.LEFT);
            labelInput.setForeground(Color.GREEN);
            JLabel labelOutput = new JLabel(s.getOutput(), JLabel.LEFT);
            labelOutput.setForeground(Color.RED);
            JLabel labelDate = new JLabel((s.getDate() - TraceJPanel.this.dateStart) +"", JLabel.RIGHT);

            labelDate.setForeground(Color.GRAY);
            this.add(labelName);
            this.add(Box.createHorizontalGlue());
            this.add(labelInput);
            this.add(Box.createHorizontalGlue());
            this.add(labelOutput);
            this.add(Box.createHorizontalGlue());
            this.add(labelDate);
            setEnabled(false);
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }
}
