package cn.edu.zufe.sort;

import java.util.LinkedList;

import cn.edu.zufe.drawable.Generator;
import cn.edu.zufe.drawable.PSection;
import cn.edu.zufe.drawable.PWell;
import cn.edu.zufe.model.Well;

public class Sort1 extends Sort {

	private PWell pStandardWell;
	private LinkedList<PWell> pWellList;
	private LinkedList<PSection> pSectionList;
	private double wDis = 0.7;	//Disϵ��
	private double wSim = 0.3;	//Simϵ��
	private double totalWeight = 0;	//��Ȩ��
	

	public Sort1(PWell pStandardWell, LinkedList<PWell> pWellList) {
		super(pStandardWell, pWellList);
		// TODO Auto-generated constructor stub
		this.pStandardWell = pStandardWell;
		this.pWellList = pWellList;
		this.pSectionList = Generator.pWellToPSection(this.pWellList);
	}

	public void doSort() {
		totalWeight = 0;
		LinkedList<PWell> tPWellList = (LinkedList<PWell>) pWellList.clone();
		pWellList.clear(); 
		pWellList.add(pStandardWell);
		tPWellList.remove(pStandardWell);
		
		double Smax = getMaxDis();
		double Nsum = (double) (getStandardWellSmallLayerNum(pStandardWell));
		//System.out.print("Nsum:"+Nsum);
		double Gmax = getMaxGradient();
		//System.out.println("  Gmax:"+Gmax);
 		
		while (tPWellList.size() > 0) {

			for (PWell pWell : pWellList) {
				if (tPWellList.contains(pWell)) {
					tPWellList.remove(pWell);
				}
			}

			PWell pushPWell = null;
			double Weight = Double.MAX_VALUE;
			
			for (PWell tPWell : tPWellList) {
				int sz = tPWellList.size();
				double Dis = 0;
				double Sim = 0;
				PSection pSectionA = Generator.pWellToPSection(tPWell, pSectionList);
				for (PWell pWell : pWellList) {
					Dis = Dis + getDis(tPWell, pWell)/Smax;
					double N = (double) getSmallLayerMatchNum(tPWell, pWell);
					PSection pSectionB = Generator.pWellToPSection(pWell, pSectionList);
					double G = getGradient(pSectionA, pSectionB);
					Sim += N/Nsum*0.9 + (1.0-G/Gmax)*0.1;
					//System.out.printf("Dis:"+Dis+"  N:"+N+"  G:"+G+"  Sim:"+Sim);
					
				}
				double tWeight = (wDis * Dis  + wSim + Sim) /sz;
				//System.out.print("  "+tPWell.getWell().getName()+"  "+tWeight);
				//System.out.println();
				if(tWeight < Weight){
					Weight = tWeight;
					pushPWell = tPWell;
				}
			}
			
			if(pushPWell != null){
				pWellList.add(pushPWell);
				totalWeight += Weight;
			}
			//System.out.println("");
			//System.out.println("");
		}
		//System.out.println("wellList");
		//for(int i=0; i<pWellList.size(); ++i){
		//	System.out.print(pWellList.get(i).getWell().getName()+" ");
		//}
		//System.out.println();
	}
	
	
	public double getwDis() {
		return wDis;
	}

	public void setwDis(double wDis) {
		this.wDis = wDis;
	}
	public double getwSim() {
		return wSim;
	}

	public void setwSim(double wSim) {
		this.wSim = wSim;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

}
