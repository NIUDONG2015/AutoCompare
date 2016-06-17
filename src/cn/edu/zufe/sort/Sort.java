package cn.edu.zufe.sort;

import java.util.LinkedList;

import cn.edu.zufe.model.Well;

public class Sort {
	private Well standardWell;
	private LinkedList<Well> wellList;
	private LinkedList<Well> cloneWellList;

	public Sort(Well standardWell, LinkedList<Well> wellList) {
		this.standardWell = standardWell;
		this.wellList = wellList;
		cloneWellList = (LinkedList<Well>) wellList.clone();
	}
	
	/***
	 * 
	 * @param well
	 * @return
	 * 		获得
	 */
	public int getStandardWellSmallLayerNum(Well standardWell){
		int standardWellSmallLayerNum = 0;
		
		for(int i=0; i<standardWell.getBigLayers().size(); ++i){
			
			//for(int j=0; j<standardWell.get)
			
		}
		return standardWellSmallLayerNum;
	}
	/***
	 * 获得两个井之间的距离
	 */
	public double getDis(Well wellA, Well wellB) {
		return Math.sqrt((wellA.getX() - wellB.getX()) * (wellA.getX() - wellB.getX())
				+ (wellB.getY() - wellB.getY() * (wellB.getY() - wellB.getY())));
	}

	/***
	 * 获得最大油井距离
	 * 
	 * @return
	 */
	public double getMaxDis() {
		double maxDis = 0;
		for (int i = 0; i < cloneWellList.size(); ++i) {
			for (int j = i + 1; j < cloneWellList.size(); ++j) {
				maxDis = Math.max(maxDis, getDis(cloneWellList.get(i), cloneWellList.get(j)));
			}
		}
		return maxDis;
	}

}
