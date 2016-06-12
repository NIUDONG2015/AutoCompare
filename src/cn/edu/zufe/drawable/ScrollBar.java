package cn.edu.zufe.drawable;

import processing.core.*;

public class ScrollBar {

	private PApplet p;
	private PGraphics pg;
	private float offset, length, size; // ���ι����������Ĳ���
	private boolean VorH; // ��¼��ʲô���͵Ĺ���������ֱOrˮƽ��
	private float x, y, w, h; // ����������λ����Ϣ
	private float colorGray = 205;
	private boolean locked; // �Ƿ��Ѿ�ѡ�й�����

	public ScrollBar(PApplet p, PGraphics pg, boolean VorH) {
		this.p = p;
		this.pg = pg;
		this.VorH = VorH;
		size = 20;
		offset = 0;
		// ˮƽ�������ʹ�ֱ�������Ĳ�����Щ����
		// ��ʼ������������
		if (VorH) {
			length = p.height - offset * 2 - size;
			h = length * length / pg.height;
			w = size;
			x = p.width - w;
			y = offset;
		} else {
			length = p.width - offset * 2 - size;
			h = size;
			w = length * length / pg.width;
			x = offset;
			y = p.height - h;
		}

	}

	public void draw(PGraphics tpg) {
		tpg.noStroke();
		// ���ƹ���������
		tpg.fill(240);
		// �˴��������β����ɲο��Ϸ�����ʼ������������
		if (VorH) {
			tpg.rect(p.width - w, 0, size, p.height);
		} else {
			tpg.rect(0, p.height - h, p.width, size);
		}
		// ���ƹ�����
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
				if (y < offset) {
					y = offset;
				}
				if (y > offset + length - h) {
					y = offset + length - h;
				}
			} else {
				x += p.mouseX - p.pmouseX;
				// ��ֹԽ��
				if (x < offset) {
					x = offset;
				}
				if (x > offset + length - w) {
					x = offset + length - w;
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
		if (VorH) {
			return -(y - offset) * pg.height / length;
		} else {
			return -(x - offset) * pg.width / length;
		}
	}
}
