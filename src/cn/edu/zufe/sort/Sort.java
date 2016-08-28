package cn.edu.zufe.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.edu.zufe.drawable.PSection;
import cn.edu.zufe.drawable.PSmallLayer;
import cn.edu.zufe.drawable.PMapWell;
import cn.edu.zufe.drawable.Generator;
import cn.edu.zufe.model.DBigLayer;
import cn.edu.zufe.model.DSmallLayer;
import cn.edu.zufe.model.DWell;

public class Sort {
	private PMapWell pStandardWell;
	private LinkedList<PMapWell> pWellList;
	private LinkedList<PMapWell> pCloneWellList;
	private LinkedList<PSection> pSectionList;
	
	public Sort(PMapWell pStandardWell, LinkedList<PMapWell> pWellList) {
		this.pStandardWell = pStandardWell;
		this.pWellList = pWellList;
		this.pCloneWellList = (LinkedList<PMapWell>) pWellList.clone();
		this.pSectionList = Generator.pWellToPSection(this.pCloneWellList);
	}

	/***
	 * 获得两油井之间的匹配小层数
	 * 
	 * @param wellA
	 * @param wellB
	 * @return
	 */
	public  int getSmallLayerMatchNum(PMapWell pWellA, PMapWell pWellB) {
		int smallLayerMatchNum = 0;
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		for (DBigLayer bigLayer : pWellA.getWell().getBigLayers()) {
			for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
				String matchName = smallLayer.getMatchResName();

				if (checkMatchNameExist(matchName) == false)
					continue;
				if (map.containsKey(matchName) == true)
					continue;
				map.put(matchName, false);
			}
		}

		for (DBigLayer bigLayer : pWellB.getWell().getBigLayers()) {
			for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
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
	public  int getStandardWellSmallLayerNum(PMapWell pStandardWell) {
		int standardWellSmallLayerNum = 0;

		for (int i = 0; i < pStandardWell.getWell().getBigLayers().size(); ++i) {
			standardWellSmallLayerNum += pStandardWell.getWell().getBigLayers().get(i).getSmallLayers().size();

		}
		return standardWellSmallLayerNum;
	}

	/***
	 * 获得两个井之间的距离
	 */
	public static double getDis(PMapWell pWellA, PMapWell pWellB) {
		return Math.sqrt((pWellA.getPX() - pWellB.getPX()) * (pWellA.getPX() - pWellB.getPX())
				+ (pWellA.getPY() - pWellB.getPY()) * (pWellA.getPY() - pWellB.getPY()));
	}

	/***
	 * 获得最大油井距离
	 * 
	 * @return
	 */
	public  double getMaxDis() {
		double maxDis = 0;
		for (int i = 0; i < pCloneWellList.size(); ++i) {
			for (int j = i + 1; j < pCloneWellList.size(); ++j) {
				maxDis = Math.max(maxDis, getDis(pCloneWellList.get(i), pCloneWellList.get(j)));
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
	public static double getGradient(PSection pSectionA, PSection pSectionB) {
		double gradientSum = 0;
		LinkedList<DSmallLayer> listA = new LinkedList<DSmallLayer>();
		for (DBigLayer bigLayer : pSectionA.getWell().getBigLayers()) {
			for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (checkMatchNameExist(smallLayer.getMatchResName()) == true)
					listA.add(smallLayer);
			}
		}
		/*LinkedList<PSmallLayer> pListA = Generator.smallLayerToPSmallLayer(pSectionA, listA);
		
		LinkedList<DSmallLayer> listB = new LinkedList<DSmallLayer>();
		for (DBigLayer bigLayer : pSectionB.getWell().getBigLayers()) {
			for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (checkMatchNameExist(smallLayer.getMatchResName()) == true)
					listB.add(smallLayer);
			}
		}
		LinkedList<PSmallLayer> pListB = Generator.smallLayerToPSmallLayer(pSectionB, listB);
		
		//梯度和匹配...结合画法
		int k = 0;
		for (int i = 0; i < pListA.size(); ++i) {
			PSmallLayer pSmallLayerA = pListA.get(i);
			for (int j = k; j < pListB.size(); ++j) {
				PSmallLayer pSmallLayerB = pListB.get(j);
				if (pSmallLayerA.getData().getMatchResName().equals(pSmallLayerB.getData().getMatchResName())) {
					k = j + 1;
					gradientSum += Math.abs(pSmallLayerA.getPy() - pSmallLayerB.getPy());
					gradientSum += Math.abs(pSmallLayerA.getPy() - pSmallLayerB.getPy() + pSmallLayerA.getPh() - pSmallLayerB.getPh());
					break;
				}
			}
		}*/
		return gradientSum;
	}

	/***
	 * 获得所有油井之间最大梯度和
	 * @return
	 */
	public  double getMaxGradient(){
		double maxGradient = 0;
		
		for (int i = 0; i < pSectionList.size(); ++i) {
			for (int j = i + 1; j < pSectionList.size(); ++j) {
				maxGradient = Math.max(maxGradient, getGradient(pSectionList.get(i), pSectionList.get(j)));
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
	public static boolean checkMatchNameExist(String matchName) {
		if (matchName == null || matchName.equals("") || matchName.equals("尖灭"))
			return false;
		return true;
	}
}
