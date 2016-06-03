package cn.edu.zufe.model;

import java.util.LinkedList;

public class BigLayer {

	private String name = null;	//´ó²ãÎ»
	private double[] depth = null;	//µ×Éî
	private LinkedList<SmallLayer> smallLayers = new LinkedList<SmallLayer>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getDepth() {
		return depth;
	}

	public void setDepth(double[] depth) {
		this.depth = depth;
	}

	public LinkedList<SmallLayer> getSmallLayers() {
		return smallLayers;
	}

	public void setSmallLayers(LinkedList<SmallLayer> smallLayers) {
		this.smallLayers = smallLayers;
	}
}
