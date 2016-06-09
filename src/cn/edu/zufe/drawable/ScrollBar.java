package cn.edu.zufe.drawable;

import processing.core.*;

public class ScrollBar {

	private PApplet p;
	private PGraphics pg;
	// private float offset, length; // ������ƫ�Ƶĳ��ȣ��͹���������ĳ���
	private boolean VorH; // ��¼��ʲô���͵Ĺ���������ֱOrˮƽ��
	private float x, y, w, h; // ����������λ����Ϣ
	private float colorGray = 205;
	private boolean locked; // �Ƿ��Ѿ�ѡ�й�����

	public ScrollBar(PApplet p, PGraphics pg, boolean VorH) {
		this.p = p;
		this.pg = pg;
		this.VorH = VorH;
		// ˮƽ�������ʹ�ֱ�������Ĳ�����Щ����
		// ��ʼ������������
		if (VorH) {
			h = p.height * p.height / pg.height;
			w = 20;
			x = p.width - w - 5;
			y = 0;
		} else {
			h = 20;
			w = p.width * p.width / pg.width;
			x = 0;
			y = p.height - h;
		}

	}

	public void draw(PGraphics tpg) {
		tpg.noStroke();
		tpg.fill(colorGray);
		tpg.rect(x, y, w, h);
		update();
	}

	public void update() {
		if (p.mousePressed && collisionDetection()) {
			colorGray = 96; // ���ʱ�ı���ɫ
			locked = true;
		}
		if (!p.mousePressed) {
			colorGray = 205;
			locked = false;
		}

		if (locked) {
			// System.out.println(p.mouseY + " : " + p.pmouseY);
			if (VorH) {
				y += p.mouseY - p.pmouseY;
				// ��ֹԽ��
				if (y < 0) {
					y = 0;
				}
				if (y > p.height - h) {
					y = p.height - h;
				}
			} else {
				x += p.mouseX - p.pmouseX;
				// ��ֹԽ��
				if (x < 0) {
					x = 0;
				}
				if (x > p.width - w) {
					x = p.width - w;
				}
			}
		}
	}

	private boolean collisionDetection() {
		if (p.mouseX > x - w && p.mouseX < x + w) {
			if (p.mouseY > y - h && p.mouseY < y + h) {
				return true;
			}
		}
		return false;
	}

	public float getImagePos() {
		if(VorH) {
			return - y * pg.height / p.height;
		} else {
			return - x * pg.width / p.width;
		}
	}
}
