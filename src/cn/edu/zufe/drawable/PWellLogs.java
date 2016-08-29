package cn.edu.zufe.drawable;

import processing.core.PGraphics;
import cn.edu.zufe.model.DWell;
import cn.edu.zufe.model.DWellLogs;
import cn.edu.zufe.model.DWellLogsAttribute;

public class PWellLogs extends PRect {

	private DWellLogs data;

	private DWell well;

	private DWellLogsAttribute wlaMax, wlaMin;

	public void setWell(DWell well) {
		this.well = well;
	}

	public void setWlaMax(DWellLogsAttribute wlaMax) {
		this.wlaMax = wlaMax;
	}

	public void setWlaMin(DWellLogsAttribute wlaMin) {
		this.wlaMin = wlaMin;
	}

	public PWellLogs(DWellLogs data, float px, float py, float pw, float ph) {
		super(px, py, pw, ph);
		this.data = data;
	}

	public void draw(PGraphics pg) {
		pg.strokeWeight(1);
		pg.stroke(255, 0, 0);
		float ngbY = (float) well.getNgbDepth();
		double top = (int) well.getDepth()[0] - 1;
		double btm = (int) well.getDepth()[1] + 1;
		DWellLogsAttribute wla1, wla2;
		for (double i = top; i < btm; i += 0.125) {
			wla1 = data.getLogsAttribute(i);
			wla2 = data.getLogsAttribute(i + 1);
			//pg.line(px + (float) wla1.getSP1(), Generator.toPixelY(ngbY, (float) i), px + (float) wla2.getSP1(), Generator.toPixelY(ngbY, (float) (i + 0.125)));
			pg.line((float) (px + wla1.getSP().getFirst()), Generator.toPixelY(ngbY, (float) i), (float) (px +  wla2.getSP().get(0)), Generator.toPixelY(ngbY, (float) (i + 0.125)));
			
		}
	}

}
