/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import QRWallMarks.QRInfo;
import modeling.MainModel;
import modeling.QRPoint;

/**
 *
 * @author Mikkel
 */
public class QRTest {
	public static void main(String[] args){
		QRInfo info = new QRInfo();
		info.name = "W00.01";
		QRPoint point = MainModel.getQRPoint(info);
		System.out.println(point);
	}
}
