package cn.edu.zufe.drawable;

import processing.core.*;
import cn.edu.zufe.model.*;

public class PWell {

	private String name; // ����
	// private double x, y; // ʵ������
	private float px = -1, py = -1; // ���Ƶ�����λ��
	private float pw = 30, ph = 30; // Ĭ�ϵĿ�͸ߣ���Ҫ�������ã�
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
