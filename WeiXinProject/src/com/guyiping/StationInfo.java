package com.guyiping;

import java.util.ArrayList;

public class StationInfo {
	public String StationName;
	public int StationNo;
	public String x_station;
	public String y_station;
	public ArrayList<GPSInfo> gpsInfo;
	
	public StationInfo(String stationName,int stationNo, String x, String y)
	{
		this.StationName = stationName;
		this.StationNo = stationNo;
		this.x_station = x;
		this.y_station = y;
	}
	
	public void addgpsInfo(GPSInfo gpsInfo)
	{
		if(this.gpsInfo == null)
		{
			this.gpsInfo = new ArrayList<GPSInfo>();
			this.gpsInfo.add(gpsInfo);		
		}
		else
			this.gpsInfo.add(gpsInfo);
		
	}
	
	
	
	

}
