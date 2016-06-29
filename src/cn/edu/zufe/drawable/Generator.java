package cn.edu.zufe.drawable;

import java.util.LinkedList;
import cn.edu.zufe.model.*;

public class Generator {

	// Generator类中主要存放数据类到绘图类的转换与生成的方法

	// 此段代码不应该放在Generator中
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
	 * 井的数据模型转化为井的绘图模型（俯视图），涉及到归一化，只能输入List并输出List
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

		// 计算位置并生成 PMapWell
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
	 * 井的数据模型转化为井的绘图模型（剖面图），涉及到总体坐标的计算，只能输入List并输出List
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

		// 获得PSection的高度（最大）
		float[] topHs = new float[wellList.size()]; // Ngb到最上层的高度
		float[] btmHs = new float[wellList.size()]; // Ngb到最下层的高度
		for (int i = 0; i < wellList.size(); ++i) {
			float ngbY = (float) wellList.get(i).getNgbDepth();
			topHs[i] = ngbY - (float) wellList.get(i).getDepth()[0];
			btmHs[i] = (float) wellList.get(i).getDepth()[1] - ngbY;
		}
		// float maxTopH = getMaxValue(topHs);
		float maxBtmH = getMaxValue(btmHs);
		float pSectionH = PSection.OFFSET_Y + PSection.ngbPos + maxBtmH; // PSection的高度

		// 计算位置生成 PSection，注入其所需要的类，x , y 两种参数注意要 * pixRatio
		LinkedList<PSection> psList = new LinkedList<PSection>();
		for (int i = 0; i < wellList.size(); ++i) {
			DWell well = wellList.get(i);

			// 生成 PSection（就是几个同样大小但位置不同的矩形）
			float psX = i * PSection.PS_WIDTH;
			float psY = 0;
			float psW = PSection.PS_WIDTH;
			float psH = pSectionH;
			PSection pSection = new PSection(well, psX, psY, psW, psH);
			psList.add(pSection);

			// 注入 PSectionWell
			float pswX = psX + PSection.PS_WIDTH / 2 - PSection.wellWidth / 2;
			float pswY = PSection.ngbPos - topHs[i];
			float pswW = PSection.wellWidth;
			float pswH = topHs[i] + btmHs[i];
			PSectionWell pSectionWell = new PSectionWell(well, pswX, pswY, pswW, pswH);
			pSection.setPSectionWell(pSectionWell);

			float ngbY = (float) wellList.get(i).getNgbDepth();
			// 注入 PBigLayers
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

			// 注入 PSmallLayers
			LinkedList<PSmallLayer> pSmallLayerList = new LinkedList<PSmallLayer>();
			for (DBigLayer bigLayer : well.getBigLayers()) {
				for (DSmallLayer smallLayer : bigLayer.getSmallLayers()) {
					// 跳过没有数据的小层绘制
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
	// if (smallLayer.getMatchResName().equals("尖灭") ||
	// smallLayer.getMatchResName() == null || smallLayer.getMatchResName() ==
	// "") {
	// continue;
	// }
	// pslList.add(new PSmallLayer(smallLayer, ps));
	// }
	// return pslList;
	// }

	/**
	 * 获取最大值
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
	 * 获取最小值
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
	 * 归一化
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
			System.out.println("Warning: 出现max < min，请检查");
			return null;
		}

		for (int i = 0; i < value.length; i++) {
			value[i] = (value[i] - min) / (max - min);
		}
		return value;
	}
}
