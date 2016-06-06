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
		// 最底端缓存图初始化
		pgBottom = createGraphics(width, height);
		pgBottom.beginDraw();
		pgBottom.endDraw();
		// ControlP5 实例化
		cp5 = new ControlP5(this);

	}

	public void draw() {
		background(255);
		image(pgBottom, 10, 10);
	}

	public void drawPGBottom() {
		if (psList != null) {
			pgBottom.beginDraw();
			pgBottom.clear();
			pgBottom.background(255);
			for (int i = 0; i < psList.size(); i++) {
				psList.get(i).draw(pgBottom);
				if (i + 1 < psList.size()) {
					psList.get(i).connect(pgBottom, psList.get(i + 1));
				}
			}
			pgBottom.endDraw();
		} else {
			pgBottom.beginDraw();
			pgBottom.clear();
			pgBottom.background(255);
			pgBottom.endDraw();
		}
	}

}
