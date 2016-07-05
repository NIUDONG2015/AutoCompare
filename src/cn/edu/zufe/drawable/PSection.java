package cn.edu.zufe.drawable;

import java.util.LinkedList;

import processing.core.PGraphics;
import cn.edu.zufe.model.DWell;

public class PSection extends PRect {

	public final static float OFFSET_Y = 800; // Y�����ƫ�Ƴ���

	public final static float PS_WIDTH = 200; // PSection�Ŀ��

	public static float wellWidth = 50; // ���Ŀ��

	public static float pixRatio = 2; // ���ر�

	public static float ngbPos = 600; // Ngb ��λ��

	private float wellWidthNow = 50;

	private float pixRatioNow = 1;

	private DWell well;

	// λ��ƫ�����Ŵ���� (!����zoomOut<=pg.hegiht+1��rect���Ƶ�ʱ���ʵ�ʴ�С��1���أ����ڻ��Ʊ߽�)
	// public final static float OFFSET_X = 0, OFFSET_Y = 100, ZOOM_OUT = 3800;
	private PSectionWell pSectionWell;

	private LinkedList<PBigLayer> pBigLayerList; // ����ͼ��

	private LinkedList<PSmallLayer> pSmallLayerList; // С���ͼ��

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
		// �;���
		// pg.fill(0);
		// pg.text(well.getName(), px, py - 20);
		pg.stroke(0);
		// ���;�
		pg.fill(255);
		pSectionWell.draw(pg);
		// �����

		// ��С��
		for (PSmallLayer ps : pSmallLayerList) {
			ps.draw(pg);
		}
	}

	/**
	 * ���������;�
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
			// �޸��;�
			float tmpX = px + PS_WIDTH / 2 - wellWidth / 2;
			pSectionWell.setPx(tmpX);
			pSectionWell.setPw(wellWidth);
			// ���
			// for (PBigLayer pBigLayer : pBigLayerList) {
			// pBigLayer.setPw(wellWidth);
			// }
			// С��
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
			// �޸��;�
			pSectionWell.setPy(Generator.toPixelY(ngbY, well));
			pSectionWell.setPh(Generator.toPixelH(well));
			pSectionWell.setScale();
			// ���
			// ...
			// С��
			for (PSmallLayer pSmallLayer : pSmallLayerList) {
				pSmallLayer.setPy(Generator.toPixelY(ngbY, pSmallLayer.getData()));
				pSmallLayer.setPh(Generator.toPixelH(pSmallLayer.getData()));
			}
		}
	}
}
