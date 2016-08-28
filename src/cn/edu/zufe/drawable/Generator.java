package cn.edu.zufe.drawable;

import java.util.LinkedList;

import cn.edu.zufe.model.DBigLayer;
import cn.edu.zufe.model.DDepth;
import cn.edu.zufe.model.DSmallLayer;
import cn.edu.zufe.model.DWell;
import cn.edu.zufe.model.DWellLogs;
import cn.edu.zufe.model.DWellLogsAttribute;

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

			float ngbY = (float) wellList.get(i).getNgbDepth(); // 用于定位

			// 注入 PSectionWell
			float pswX = psX + PSection.PS_WIDTH / 2 - PSection.wellWidth / 2;
			float pswY = toPixelY(ngbY, well);
			float pswW = PSection.wellWidth;
			float pswH = toPixelH(well);
			PSectionWell pSectionWell = new PSectionWell(well, pswX, pswY, pswW, pswH);
			pSection.setPSectionWell(pSectionWell);

			// 注入PWellLogs
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
			// 左SP1 - 右R04

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
					// 跳过没有数据或是虚拟的小层绘制
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
	 * 从 DDepth 数据模型中计算 y 在画布上对应的位置
	 * 
	 * @param ngbY
	 * @param data
	 * @return
	 */
	public static float toPixelY(float ngbY, DDepth data) {
		return PSection.ngbPos - (ngbY - (float) data.getDepth()[0]) * PSection.pixRatio;
	}

	/**
	 * 计算 y 在画布上对应的位置
	 * 
	 * @param ngbY
	 * @param depth
	 * @return
	 */
	public static float toPixelY(float ngbY, float depth) {
		return PSection.ngbPos - (ngbY - depth) * PSection.pixRatio;
	}

	/**
	 * 从 DDepth 数据模型中计算 h 在画布上对应的长度
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
		// 在井的深度范围内 以 0.125的大小步进
		for (double i = top; i < btm; i += 0.125) {
			// 写这些还是要靠sublime3
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
		// 在井的深度范围内 以 0.125的大小步进
		for (double i = top; i < btm; i += 0.125) {
			// 写这些还是要靠sublime3
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
