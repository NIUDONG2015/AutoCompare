package cn.edu.zufe.ui;

import java.util.LinkedList;

import processing.core.*;
import cn.edu.zufe.drawable.PWell;

public class PAppletWellView extends PApplet {

	private PAppletSC psc;
	private PGraphics pgBottom, pgHighlight;
	private int width, height;
	private LinkedList<PWell> pwList = null;
	private PShape iconOrigin, iconClicked; // 油田图标

	public PAppletWellView(int width, int height, PApplet psc) {
		this.width = width;
		this.height = height;
		this.psc = (PAppletSC) psc;
	}

	public void setPWells(LinkedList<PWell> pwList) {
		this.pwList = pwList;
	}

	public void drawPG() {
		if (pwList != null) {
			pgBottom.beginDraw();
			pgBottom.clear();
			pgBottom.background(0, 0, 100);
			for (PWell pw : pwList) {
				pw.draw(pgBottom, iconOrigin, iconClicked);
			}
			pgBottom.endDraw();
		}
	}

	public void setup() {
		this.size(width, height);
		pgBottom = createGraphics(width, height);
		pgBottom.beginDraw();
		pgBottom.colorMode(HSB, 360, 100, 100); // 色彩模式HSB
		pgBottom.endDraw();
		pgHighlight = createGraphics(width, height);
		pgHighlight.beginDraw();
		pgHighlight.endDraw();
		iconOrigin = loadShape("res//oil_field.svg");
		iconClicked = loadShape("res//oil_field_clicked.svg");
	}

	public void draw() {
		image(pgBottom, 0, 0);

		if (pwList != null) {
			for (PWell pw : pwList) {
				if (pw.collisionDetection(mouseX, mouseY)) {
					pgHighlight.beginDraw();
					pgHighlight.clear();
					pw.highlight(pgHighlight, iconOrigin,iconClicked);
					pgHighlight.endDraw();
					break;
				} else {
					pgHighlight.beginDraw();
					pgHighlight.clear();
					pgHighlight.endDraw();
				}
			}
		}
		image(pgHighlight, 0, 0);
	}

	public void mousePressed() {
		for (PWell pw : pwList) {
			pw.setClicked(false);
		}

		if (mouseButton == LEFT) {
			for (PWell pw : pwList) {
				if (pw.collisionDetection(mouseX, mouseY)) {
					pw.setClicked(true);
					break;
				}
			}
		}
		drawPG();
	}
}
