package cn.edu.zufe.match;

import java.util.LinkedList;

/**
 * @author tangcheng1
 */
import cn.edu.zufe.model.DBigLayer;
import cn.edu.zufe.model.DSmallLayer;
import cn.edu.zufe.model.DWell;

public class Match2 implements Cloneable {
	DWell standardWell;
	DWell doWell;

	public Match2(DWell stdWell, DWell matWell) {
		standardWell = stdWell;
		doWell = matWell;
	}

	/**
	 * 两口井之间的匹配
	 * 
	 * @param standardWell
	 *            标准井
	 * @param doWell
	 *            待匹配井
	 */
	public void doMatch(boolean isFirstElement) {
		if (isFirstElement) {
			clearNotTrueSmallLayer(standardWell);
			// 归一化处理标准井
			for (int i = 0; i < standardWell.getBigLayers().size(); ++i) {
				DBigLayer bigLayer = standardWell.getBigLayers().get(i);
				double top, btn;
				if (bigLayer.getSmallLayers().size() > 0) {
					top = bigLayer.getSmallLayers().get(0).getDepth()[0];
					btn = bigLayer.getDepth()[0];
					for (int j = 0; j < bigLayer.getSmallLayers().size() - 1; ++j) {
						DSmallLayer smallLayer = bigLayer.getSmallLayers().get(j);

						DSmallLayer smallLayerNext = bigLayer.getSmallLayers().get(j + 1);
						double nor = ((smallLayer.getDepth()[1] + smallLayerNext.getDepth()[0]) / 2 - top)
								/ (btn - top);
						smallLayer.setNor(nor);
						smallLayer.setMatchResName(smallLayer.getName());
					}
					bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1).setNor(1);
					bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1).setMatchResName(
							bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1).getName());
				}
			}
		}
		// 归一化处理待匹配井
		clearNotTrueSmallLayer(doWell);

		for (int j = 0; j < doWell.getBigLayers().size(); ++j) {
			DBigLayer bigLayer = doWell.getBigLayers().get(j);
			if (bigLayer.getDepth()[0] == 0)
				continue;
			if (bigLayer.getSmallLayers().size() > 0) {
				double top;
				if (j > 0)
					top = doWell.getBigLayers().get(j - 1).getDepth()[0];
				else
					top = bigLayer.getSmallLayers().get(0).getDepth()[0];
				double btn = 0;
				if (bigLayer.getDepth()[0] != 0)
					btn = bigLayer.getDepth()[0];
				else {
					int l = bigLayer.getSmallLayers().size() - 1;
					while (btn == 0) {
						btn = bigLayer.getSmallLayers().get(l).getDepth()[1];
						--l;
						if (l == 0)
							break;
					}
				}
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);

					double nor = ((smallLayer.getDepth()[0] + smallLayer.getDepth()[1]) / 2 - top) / (btn - top);
					smallLayer.setNor(nor);
				}
			}
		}
		// 深度匹配
		for (int j = 0; j < doWell.getBigLayers().size(); ++j) {
			DBigLayer bigLayer = doWell.getBigLayers().get(j);
			DBigLayer standardBigLayer = standardWell.getBigLayers().get(j);
			int l = 0;
			if (bigLayer.getDepth()[0] == 0) {
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
					smallLayer.setMatchResName("尖灭");
				}
			} else {
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
					for (int m = l; m < standardBigLayer.getSmallLayers().size() - 1; ++m) {
						double nor1 = standardBigLayer.getSmallLayers().get(m).getNor();
						double nor2 = standardBigLayer.getSmallLayers().get(m + 1).getNor();
						if (smallLayer.getDepth()[0] == 0.0)
							smallLayer.setMatchResName("尖灭");
						else if (m == 0 && smallLayer.getNor() < nor1)
							smallLayer.setMatchResName(standardBigLayer.getSmallLayers().get(0).getName());
						else if (smallLayer.getNor() > nor1 && smallLayer.getNor() < nor2)
							smallLayer.setMatchResName(standardBigLayer.getSmallLayers().get(m).getName());
					}
				}
			}
		}
		// 补全带匹配井虚拟小层，添加到最后就好了
		// 在井数据中添加一个Istrue字段 区分虚拟的与真实存在的
		LinkedList<String> doWellMatchResName = new LinkedList<String>();
		// 获得匹配层的所有匹配结果
		for (DBigLayer bigLayer : doWell.getBigLayers()) {
			for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (smallLayer.getMatchResName() == null || smallLayer.getMatchResName().equals("")
						|| smallLayer.getMatchResName().equals("尖灭")
						|| doWellMatchResName.contains(smallLayer.getMatchResName())) {
					continue;
				}
				doWellMatchResName.add(smallLayer.getMatchResName());
			}
		}

		// 将标准井中未匹配到结果的小层加入到链表中
		LinkedList<DSmallLayer> lastSmallLayerList = new LinkedList<DSmallLayer>();
		for (DBigLayer bigLayer : standardWell.getBigLayers()) {
			for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (smallLayer.getName() == null || smallLayer.getName().equals("") || smallLayer.getName().equals("尖灭")
						|| doWellMatchResName.contains(smallLayer.getName())) {
					continue;
				}
				DSmallLayer tsmallLayer = (DSmallLayer) smallLayer.clone();

				lastSmallLayerList.add(tsmallLayer);
				// doWell.getBigLayers().getLast().getSmallLayers().add(tsmallLayer);
				// lastSmallLayerList.add(tsmallLayer);

			}
		}
		// 把标准井中获得的虚拟小层加入到待匹配井最后的小层链表中
		for (int i = 0; i < lastSmallLayerList.size(); ++i) {
			DSmallLayer smallLayer = (DSmallLayer) lastSmallLayerList.get(i).clone();
			smallLayer.setTrue(false);
			for(int j = 0;j<doWell.getBigLayers().size();++j){
				DBigLayer bigLayer = doWell.getBigLayers().get(j);
				for(int k =0; k<bigLayer.getSmallLayers().size(); ++k){
					DSmallLayer dsLayer = bigLayer.getSmallLayers().get(k);
					if(!(dsLayer.getMatchResName().equals(smallLayer.getName())) &&dsLayer.getName().equals(smallLayer.getName()))
						bigLayer.getSmallLayers().add(smallLayer);
				}
			}
		}
		// out test
		DWell well = doWell;
		System.out.println("井号:" + well.getName());
		for (int j = 0; j < well.getBigLayers().size(); ++j) {
			DBigLayer bigLayer = well.getBigLayers().get(j);
			System.out.println("	层位:" + bigLayer.getName());
			for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
				DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
				System.out.println("			层位:" + smallLayer.getName() + "  归一化:" + smallLayer.getNor() + "  匹配结果:"
						+ smallLayer.getMatchResName());
			}
			System.out.println("");
		}
		System.out.println("");
	}

	// 清除虚拟小层
	private void clearNotTrueSmallLayer(DWell dwell) {

		for (int i = 0; i < dwell.getBigLayers().size(); ++i) {
			DBigLayer bigLayer = dwell.getBigLayers().get(i);
			for (int j = 0; j < bigLayer.getSmallLayers().size(); ++j) {
				DSmallLayer smallLayer = bigLayer.getSmallLayers().get(j);
				if (smallLayer.getIsTrue() == false) {
					bigLayer.getSmallLayers().remove(smallLayer);
				}
			}
		}

	}
}
