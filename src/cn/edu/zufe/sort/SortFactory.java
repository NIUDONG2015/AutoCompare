package cn.edu.zufe.sort;

import java.util.LinkedList;

import cn.edu.zufe.drawable.PMapWell;
import cn.edu.zufe.model.Well;

public class SortFactory {
	private PMapWell pStandardWell;
	private LinkedList<PMapWell> pWellList;
	public Sort1 mSort1;
	public Sort2 mSort2;
	public SortFactory(PMapWell pWell,LinkedList<PMapWell> pwList){
		this.pStandardWell = pWell;
		this.pWellList = pwList;
	}
	public void doSort(int index){
		switch(index){
		case 1: mSort1 = new Sort1(pStandardWell,pWellList);
				mSort1.doSort();
				break;
		case 2: mSort2 = new Sort2(pStandardWell,pWellList);
				mSort2.doSort();
				break;
		default:System.out.println("Can't select a match function");
				break;
		}
	}
}
