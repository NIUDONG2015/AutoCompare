package cn.edu.zufe.drawable;

import java.util.Arrays;
import java.util.LinkedList;

import cn.edu.zufe.model.*;
import cn.edu.zufe.ui.PAppletSC;

public class Generator {
	
	public static PSection pWellToPSection(PWell pWell, LinkedList<PSection> pSectionList){
		PSection pSection = null;
		for(PSection tPSection : pSectionList){
			if(pWell.getWell().equals(tPSection.getWell())){
				pSection = tPSection;
			}
		}
		return pSection;
	}
	public static LinkedList<PWell> wellToPWells(LinkedList<Well> wellList) {
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

		// 待修改，参数在此处计算完整
		LinkedList<PWell> pwList = new LinkedList<PWell>();
		for (int i = 0; i < wellList.size(); ++i) {
			pwList.add(new PWell(wellList.get(i), (float) norX[i], (float) norY[i]));
		}
		return pwList;
	}
	
	public static LinkedList<PSection> pWellToPSection(LinkedList<PWell> pWellList) {
		if (pWellList == null) {
			return null;
		}
		if (pWellList.size() == 0) {
			return null;
		}

		double[] tops = new double[pWellList.size()];
		double[] btms = new double[pWellList.size()];
		double[] all = new double[pWellList.size() * 2];
		for (int i = 0; i < pWellList.size(); ++i) {
			tops[i] = pWellList.get(i).getWell().getDepth()[0];
			btms[i] = pWellList.get(i).getWell().getDepth()[1];
			all[i] = tops[i];
		}
		System.arraycopy(btms, 0, all, tops.length, btms.length);

		// for(int i = 0; i < all.length; ++i)
		// System.out.println(all[i]);

		double max = getMaxValue(all);
		double min = getMinValue(all);
		double[] norTops = normalization(tops, max, min);
		double[] norBtms = normalization(btms, max, min);

		// 待修改，参数在此处计算完整
		float bigw = (PAppletSC.width - ScrollBar.size) / pWellList.size();
		LinkedList<PSection> psList = new LinkedList<PSection>();
		for (int i = 0; i < pWellList.size(); ++i) {
			float norY = (float) norTops[i];
			float norH = (float) (norBtms[i] - norTops[i]);
			// System.out.println(wellList.get(i).getName() + "   底：" +
			// norBtms[i] + "   顶：" + norTops[i]);
			float w = (float) (bigw * 0.3);
			psList.add(new PSection(pWellList.get(i).getWell(), (float) 20 + i * bigw, norY, norH, w));
		}
		return psList;
	}
	
	public static LinkedList<PSection> wellToPSection(LinkedList<Well> wellList) {
		if (wellList == null) {
			return null;
		}
		if (wellList.size() == 0) {
			return null;
		}

		double[] tops = new double[wellList.size()];
		double[] btms = new double[wellList.size()];
		double[] all = new double[wellList.size() * 2];
		for (int i = 0; i < wellList.size(); ++i) {
			tops[i] = wellList.get(i).getDepth()[0];
			btms[i] = wellList.get(i).getDepth()[1];
			all[i] = tops[i];
		}
		System.arraycopy(btms, 0, all, tops.length, btms.length);

		// for(int i = 0; i < all.length; ++i)
		// System.out.println(all[i]);

		double max = getMaxValue(all);
		double min = getMinValue(all);
		double[] norTops = normalization(tops, max, min);
		double[] norBtms = normalization(btms, max, min);

		// 待修改，参数在此处计算完整
		float bigw = (PAppletSC.width - ScrollBar.size) / wellList.size();
		LinkedList<PSection> psList = new LinkedList<PSection>();
		for (int i = 0; i < wellList.size(); ++i) {
			float norY = (float) norTops[i];
			float norH = (float) (norBtms[i] - norTops[i]);
			// System.out.println(wellList.get(i).getName() + "   底：" +
			// norBtms[i] + "   顶：" + norTops[i]);
			float w = (float) (bigw * 0.3);
			psList.add(new PSection(wellList.get(i), (float) 20 + i * bigw, norY, norH, w));
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
