package me.graphics.info;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import me.logic.info.Gate;

/**
 * Objet pour la liste des porte logic dans la JList
 */
public class Gates extends AbstractListModel<Gate>{
	
	public java.util.List<Gate> gates;

	
	public Gates(List<Gate> gates) {
		this.gates = new ArrayList<Gate>(gates);
	}
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return gates.size();
	}

	@Override
	public Gate getElementAt(int index) {
		// TODO Auto-generated method stub
		return gates.get(index);
	}
	
	

}
