/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeling;

import java.awt.Color;

/**
 *
 * @author Mikkel
 */
public class Cube {
	private CustomPoint3D position;
	private Color color;

	public CustomPoint3D getPosition() {
		return position;
	}

	public void setPosition(CustomPoint3D position) {
		this.position = position;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
