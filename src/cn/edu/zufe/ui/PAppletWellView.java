package cn.edu.zufe.ui;

import processing.core.*;

public class PAppletWellView extends PApplet {

	private PAppletSC psc;
	private PGraphics pg;

	public PAppletWellView(int width, int height, PApplet psc) {
		this.width = width;
		this.height = height;
		this.psc = (PAppletSC) psc;
	}

	public void setup() {
		size(width, height);
		pg = createGraphics(width, height);
		// ...
	}

	public void draw() {
		image(pg, 0, 0);
	}
}
