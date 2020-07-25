package me.graphics.info;

import java.awt.*;

import javax.swing.*;

import me.graphics.info.graphics.Controle;
import me.logic.info.Gate;

/**
 * Gestion du paramètrage des signaux et du contrôle de la simulation de la porte logic
 */
public class ComponentJPanel extends JPanel{

	public Gate gate;
	private boolean inSimulation;
	private JLabel labelComponent;
	/**
	 * @see WireJPanel
	 */
	private WireJPanel wireJPanel;

	private JButton button;
	private JButton buttonRetry;
	/**
	 * @see Controle
	 * Controle du graphique
	 */
	private Controle controleSimulation;
	/**
	 * @see Controle
	 * Controle de la trace de la simulation
	 */
	private Controle controleTrace;

	public void setControle(Controle controleSimulation, Controle controleTrace){
		this.controleSimulation = controleSimulation;
		this.controleTrace = controleTrace;
	}


	/**
	 * Modification de l'état de la simulation et changement d'état du bouton de contrôle
	 * @param state
	 */
	private void setState(boolean state){
		this.inSimulation = state;
		button.setText(state ? "STOP" : "run");
		button.setBackground(state ? Color.RED : Color.GREEN);
	}


	/**
	 * Information de la gate sélectionnée pour la simulation et changement de l"état du JPanel
	 * @param gate
	 */
	public void setGate(Gate gate){
		gate.setDelay(this.gate != null ? this.gate.getDelay() : 1);
		this.gate = gate;
		if(this.inSimulation){
			this.wireJPanel.stop();
			setState(false);
		}
		if(this.wireJPanel != null) this.remove(wireJPanel);
		this.wireJPanel = new WireJPanel(this.gate.getSignals().wiresInput);
		labelComponent.setText(gate.toString());
		this.wireJPanel.setPreferredSize(new Dimension(300,50));
		labelComponent.setIcon(new ImageIcon(new ImageIcon("resources/"+gate.toString()+".png").getImage().getScaledInstance(120, 60, Image.SCALE_DEFAULT)));
		this.add(this.wireJPanel, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10),20,20));
		labelComponent.setToolTipText("<html>"+this.gate.info().replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")+"</html>");
	}

	public ComponentJPanel()
	{
		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		this.setBackground(COLOR.BACKGROUND_1.getColor());
	    this.setLayout(new GridBagLayout());
		JLabel labelTitle = new JLabel("Composant : ");
		labelComponent = new JLabel();
		labelComponent.setForeground(COLOR.FOREGROUND_1.getColor());
		labelComponent.setFont(new Font("Monaco", Font.PLAIN, 20));
		labelTitle.setForeground(COLOR.FOREGROUND_1.getColor());
		labelTitle.setFont(new Font("Monaco", Font.PLAIN, 20));
		button = new JButton();
		button.setPreferredSize(new Dimension(100, 40));
		button.setFont(new Font("Monaco", Font.PLAIN, 20));
		buttonRetry = new JButton("Reprendre");
		buttonRetry.setPreferredSize(new Dimension(200, 40));
		buttonRetry.setVisible(false);
		buttonRetry.setFont(new Font("Monaco", Font.PLAIN, 20));
		buttonRetry.setBackground(Color.GREEN);
		this.setState(false);

		labelComponent.setHorizontalAlignment(JLabel.CENTER);
		labelTitle.setVerticalAlignment(JLabel.CENTER);
		this.add(labelTitle,new GridBagConstraints(0,0,1,1,0.5,0.1,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(10,10,10,10),0,0));
		this.add(labelComponent,new GridBagConstraints(1,0, 1,1,0.5,0,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(10,10,10,10),0,0));

		this.add(new InputNumeric("Temps       (Seconds)", 100, value -> controleSimulation.setTimeMax(value)),new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.EAST, new Insets(10,10,10,10),0,0));
		this.add(new InputNumeric("Delay (Millisecondes)", 1, value -> this.gate.setDelay(value)),new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.EAST, new Insets(10,10,10,10),0,0));

		this.add(button,new GridBagConstraints(0,3,1,1,0,0.2,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(10,10,10,10),0,0));
		this.add(buttonRetry,new GridBagConstraints(1,3,2,1,0,0.2,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets(10,10,10,10),0,0));
		button.addActionListener((ActionEvent) -> {
			this.setState(!this.inSimulation);
			if(this.inSimulation){
				this.wireJPanel.run();
				controleSimulation.start();
				controleTrace.start();
				buttonRetry.setVisible(false);
			} else {
				this.wireJPanel.stop();
				controleSimulation.pause();
				controleTrace.pause();
				buttonRetry.setVisible(true);
			}


		});
		buttonRetry.addActionListener((ActionEvent) -> {
			controleSimulation.resume();
			controleTrace.resume();
			this.wireJPanel.run();
			this.setState(true);
			buttonRetry.setVisible(false);
		});
	}
}
