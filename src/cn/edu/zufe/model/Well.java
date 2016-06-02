package cn.edu.zufe.model;

import java.util.LinkedList;

public class Well {

	private String name;
	private double x;
	private double y;
	private LinkedList<BigLayer> bigLayers = new LinkedList<BigLayer>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public LinkedList<BigLayer> getBigLayers() {
		return bigLayers;
	}

	public void setBigLayers(LinkedList<BigLayer> bigLayers) {
		this.bigLayers = bigLayers;
	}
}
