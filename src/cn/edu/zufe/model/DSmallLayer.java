package cn.edu.zufe.model;

public class DSmallLayer extends DDepth {

	private double[] norDepth;
	private double nor;
	private String eleResult = null; // 电测解释结果
	private String matchResName = null;

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
