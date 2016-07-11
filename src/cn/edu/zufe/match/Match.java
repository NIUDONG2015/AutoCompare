package cn.edu.zufe.match;

import java.util.LinkedList;

import cn.edu.zufe.model.*;

public class Match {
	private DWell standardWell;	//��׼��
	private DWell matchWell;	//ƥ�侮

	public Match(DWell staWell, DWell matWell) {
		standardWell = staWell;
		matchWell = matWell;
	}

	public void doMatch() {
		// ��һ������
		//if (standardWell.getName().equals("GN152")) {
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
						//smallLayer.setMatchResName(smallLayer.getName());
					}
					bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1).setNor(1);
					bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1).setMatchResName(
							bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1).getName());
				}
			}
		//}
		
		for (int i = 0; i < matchWell.getBigLayers().size(); ++i) {
			DBigLayer bigLayer = matchWell.getBigLayers().get(i);
			if (bigLayer.getDepth()[0] == 0)
				continue;
			if (bigLayer.getSmallLayers().size() > 0) {
				double top;
				if (i > 0)
					top = matchWell.getBigLayers().get(i - 1).getDepth()[0];
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
					//smallLayer.setMatchResName(null);
				}
			}
		}

		// ���ƥ��

		for (int i = 0; i < matchWell.getBigLayers().size(); ++i) {
			DBigLayer bigLayer = matchWell.getBigLayers().get(i);
			DBigLayer standardBigLayer = standardWell.getBigLayers().get(i);
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

		// ˳���һ����С��λ�����������������ȣ�
		norDepths();

		// out

		// for (int i = 0; i < wellList.size(); ++i) {
		// DWell well = wellList.get(i);
		// System.out.println("����:" + well.getName());
		// for (int j = 0; j < well.getBigLayers().size(); ++j) {
		// DBigLayer bigLayer = well.getBigLayers().get(j);
		// System.out.println(" ��λ:" + bigLayer.getName());
		// for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
		// DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
		// System.out.println(" ��λ:" + smallLayer.getName() + " ��һ��:" +
		// smallLayer.getNor() + " ƥ����:" + smallLayer.getMatchResName());
		// }
		// System.out.println("");
		// }
		// System.out.println("");
		// }

	}

	/**
	 * ˳���һ����С��λ�����������������ȣ�
	 */
	private void norDepths() {
		
			// Ѱ�Ҷ����͵ײ�
			double top = -1, btm = -1;
			// Ѱ�ҵײ����Դ�����Ϊ�ף�
//			for (int j = well.getBigLayers().size() - 1; j > 0; --j) {
//				double bigLayerDepth = well.getBigLayers().get(j).getDepth()[0];
//				if (bigLayerDepth != 0) {
//					btm = bigLayerDepth;
//					break;
//				}
//			}
			
			// Ѱ�ҵײ���������С��Ϊ�ף�
			Outer1: // �������ѭ��
			for (int j = matchWell.getBigLayers().size() - 1; j > 0; --j) {
				DBigLayer bigLayer = matchWell.getBigLayers().get(j);
				for (int k = bigLayer.getSmallLayers().size() - 1; k > 0 ; --k) {
					// ɰ�ҵ���
					double smallLayerBtmDepth = bigLayer.getSmallLayers().get(k).getDepth()[1];
					if (smallLayerBtmDepth != 0) {
						// ɰ�ҵ��������ݣ�ɰ�Ҷ��������ݵ����δ�����
						btm = smallLayerBtmDepth;
						break Outer1;
					}
				}
			}
			
			// Ѱ�Ҷ���������ǳ��С��Ϊ������
			Outer2: // �������ѭ��
			for (int j = 0; j < matchWell.getBigLayers().size(); ++j) {
				DBigLayer bigLayer = matchWell.getBigLayers().get(j);
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					// ɰ�Ҷ���
					double smallLayerTopDepth = bigLayer.getSmallLayers().get(k).getDepth()[0];
					if (smallLayerTopDepth != 0) {
						// ɰ�Ҷ��������ݣ�ɰ�ҵ��������ݵ����δ�����
						top = smallLayerTopDepth;
						break Outer2;
					}
				}
			}
			if (top <= 0 || btm <= 0) {
				System.out.println(matchWell.getName() + "-Ѱ���;������͵ײ�ʱ�����쳣:(" + top + "," + btm + ")");
			}

			// ���ö����͵ײ�
			matchWell.setDepth(new double[] { top, btm });

			// ��ʼ��һ��
			for (int j = 0; j < matchWell.getBigLayers().size(); ++j) {
				DBigLayer bigLayer = matchWell.getBigLayers().get(j);
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
			}
		}
}
