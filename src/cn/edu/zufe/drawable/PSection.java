package cn.edu.zufe.drawable;

import processing.core.*;

import java.util.LinkedList;

import cn.edu.zufe.model.*;

public class PSection {

	private Well well;
	private float px = -1, py = -1; // 图左上角位置
	private float ph = -1; // 高度
	private float pw = 20; // 宽度

	// 位移偏量及放大参数 (!必须zoomOut<=pg.hegiht+1，rect绘制的时候比实际大小多1像素，用于绘制边界)
	public final static float OFFSET_X = 0, OFFSET_Y = 100, ZOOM_OUT = 3800;
	private LinkedList<PSmallLayer> pSmallLayerList = new LinkedList<PSmallLayer>(); // 小层绘图类

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
	public PSection(Well well, float px, float py,float pw, float ph) {
		this.well = well;
		this.px = px;
		this.py = py;
		this.pw = pw;
		this.ph = ph;

		// System.out.println("(" + px + "," + py + ") | " + ph);
		// 转化小层数据为绘图类
		for (BigLayer bigLayer : well.getBigLayers()) {
			if (bigLayer.getSmallLayers().size() > 0) {
				pSmallLayerList.addAll(Generator.smallLayerToPSmallLayer(this, bigLayer.getSmallLayers()));
			}
		}
	}

	/**
	 * 缓存图 画出油井和小层
	 * 
	 * @param pg
	 */
	public void draw(PGraphics pg) {
		// 油井名
		pg.fill(0);
		pg.text(well.getName(), px, py - 20);
		pg.stroke(0);
		// 画油井
		pg.fill(255);
		pg.rect(px, py, pw, ph);
		// 画小层
		for (PSmallLayer psl : pSmallLayerList) {
			psl.draw(pg);
		}
	}

	/**
	 * 连接两个油井
	 * 
	 * @param pg
	 * @param ps
	 */
	public void connect(PGraphics pg, PSection ps) {
		for (PSmallLayer psl2 : ps.getPSmallLayerList()) {
			psl2.setFound(false);
		}

		for (PSmallLayer psl1 : pSmallLayerList) {
			for (PSmallLayer psl2 : ps.getPSmallLayerList()) {
				if (!psl2.isFound() && psl2.compare(psl1)) {
					psl1.connect(pg, psl2);
					psl2.setFound(true);
					break;
				}
			}
		}
	}

	/**
	 * 小层信息链表Get Set
	 * 
	 * @return
	 */
	public LinkedList<PSmallLayer> getPSmallLayerList() {
		return pSmallLayerList;
	}

	public Well getWell(){
		return this.well;
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

	public float getpw() {
		return pw;
	}

}
