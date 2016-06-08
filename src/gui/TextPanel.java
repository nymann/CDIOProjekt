/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Mikkel
 */
public class TextPanel extends JPanel{
	JTextArea jTextArea;

	public TextPanel() {
		this.jTextArea = new JTextArea();
		this.add(jTextArea);
	}
	
	public void addTextLine(String text){
		this.jTextArea.setText(this.jTextArea.getText() + text + "\n");
	}
}
