package com.guyiping;

import java.util.ArrayList;

public class LineInfo {
	
	String lineDirection;
	public ArrayList<StationInfo> stationInfo;
	
	public LineInfo(String lineDirection)
	{
		this.lineDirection = lineDirection;
		stationInfo = new ArrayList<StationInfo>();
	}
	
	
	public void addStationInfo(StationInfo stationInfo)
	{
		if(this.stationInfo == null)
		{
			this.stationInfo = new ArrayList<StationInfo>();
			this.stationInfo.add(stationInfo);		
		}
		else
			this.stationInfo.add(stationInfo);
	}
	
	
	public String toString()
	{
		StringBuffer Info = new StringBuffer();
		for (int i = 0; i < stationInfo.size(); i++) {
			Info.append(stationInfo.get(i).StationNo).append(".").append(stationInfo.get(i).StationName).append("\n");
		}
		return Info.toString();
		
	}

}
