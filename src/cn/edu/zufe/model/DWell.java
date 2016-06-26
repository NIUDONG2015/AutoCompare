package cn.edu.zufe.model;

import java.util.LinkedList;

public class DWell extends DDepth {

	private double x;
	private double y;
	private double ngbDepth = -1;
	private DWellLogs wellLogs;
	private LinkedList<DBigLayer> bigLayers = new LinkedList<DBigLayer>();

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public DWellLogs getWellLogs() {
		return wellLogs;
	}

	public void setWellLogs(DWellLogs wellLogs) {
		this.wellLogs = wellLogs;
	}

	public LinkedList<DBigLayer> getBigLayers() {
		return bigLayers;
	}

	public void setBigLayers(LinkedList<DBigLayer> bigLayers) {
		this.bigLayers = bigLayers;
	}

	public double getNgbDepth() {
		if (ngbDepth == -1) {
			for (DBigLayer bigLayer : bigLayers) {
				if (bigLayer.getName().equals("Ngb")) {
					// 这里取Ngb底对齐
					ngbDepth = bigLayer.getDepth()[0];
					break;
				}
			}
			return ngbDepth;
		} else {
			return ngbDepth;
		}
	}
}
