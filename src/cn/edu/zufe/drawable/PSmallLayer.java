package cn.edu.zufe.drawable;

import processing.core.PGraphics;
import cn.edu.zufe.model.SmallLayer;

public class PSmallLayer {

	private PSmallLayer nextPSmallLayer, prevPSmallLayer;
	private PSection ps;
	private boolean found;
	private SmallLayer smallLayer;
	private float px, py, ph, pw;

	public float getPx() {
		return px;
	}

	public float getPy() {
		return py;
	}

	public float getPh() {
		return ph;
	}

	public PSection getPs() {
		return ps;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public SmallLayer getSmallLayer() {
		return smallLayer;
	}

	public PSmallLayer(SmallLayer sl, PSection ps) {
		this.smallLayer = sl;
		this.ps = ps;
		this.px = ps.getpx();
		this.py = ps.getpy() + ps.getph() * (float) smallLayer.getNorDepth()[0];
		this.pw = ps.getpw();
		this.ph = ps.getph() * (float) (smallLayer.getNorDepth()[1] - smallLayer.getNorDepth()[0]);
	}

	public void draw(PGraphics pg) {
		pg.fill(100);
		pg.rect(px, py, pw, ph);
		pg.fill(255, 0, 0);
		pg.text(smallLayer.getName() + "£º" + smallLayer.getMatchResName(), px + pw + 3, py + ph / 2);
		pg.fill(255);
	}

	public void connect(PGraphics pg, PSmallLayer psl) {
		pg.fill(255);
		pg.stroke(0);
		pg.line(px + pw, py, psl.getPx(), psl.getPy());
		pg.line(px + pw, py + ph, psl.getPx(), psl.getPy() + psl.getPh());
	}

	public boolean compare(PSmallLayer psl) {
		if (smallLayer.getMatchResName().equals(psl.getSmallLayer().getMatchResName())) {
			return true;
		} else {
			return false;
		}
	}
}
