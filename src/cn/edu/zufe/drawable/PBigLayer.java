package cn.edu.zufe.drawable;

import processing.core.PGraphics;
import cn.edu.zufe.model.DBigLayer;
import cn.edu.zufe.model.DDepth;

public class PBigLayer extends PRect {

	private DBigLayer data;

	public DBigLayer getData() {
		return data;
	}

	public PBigLayer(DDepth data, float px, float py, float pw, float ph) {
		super(px, py, pw, ph);
		this.data = (DBigLayer) data;
	}

	public void draw(PGraphics pg) {
		pg.noFill();
		pg.rect(px, py, pw, ph);
	}

}
