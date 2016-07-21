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
	 * ���ھ�֮���ƥ��
	 * 
	 * @param standardWell
	 *            ��׼��
	 * @param doWell
	 *            ��ƥ�侮
	 */
	public void doMatch(boolean isFirstElement) {
		if (isFirstElement) {
			clearNotTrueSmallLayer(standardWell);
			// ��һ�������׼��
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
		// ��һ�������ƥ�侮
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
		// ���ƥ��
		for (int j = 0; j < doWell.getBigLayers().size(); ++j) {
			DBigLayer bigLayer = doWell.getBigLayers().get(j);
			DBigLayer standardBigLayer = standardWell.getBigLayers().get(j);
			int l = 0;
			if (bigLayer.getDepth()[0] == 0) {
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
					smallLayer.setMatchResName("����");
				}
			} else {
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
					for (int m = l; m < standardBigLayer.getSmallLayers().size() - 1; ++m) {
						double nor1 = standardBigLayer.getSmallLayers().get(m).getNor();
						double nor2 = standardBigLayer.getSmallLayers().get(m + 1).getNor();
						if (smallLayer.getDepth()[0] == 0.0)
							smallLayer.setMatchResName("����");
						else if (m == 0 && smallLayer.getNor() < nor1)
							smallLayer.setMatchResName(standardBigLayer.getSmallLayers().get(0).getName());
						else if (smallLayer.getNor() > nor1 && smallLayer.getNor() < nor2)
							smallLayer.setMatchResName(standardBigLayer.getSmallLayers().get(m).getName());
					}
				}
			}
		}
		// ��ȫ��ƥ�侮����С�㣬��ӵ����ͺ���
		// �ھ����������һ��Istrue�ֶ� �������������ʵ���ڵ�
		LinkedList<String> doWellMatchResName = new LinkedList<String>();
		// ���ƥ��������ƥ����
		for (DBigLayer bigLayer : doWell.getBigLayers()) {
			for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (smallLayer.getMatchResName() == null || smallLayer.getMatchResName().equals("")
						|| smallLayer.getMatchResName().equals("����")
						|| doWellMatchResName.contains(smallLayer.getMatchResName())) {
					continue;
				}
				doWellMatchResName.add(smallLayer.getMatchResName());
			}
		}

		// ����׼����δƥ�䵽�����С����뵽������
		LinkedList<DSmallLayer> lastSmallLayerList = new LinkedList<DSmallLayer>();
		for (DBigLayer bigLayer : standardWell.getBigLayers()) {
			for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (smallLayer.getName() == null || smallLayer.getName().equals("") || smallLayer.getName().equals("����")
						|| doWellMatchResName.contains(smallLayer.getName())) {
					continue;
				}
				DSmallLayer tsmallLayer = (DSmallLayer) smallLayer.clone();

				lastSmallLayerList.add(tsmallLayer);
				// doWell.getBigLayers().getLast().getSmallLayers().add(tsmallLayer);
				// lastSmallLayerList.add(tsmallLayer);

			}
		}
		// �ѱ�׼���л�õ�����С����뵽��ƥ�侮����С��������
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
		System.out.println("����:" + well.getName());
		for (int j = 0; j < well.getBigLayers().size(); ++j) {
			DBigLayer bigLayer = well.getBigLayers().get(j);
			System.out.println("	��λ:" + bigLayer.getName());
			for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
				DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
				System.out.println("			��λ:" + smallLayer.getName() + "  ��һ��:" + smallLayer.getNor() + "  ƥ����:"
						+ smallLayer.getMatchResName());
			}
			System.out.println("");
		}
		System.out.println("");
	}

	// �������С��
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
