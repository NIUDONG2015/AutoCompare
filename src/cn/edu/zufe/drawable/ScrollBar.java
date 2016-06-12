package cn.edu.zufe.drawable;

import processing.core.*;

public class ScrollBar {

	private PApplet p;
	private PGraphics pg;
	private float offset, length, size; // 矩形滚动条容器的参数
	private boolean VorH; // 记录是什么类型的滚动条（垂直Or水平）
	private float x, y, w, h; // 滚动条具体位置信息
	private float colorGray = 205;
	private boolean locked; // 是否已经选中滚动条

	public ScrollBar(PApplet p, PGraphics pg, boolean VorH) {
		this.p = p;
		this.pg = pg;
		this.VorH = VorH;
		size = 20;
		offset = 0;
		// 水平滚动条和垂直滚动条的参数有些区别
		// 初始化滚动条参数
		if (VorH) {
			length = p.height - offset * 2 - size;
			h = length * length / pg.height;
			w = size;
			x = p.width - w;
			y = offset;
		} else {
			length = p.width - offset * 2 - size;
			h = size;
			w = length * length / pg.width;
			x = offset;
			y = p.height - h;
		}

	}

	public void draw(PGraphics tpg) {
		tpg.noStroke();
		// 绘制滚动条容器
		tpg.fill(240);
		// 此处容器矩形参数可参考上方“初始滚动条参数”
		if (VorH) {
			tpg.rect(p.width - w, 0, size, p.height);
		} else {
			tpg.rect(0, p.height - h, p.width, size);
		}
		// 绘制滚动条
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
				if (y < offset) {
					y = offset;
				}
				if (y > offset + length - h) {
					y = offset + length - h;
				}
			} else {
				x += p.mouseX - p.pmouseX;
				// 防止越界
				if (x < offset) {
					x = offset;
				}
				if (x > offset + length - w) {
					x = offset + length - w;
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
		if (VorH) {
			return -(y - offset) * pg.height / length;
		} else {
			return -(x - offset) * pg.width / length;
		}
	}
}
