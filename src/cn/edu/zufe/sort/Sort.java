package cn.edu.zufe.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.edu.zufe.model.BigLayer;
import cn.edu.zufe.model.SmallLayer;
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
	 * 获得两油井之间的匹配小层数
	 * 
	 * @param wellA
	 * @param wellB
	 * @return
	 */
	public int getSmallLayerMatchNum(Well wellA, Well wellB) {
		int smallLayerMatchNum = 0;
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		for (BigLayer bigLayer : wellA.getBigLayers()) {
			for (SmallLayer smallLayer : bigLayer.getSmallLayers()) {
				String matchName = smallLayer.getMatchResName();

				if (checkMatchNameExist(matchName) == false)
					continue;
				if (map.containsKey(matchName) == true)
					continue;
				map.put(matchName, false);
			}
		}

		for (BigLayer bigLayer : wellB.getBigLayers()) {
			for (SmallLayer smallLayer : bigLayer.getSmallLayers()) {
				String matchName = smallLayer.getMatchResName();

				if (checkMatchNameExist(matchName) == false)
					continue;
				if (map.containsKey(matchName) == false)
					continue;

				Boolean value = map.get(matchName);
				if (value == true)
					continue;
				++smallLayerMatchNum;
				map.remove(matchName);
				map.put(matchName, true);
			}
		}
		return smallLayerMatchNum;
	}

	/***
	 * 
	 * @param well
	 * @return 获得标准井的小层个数
	 */
	public int getStandardWellSmallLayerNum(Well standardWell) {
		int standardWellSmallLayerNum = 0;

		for (int i = 0; i < standardWell.getBigLayers().size(); ++i) {
			standardWellSmallLayerNum += standardWell.getBigLayers().get(i).getSmallLayers().size();

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

	/***
	 * 获得两口井的梯度和
	 * 
	 * @param wellA
	 * @param wellB
	 * @return
	 */
	public double getGradient(Well wellA, Well wellB) {
		double gradientSum = 0;
		ArrayList<SmallLayer> listA = new ArrayList<SmallLayer>();
		for (BigLayer bigLayer : wellA.getBigLayers()) {
			for (SmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (checkMatchNameExist(smallLayer.getMatchResName()) == true)
					listA.add(smallLayer);
			}
		}
		ArrayList<SmallLayer> listB = new ArrayList<SmallLayer>();
		for (BigLayer bigLayer : wellB.getBigLayers()) {
			for (SmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (checkMatchNameExist(smallLayer.getMatchResName()) == true)
					listB.add(smallLayer);
			}
		}

		//梯度和匹配...要结合画法
		int k = 0;
		for (int i = 0; i < listA.size(); ++i) {
			SmallLayer smallLayerA = listA.get(i);
			for (int j = k; j < listB.size(); ++j) {
				SmallLayer smallLayerB = listB.get(j);
				if (smallLayerA.getMatchResName().equals(smallLayerB.getMatchResName())) {
					k = j + 1;
					gradientSum += Math.abs(smallLayerA.getDepth()[0] - smallLayerB.getDepth()[0]);
					gradientSum += Math.abs(smallLayerA.getDepth()[1] - smallLayerB.getDepth()[1]);
					break;
				}
			}
		}
		return gradientSum;
	}

	/***
	 * 获得所有油井之间最大梯度和
	 * @return
	 */
	public double getMaxGradient(){
		double maxGradient = 0;
		
		for (int i = 0; i < cloneWellList.size(); ++i) {
			for (int j = i + 1; j < cloneWellList.size(); ++j) {
				maxGradient = Math.max(maxGradient, getGradient(cloneWellList.get(i), cloneWellList.get(j)));
			}
		}
		return maxGradient;
	}
	
	/***
	 * 判断匹配后的小层是否存在
	 * 
	 * @param matchName
	 * @return
	 */
	public boolean checkMatchNameExist(String matchName) {
		if (matchName == null || matchName.equals("") || matchName.equals("尖灭"))
			return false;
		return true;
	}
}
