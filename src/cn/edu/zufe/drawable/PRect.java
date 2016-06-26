package cn.edu.zufe.drawable;

import processing.core.PGraphics;

public class PRect {

	protected float px, py, pw, ph;

	public void setPx(float px) {
		this.px = px;
	}

	public void setPy(float py) {
		this.py = py;
	}

	public void setPw(float pw) {
		this.pw = pw;
	}

	public void setPh(float ph) {
		this.ph = ph;
	}

	public float getPx() {
		return px;
	}

	public float getPy() {
		return py;
	}

	public float getPw() {
		return pw;
	}

	public float getPh() {
		return ph;
	}

	public PRect(float px, float py, float pw, float ph) {
		this.px = px;
		this.py = py;
		this.pw = pw;
		this.ph = ph;
	}

	public void draw(PGraphics pg) {
		pg.rect(px, py, pw, ph);
	}

}
