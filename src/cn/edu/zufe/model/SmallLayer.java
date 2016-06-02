package cn.edu.zufe.model;

public class SmallLayer {

	private String name;
	private double[] depth;
	private double nor;
	private String eleResult;
	private String matchResName;

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

	public double getNor() {
		return nor;
	}

	public void setNor(double nor) {
		this.nor = nor;
	}

	public String getEleResult() {
		return eleResult;
	}

	public void setEleResult(String eleResult) {
		this.eleResult = eleResult;
	}

	public String getMatchResName() {
		return matchResName;
	}

	public void setMatchResName(String matchResName) {
		this.matchResName = matchResName;
	}

}
