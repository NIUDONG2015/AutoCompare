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
	 * ��һ����С��λ�����������������ȣ�
	 */
	void doMatch() {
		for (int i = 0; i < wellList.size(); ++i) {
			DWell well = wellList.get(i);
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
			for (int j = well.getBigLayers().size() - 1; j > 0; --j) {
				DBigLayer bigLayer = well.getBigLayers().get(j);
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
			for (int j = 0; j < well.getBigLayers().size(); ++j) {
				DBigLayer bigLayer = well.getBigLayers().get(j);
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
				System.out.println(well.getName() + "-Ѱ���;������͵ײ�ʱ�����쳣:(" + top + "," + btm + ")");
			}

			// ���ö����͵ײ�
			well.setDepth(new double[] { top, btm });

			/*// ��ʼ��һ��
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
		//���
//		for (int i = 0; i < wellList.size(); ++i) {
//			DWell well = wellList.get(i);
//			System.out.println("����:" + well.getName());
//			System.out.println("�����͵ײ�:(" + well.getDepth()[0] + "," + well.getDepth()[1] + ")");
//			for (int j = 0; j < well.getBigLayers().size(); ++j) {
//				DBigLayer bigLayer = well.getBigLayers().get(j);
//				System.out.println("	��λ:" + bigLayer.getName());
//				for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
//					DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
//					System.out.println("			��λ:" + smallLayer.getName() + "  ��һ���㶥:" + smallLayer.getNorDepth()[0] + "  ��һ�����:" + smallLayer.getNorDepth()[1]);
//				}
//				System.out.println("");
//			}
//			System.out.println("");
//		}
	}
}
