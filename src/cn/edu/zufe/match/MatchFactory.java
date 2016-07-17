package cn.edu.zufe.match;

import java.util.LinkedList;

import cn.edu.zufe.model.DWell;

public class MatchFactory {
	private DWell standardWell;
	private DWell doWell;
	private LinkedList<DWell> wellList;
	public boolean isFirstElement;
	public Match0 mMatch0;
	public Match1 mMatch1;
	public Match2 mMatch2;

	public MatchFactory(LinkedList<DWell> wList){
		wellList = wList;
	}
	public MatchFactory(DWell staWell, LinkedList<DWell> wList) {
		standardWell = staWell;
		wellList = wList;
	}

	public MatchFactory(DWell staWell, DWell matWell, boolean isFirst) {
		standardWell = staWell;
		doWell = matWell;
		isFirstElement = isFirst;
	}
	
	public void doMatch(int index) {
		switch (index) {
		case 0:
			mMatch0 = new Match0(wellList);
			mMatch0.doMatch();
			break;
		case 1:
			mMatch1 = new Match1(standardWell, wellList);
			mMatch1.doMatch();
			break;
		case 2:
			mMatch2 = new Match2(standardWell, doWell);
			mMatch2.doMatch(isFirstElement);
			break;
		default:
			System.out.println("Can't select a match function");
			break;
		}
	}
}
