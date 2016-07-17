package cn.edu.zufe.model;

public class DSmallLayer extends DDepth implements Cloneable {

	private double[] norDepth;
	private double nor;
	private String eleResult = null; // 电测解释结果
	private String matchResName = null;
	private boolean isTrue = true;	//标记是否为虚拟小层
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

	public boolean getIsTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}
	
	public Object clone(){
		Object o =null;
		try{
			o = super.clone();
		}catch(CloneNotSupportedException e){
			
		}
		return o;
	}
	
}
