/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Mikkel
 */
public class InfoPanel extends JPanel{
	private JTextArea textArea;
	private Map<String,Object> info;
	
	public InfoPanel(){
		this.info = new HashMap<>();
		this.textArea = new JTextArea();
		super.add(this.textArea);
	}
	
	public void setInfo(String name, Object value){
		info.put(name, value);
		//textArea.setRows(info.size());
		
		//TODO remove
		textArea.setLineWrap(true);
		textArea.setRows(10);
		
		
		updateText();
	}
	
	public void setColumns(int c){
		textArea.setColumns(c);
	}
	
	private void updateText(){
		String panelText = "";
		for(Entry<String,Object> entry: info.entrySet()){
			panelText += entry.getKey() + " :" + entry.getValue() + "\n";
		}
		textArea.setText(panelText);
	}
	
	
	
}
