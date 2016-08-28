package cn.edu.zufe.model;

import java.util.LinkedList;

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
	private LinkedList<Double> DEPTH = new LinkedList<Double>();
	private LinkedList<Double> AC = new LinkedList<Double>();
	private LinkedList<Double> CAL = new LinkedList<Double>();
	private LinkedList<Double> GR = new LinkedList<Double>();
	private LinkedList<Double> COND = new LinkedList<Double>();
	private LinkedList<Double> RLML = new LinkedList<Double>();
	private LinkedList<Double> RNML = new LinkedList<Double>();
	private LinkedList<Double> R04 = new LinkedList<Double>();
	private LinkedList<Double> R25 = new LinkedList<Double>();
	private LinkedList<Double> R4 = new LinkedList<Double>();
	private LinkedList<Double> SP = new LinkedList<Double>();
	private LinkedList<Double> RFOC = new LinkedList<Double>();
	private LinkedList<Double> RILD = new LinkedList<Double>();
	private LinkedList<Double> RILM = new LinkedList<Double>();
	public LinkedList<Double> getDEPTH() {
		return DEPTH;
	}
	public void setDEPTH(LinkedList<Double> dEPTH) {
		DEPTH = dEPTH;
	}
	public LinkedList<Double> getAC() {
		return AC;
	}
	public void setAC(LinkedList<Double> aC) {
		AC = aC;
	}
	public LinkedList<Double> getCAL() {
		return CAL;
	}
	public void setCAL(LinkedList<Double> cAL) {
		CAL = cAL;
	}
	public LinkedList<Double> getGR() {
		return GR;
	}
	public void setGR(LinkedList<Double> gR) {
		GR = gR;
	}
	public LinkedList<Double> getCOND() {
		return COND;
	}
	public void setCOND(LinkedList<Double> cOND) {
		COND = cOND;
	}
	public LinkedList<Double> getRLML() {
		return RLML;
	}
	public void setRLML(LinkedList<Double> rLML) {
		RLML = rLML;
	}
	public LinkedList<Double> getRNML() {
		return RNML;
	}
	public void setRNML(LinkedList<Double> rNML) {
		RNML = rNML;
	}
	public LinkedList<Double> getR04() {
		return R04;
	}
	public void setR04(LinkedList<Double> r04) {
		R04 = r04;
	}
	public LinkedList<Double> getR25() {
		return R25;
	}
	public void setR25(LinkedList<Double> r25) {
		R25 = r25;
	}
	public LinkedList<Double> getR4() {
		return R4;
	}
	public void setR4(LinkedList<Double> r4) {
		R4 = r4;
	}
	public LinkedList<Double> getSP() {
		return SP;
	}
	public void setSP(LinkedList<Double> sP) {
		SP = sP;
	}
	public LinkedList<Double> getRFOC() {
		return RFOC;
	}
	public void setRFOC(LinkedList<Double> rFOC) {
		RFOC = rFOC;
	}
	public LinkedList<Double> getRILD() {
		return RILD;
	}
	public void setRILD(LinkedList<Double> rILD) {
		RILD = rILD;
	}
	public LinkedList<Double> getRILM() {
		return RILM;
	}
	public void setRILM(LinkedList<Double> rILM) {
		RILM = rILM;
	}
	
}
