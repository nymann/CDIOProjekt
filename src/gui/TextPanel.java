/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Mikkel
 */
public class TextPanel extends JScrollPane{
	JTextArea jTextArea;
	JPanel panel;

	public TextPanel() {
		this.jTextArea = new JTextArea();
		this.panel = new JPanel();
		panel.add(jTextArea);
		super.setViewportView(panel);
	}
	
	public void addTextLine(String text){
		this.jTextArea.setText(this.jTextArea.getText() + text + "\n");
	}
	
	public void setText(String text){
		this.jTextArea.setText(text);
	}
}
