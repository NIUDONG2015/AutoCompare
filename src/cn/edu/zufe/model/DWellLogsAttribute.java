package cn.edu.zufe.model;

/*#
 ~Curve Information Block
 #MNEM.UNIT       API CODE      Curve Description
 #----------    -------------   -------------------
 DEPT.M                    :   Depth in Meters
 AC  .Î¢Ãë/Ã×                      :   AC
 CAL .ÀåÃ×                      :   CAL
 CAL .ÀåÃ×                      :   CAL
 COND.mS/m                      :   COND
 RLML.Å·Ä·Ã×                      :   RLML
 RNML.Å·Ä·Ã×                      :   RNML
 R04 .Å·Ä·Ã×                      :   R04
 R25 .Å·Ä·Ã×                      :   R25
 R4  .Å·Ä·Ã×                      :   R4
 SP  .ºÁ·ü                      :   SP
 SP  .ºÁ·ü                      :   SP*/
public class DWellLogsAttribute {
	private double DEPTH;
	private double AC;
	private double CAL1;
	private double CAL2;
	private double COND;
	private double RLML;
	private double RNML;
	private double R04;
	private double R25;
	private double R4;
	private double SP1;
	private double SP2;

	public double getDEPTH() {
		return DEPTH;
	}

	public void setDEPTH(double dEPTH) {
		DEPTH = dEPTH;
	}

	public double getAC() {
		return AC;
	}

	public void setAC(double aC) {
		AC = aC;
	}

	public double getCAL1() {
		return CAL1;
	}

	public void setCAL1(double cAL1) {
		CAL1 = cAL1;
	}

	public double getCAL2() {
		return CAL2;
	}

	public void setCAL2(double cAL2) {
		CAL2 = cAL2;
	}

	public double getCOND() {
		return COND;
	}

	public void setCOND(double cOND) {
		COND = cOND;
	}

	public double getRLML() {
		return RLML;
	}

	public void setRLML(double rLML) {
		RLML = rLML;
	}

	public double getRNML() {
		return RNML;
	}

	public void setRNML(double rNML) {
		RNML = rNML;
	}

	public double getR04() {
		return R04;
	}

	public void setR04(double r04) {
		R04 = r04;
	}

	public double getR25() {
		return R25;
	}

	public void setR25(double r25) {
		R25 = r25;
	}

	public double getR4() {
		return R4;
	}

	public void setR4(double r4) {
		R4 = r4;
	}

	public double getSP1() {
		return SP1;
	}

	public void setSP1(double sP1) {
		SP1 = sP1;
	}

	public double getSP2() {
		return SP2;
	}

	public void setSP2(double sP2) {
		SP2 = sP2;
	}
}
