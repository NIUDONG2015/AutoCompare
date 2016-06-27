package cn.edu.zufe.ui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.LinkedList;

import cn.edu.zufe.drawable.PSection;
import cn.edu.zufe.drawable.ScrollBar;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;

/**
 * Stratigraphic Correlation
 */
public class PAppletSC extends PApplet implements ComponentListener {

	private LinkedList<PSection> psList = null;
	public static int width, height;
	private PGraphics pgBottom;
	private ScrollBar hScrollBar, vScrollBar;
	private int psListSizeNow = -1; // 用于判断psList.Size()有没有变化，变化则重建pg

	public PAppletSC(int w, int h) {
		width = w;
		height = h;
		addComponentListener(this);
	}

	public void setPSList(LinkedList<PSection> psList) {
		this.psList = psList;
	}

	public void setup() {
		size(width, height);
		// 最底端缓存图初始化
		pgBottom = createGraphics(width - ScrollBar.SIZE, height - ScrollBar.SIZE);
		pgBottom.beginDraw();
		pgBottom.endDraw();
		// 滚动条声明
		vScrollBar = new ScrollBar(this, pgBottom, true);
		hScrollBar = new ScrollBar(this, pgBottom, false);
	}

	public void draw() {
		background(255);
		if (pgBottom != null) {
			image(pgBottom, hScrollBar.getImagePos(), vScrollBar.getImagePos());
		}
		// 滚动条绘出
		drawPGScrollBar();
	}

	public void mouseWheel(MouseEvent event) {
		// 滚动滚轮时的响应
		vScrollBar.mouseWheel(event.getCount());
	}

	public void drawPGBottom() {
		noLoop();
		if (psList != null) {
			for (int i = 0; i < psList.size(); i++) {
				psList.get(i).setWellWidth();
				psList.get(i).setPixRatio();
			}
		}
		// 根据 PSection 重新设置还存图大小
		if (psList != null && psList.size() > 0 && psList.size() != psListSizeNow) {
			psListSizeNow = psList.size();
			PSection lastPSection = psList.getLast();
			int newWidth = (int) (lastPSection.getPx() + lastPSection.getPw());
			int newHeight = (int) (lastPSection.getPh());
			pgBottom.dispose();
			pgBottom = createGraphics(newWidth, newHeight);
		}

		pgBottom.beginDraw();
		pgBottom.clear();
		pgBottom.background(255);
		pgBottom.stroke(0);
		pgBottom.rect(0, 0, pgBottom.width - 1, pgBottom.height - 1); // 用于判断边界
		// PSection 绘出
		if (psList != null) {

			for (int i = 0; i < psList.size(); i++) {
				psList.get(i).draw(pgBottom);
				if (i + 1 < psList.size()) {
					psList.get(i).connect(pgBottom, psList.get(i + 1));
				}
			}
			pgBottom.endDraw();
		}
		pgBottom.endDraw();

		// 改变pgBottom大小时重新设置滚动条大小
		if (vScrollBar != null) {
			vScrollBar.setBarPos(pgBottom);
		}
		if (hScrollBar != null) {
			hScrollBar.setBarPos(pgBottom);
		}
		loop();
	}

	private void drawPGScrollBar() {
		hScrollBar.draw(this);
		vScrollBar.draw(this);
	}

	public void savePGBottom(String fileName) {
		pgBottom.save(fileName);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void componentResized(ComponentEvent e) {
		noLoop();
		// 改变组件大小时重新设置滚动条大小
		if (vScrollBar != null) {
			vScrollBar.setBarPos(pgBottom);
		}
		if (hScrollBar != null) {
			hScrollBar.setBarPos(pgBottom);
		}
		loop();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO 自动生成的方法存根

	}
}
