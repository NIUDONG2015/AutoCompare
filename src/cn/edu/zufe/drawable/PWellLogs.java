package cn.edu.zufe.drawable;

import processing.core.PGraphics;
import cn.edu.zufe.model.DWellLogs;

public class PWellLogs extends PRect {

	DWellLogs data;
	PSectionWell pWell;

	public void setPWell(PSectionWell pWell) {
		this.pWell = pWell;
	}

	public PWellLogs(DWellLogs data, float px, float py, float pw, float ph) {
		super(px, py, pw, ph);
		this.data = data;
	}
	
	public void draw(PGraphics pg) {
		
	}

}
