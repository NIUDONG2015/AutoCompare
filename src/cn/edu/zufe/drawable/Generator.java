package cn.edu.zufe.drawable;

import java.util.Arrays;
import java.util.LinkedList;

import cn.edu.zufe.model.*;
import cn.edu.zufe.ui.PAppletSC;

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

	public static LinkedList<PMapWell> wellToPWells(LinkedList<Well> wellList) {
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

		LinkedList<Well> wellList = new LinkedList<Well>();
		for (PMapWell pw : pWellList) {
			wellList.add(pw.getWell());
		}

		return wellToPSection(wellList);
	}

	public static LinkedList<PSection> wellToPSection(LinkedList<Well> wellList) {
		if (wellList == null) {
			return null;
		}
		if (wellList.size() == 0) {
			return null;
		}

//		double[] tops = new double[wellList.size()];
//		double[] btms = new double[wellList.size()];
//		double[] all = new double[wellList.size() * 2];
//		for (int i = 0; i < wellList.size(); ++i) {
//			tops[i] = wellList.get(i).getDepth()[0];
//			btms[i] = wellList.get(i).getDepth()[1];
//			all[i] = tops[i];
//		}
//		System.arraycopy(btms, 0, all, tops.length, btms.length);
//
//		double max = getMaxValue(all);
//		double min = getMinValue(all);
//		double[] norTops = normalization(tops, max, min);
//		double[] norBtms = normalization(btms, max, min);

		// 计算位置并生成 PSection（主要是为了计算y,h）
		// 动态生成宽度的算法需改进
//		float bigw = (PAppletSC.width - ScrollBar.size) / wellList.size();
		LinkedList<PSection> psList = new LinkedList<PSection>();
		for (int i = 0; i < wellList.size(); ++i) {
//			float px = (float) 20 + i * bigw;
//			float py = (float) (PSection.OFFSET_Y + norTops[i] * PSection.ZOOM_OUT);
//			float pw = (float) (bigw * 0.3);
//			float ph = (float) (norBtms[i] - norTops[i]) * PSection.ZOOM_OUT;
			float px = (float)i * 100;
			psList.add(new PSection(wellList.get(i), px));
		}
		return psList;
	}

	public static LinkedList<PSmallLayer> smallLayerToPSmallLayer(PSection ps, LinkedList<SmallLayer> smallLayerList) {
		if (smallLayerList == null) {
			return null;
		}
		if (smallLayerList.size() == 0) {
			return null;
		}

		LinkedList<PSmallLayer> pslList = new LinkedList<PSmallLayer>();
		for (SmallLayer smallLayer : smallLayerList) {
			if (smallLayer.getMatchResName().equals("尖灭") || smallLayer.getMatchResName() == null || smallLayer.getMatchResName() == "") {
				continue;
			}
			pslList.add(new PSmallLayer(smallLayer, ps));
		}
		return pslList;
	}

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
