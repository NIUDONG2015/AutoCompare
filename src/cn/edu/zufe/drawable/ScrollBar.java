package cn.edu.zufe.drawable;

import processing.core.*;

public class ScrollBar {

	private PApplet p;
	private PGraphics pg;
	// private float offset, length; // 滚动条偏移的长度，和滚动条本身的长度
	private boolean VorH; // 记录是什么类型的滚动条（垂直Or水平）
	private float x, y, w, h; // 滚动条具体位置信息
	private float colorGray = 205;
	private boolean locked; // 是否已经选中滚动条

	public ScrollBar(PApplet p, PGraphics pg, boolean VorH) {
		this.p = p;
		this.pg = pg;
		this.VorH = VorH;
		// 水平滚动条和垂直滚动条的参数有些区别
		// 初始化滚动条参数
		if (VorH) {
			h = p.height * p.height / pg.height;
			w = 20;
			x = p.width - w - 5;
			y = 0;
		} else {
			h = 20;
			w = p.width * p.width / pg.width;
			x = 0;
			y = p.height - h;
		}

	}

	public void draw(PGraphics tpg) {
		tpg.noStroke();
		tpg.fill(colorGray);
		tpg.rect(x, y, w, h);
		update();
	}

	public void update() {
		if (p.mousePressed && collisionDetection()) {
			colorGray = 96; // 点击时改变颜色
			locked = true;
		}
		if (!p.mousePressed) {
			colorGray = 205;
			locked = false;
		}

		if (locked) {
			// System.out.println(p.mouseY + " : " + p.pmouseY);
			if (VorH) {
				y += p.mouseY - p.pmouseY;
				// 防止越界
				if (y < 0) {
					y = 0;
				}
				if (y > p.height - h) {
					y = p.height - h;
				}
			} else {
				x += p.mouseX - p.pmouseX;
				// 防止越界
				if (x < 0) {
					x = 0;
				}
				if (x > p.width - w) {
					x = p.width - w;
				}
			}
		}
	}

	private boolean collisionDetection() {
		if (p.mouseX > x - w && p.mouseX < x + w) {
			if (p.mouseY > y - h && p.mouseY < y + h) {
				return true;
			}
		}
		return false;
	}

	public float getImagePos() {
		if(VorH) {
			return - y * pg.height / p.height;
		} else {
			return - x * pg.width / p.width;
		}
	}
}
