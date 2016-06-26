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
		pg.fill(100);
		pg.rect(px, py, pw, ph);
		pg.fill(255, 0, 0);
		pg.text(data.getName() + "£º" + data.getMatchResName(), px + pw + 3, py + ph / 2);
		pg.fill(255);
	}

	public void connect(PGraphics pg, PSmallLayer psOther) {
		pg.fill(255);
		pg.stroke(0);
		pg.line(px + pw, py, psOther.getPx(), psOther.getPy());
		pg.line(px + pw, py + ph, psOther.getPx(), psOther.getPy() + psOther.getPh());
	}

	public boolean compare(PSmallLayer psOther) {
		if (!data.getMatchResName().equals("¼âÃð") && data.getMatchResName().equals(psOther.getData().getMatchResName())) {
			return true;
		} else {
			return false;
		}
	}
}
