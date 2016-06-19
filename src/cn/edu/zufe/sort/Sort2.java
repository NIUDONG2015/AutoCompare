package cn.edu.zufe.sort;

import java.util.LinkedList;

import cn.edu.zufe.drawable.PWell;
import cn.edu.zufe.model.Well;

public class Sort2 extends Sort{
	
	private PWell pStandardWell;
	private LinkedList<PWell> pWellList;
	private LinkedList<PWell> copyPWellList;
	
	public Sort2(PWell pStandardWell, LinkedList<PWell> pWellList) {
		super(pStandardWell, pWellList);
		// TODO Auto-generated constructor stub
		this.pStandardWell = pStandardWell;
		this.pWellList = pWellList;
	}
	
	public void doSort(){
		double wDis = 0, wSim = 1;
		int sum = 0;
		double totalWeight = Double.MAX_VALUE;
		while(wDis <= 1.0){

			copyPWellList = (LinkedList<PWell>) this.pWellList.clone();
			Sort1 sort1 = new Sort1(pStandardWell, copyPWellList);
			sort1.setwDis(wDis);
			sort1.setwSim(wSim);
			sort1.doSort();
			/*System.out.println(++sum+": "+sort1.getTotalWeight());
			for(int i=0; i<copyPWellList.size(); ++i){
				System.out.print(copyPWellList.get(i).getWell().getName()+" ");
			}
			System.out.println();*/
			if(totalWeight > sort1.getTotalWeight()){
				pWellList.clear();
				for(PWell pWell : copyPWellList){
					pWellList.add(pWell);
				}
				totalWeight = sort1.getTotalWeight();
			}
			wDis += 0.01;
			wSim -= 0.01;
		}
		
		/*System.out.println("wellList");
		for(int i=0; i<wellList.size(); ++i){
			System.out.print(wellList.get(i).getName()+" ");
		}
		System.out.println();*/
	}
}
