package me.graphics.info;

import javax.swing.*;
import java.awt.*;

public class InputNumeric extends JPanel{


    public InputNumeric(String name, int value, Action<Integer> action) {
        this.setBackground(COLOR.BACKGROUND_2.getColor());
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20,15));
        ValueEntry valueEntry = new ValueEntry(value);
        JLabel label = new JLabel(name);
        label.setFont(new Font("Monaco", Font.PLAIN, 18));
        label.setForeground(COLOR.FOREGROUND_1.getColor());
        label.setHorizontalAlignment(JLabel.RIGHT);
        this.add(label);
        JButton button = new JButton();
        button.setIcon(new ImageIcon("resources/ok.png"));
        button.setBackground(COLOR.BACKGROUND_2.getColor());
        button.addActionListener((e) -> {
            valueEntry.valid();
            action.action(valueEntry.getValue());
        });
        this.add(label);
        this.add(valueEntry);
        this.add(button);
    }
}
