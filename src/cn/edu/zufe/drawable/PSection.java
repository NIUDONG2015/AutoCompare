package cn.edu.zufe.drawable;

import processing.core.*;

import java.util.LinkedList;

import cn.edu.zufe.model.*;

public class PSection {

	private Well well;
	private float px = -1, py = -1; // ͼ���Ͻ�λ��
	private float ph = -1; // �߶�
	private static float pw = 80; // ���
	private static float offsetX = 0, offsetY = 0, zoomOut = 300; // λ��ƫ�����Ŵ����
	private LinkedList<SmallLayer> smallLayerList = new LinkedList<>(); // ����С�����ݣ��������

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
	public PSection(Well well, float norX, float norY, float norH) {
		this.well = well;
		this.px = norX;
		this.py = offsetY + norY * zoomOut;
		this.ph = norH * zoomOut;
		System.out.println("(" + px + "," + py + ") | " + ph);
		setSmallLayerList();
	}

	/**
	 * ����ͼ �����;���С��
	 * 
	 * @param pg
	 */
	public void draw(PGraphics pg) {

		// ���;�
		pg.rect(px, py, pw, ph);

		// ��С��
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
	 * ���������;�
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

				// С�㲻ƥ��
				if (smallLayer0.getMatchResName().equals(smallLayer1.getMatchResName()) == false) {
					continue;
				}

				if (!is_connect) {
					is_connect = true;
				}
				try {
					// ɰ�Ҷ�������
					float topH1 = (float) (ps.getpy() + ps.getph() * smallLayer1.getNorDepth()[0]);
					pg.line(px + pw, topH0, ps.getpx(), topH1);

					// ɰ�ҵ�������
					float bottomH1 = (float) (ps.getpy() + ps.getph() * smallLayer1.getNorDepth()[1]);
					pg.line(px + pw, bottomH0, ps.getpx(), bottomH1);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// �Ҳ���ͬ��С��
			if (is_connect == false) {
				// ����
				pg.line(px + pw, topH0, px + pw + 20, topH0);
				pg.line(px + pw, bottomH0, px + pw + 20, topH0);
			}

		}
	}

	/**
	 * С����Ϣ����Get Set
	 * 
	 * @return
	 */
	public LinkedList<SmallLayer> getSmallLayerList() {
		return smallLayerList;
	}

	public void setSmallLayerList() {
		// ��ȡ�;�С����Ϣ
		for (BigLayer bigLayer : well.getBigLayers()) {

			for (SmallLayer smallLayer : bigLayer.getSmallLayers()) {
				if (smallLayer.getMatchResName().equals("����") || smallLayer.getMatchResName() == null || smallLayer.getMatchResName() == "") {
					continue;
				}
				smallLayerList.add(smallLayer);
			}
		}
	}

	/**
	 * ��ø߶�,λ����Ϣ
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
