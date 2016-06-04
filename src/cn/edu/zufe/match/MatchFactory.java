package cn.edu.zufe.match;

import java.util.LinkedList;

import cn.edu.zufe.model.Well;

public class MatchFactory {
	private Well standardWell;
	private LinkedList<Well> wellList;
	public Match1 mMatch1;
	public MatchFactory(Well staWell,LinkedList<Well> wList){
		standardWell = staWell;
		wellList = new LinkedList<Well>();
		for(int i=0;i<wList.size();++i){
			Well well = wList.get(i);
			wellList.add(well);
		}
	}
	public void doMatch(int index){
		switch(index){
		case 1: mMatch1 = new Match1(standardWell,wellList);
				mMatch1.doMatch();
				break;
		default:System.out.println("Cant select a match function");
				break;
		}
	}
}
