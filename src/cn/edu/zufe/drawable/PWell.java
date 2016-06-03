package cn.edu.zufe.drawable;

import processing.core.*;
import cn.edu.zufe.model.*;

public class PWell {

	private String name; // 名字
	private double x, y; // 实际坐标
	private float px = -1, py = -1; // 绘制的中心位置
	private float pw = 10, ph = 10; // 默认的宽和高（需要自行设置）
	private PShape icon; // 油井图片(可选)

	public PWell(Well w) {
		this.name = w.getName();
		this.x = w.getX();
		this.y = w.getY();
	}

	public PWell(Well w, PShape icon) {
		this.name = w.getName();
		this.x = w.getX();
		this.y = w.getY();
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public float getPx() {
		return px;
	}

	public void setPx(float px) {
		this.px = px;
	}

	public float getPy() {
		return py;
	}

	public void setPy(float py) {
		this.py = py;
	}

	public void draw(PGraphics pg) {

		if (px != -1 && py != -1) {
			if (icon != null) {
				pg.shapeMode(PApplet.CENTER);
				pg.shape(icon, px, py, pw, ph);
			} else {
				pg.ellipseMode(PApplet.CENTER);
				pg.fill(100);
				pg.ellipse(px, py, pw, ph);
			}
		}

	}
}
