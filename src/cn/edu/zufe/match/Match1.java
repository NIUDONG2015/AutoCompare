package cn.edu.zufe.match;

import java.util.LinkedList;

import cn.edu.zufe.model.*;

public class Match1 {
	private Well standardWell;
	private LinkedList<Well> wellList;

	public Match1(Well staWell, LinkedList<Well> wList) {
		standardWell = staWell;
		wellList = wList;
	}

	public void doMatch() {
		// ��һ������
		for (int i = 0; i < standardWell.getBigLayers().size(); ++i) {
			BigLayer bigLayer = standardWell.getBigLayers().get(i);
			double top, btn;
			if (bigLayer.getSmallLayers().size() > 0) {
				top = bigLayer.getSmallLayers().get(0).getDepth()[0];

				btn = bigLayer.getDepth()[0];
				for (int j = 0; j < bigLayer.getSmallLayers().size() - 1; ++j) {
					SmallLayer smallLayer = bigLayer.getSmallLayers().get(j);
					SmallLayer smallLayerNext = bigLayer.getSmallLayers().get(j + 1);
					double nor = ((smallLayer.getDepth()[1] + smallLayerNext.getDepth()[0]) / 2 - top) / (btn - top);
					smallLayer.setNor(nor);
					smallLayer.setMatchResName(smallLayer.getName());
				}
				bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1).setNor(1);
				bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1)
						.setMatchResName(bigLayer.getSmallLayers().get(bigLayer.getSmallLayers().size() - 1).getName());
			}
		}

		for (int i = 0; i < wellList.size(); ++i) {
			if (i == 1)
				continue;
			else {
				Well well = wellList.get(i);
				for (int j = 0; j < well.getBigLayers().size(); ++j) {
					BigLayer bigLayer = well.getBigLayers().get(j);
					if (bigLayer.getSmallLayers().size() > 0) {
						double top;
						if (j > 0)
							top = well.getBigLayers().get(j - 1).getDepth()[0];
						else
							top = bigLayer.getSmallLayers().get(0).getDepth()[0];
						double btn = bigLayer.getDepth()[0];
						for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
							SmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
							double nor = ((smallLayer.getDepth()[0] + smallLayer.getDepth()[1]) / 2 - top) / (btn - top);
							smallLayer.setNor(nor);
						}
					}
				}
			}
		}
		// ���ƥ��
		for (int i = 0; i < wellList.size(); ++i) {
			if (i == 1)
				continue;
			else {
				Well well = wellList.get(i);
				for (int j = 0; j < well.getBigLayers().size(); ++j) {
					BigLayer bigLayer = well.getBigLayers().get(j);
					BigLayer standardBigLayer = standardWell.getBigLayers().get(j);
					int l = 0;
					for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
						SmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
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
		}
		//˳���һ����С��λ�����������������ȣ�
		norDepths();
		
		// out

//		for (int i = 0; i < wellList.size(); ++i) {
//			Well well = wellList.get(i);
//			System.out.println("����:" + well.getName());
//			for (int j = 0; j < well.getBigLayers().size(); ++j) {
//				BigLayer bigLayer = well.getBigLayers().get(j);
//				System.out.println("	��λ:" + bigLayer.getName());
//				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
//					SmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
//					System.out.println("			��λ:" + smallLayer.getName() + "  ��һ��:" + smallLayer.getNor() + "  ƥ����:" + smallLayer.getMatchResName());
//				}
//				System.out.println("");
//			}
//			System.out.println("");
//		}

	}
	
	/**
	 * ˳���һ����С��λ�����������������ȣ�
	 */
	private void norDepths() {
		for (int i = 0; i < wellList.size(); ++i) {
			Well well = wellList.get(i);
			// Ѱ�Ҷ����͵ײ�
			double top = -1, btm = -1;
			// ��Ѱ�ҵײ����Դ�����Ϊ�ף�
			for (int j = well.getBigLayers().size() - 1; j > 0; --j) {
				double bigLayerDepth = well.getBigLayers().get(j).getDepth()[0];
				if (bigLayerDepth != 0) {
					btm = bigLayerDepth;
					break;
				}
			}
			// ��Ѱ�Ҷ���������ǳ��С��Ϊ������
			Outer: // �������ѭ��
			for (int j = 0; j < well.getBigLayers().size(); ++j) {
				BigLayer bigLayer = well.getBigLayers().get(j);
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					// ɰ�Ҷ���
					double smallLayerTopDepth = bigLayer.getSmallLayers().get(k).getDepth()[0];
					if (smallLayerTopDepth != 0) {
						// ɰ�Ҷ��������ݣ�ɰ�ҵ��������ݵ����δ�����
						top = smallLayerTopDepth;
						break Outer;
					}
				}
			}
			if (top <= 0 || btm <= 0) {
				System.out.println(well.getName() + "-Ѱ���;������͵ײ�ʱ�����쳣:(" + top + "," + btm + ")");
			}

			// ���ö����͵ײ�
			well.setDepth(new double[] { top, btm });

			// ��ʼ��һ��
			for (int j = 0; j < well.getBigLayers().size(); ++j) {
				BigLayer bigLayer = well.getBigLayers().get(j);
				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
					SmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
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
		//���
//		for (int i = 0; i < wellList.size(); ++i) {
//			Well well = wellList.get(i);
//			System.out.println("����:" + well.getName());
//			System.out.println("�����͵ײ�:(" + well.getDepth()[0] + "," + well.getDepth()[1] + ")");
//			for (int j = 0; j < well.getBigLayers().size(); ++j) {
//				BigLayer bigLayer = well.getBigLayers().get(j);
//				System.out.println("	��λ:" + bigLayer.getName());
//				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
//					SmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
//					System.out.println("			��λ:" + smallLayer.getName() + "  ��һ���㶥:" + smallLayer.getNorDepth()[0] + "  ��һ�����:" + smallLayer.getNorDepth()[1]);
//				}
//				System.out.println("");
//			}
//			System.out.println("");
//		}
	}
}
