package cn.edu.zufe.model;

import java.util.LinkedList;

public class DBigLayer extends DDepth {

	private LinkedList<DSmallLayer> smallLayers = new LinkedList<DSmallLayer>();

	public LinkedList<DSmallLayer> getSmallLayers() {
		return smallLayers;
	}

	public void setSmallLayers(LinkedList<DSmallLayer> smallLayers) {
		this.smallLayers = smallLayers;
	}
}
