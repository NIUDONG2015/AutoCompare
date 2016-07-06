package cn.edu.zufe.drawable;

import processing.core.PGraphics;
import cn.edu.zufe.model.DWell;
import cn.edu.zufe.model.DWellLogs;

public class PWellLogs extends PRect {

	DWellLogs data;

	DWell well;

	public void setWell(DWell well) {
		this.well = well;
	}

	public PWellLogs(DWellLogs data, float px, float py, float pw, float ph) {
		super(px, py, pw, ph);
		this.data = data;
	}

	public void draw(PGraphics pg) {

	}

}
