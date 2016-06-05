package cn.edu.zufe.drawable;

import processing.core.*;

import java.util.LinkedList;

import cn.edu.zufe.model.*;

public class PSection {

	private Well well;
	private float px = -1, py = -1; // 图左上角位置
	private float ph = -1; // 高度
	private static float pw = 80; // 宽度
	private static float offsetX = 50, offsetY = 20, zoomOut = 200; // 位移偏量及放大参数
	private LinkedList<SmallLayer> smallLayerList = new LinkedList<>(); // 保存小层数据，方便遍历

	/**
	 * 构造函数
	 * 
	 * @param well
	 *            油井
	 * @param norX
	 *            归一化前X坐标值
	 * @param norY
	 *            归一化前的Y坐标值
	 */
	public PSection(Well well, float norX, float norY, float height) {
		this.well = well;
		this.px = offsetX + norX * zoomOut;
		this.py = offsetY + norY * zoomOut;
		this.ph = height;

		setSmallLayerList();
	}

	/**
	 * 缓存图 画出油井和小层
	 * 
	 * @param pg
	 */
	public void draw(PGraphics pg) {

		// 画油井
		pg.rect(px, py, pw, ph);

		// 画小层
		for (SmallLayer smallLayer : smallLayerList) {
			try {
				float topH = (float) (py + ph * smallLayer.getNorDepth()[0]);
				pg.line(px, py + topH, px + pw, py + topH);

				float bottomH = (float) (py + ph * smallLayer.getNorDepth()[1]);
				pg.line(px, py + bottomH, px + pw, py + bottomH);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			boolean is_connect = false;
			for (SmallLayer smallLayer1 : ps.getSmallLayerList()) {

				// 小层不匹配
				if (smallLayer0.getMatchResName().equals(smallLayer1.getMatchResName()) == false) {
					continue;
				}

				if (!is_connect) {
					is_connect = true;
				}
				try {
					// 砂岩顶深连接
					float topH1 = (float) (ps.getpy() + ps.getph() * smallLayer1.getNorDepth()[0]);
					pg.line(px + pw, topH0, ps.getpx(), topH1);

					// 砂岩底深连接
					float bottomH1 = (float) (ps.getpy() + ps.getph() * smallLayer1.getNorDepth()[1]);
					pg.line(px + pw, bottomH0, ps.getpx(), bottomH1);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// 找不到同名小层
			if (is_connect == false) {
				// 尖灭
				pg.line(px + pw, topH0, px + pw + 20, topH0);
				pg.line(px + pw, bottomH0, px + pw + 20, bottomH0);
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

				if (smallLayer.getMatchResName().equals("尖灭") || smallLayer.getMatchResName() == null
						|| smallLayer.getMatchResName() == "") {
					continue;
				}
				this.smallLayerList.add(smallLayer);
			}
		}
		this.smallLayerList = smallLayerList;
	}

	/**
	 * 获得高度,位置信息
	 */
	public float getph() {
		return this.getph();
	}

	public float getpx() {
		return this.getpx();
	}

	public float getpy() {
		return this.getpy();
	}

}
