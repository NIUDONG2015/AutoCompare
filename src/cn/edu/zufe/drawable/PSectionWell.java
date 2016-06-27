package cn.edu.zufe.drawable;

import processing.core.PGraphics;
import cn.edu.zufe.model.DDepth;
import cn.edu.zufe.model.DWell;

public class PSectionWell extends PRect {

	private DWell data;

	private final float sScale = 10; // ��Ҫ�̶�
	private final float mLen = 5; // ���̶��߳���
	private final float sLen = 2.5f; // ��Ҫ�̶��߳���

	private float sScalePix, mLenPix, sLenPix;
	private float num, cnt, firstY;

	public DWell getData() {
		return data;
	}

	public PSectionWell(DDepth data, float px, float py, float pw, float ph) {
		super(px, py, pw, ph);
		this.data = (DWell) data;
		setScale();
	}

	public void draw(PGraphics pg) {
		pg.noFill();
		super.draw(pg);
		drawScale(pg);
	}

	public void setScale() {
		sScalePix = sScale * PSection.pixRatio; // ��Ҫ�̶ȣ���λ�����أ�
		mLenPix = mLen * PSection.pixRatio; // ���̶��߳���
		sLenPix = sLen * PSection.pixRatio; // ��Ҫ�̶��߳���
		
		num = (int) ((data.getDepth()[1] - data.getDepth()[0]) / sScale); // ���߸���

		float nearlyAliquotTen = (int) (Math.rint(data.getDepth()[0] / sScale) * sScale); // ������ܱ�10��������
		int decade = (int) (nearlyAliquotTen / 100 - (int) (nearlyAliquotTen / 100)) / 10;
		cnt = decade > 5 ? 10 - decade : decade - 5;

		float ngbY = (float) data.getNgbDepth();
		firstY = PSection.ngbPos - (ngbY - nearlyAliquotTen) * PSection.pixRatio;
	}

	private void drawScale(PGraphics pg) {

		float cursorY = firstY;
		for (int i = 0; i <= num; i++) {
			cursorY += sScalePix;
			if ((i - cnt) / 5 == (int) ((i - cnt) / 5)) {
				// ��Ҫ�̶�
				pg.line(px, cursorY, px - mLenPix, cursorY);
			} else {
				// ��Ҫ�̶�
				pg.line(px, cursorY, px - sLenPix, cursorY);
			}
		}
	}
}
