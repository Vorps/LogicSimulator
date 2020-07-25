package me.graphics.info;

import javax.swing.*;
import java.awt.*;

/**
 * Fenètre modal pour la configuration de signaux dans le temps
 */
public class WireDialog extends JDialog {


    public WireDialog(WireInputConf wireInputConf){
        super();
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setSize(new Dimension(400,300));
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        this.setBackground(COLOR.BACKGROUND_2.getColor());

        ButtonWire buttonWire1 = new ButtonWire();
        buttonWire1.addActionListener((e) -> {
            wireInputConf.signal1 = this.actionButton(buttonWire1, wireInputConf.signal1);
        });

        ValueEntry valueEntry1 = new ValueEntry(100);

        ButtonWire buttonWire2 = new ButtonWire();
        buttonWire2.addActionListener((e) -> {
            wireInputConf.signal2 = this.actionButton(buttonWire2, wireInputConf.signal2);
        });

        ValueEntry valueEntry2 = new ValueEntry(100);

        JButton validate = new JButton();
        validate.setPreferredSize(new Dimension(40,40));
        JCheckBox jCheckBox = new JCheckBox();
        jCheckBox.setText("Activer");
        jCheckBox.setFont(new Font("Monaco", Font.PLAIN, 18));
        jCheckBox.setForeground(COLOR.FOREGROUND_1.getColor());
        jCheckBox.setSelected(true);
        validate.addActionListener((e) -> {
            valueEntry1.valid();
            valueEntry2.valid();
            wireInputConf.time1 = valueEntry1.getValue();
            wireInputConf.time2 =  valueEntry2.getValue();
            if(wireInputConf.time1 != -1 && wireInputConf.time2 != -1) {
                wireInputConf.active = jCheckBox.isSelected();
                this.setVisible(false);
                this.dispose();
            }
        });
        validate.setIcon(new ImageIcon("resources/ok.png"));


        this.add(buttonWire1, new GridBagConstraints(1,1,1,1,1,3, GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(0,0,0,0),10,10));
        this.add(valueEntry1, new GridBagConstraints(2,1,1,1,1,3, GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(0,0,0,0),10,10));
        this.add(buttonWire2, new GridBagConstraints(1,2,1,1,1,3, GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(0,0,0,0),10,10));
        this.add(valueEntry2, new GridBagConstraints(2,2,1,1,1,3, GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(0,0,0,0),10,10));
        this.add(validate, new GridBagConstraints(1,3,1,1,1,3, GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(0,0,0,0),10,10));
        this.add(jCheckBox, new GridBagConstraints(2,3,4,1,1,3, GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(0,0,0,0),10,10));
        buttonWire1.setSignal(wireInputConf.signal1);
        buttonWire2.setSignal(wireInputConf.signal2);
    }

    private boolean actionButton(ButtonWire buttonWire, boolean state){
        state = ! state;
        buttonWire.setSignal(state);
        return state;
    }



}
