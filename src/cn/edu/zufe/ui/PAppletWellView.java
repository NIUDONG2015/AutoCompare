package cn.edu.zufe.ui;

import java.util.LinkedList;

import processing.core.*;
import cn.edu.zufe.drawable.PWell;

public class PAppletWellView extends PApplet {

	private PAppletSC psc;
	private PGraphics pg;
	private int width, height;
	private LinkedList<PWell> pwList = null;

	public PAppletWellView(int width, int height, PApplet psc) {
		this.width = width;
		this.height = height;
		this.psc = (PAppletSC) psc;

	}

	public void setPWells(LinkedList<PWell> pwList) {
		this.pwList = pwList;
		PShape icon = loadShape("res//oil_field.svg");
		pg.beginDraw();
		for (PWell pw : pwList) {
			pw.draw(pg, icon);
		}
		pg.endDraw();
	}

	public void setup() {
		this.size(width, height);
		pg = createGraphics(width, height);
		pg.beginDraw();
		pg.colorMode(HSB, 360, 100, 100); // É«²ÊÄ£Ê½HSB
		pg.endDraw();
	}

	public void draw() {
		background(255);
		image(pg, 0, 0);
	}
}
