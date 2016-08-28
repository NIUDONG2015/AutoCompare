package cn.edu.zufe.drawable;

import java.util.LinkedList;

import cn.edu.zufe.model.DBigLayer;
import cn.edu.zufe.model.DDepth;
import cn.edu.zufe.model.DSmallLayer;
import cn.edu.zufe.model.DWell;
import cn.edu.zufe.model.DWellLogs;
import cn.edu.zufe.model.DWellLogsAttribute;

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

			float ngbY = (float) wellList.get(i).getNgbDepth(); // ���ڶ�λ

			// ע�� PSectionWell
			float pswX = psX + PSection.PS_WIDTH / 2 - PSection.wellWidth / 2;
			float pswY = toPixelY(ngbY, well);
			float pswW = PSection.wellWidth;
			float pswH = toPixelH(well);
			PSectionWell pSectionWell = new PSectionWell(well, pswX, pswY, pswW, pswH);
			pSection.setPSectionWell(pSectionWell);

			// ע��PWellLogs
			DWellLogsAttribute wlaMax = getMaxLogsValue(well);
			DWellLogsAttribute wlaMin = getMinLogsValue(well);
			System.out.println(well.getName() + ":");
			wlaPrintMaxAndMin(wlaMax, wlaMin);

			PWellLogs[] pWellLogs = new PWellLogs[2];
			pWellLogs[0] = new PWellLogs(well.getWellLogs(), pswX - PSection.PWL_WIDTH, pswY, PSection.PWL_WIDTH, pswH);
			pWellLogs[0].setWell(well);
			pWellLogs[0].setWlaMax(wlaMax);
			pWellLogs[0].setWlaMin(wlaMin);

			pWellLogs[1]= new PWellLogs(well.getWellLogs(), pswX + PSection.wellWidth, pswY, PSection.PWL_WIDTH, pswH);
			pWellLogs[1].setWell(well);
			pWellLogs[1].setWlaMax(wlaMax);
			pWellLogs[1].setWlaMin(wlaMin);
			pSection.setPWellLogs(pWellLogs);
			// ��SP1 - ��R04

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
					// ����û�����ݻ��������С�����
					if (smallLayer.getDepth()[0] == 0 || smallLayer.getDepth()[1] == 0 || smallLayer.getIsTrue() == false) {
						continue;
					}

					float pslX = pswX;
					float pslY = toPixelY(ngbY, smallLayer);
					float pslW = PSection.wellWidth;
					float pslH = toPixelH(smallLayer);
					PSmallLayer pSmallLayer = new PSmallLayer(smallLayer, pslX, pslY, pslW, pslH);
					pSmallLayerList.add(pSmallLayer);
				}
			}
			pSection.setPSmallLayerList(pSmallLayerList);

		}
		return psList;
	}

	/**
	 * �� DDepth ����ģ���м��� y �ڻ����϶�Ӧ��λ��
	 * 
	 * @param ngbY
	 * @param data
	 * @return
	 */
	public static float toPixelY(float ngbY, DDepth data) {
		return PSection.ngbPos - (ngbY - (float) data.getDepth()[0]) * PSection.pixRatio;
	}

	/**
	 * ���� y �ڻ����϶�Ӧ��λ��
	 * 
	 * @param ngbY
	 * @param depth
	 * @return
	 */
	public static float toPixelY(float ngbY, float depth) {
		return PSection.ngbPos - (ngbY - depth) * PSection.pixRatio;
	}

	/**
	 * �� DDepth ����ģ���м��� h �ڻ����϶�Ӧ�ĳ���
	 * 
	 * @param data
	 * @return
	 */
	public static float toPixelH(DDepth data) {
		return (float) (data.getDepth()[1] - data.getDepth()[0]) * PSection.pixRatio;
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

	public static DWellLogsAttribute getMaxLogsValue(DWell well) {
		DWellLogs dwl = well.getWellLogs();
		if (dwl == null) {
			return null;
		}

		DWellLogsAttribute max = new DWellLogsAttribute();

		/*max.setAC(-1000);
		max.setCAL1(-1000);
		max.setCAL2(-1000);
		max.setCOND(-1000);
		max.setR04(-1000);
		max.setR25(-1000);
		max.setR4(-1000);
		max.setRLML(-1000);
		max.setRNML(-1000);
		max.setSP1(-1000);
		max.setSP2(-1000);*/

		double top = (int) well.getDepth()[0] - 1;
		double btm = (int) well.getDepth()[1] + 1;
		// �ھ�����ȷ�Χ�� �� 0.125�Ĵ�С����
		for (double i = top; i < btm; i += 0.125) {
			// д��Щ����Ҫ��sublime3
			DWellLogsAttribute wla = dwl.getLogsAttribute(i);
			if (wla != null) {
				/*max.setAC(Math.max(max.getAC(), wla.getAC()));
				max.setCAL1(Math.max(max.getCAL1(), wla.getCAL1()));
				max.setCAL2(Math.max(max.getCAL2(), wla.getCAL2()));
				max.setCOND(Math.max(max.getCOND(), wla.getCOND()));
				max.setR04(Math.max(max.getR04(), wla.getR04()));
				max.setR25(Math.max(max.getR25(), wla.getR25()));
				max.setR4(Math.max(max.getR4(), wla.getR4()));
				max.setRLML(Math.max(max.getRLML(), wla.getRLML()));
				max.setRNML(Math.max(max.getRNML(), wla.getRNML()));
				max.setSP1(Math.max(max.getSP1(), wla.getSP1()));
				max.setSP2(Math.max(max.getSP2(), wla.getSP2()));*/
			} else {
				// System.out.println(i + ":null");
			}
		}
		return max;
	}

	public static DWellLogsAttribute getMinLogsValue(DWell well) {
		DWellLogs dwl = well.getWellLogs();
		if (dwl == null) {
			return null;
		}

		DWellLogsAttribute min = new DWellLogsAttribute();

	/*	min.setAC(10000);
		min.setCAL1(10000);
		min.setCAL2(10000);
		min.setCOND(10000);
		min.setR04(10000);
		min.setR25(10000);
		min.setR4(10000);
		min.setRLML(10000);
		min.setRNML(10000);
		min.setSP1(10000);
		min.setSP2(10000);*/

		double top = (int) well.getDepth()[0] - 1;
		double btm = (int) well.getDepth()[1] + 1;
		// �ھ�����ȷ�Χ�� �� 0.125�Ĵ�С����
		for (double i = top; i < btm; i += 0.125) {
			// д��Щ����Ҫ��sublime3
			DWellLogsAttribute wla = dwl.getLogsAttribute(i);
			if (wla != null) {
			/*	min.setAC(Math.min(min.getAC(), wla.getAC()));
				min.setCAL1(Math.min(min.getCAL1(), wla.getCAL1()));
				min.setCAL2(Math.min(min.getCAL2(), wla.getCAL2()));
				min.setCOND(Math.min(min.getCOND(), wla.getCOND()));
				min.setR04(Math.min(min.getR04(), wla.getR04()));
				min.setR25(Math.min(min.getR25(), wla.getR25()));
				min.setR4(Math.min(min.getR4(), wla.getR4()));
				min.setRLML(Math.min(min.getRLML(), wla.getRLML()));
				min.setRNML(Math.min(min.getRNML(), wla.getRNML()));
				min.setSP1(Math.min(min.getSP1(), wla.getSP1()));
				min.setSP2(Math.min(min.getSP2(), wla.getSP2()));*/
			} else {
				// System.out.println(i + ":null");
			}
		}
		return min;
	}

	// test syso
	public static void wlaPrintMaxAndMin(DWellLogsAttribute max, DWellLogsAttribute min) {
		if (max == null || min == null) {
			System.out.println("~null");
			return;
		}

		/*System.out.println("max\t\tmin");
		System.out.println(max.getAC() + "\t\t" + min.getAC());
		System.out.println(max.getCAL1() + "\t\t" + min.getCAL1());
		System.out.println(max.getCAL2() + "\t\t" + min.getCAL2());
		System.out.println(max.getCOND() + "\t\t" + min.getCOND());
		System.out.println(max.getR04() + "\t\t" + min.getR04());
		System.out.println(max.getR25() + "\t\t" + min.getR25());
		System.out.println(max.getR4() + "\t\t" + min.getR4());
		System.out.println(max.getRLML() + "\t\t" + min.getRLML());
		System.out.println(max.getRNML() + "\t\t" + min.getRNML());
		System.out.println(max.getSP1() + "\t\t" + min.getSP1());
		System.out.println(max.getSP2() + "\t\t" + min.getSP2());*/
	}
}
