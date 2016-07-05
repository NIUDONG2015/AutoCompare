package cn.edu.zufe.drawable;

import java.util.LinkedList;

import processing.core.PGraphics;
import cn.edu.zufe.model.DWell;

public class PSection extends PRect {

	public final static float OFFSET_Y = 800; // Y的相对偏移常量

	public final static float PS_WIDTH = 200; // PSection的宽度

	public static float wellWidth = 50; // 井的宽度

	public static float pixRatio = 2; // 像素比

	public static float ngbPos = 600; // Ngb 的位置

	private float wellWidthNow = 50;

	private float pixRatioNow = 1;

	private DWell well;

	// 位移偏量及放大参数 (!必须zoomOut<=pg.hegiht+1，rect绘制的时候比实际大小多1像素，用于绘制边界)
	// public final static float OFFSET_X = 0, OFFSET_Y = 100, ZOOM_OUT = 3800;
	private PSectionWell pSectionWell;

	private LinkedList<PBigLayer> pBigLayerList; // 大层绘图类

	private LinkedList<PSmallLayer> pSmallLayerList; // 小层绘图类

	public DWell getWell() {
		return well;
	}

	public LinkedList<PSmallLayer> getPSmallLayerList() {
		return pSmallLayerList;
	}

	public void setPSectionWell(PSectionWell pSectionWell) {
		this.pSectionWell = pSectionWell;
	}

	public void setPSmallLayerList(LinkedList<PSmallLayer> pSmallLayerList) {
		this.pSmallLayerList = pSmallLayerList;
	}

	public void setPBigLayerList(LinkedList<PBigLayer> pBigLayerList) {
		this.pBigLayerList = pBigLayerList;
	}

	public PSection(DWell well, float px, float py, float pw, float ph) {
		super(px, py, pw, ph);
		this.well = well;
	}

	public void draw(PGraphics pg) {
		// 油井名
		// pg.fill(0);
		// pg.text(well.getName(), px, py - 20);
		pg.stroke(0);
		// 画油井
		pg.fill(255);
		pSectionWell.draw(pg);
		// 画大层

		// 画小层
		for (PSmallLayer ps : pSmallLayerList) {
			ps.draw(pg);
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

	public void connectNull(PGraphics pg, boolean leftOrRight) {
		for (PSmallLayer psl : pSmallLayerList) {
			psl.connectNull(pg, leftOrRight);
		}
	}

	public void setWellWidth() {
		if (wellWidthNow != wellWidth) {
			wellWidthNow = wellWidth;
			// 修改油井
			float tmpX = px + PS_WIDTH / 2 - wellWidth / 2;
			pSectionWell.setPx(tmpX);
			pSectionWell.setPw(wellWidth);
			// 大层
			// for (PBigLayer pBigLayer : pBigLayerList) {
			// pBigLayer.setPw(wellWidth);
			// }
			// 小层
			for (PSmallLayer pSmallLayer : pSmallLayerList) {
				pSmallLayer.setPx(tmpX);
				pSmallLayer.setPw(wellWidth);
			}
		}
	}

	public void setPixRatio() {
		if (pixRatioNow != pixRatio) {
			pixRatioNow = pixRatio;
			float ngbY = (float) well.getNgbDepth();
			// 修改油井
			pSectionWell.setPy(Generator.toPixelY(ngbY, well));
			pSectionWell.setPh(Generator.toPixelH(well));
			pSectionWell.setScale();
			// 大层
			// ...
			// 小层
			for (PSmallLayer pSmallLayer : pSmallLayerList) {
				pSmallLayer.setPy(Generator.toPixelY(ngbY, pSmallLayer.getData()));
				pSmallLayer.setPh(Generator.toPixelH(pSmallLayer.getData()));
			}
		}
	}
}
