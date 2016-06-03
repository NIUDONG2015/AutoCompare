package cn.edu.zufe.drawable;

import processing.core.*;
import cn.edu.zufe.model.*;

public class PWell {

	private String name; // 名字
	// private double x, y; // 实际坐标
	private float px = -1, py = -1; // 绘制的中心位置
	private float pw = 30, ph = 30; // 默认的宽和高（需要自行设置）
	private static float offsetX = 100, offsetY = 100, zoomOut = 200;

	public PWell(String name, float px, float py) {
		this.name = name;
		this.px = px;
		this.py = py;
		// this.icon = new PApplet().loadShape("res//oil_field.svg");
	}

	public String getName() {
		return name;
	}

	public void draw(PGraphics pg, PShape icon) {

		if (px != -1 && py != -1) {
			if (icon != null) {
				pg.shapeMode(PApplet.CENTER);
				pg.shape(icon, offsetX + px * zoomOut, offsetY + py * zoomOut, pw, ph);
			} else {
				pg.ellipseMode(PApplet.CENTER);
				pg.fill(100);
				pg.ellipse(offsetX + px * zoomOut, offsetY + py * zoomOut, pw, ph);
			}
		}

	}
}
