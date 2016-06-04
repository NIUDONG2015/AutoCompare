package cn.edu.zufe.ui;

import java.util.LinkedList;

import processing.core.*;
import cn.edu.zufe.drawable.PWell;

public class PAppletWellView extends PApplet {

	private PAppletSC psc;
	private PGraphics pgBottom, pgHighlight;
	private int width, height;
	private LinkedList<PWell> pwList = null;
	private PShape iconOrigin, iconClicked; // ����ͼ��

	public PAppletWellView(int width, int height, PApplet psc) {
		this.width = width;
		this.height = height;
		this.psc = (PAppletSC) psc;
	}

	public void setPWells(LinkedList<PWell> pwList) {
		this.pwList = pwList;
	}

	public void setup() {
		this.size(width, height);
		// ��׶˻���ͼ��ʼ��
		pgBottom = createGraphics(width, height);
		pgBottom.beginDraw();
		pgBottom.endDraw();
		// ��������ͼ��ʼ��
		pgHighlight = createGraphics(width, height);
		pgHighlight.beginDraw();
		pgHighlight.endDraw();
		// ���� SVG ʸ��ͼ
		iconOrigin = loadShape("res//oil_field.svg");
		iconClicked = loadShape("res//oil_field_clicked.svg");
	}

	public void draw() {
		image(pgBottom, 0, 0);
		highlight();
	}

	/**
	 * ���� pgBottom
	 */
	public void drawPGBottom() {
		if (pwList != null) {
			pgBottom.beginDraw();
			pgBottom.clear();
			pgBottom.background(255);
			for (PWell pw : pwList) {
				pw.draw(pgBottom, iconOrigin, iconClicked);
			}
			pgBottom.endDraw();
		}
	}
	
	/**
	 * ���� pgHighlight
	 * @param pw
	 */
	public void drawPGHighlight(PWell pw) {
		if (pw != null) {
			pgHighlight.beginDraw();
			pgHighlight.clear();
			pw.highlight(pgHighlight, iconOrigin, iconClicked);
			pgHighlight.endDraw();
		}
	}

	/**
	 * �������ʱ����
	 */
	private void highlight() {
		if (pwList != null) {
			for (PWell pw : pwList) {
				// �����ͼ����ײ
				if (pw.collisionDetection(mouseX, mouseY)) {
					drawPGHighlight(pw);
					image(pgHighlight, 0, 0);
					break;
				}
			}
		}
	}

	public void mousePressed() {
		for (PWell pw : pwList) {
			pw.setClicked(false);
		}

		if (mouseButton == LEFT) {
			for (PWell pw : pwList) {
				if (pw.collisionDetection(mouseX, mouseY)) {
					pw.setClicked(true);
					break;
				}
			}
		}
		drawPGBottom();
	}
}
