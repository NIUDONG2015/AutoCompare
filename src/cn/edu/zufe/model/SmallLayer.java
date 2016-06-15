package cn.edu.zufe.model;

public class SmallLayer {

	private String name = null; // 小层位
	private double[] depth;
	private double[] norDepth;

	private double nor;
	private String eleResult = null; // 电测解释结果
	private String matchResName = null;
	public boolean beFound = false; // !!!临时Debug使用，重构时删除

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

	public double[] getNorDepth() {
		return norDepth;
	}

	public void setNorDepth(double[] norDepth) {
		this.norDepth = norDepth;
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
