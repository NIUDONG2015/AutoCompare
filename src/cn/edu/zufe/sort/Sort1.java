package cn.edu.zufe.sort;

import java.util.LinkedList;

import cn.edu.zufe.drawable.PWell;
import cn.edu.zufe.model.Well;

public class Sort1 extends Sort{

	private Well standardWell;
	private LinkedList<Well> wellList;
	
	public Sort1(Well standardWell, LinkedList<Well> wellList) {
		super(standardWell, wellList);
		// TODO Auto-generated constructor stub
		this.standardWell = standardWell;
		this.wellList = wellList;
	}

	public void doSort(){
		LinkedList<Well> tWellList = (LinkedList<Well>) wellList.clone();
		wellList.clear();
		wellList.add(standardWell);
		
		/*while(tWellList.size() > 0){
			
			for(Well well:wellList){
				if(tWellList.contains(well)){
					tWellList.remove(well);
				}
			}
			int id = -1;
			double Weight = Double.MAX_VALUE;
			
			for(Well tWell:tWellList){
				double Dis = 0;
			}
		}*/
	}
}
