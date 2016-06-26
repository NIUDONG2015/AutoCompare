package cn.edu.zufe.drawable;

import cn.edu.zufe.model.DDepth;
import cn.edu.zufe.model.DWell;

public class PSectionWell extends PRect {
	
	private DWell data;
	
	public DWell getData() {
		return data;
	}

	public PSectionWell(DDepth data,float px, float py, float pw, float ph) {
		super(px, py, pw, ph);
		this.data = (DWell) data;
	}

}
