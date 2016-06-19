package cn.edu.zufe.sort;

import java.util.LinkedList;

import cn.edu.zufe.drawable.PWell;
import cn.edu.zufe.model.Well;

public class Sort1 extends Sort {

	private Well standardWell;
	private LinkedList<Well> wellList;

	public Sort1(Well standardWell, LinkedList<Well> wellList) {
		super(standardWell, wellList);
		// TODO Auto-generated constructor stub
		this.standardWell = standardWell;
		this.wellList = wellList;
	}

	public void doSort() {

		LinkedList<Well> tWellList = (LinkedList<Well>) wellList.clone();
		wellList.clear(); // ½«
		wellList.add(standardWell);
		
		double Smax = getMaxDis();
		double Nsum = (double) (getStandardWellSmallLayerNum(standardWell));
		double Gmax = getMaxGradient();
 		
		while (tWellList.size() > 0) {

			for (Well well : wellList) {
				if (tWellList.contains(well)) {
					tWellList.remove(well);
				}
			}

			Well pushWell = null;
			double Weight = Double.MAX_VALUE;
			
			for (Well tWell : tWellList) {
				int sz = wellList.size();
				double Dis = 0;
				double Sim = 0;
				
				for (Well well : wellList) {
					Dis += getDis(tWell, well);
					double N = (double) getSmallLayerMatchNum(tWell, well);
					double G = getGradient(tWell, well);
					Sim += N/Nsum*0.9 + (1.0-G/Gmax)*0.1;
				}
				double tWeight = (0.7 * Dis  + 0.3 + Sim) /sz;
				if(tWeight < Weight){
					Weight = tWeight;
					pushWell = tWell;
				}
			}
			
			if(pushWell != null){
				wellList.add(pushWell);
			}
		}
	}
}
