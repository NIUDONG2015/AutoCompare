package cn.edu.zufe.drawable;

import java.text.DecimalFormat;
import java.util.LinkedList;

import processing.core.*;
import cn.edu.zufe.model.*;

public class PMapWell {

	private DWell well;
	private float px = -1, py = -1; // 绘制的中心位置
	private float pw = 30, ph = 30; // 默认的宽和高（需要自行设置）
	public final static float OFFSET_X = 100, OFFSET_Y = 100, ZOOM_OUT = 200; // 偏移以及放大参数
	private boolean clicked = false;

	/**
	 * 构造函数
	 * 
	 * @param well
	 *            油井
	 * @param norX
	 *            归一化后的X坐标值
	 * @param norY
	 *            归一化后的Y坐标值
	 */
	public PMapWell(DWell well, float px, float py) {
		this.well = well;
		this.px = px;
		this.py = py;
	}

	public DWell getWell() {
		return well;
	}

	/**
	 * 画出
	 * 
	 * @param pg
	 *            缓存图
	 * @param iconOrigin
	 *            原图标
	 * @param iconClicked
	 *            点击后的图标
	 */
	public void draw(PGraphics pg, PShape iconOrigin, PShape iconClicked) {

		if (px != -1 && py != -1) {
			PShape shape;
			if (clicked) {
				pg.fill(0); // 点击时的颜色
				shape = iconClicked;
				// drawInfo(pg, 10, 10); // 点击时显示信息
			} else {
				pg.fill(100); // 不点击时的颜色
				shape = iconOrigin;
			}

			if (shape != null) {
				pg.shapeMode(PApplet.CENTER);
				pg.shape(shape, px, py, pw, ph);
			} else {
				pg.ellipseMode(PApplet.CENTER);
				pg.ellipse(px, py, pw, ph);
			}
		}

	}
	
	/***
	 * 画出最小生成树路径 
	 * 
	 * @param pg
	 * 		缓存图
	 * @param pw
	 * 		起始油井
	 * @param pwList
	 * 		所有油井
	 */
	public void drawMSTPath(PGraphics pg, PMapWell pw, LinkedList<PMapWell> pwList){
		
		boolean[][] mp = new boolean[pwList.size()][pwList.size()];
		
		for(int i=0; i<pwList.size(); ++i){
			for(int j=0; j<pwList.size(); ++j){
				if(mp[i][j]){
					
				}
			}
		}
	}
	
	/**
	 * 高亮
	 * 
	 * @param pg
	 *            缓存图
	 * @param iconOrigin
	 *            原图标
	 * @param iconClicked
	 *            点击后的图标
	 */
	public void highlight(PGraphics pg, PShape iconOrigin, PShape iconClicked) {

		float ratio = 1.5f; // 突出显示的系数
		PShape shape;
		if (clicked) {
			pg.fill(0); // 点击时的颜色
			shape = iconClicked;
		} else {
			pg.fill(100); // 不点击时的颜色
			shape = iconOrigin;
		}

		if (px != -1 && py != -1) {
			if (shape != null) {
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
		// 高亮时显示信息
		drawInfo(pg, 10, 10);
	}

	/**
	 * 显示相关信息
	 * 
	 * @param pg
	 * @param x
	 * @param y
	 */
	private void drawInfo(PGraphics pg, float x, float y) {
		int txtSize = 12; // 设置字体大小
		// 遮挡块
		pg.fill(255);
		pg.stroke(255);
		pg.rectMode(PApplet.CORNER);
		pg.rect(x, y, 110, txtSize * 3);
		// 画上文字
		pg.fill(0);
		pg.textSize(txtSize);
		pg.text("Name: " + well.getName(), x, y + txtSize);
		// 使用DecimalFormat格式化double类型，防止以科学记数法表示
		DecimalFormat df = new DecimalFormat("0.00");
		pg.text("X:" + df.format(well.getX()), x, y + txtSize * 2);
		pg.text("Y:" + df.format(well.getY()), x, y + txtSize * 3);
	}

	/**
	 * 是否与鼠标碰撞（矩形检测）
	 * 
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

	public void setClicked() {
		this.clicked = !clicked;
	}
	
	public double getPX(){
		return this.px;
	}
	
	public double getPY(){
		return this.py;
	}
}
