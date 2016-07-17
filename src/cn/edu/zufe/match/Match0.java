package cn.edu.zufe.match;

import java.util.LinkedList;

import cn.edu.zufe.model.DBigLayer;
import cn.edu.zufe.model.DSmallLayer;
import cn.edu.zufe.model.DWell;

public class Match0 {
	private LinkedList<DWell> wellList = null;
	
	public Match0(LinkedList<DWell> wList) {
		// TODO Auto-generated constructor stub
		wellList = wList;
	}

	/**
	 * 归一化（小层位置相对于整个井的深度）
	 */
	void doMatch() {
		for (int i = 0; i < wellList.size(); ++i) {
			DWell well = wellList.get(i);
			// 寻找顶部和底部
			double top = -1, btm = -1;
			// 寻找底部（以大层最深处为底）
//			for (int j = well.getBigLayers().size() - 1; j > 0; --j) {
//				double bigLayerDepth = well.getBigLayers().get(j).getDepth()[0];
//				if (bigLayerDepth != 0) {
//					btm = bigLayerDepth;
//					break;
//				}
//			}
			
			// 寻找底部（以最深小层为底）
			Outer1: // 跳出多层循环
			for (int j = well.getBigLayers().size() - 1; j > 0; --j) {
				DBigLayer bigLayer = well.getBigLayers().get(j);
				for (int k = bigLayer.getSmallLayers().size() - 1; k > 0 ; --k) {
					// 砂岩底深
					double smallLayerBtmDepth = bigLayer.getSmallLayers().get(k).getDepth()[1];
					if (smallLayerBtmDepth != 0) {
						// 砂岩底深无数据，砂岩顶深有数据的情况未作辨别
						btm = smallLayerBtmDepth;
						break Outer1;
					}
				}
			}
			
			// 寻找顶部（以最浅的小层为顶部）
			Outer2: // 跳出多层循环
			for (int j = 0; j < well.getBigLayers().size(); ++j) {
				DBigLayer bigLayer = well.getBigLayers().get(j);
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					// 砂岩顶深
					double smallLayerTopDepth = bigLayer.getSmallLayers().get(k).getDepth()[0];
					if (smallLayerTopDepth != 0) {
						// 砂岩顶深无数据，砂岩底深有数据的情况未作辨别
						top = smallLayerTopDepth;
						break Outer2;
					}
				}
			}
			if (top <= 0 || btm <= 0) {
				System.out.println(well.getName() + "-寻找油井顶部和底部时出现异常:(" + top + "," + btm + ")");
			}

			// 设置顶部和底部
			well.setDepth(new double[] { top, btm });

			/*// 开始归一化
			for (int j = 0; j < well.getBigLayers().size(); ++j) {
				DBigLayer bigLayer = well.getBigLayers().get(j);
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
					double smallLayerTopDepth = (smallLayer.getDepth()[0] - top) / (btm - top);
					if (smallLayerTopDepth < 0) {
						smallLayerTopDepth = 0;
					}
					double smallLayerBtmDepth = (smallLayer.getDepth()[1] - top) / (btm - top);
					if (smallLayerBtmDepth < 0) {
						smallLayerBtmDepth = 0;
					}
					smallLayer.setNorDepth(new double[] { smallLayerTopDepth, smallLayerBtmDepth });
				}
			}*/
		}
		//输出
//		for (int i = 0; i < wellList.size(); ++i) {
//			DWell well = wellList.get(i);
//			System.out.println("井号:" + well.getName());
//			System.out.println("顶部和底部:(" + well.getDepth()[0] + "," + well.getDepth()[1] + ")");
//			for (int j = 0; j < well.getBigLayers().size(); ++j) {
//				DBigLayer bigLayer = well.getBigLayers().get(j);
//				System.out.println("	层位:" + bigLayer.getName());
//				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
//					DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
//					System.out.println("			层位:" + smallLayer.getName() + "  归一化层顶:" + smallLayer.getNorDepth()[0] + "  归一化层底:" + smallLayer.getNorDepth()[1]);
//				}
//				System.out.println("");
//			}
//			System.out.println("");
//		}
	}
}
