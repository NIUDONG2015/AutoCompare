package cn.edu.zufe.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.LinkedList;

import cn.edu.zufe.drawable.PSection;
import cn.edu.zufe.drawable.ScrollBar;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;
import controlP5.*;

/**
 * Stratigraphic Correlation
 */
public class PAppletSC extends PApplet {

	private LinkedList<PSection> psList = null;
	public static int width, height;
	private PGraphics pgBottom, pgScrollBar;
	ControlP5 cp5;
	private ScrollBar hScrollBar, vScrollBar;

	public PAppletSC(int w, int h) {
		width = w;
		height = h;
	}

	public void setPSList(LinkedList<PSection> psList) {
		this.psList = psList;
	}

	public void setup() {
		size(width, height);
		// 最底端缓存图初始化
		pgBottom = createGraphics(width - ScrollBar.size, 4001);
		pgBottom.beginDraw();
		pgBottom.endDraw();
		// 滚动条缓存图初始化
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		pgScrollBar = createGraphics((int) screensize.getWidth(), (int) screensize.getHeight());
		pgScrollBar.beginDraw();
		pgScrollBar.endDraw();
		// ControlP5 实例化
		cp5 = new ControlP5(this);
		// 滚动条
		vScrollBar = new ScrollBar(this, pgBottom, true);
		hScrollBar = new ScrollBar(this, pgBottom, false);
	}

	public void draw() {
		vScrollBar.setBarPos();
		hScrollBar.setBarPos();
		
		background(255);
		image(pgBottom, hScrollBar.getImagePos(), vScrollBar.getImagePos());
		drawPGScrollBar();
		image(pgScrollBar, 0, 0);
	}

	public void mouseWheel(MouseEvent event) {
		vScrollBar.mouseWheel(event.getCount());
	}

	public void drawPGBottom() {
		if (psList != null) {
			pgBottom.beginDraw();
			pgBottom.clear();
			pgBottom.background(255);
			pgBottom.stroke(0);
			pgBottom.rect(0, 0, pgBottom.width - 1, pgBottom.height - 1); // 用于判断边界
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
			pgBottom.stroke(0);
			pgBottom.rect(0, 0, pgBottom.width - 1, pgBottom.height - 1); // 用于判断边界
			pgBottom.endDraw();
		}
	}

	private void drawPGScrollBar() {
		pgScrollBar.beginDraw();
		pgScrollBar.clear();
		hScrollBar.draw(pgScrollBar);
		vScrollBar.draw(pgScrollBar);
		pgScrollBar.endDraw();
		
	}

	public void savePGBottom(String fileName) {
		pgBottom.save(fileName);
	}

	public void reSize() {

	}
}
