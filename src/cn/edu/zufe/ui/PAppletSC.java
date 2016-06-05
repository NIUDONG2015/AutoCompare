package cn.edu.zufe.ui;

import java.util.LinkedList;

import cn.edu.zufe.drawable.PSection;
import processing.core.PApplet;
import processing.core.PGraphics;
import controlP5.*;

/**
 * Stratigraphic Correlation
 */
public class PAppletSC extends PApplet {

	private LinkedList<PSection> psList = null;
	private int width, height;
	private PGraphics pgBottom, pgHighlight;
	ControlP5 cp5;

	public PAppletSC(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setPSList(LinkedList<PSection> psList) {
		this.psList = psList;
	}

	public void setup() {
		size(width, height);
		// ��׶˻���ͼ��ʼ��
		pgBottom = createGraphics(width, height);
		pgBottom.beginDraw();
		pgBottom.endDraw();
		// ControlP5 ʵ����
		cp5 = new ControlP5(this);
		
	}

	public void draw() {
		image(pgBottom, 0, 0);
	}

	public void drawPGBottom() {
		if (psList != null) {
			pgBottom.beginDraw();
			pgBottom.clear();
			pgBottom.background(255);
			for (PSection ps : psList) {
				ps.draw(pgBottom);
			}
			pgBottom.endDraw();
		}
	}

}
