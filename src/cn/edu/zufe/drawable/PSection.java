package cn.edu.zufe.drawable;

import processing.core.*;

import java.util.LinkedList;

import cn.edu.zufe.model.*;

public class PSection {

	private Well well;
	private float px = -1, py = -1; // ͼ���Ͻ�λ��
	private float ph = -1; // �߶�
	private float pw = 20; // ���

	// λ��ƫ�����Ŵ���� (!����zoomOut<=pg.hegiht+1��rect���Ƶ�ʱ���ʵ�ʴ�С��1���أ����ڻ��Ʊ߽�)
	public final static float OFFSET_X = 0, OFFSET_Y = 100, ZOOM_OUT = 3800;
	private LinkedList<PSmallLayer> pSmallLayerList = new LinkedList<PSmallLayer>(); // С���ͼ��

	/**
	 * ���캯��
	 * 
	 * @param well
	 *            �;�
	 * @param norX
	 *            ��һ����X����ֵ
	 * @param norY
	 *            ��һ�����Y����ֵ
	 */
	public PSection(Well well, float px, float py,float pw, float ph) {
		this.well = well;
		this.px = px;
		this.py = py;
		this.pw = pw;
		this.ph = ph;

		// System.out.println("(" + px + "," + py + ") | " + ph);
		// ת��С������Ϊ��ͼ��
		for (BigLayer bigLayer : well.getBigLayers()) {
			if (bigLayer.getSmallLayers().size() > 0) {
				pSmallLayerList.addAll(Generator.smallLayerToPSmallLayer(this, bigLayer.getSmallLayers()));
			}
		}
	}

	/**
	 * ����ͼ �����;���С��
	 * 
	 * @param pg
	 */
	public void draw(PGraphics pg) {
		// �;���
		pg.fill(0);
		pg.text(well.getName(), px, py - 20);
		pg.stroke(0);
		// ���;�
		pg.fill(255);
		pg.rect(px, py, pw, ph);
		// ��С��
		for (PSmallLayer psl : pSmallLayerList) {
			psl.draw(pg);
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

	/**
	 * С����Ϣ����Get Set
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
	 * ��ø߶�,λ����Ϣ
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
