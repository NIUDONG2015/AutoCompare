package cn.edu.zufe.ui;

import java.util.LinkedList;

import cn.edu.zufe.drawable.PSection;
import processing.core.PApplet;

/**
 * Stratigraphic Correlation
 */
public class PAppletSC extends PApplet {

	private LinkedList<PSection> psList = null;

	private int width, height;

	public PAppletSC(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setup() {
		size(width, height);
	}

	public void draw() {

	}

	public void setPSList(LinkedList<PSection> psList) {
		this.psList = psList;
	}

}
