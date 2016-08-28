package cn.edu.zufe.model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class DWellLogs {

	private Map<LinkedList<Double>, DWellLogsAttribute> mpWellLogs = new LinkedHashMap<LinkedList<Double>, DWellLogsAttribute>();

	public Map<LinkedList<Double>, DWellLogsAttribute> getmpWellLogs() {
		return mpWellLogs;
	}

	public void setmpWellLogs(Map<LinkedList<Double>, DWellLogsAttribute> wellLogs) {
		this.mpWellLogs = wellLogs;
	}

	public DWellLogsAttribute getLogsAttribute(double depth) {
		DWellLogsAttribute wla = null;
		try {
			wla = mpWellLogs.get(depth);
		} catch (Exception e) {
			wla = null;
		}
		return wla;
	}
}
