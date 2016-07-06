package cn.edu.zufe.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DWellLogs {

	private Map<Double, DWellLogsAttribute> mpWellLogs = new LinkedHashMap<Double, DWellLogsAttribute>();

	public Map<Double, DWellLogsAttribute> getmpWellLogs() {
		return mpWellLogs;
	}

	public void setmpWellLogs(Map<Double, DWellLogsAttribute> wellLogs) {
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
