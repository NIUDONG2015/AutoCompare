package cn.edu.zufe.ui;

import java.util.LinkedList;

import cn.edu.zufe.drawable.PSection;
import cn.edu.zufe.drawable.ScrollBar;
import processing.core.PApplet;
import processing.core.PGraphics;
import controlP5.*;

/**
 * Stratigraphic Correlation
 */
public class PAppletSC extends PApplet {

	private LinkedList<PSection> psList = null;
	private int width, height;
	private PGraphics pgBottom, pgScrollBar;
	ControlP5 cp5;
	private ScrollBar hScrollBar;

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
		pgBottom = createGraphics(width, 4000);
		pgBottom.beginDraw();
		pgBottom.endDraw();
		// 最底端缓存图初始化
		pgScrollBar = createGraphics(width, height);
		pgScrollBar.beginDraw();
		pgScrollBar.endDraw();
		// ControlP5 实例化
		cp5 = new ControlP5(this);
		// 滚动条
		hScrollBar = new ScrollBar(this, pgBottom, true);

	}

	public void draw() {
		background(255);
		image(pgBottom, 0, hScrollBar.getImagePos());
		drawPGScrollBar();
		image(pgScrollBar, 0, 0);
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

	
	private void drawPGScrollBar() {
		pgScrollBar.beginDraw();
		pgScrollBar.clear();
		hScrollBar.draw(pgScrollBar);
		pgScrollBar.endDraw();
	}
}
