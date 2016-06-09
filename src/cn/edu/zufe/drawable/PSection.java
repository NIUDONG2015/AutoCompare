package cn.edu.zufe.drawable;

import processing.core.*;

import java.util.LinkedList;

import cn.edu.zufe.model.*;

public class PSection {

	private Well well;
	private float px = -1, py = -1; // 图左上角位置
	private float ph = -1; // 高度
	private static float pw = 20; // 宽度
	private static float offsetX = 0, offsetY = 0, zoomOut = 4000;// 位移偏量及放大参数
	private LinkedList<SmallLayer> smallLayerList = new LinkedList<>(); // 保存小层数据，方便遍历

	/**
	 * 构造函数
	 * 
	 * @param well
	 *            油井
	 * @param norX
	 *            归一化后X坐标值
	 * @param norY
	 *            归一化后的Y坐标值
	 */
	public PSection(Well well, float norX, float norY, float norH) {
		this.well = well;
		this.px = norX;
		this.py = offsetY + norY * zoomOut;
		this.ph = norH * zoomOut;
		// System.out.println("(" + px + "," + py + ") | " + ph);
		setSmallLayerList();
	}

	/**
	 * 缓存图 画出油井和小层
	 * 
	 * @param pg
	 */
	public void draw(PGraphics pg) {

		pg.stroke(0);
		// 画油井
		pg.rect(px, py, pw, ph);
		pg.stroke(0);
		// 画小层
		for (SmallLayer smallLayer : smallLayerList) {
			float topH = ph * (float) smallLayer.getNorDepth()[0];
			pg.line(px, py + topH, px + pw, py + topH);
			float bottomH = ph * (float) smallLayer.getNorDepth()[1];
			pg.line(px, py + bottomH, px + pw, py + bottomH);
		}

	}

	/**
	 * 连接两个油井
	 * 
	 * @param pg
	 * @param ps
	 */
	public void connect(PGraphics pg, PSection ps) {

		for (SmallLayer smallLayer0 : smallLayerList) {

			float topH0 = (float) (py + ph * smallLayer0.getNorDepth()[0]);
			float bottomH0 = (float) (py + ph * smallLayer0.getNorDepth()[1]);
			boolean isConnect = false;
			for (SmallLayer smallLayer1 : ps.getSmallLayerList()) {

				// 小层不匹配
				if (smallLayer0.getMatchResName().equals(smallLayer1.getMatchResName()) == false) {
					continue;
				}

				if (!isConnect) {
					isConnect = true;
				}
				pg.stroke(255, 0, 0);
				// 砂岩顶深连接
				float topH1 = (float) (ps.getpy() + ps.getph() * smallLayer1.getNorDepth()[0]);
				pg.line(px + pw, topH0, ps.getpx(), topH1);

				// 砂岩底深连接
				float bottomH1 = (float) (ps.getpy() + ps.getph() * smallLayer1.getNorDepth()[1]);
				pg.line(px + pw, bottomH0, ps.getpx(), bottomH1);

			}
			// 找不到同名小层
			if (isConnect == false) {
				// 尖灭
				pg.stroke(255, 0, 0);
				pg.line(px + pw, topH0, px + pw + 80, topH0);
				pg.line(px + pw, bottomH0, px + pw + 80, topH0);
			}

		}
		
		//当前井右侧井的左侧尖灭
		for (SmallLayer smallLayer1 : ps.getSmallLayerList()) {
			float topH1 = (float) (ps.getpy() + ps.getph() * smallLayer1.getNorDepth()[0]);
			float bottomH1 = (float) (ps.getpy() + ps.getph() * smallLayer1.getNorDepth()[1]);
			boolean isConnect = false;
			
				for (SmallLayer smallLayer0 : smallLayerList) {
				// 小层不匹配
				if (smallLayer1.getMatchResName().equals(smallLayer0.getMatchResName()) == false) {
					continue;
				}

				if (!isConnect) {
					isConnect = true;
				}
			}
			// 找不到同名小层
			if (isConnect == false) {
				// 尖灭
				pg.stroke(255, 0, 0);
				pg.line(ps.getpx() , topH1, ps.getpx()  - 80, topH1);
				pg.line(ps.getpx() , bottomH1, ps.getpx() - 80, topH1);
			}

		}
	}

	/**
	 * 小层信息链表Get Set
	 * 
	 * @return
	 */
	public LinkedList<SmallLayer> getSmallLayerList() {
		return smallLayerList;
	}

	public void setSmallLayerList() {
		// 获取油井小层信息
		for (BigLayer bigLayer : well.getBigLayers()) {

			for (SmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (smallLayer.getMatchResName().equals("尖灭") || smallLayer.getMatchResName() == null || smallLayer.getMatchResName() == "") {
					continue;
				}
				smallLayerList.add(smallLayer);
			}
		}
	}

	/**
	 * 获得高度,位置信息
	 */
	public float getph() {
		return ph;
	}

	public float getpx() {
		return px;
	}

	public float getpy() {
		return py;
	}

}
