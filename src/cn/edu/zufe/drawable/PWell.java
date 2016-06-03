package cn.edu.zufe.drawable;

import processing.core.*;

public class PWell {

	private String name; // 名字
	private float px = -1, py = -1; // 绘制的中心位置
	private float pw = 30, ph = 30; // 默认的宽和高（需要自行设置）
	private static float offsetX = 100, offsetY = 100, zoomOut = 200; // 偏移以及放大参数
	private boolean clicked = false;

	/**
	 * 构造函数
	 * 
	 * @param name
	 *            油井名
	 * @param norX
	 *            归一化后的X坐标值
	 * @param norY
	 *            归一化后的Y坐标值
	 */
	public PWell(String name, float norX, float norY) {
		this.name = name;
		this.px = offsetX + norX * zoomOut;
		this.py = offsetY + norY * zoomOut;
	}

	public String getName() {
		return name;
	}

	public void draw(PGraphics pg, PShape iconOrigin, PShape iconClicked) {

		if (px != -1 && py != -1) {
			PShape shape;
			if(clicked) {
				pg.fill(240, 0, 0);
				shape = iconClicked;
			} else {
				pg.fill(240, 90, 80);
				shape = iconOrigin;
			}
			
			if (iconOrigin != null) {
				pg.shapeMode(PApplet.CENTER);
				pg.shape(shape, px, py, pw, ph);
			} else {
				pg.ellipseMode(PApplet.CENTER);
				pg.ellipse(px, py, pw, ph);
			}
		}

	}
	
	public void highlight(PGraphics pg, PShape iconOrigin, PShape iconClicked) {

		float ratio = 1.5f; // 突出显示的系数
		PShape shape;
		if(clicked) {
			pg.fill(240, 0, 0);
			shape = iconClicked;
		} else {
			pg.fill(240, 90, 80);
			shape = iconOrigin;
		}
		
		if (px != -1 && py != -1) {			
			if (iconOrigin != null) {
				pg.fill(255);
				pg.noStroke();
				pg.rectMode(PApplet.CENTER);
				pg.rect(px, py, pw, ph);
				pg.shapeMode(PApplet.CENTER);
				pg.shape(shape, px, py, pw * ratio, ph * ratio);
			} else {
				pg.ellipseMode(PApplet.CENTER);
				pg.ellipse(px, py, pw * ratio, ph * ratio);
			}
		}

	}

	/**
	 * 是否与鼠标碰撞
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean collisionDetection(int mouseX, int mouseY) {
		if (mouseX > px - pw / 2 && mouseX < px + pw / 2) {
			if (mouseY > py - ph / 2 && mouseY < py + ph / 2) {
				return true;
			}
		}
		return false;
	}

	public void setClicked(boolean b) {
		this.clicked = b;
	}
}
