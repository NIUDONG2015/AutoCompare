package cn.edu.zufe.match;

import java.util.LinkedList;

import cn.edu.zufe.model.DWell;

public class MatchFactory {
	private DWell standardWell;
	private LinkedList<DWell> wellList;
	public Match1 mMatch1;
	public MatchFactory(DWell staWell,LinkedList<DWell> wList){
		standardWell = staWell;
		wellList = wList;
	}
	public void doMatch(int index){
		switch(index){
		case 1: mMatch1 = new Match1(standardWell,wellList);
				mMatch1.doMatch();
				break;
		default:System.out.println("Can't select a match function");
				break;
		}
	}
}
