package cn.edu.zufe.drawable;

import processing.core.*;

public class ScrollBar {

	private PApplet p;
	private PGraphics pg;
	private float offset, length; // ���ι����������Ĳ���
	private boolean VorH; // ��¼��ʲô���͵Ĺ���������ֱOrˮƽ��
	private float x, y, w, h; // ����������λ����Ϣ
	private float colorGray = 205;
	private boolean locked; // �Ƿ��Ѿ�ѡ�й�����
	private boolean visible = true; // �Ƿ���ʾ������
	public static int size = 20;

	public ScrollBar(PApplet p, PGraphics pg, boolean VorH) {
		this.p = p;
		this.pg = pg;
		this.VorH = VorH;
		offset = 0;
		// ˮƽ�������ʹ�ֱ�������Ĳ�����Щ����
		// ��ʼ������������
		if (VorH) {
			w = size;
			y = offset;
		} else {
			h = size;
			x = offset;
		}
		setBarPos();
	}

	public void setBarPos() {
		if (VorH) {
			length = p.getHeight() - offset * 2 - size;
			h = length * length / pg.height;
			x = p.getWidth() - w;
			visible = length >= pg.height ? false : true;
		} else {
			length = p.getWidth() - offset * 2 - size;
			w = length * length / pg.width;
			y = p.getHeight() - h;
			visible = length >= pg.width ? false : true;
		}
	}

	public void draw(PGraphics tpg) {
		if (!visible) {
			return;
		}
		tpg.noStroke();
		// ���ƹ���������
		tpg.fill(240);
		// �˴��������β����ɲο��Ϸ�����ʼ������������
		if (VorH) {
			tpg.rect(p.getWidth() - w, 0, size, p.getHeight());
		} else {
			tpg.rect(0, p.getHeight() - h, p.getWidth(), size);
		}
		// ���ƹ�����
		tpg.fill(colorGray);
		tpg.rect(x, y, w, h);
		update();
	}

	private void update() {
		if (!visible) {
			return;
		}
		if (p.mousePressed && collisionDetection()) {
			colorGray = 96; // ���ʱ�ı���ɫ
			locked = true;
		}
		if (!p.mousePressed) {
			colorGray = 205;
			locked = false;
		}

		if (locked) {
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

	public void mouseWheel(int dir) {
		if (dir == 1) {
			y += 10;
		} else if (dir == -1) {
			y -= 10;
		} else {
			System.out.println("error: wrong dir value(" + dir + ")");
		}
		// ��ֹԽ��
		if (y < offset) {
			y = offset;
		}
		if (y > offset + length - h) {
			y = offset + length - h;
		}
	}

	public float getImagePos() {
		if (!visible) {
			return 0;
		}
		if (VorH) {
			return -(y - offset) * pg.height / length;
		} else {
			return -(x - offset) * pg.width / length;
		}
	}
}
