package cn.edu.zufe.sort;

import java.util.LinkedList;

import cn.edu.zufe.drawable.PWell;
import cn.edu.zufe.model.Well;

public class SortFactory {
	private Well standardWell;
	private LinkedList<Well> wellList;
	public Sort1 mSort1;
	public Sort2 mSort2;
	public SortFactory(Well well,LinkedList<Well> tWellList){
		standardWell = well;
		wellList = tWellList;
	}
	public void doSort(int index){
		switch(index){
		case 1: mSort1 = new Sort1(standardWell,wellList);
				mSort1.doSort();
				break;
		case 2: mSort2 = new Sort2(standardWell,wellList);
				mSort2.doSort();
				break;
		default:System.out.println("Can't select a match function");
				break;
		}
	}
}
