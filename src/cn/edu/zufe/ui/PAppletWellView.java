package cn.edu.zufe.ui;

import java.util.LinkedList;

import processing.core.*;
import cn.edu.zufe.drawable.*;
import cn.edu.zufe.model.*;
import cn.edu.zufe.sort.SortFactory;

public class PAppletWellView extends PApplet {

	private PAppletSC psc;
	private PGraphics pgBottom, pgHighlight;
	private int width, height;
	private LinkedList<PMapWell> pwList = null;
	private LinkedList<DWell> compareWellList = new LinkedList<DWell>();
	private PShape iconOrigin, iconClicked; // ����ͼ��
	private int sort = 0;	//���򷽷�
	
	public PAppletWellView(int width, int height, PApplet psc) {
		this.width = width;
		this.height = height;
		this.psc = (PAppletSC) psc;
	}

	public void setPWells(LinkedList<PMapWell> pwList) {
		this.pwList = pwList;
	}

	public void setup() {
		size(width, height);
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
		background(255);
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
			for (PMapWell pw : pwList) {
				pw.draw(pgBottom, iconOrigin, iconClicked);
			}
			pgBottom.endDraw();
		} else {
			pgBottom.beginDraw();
			pgBottom.clear();
			pgBottom.background(255);
			pgBottom.endDraw();
		}
	}
	

	/**
	 * ���� pgHighlight
	 * 
	 * @param pw
	 */
	public void drawPGHighlight(PMapWell pw) {
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
			for (PMapWell pw : pwList) {
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
		if(sort != 0) {
			return ;
		}
		
		if (pwList != null) {
			if (mouseButton == LEFT) {
				for (PMapWell pw : pwList) {
					if (pw.collisionDetection(mouseX, mouseY)) {
						pw.setClicked();
						if (!compareWellList.contains(pw.getWell())) {
							compareWellList.add(pw.getWell());
						} else {
							compareWellList.remove(pw.getWell());
						}
						LinkedList<PSection> psList = Generator.wellToPSection(compareWellList);
						psc.setPSList(psList);
						psc.drawPGBottom();
						break;
					}
				}
			}
			drawPGBottom();
		}
	}
	
	public void setSort(int tsort){
		sort = tsort;
		for(PMapWell pw : pwList){
			pw.setClicked(false);
		}
		compareWellList.clear();
		psc.setPSList(null);
		psc.drawPGBottom();
		
		if(sort != 0){
			for(PMapWell pw : pwList){
				compareWellList.add(pw.getWell());
			}
			LinkedList<PMapWell> tPWList = Generator.wellToPMapWells(compareWellList);
			
			SortFactory sortMethod = new SortFactory(pwList.get(1), tPWList);
			sortMethod.doSort(tsort);
			
			LinkedList<PSection> psList = Generator.pWellToPSection(tPWList);
			psc.setPSList(psList);
			psc.drawPGBottom();
		}
	}
	
}
