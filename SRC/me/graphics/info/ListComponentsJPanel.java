package me.graphics.info;

import me.logic.info.Gate;
import me.logic.info.GateEnum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Liste et sélection des portes logiques pour la simulation
 */
public class ListComponentsJPanel extends JPanel {

    public JList<Gate> jList;
    private Gate gate;

    public ListComponentsJPanel(){
        this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.setBackground(COLOR.BACKGROUND_1.getColor());
        this.setLayout( new GridBagLayout());
        JLabel label = new JLabel("Sélection du composant à simuler",JLabel.CENTER);
        label.setForeground(COLOR.FOREGROUND_1.getColor());
        label.setFont(new Font("Monaco", Font.PLAIN, 20));
        this.jList = new JList<>(new Gates(Arrays.stream(GateEnum.values()).map(GateEnum::invoke).collect(Collectors.toList())));
        jList.setBackground(COLOR.BACKGROUND_2.getColor());
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setCellRenderer(new ListRenderer());
        jList.setBorder(new EmptyBorder(0,0,0,0));

        JScrollPane scrollPane = new JScrollPane(jList);

        scrollPane.setPreferredSize(new Dimension(500, 200));
        scrollPane.setBackground(COLOR.BACKGROUND_2.getColor());
        jList.setLayoutOrientation(JList.VERTICAL);
        this.add(label,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
        this.add(scrollPane, new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
    }

    /**
     * Sélection de la porte logique
     * @param gateSelect
     */
    public void setGateSelect(Action<Gate> gateSelect){
        this.jList.addListSelectionListener((e) -> {
            if(this.jList.getSelectedValue() != null && gate != this.jList.getSelectedValue()){
                this.gate = this.jList.getSelectedValue();
                gateSelect.action(gate);
            }

        });
    }


    /**
     * Redefinition du rendu dans une cellule de JList
     */
    class ListRenderer extends JLabel
            implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list,
                                                      Object value, // valeur à afficher
                                                      int index, // indice d'item
                                                      boolean isSelected, // l'item est-il sélectionné
                                                      boolean cellHasFocus) // La liste a-t-elle le focus
        {
            String s = value.toString();
            if (isSelected) {
                setForeground(COLOR.FOREGROUND_1.getColor());
                setBackground(COLOR.BACKGROUND_3.getColor());
                setBorder(new LineBorder(COLOR.FOREGROUND_1.getColor(),2,true));
            }else{
                setForeground(Color.GRAY);
                setBackground(COLOR.BACKGROUND_2.getColor());
                setBorder(new LineBorder(COLOR.BACKGROUND_2.getColor()));
            }
            setIcon(new ImageIcon(new ImageIcon("resources/"+s+".png").getImage().getScaledInstance(120, 60, Image.SCALE_DEFAULT)));
            setText(s);
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }
}

