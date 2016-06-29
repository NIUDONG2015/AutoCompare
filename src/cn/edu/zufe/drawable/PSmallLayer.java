package cn.edu.zufe.drawable;

import processing.core.PGraphics;
import cn.edu.zufe.model.DDepth;
import cn.edu.zufe.model.DSmallLayer;

public class PSmallLayer extends PRect {

	private DSmallLayer data;
	private boolean found;

	public DSmallLayer getData() {
		return data;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public PSmallLayer(DDepth data, float px, float py, float pw, float ph) {
		super(px, py, pw, ph);
		this.data = (DSmallLayer) data;
	}

	public void draw(PGraphics pg) {
		// 此处可以优化为枚举类型
		if (data.getEleResult().equals("水层")) {
			pg.fill(1, 176, 241);
			pg.rect(px, py, pw, ph);
		} else if (data.getEleResult().equals("油水同层")) {
			pg.fill(250, 0, 0);
			pg.triangle(px, py, px + pw, py, px, py + ph);
			pg.fill(1, 176, 241);
			pg.triangle(px + pw, py + ph, px + pw, py, px, py + ph);
		} else if (data.getEleResult().equals("油层")) {
			pg.fill(250, 0, 0);
			pg.rect(px, py, pw, ph);
		} else if (data.getEleResult().equals("干层")) {
			pg.noFill();
			pg.rect(px, py, pw, ph);
			int count = (int) (pw / 4);
			for (int i = 1; i < count; i++) {
				pg.line(px + i * 4, py, px + i * 4, py + ph);
			}
		} else if (data.getEleResult().equals("含油水层")) {
			pg.fill(1, 176, 241);
			pg.rect(px, py, pw, ph);
			pg.fill(250, 0, 0);
			pg.quad(px + pw / 7 * 2, py, px + pw / 7 * 3, py, px + pw / 7 * 2, py + ph, px + pw / 7, py + ph);
			pg.quad(px + pw / 7 * 5, py, px + pw / 7 * 6, py, px + pw / 7 * 5, py + ph, px + pw / 7 * 4, py + ph);
		} else {
			pg.noFill();
			pg.rect(px, py, pw, ph);
		}
		// 显示匹配结果
		// pg.fill(255, 0, 0);
		// pg.text(data.getName() + "：" + data.getMatchResName(), px + pw + 3,
		// py + ph / 2);
		// pg.fill(255);
	}

	/**
	 * 向右连接小层
	 * @param pg
	 * @param psOther
	 */
	public void connect(PGraphics pg, PSmallLayer psOther) {
		pg.fill(255);
		pg.stroke(0);
		pg.line(px + pw, py, psOther.getPx(), psOther.getPy());
		pg.line(px + pw, py + ph, psOther.getPx(), psOther.getPy() + psOther.getPh());
	}

	/**
	 * 连接边缘
	 * @param pg
	 * @param leftOrRight
	 */
	public void connectNull(PGraphics pg, boolean leftOrRight) {
		pg.fill(255);
		pg.stroke(0);
		if (leftOrRight) {
			pg.line(px, py, 0, py);
			pg.line(px, py + ph, 0, py + ph);
		} else {
			pg.line(px, py, pg.width, py);
			pg.line(px, py + ph, pg.width, py + ph);
		}
	}

	/**
	 * 比较小层匹配结果
	 * @param psOther
	 * @return
	 */
	public boolean compare(PSmallLayer psOther) {
		if (!data.getMatchResName().equals("尖灭") && data.getMatchResName().equals(psOther.getData().getMatchResName())) {
			return true;
		} else {
			return false;
		}
	}
}
