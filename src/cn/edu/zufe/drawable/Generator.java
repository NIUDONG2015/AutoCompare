package cn.edu.zufe.drawable;

import java.util.LinkedList;
import cn.edu.zufe.model.*;

public class Generator {

	// Generator������Ҫ��������ൽ��ͼ���ת�������ɵķ���

	// �˶δ��벻Ӧ�÷���Generator��
	public static PSection pWellToPSection(PMapWell pWell, LinkedList<PSection> pSectionList) {
		PSection pSection = null;
		for (PSection tPSection : pSectionList) {
			if (pWell.getWell().equals(tPSection.getWell())) {
				pSection = tPSection;
			}
		}
		return pSection;
	}

	/**
	 * ��������ģ��ת��Ϊ���Ļ�ͼģ�ͣ�����ͼ�����漰����һ����ֻ������List�����List
	 * 
	 * @param wellList
	 * @return
	 */
	public static LinkedList<PMapWell> wellToPMapWells(LinkedList<DWell> wellList) {
		if (wellList == null) {
			return null;
		}
		if (wellList.size() == 0) {
			return null;
		}

		double[] x = new double[wellList.size()];
		double[] y = new double[wellList.size()];
		for (int i = 0; i < wellList.size(); ++i) {
			x[i] = wellList.get(i).getX();
			y[i] = wellList.get(i).getY();
		}

		double maxX = getMaxValue(x);
		double minX = getMinValue(x);
		double[] norX = normalization(x, maxX, minX);

		double maxY = getMaxValue(y);
		double minY = getMinValue(y);
		double[] norY = normalization(y, maxY, minY);

		// ����λ�ò����� PMapWell
		LinkedList<PMapWell> pwList = new LinkedList<PMapWell>();
		for (int i = 0; i < wellList.size(); ++i) {
			float px = (float) (PMapWell.OFFSET_X + norX[i] * PMapWell.ZOOM_OUT);
			float py = (float) (PMapWell.OFFSET_Y + norY[i] * PMapWell.ZOOM_OUT);
			pwList.add(new PMapWell(wellList.get(i), px, py));
		}
		return pwList;
	}

	public static LinkedList<PSection> pWellToPSection(LinkedList<PMapWell> pWellList) {
		if (pWellList == null) {
			return null;
		}
		if (pWellList.size() == 0) {
			return null;
		}

		LinkedList<DWell> wellList = new LinkedList<DWell>();
		for (PMapWell pw : pWellList) {
			wellList.add(pw.getWell());
		}

		return wellToPSection(wellList);
	}

	/**
	 * ��������ģ��ת��Ϊ���Ļ�ͼģ�ͣ�����ͼ�����漰����������ļ��㣬ֻ������List�����List
	 * 
	 * @param wellList
	 * @return
	 */
	public static LinkedList<PSection> wellToPSection(LinkedList<DWell> wellList) {
		if (wellList == null) {
			return null;
		}
		if (wellList.size() == 0) {
			return null;
		}

		// ���PSection�ĸ߶ȣ����
		float[] topHs = new float[wellList.size()]; // Ngb�����ϲ�ĸ߶�
		float[] btmHs = new float[wellList.size()]; // Ngb�����²�ĸ߶�
		for (int i = 0; i < wellList.size(); ++i) {
			float ngbY = (float) wellList.get(i).getNgbDepth();
			topHs[i] = ngbY - (float) wellList.get(i).getDepth()[0];
			btmHs[i] = (float) wellList.get(i).getDepth()[1] - ngbY;
		}
		// float maxTopH = getMaxValue(topHs);
		float maxBtmH = getMaxValue(btmHs);
		float pSectionH = PSection.OFFSET_Y + PSection.ngbPos + maxBtmH; // PSection�ĸ߶�

		// ����λ������ PSection��ע��������Ҫ���࣬x , y ���ֲ���ע��Ҫ * pixRatio
		LinkedList<PSection> psList = new LinkedList<PSection>();
		for (int i = 0; i < wellList.size(); ++i) {
			DWell well = wellList.get(i);

			// ���� PSection�����Ǽ���ͬ����С��λ�ò�ͬ�ľ��Σ�
			float psX = i * PSection.PS_WIDTH;
			float psY = 0;
			float psW = PSection.PS_WIDTH;
			float psH = pSectionH;
			PSection pSection = new PSection(well, psX, psY, psW, psH);
			psList.add(pSection);

			// ע�� PSectionWell
			float pswX = psX + PSection.PS_WIDTH / 2 - PSection.wellWidth / 2;
			float pswY = PSection.ngbPos - topHs[i];
			float pswW = PSection.wellWidth;
			float pswH = topHs[i] + btmHs[i];
			PSectionWell pSectionWell = new PSectionWell(well, pswX, pswY, pswW, pswH);
			pSection.setPSectionWell(pSectionWell);

			float ngbY = (float) wellList.get(i).getNgbDepth();
			// ע�� PBigLayers
			// LinkedList<PBigLayer> pBigLayerList = new
			// LinkedList<PBigLayer>();
			// for (DBigLayer bigLayer : well.getBigLayers()) {
			// float pblX = pswX;
			// float pblY = ngbPos - (ngbY - (float) bigLayer.getDepth()[0]) *
			// pixRatio;
			// float pblW = wellWidth;
			// float pblH = (float) (bigLayer.getDepth()[0] -
			// bigLayer.getSmallLayers().get(0).getDepth()[0]) * pixRatio;
			// PBigLayer pBigLayer = new PBigLayer(bigLayer, pblX, pblY, pblW,
			// pblH);
			// pBigLayerList.add(pBigLayer);
			// }
			// pSection.setPBigLayerList(pBigLayerList);

			// ע�� PSmallLayers
			LinkedList<PSmallLayer> pSmallLayerList = new LinkedList<PSmallLayer>();
			for (DBigLayer bigLayer : well.getBigLayers()) {
				for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
					// ����û�����ݵ�С�����
					if (smallLayer.getDepth()[0] == 0 || smallLayer.getDepth()[1] == 0) {
						continue;
					}

					float pslX = pswX;
					float pslY = PSection.ngbPos - (ngbY - (float) smallLayer.getDepth()[0]);
					float pslW = PSection.wellWidth;
					float pslH = (float) (smallLayer.getDepth()[1] - smallLayer.getDepth()[0]);
					PSmallLayer pSmallLayer = new PSmallLayer(smallLayer, pslX, pslY, pslW, pslH);
					pSmallLayerList.add(pSmallLayer);
				}
			}
			pSection.setPSmallLayerList(pSmallLayerList);

		}
		return psList;
	}

	// public static LinkedList<PSmallLayer> smallLayerToPSmallLayer(PSection
	// ps, LinkedList<DSmallLayer> smallLayerList) {
	// if (smallLayerList == null) {
	// return null;
	// }
	// if (smallLayerList.size() == 0) {
	// return null;
	// }
	//
	// LinkedList<PSmallLayer> pslList = new LinkedList<PSmallLayer>();
	// for (DSmallLayer smallLayer : smallLayerList) {
	// if (smallLayer.getMatchResName().equals("����") ||
	// smallLayer.getMatchResName() == null || smallLayer.getMatchResName() ==
	// "") {
	// continue;
	// }
	// pslList.add(new PSmallLayer(smallLayer, ps));
	// }
	// return pslList;
	// }

	/**
	 * ��ȡ���ֵ
	 * 
	 * @param value
	 * @return
	 */
	public static double getMaxValue(double[] value) {
		double max = value[0];
		for (int i = 1; i < value.length; i++) {
			max = Math.max(value[i], max);
		}
		return max;
	}

	public static float getMaxValue(float[] value) {
		float max = value[0];
		for (int i = 1; i < value.length; i++) {
			max = Math.max(value[i], max);
		}
		return max;
	}

	/**
	 * ��ȡ��Сֵ
	 * 
	 * @param value
	 * @return
	 */
	public static double getMinValue(double[] value) {
		double min = value[0];
		for (int i = 1; i < value.length; i++) {
			min = Math.min(value[i], min);
		}
		return min;
	}

	public static float getMinValue(float[] value) {
		float min = value[0];
		for (int i = 1; i < value.length; i++) {
			min = Math.min(value[i], min);
		}
		return min;
	}

	/**
	 * ��һ��
	 * 
	 * @param value
	 * @param max
	 * @param min
	 * @return
	 */
	public static double[] normalization(double[] value, double max, double min) {
		if (max == min) {
			return new double[value.length];
		} else if (max <= min) {
			System.out.println("Warning: ����max < min������");
			return null;
		}

		for (int i = 0; i < value.length; i++) {
			value[i] = (value[i] - min) / (max - min);
		}
		return value;
	}
}
