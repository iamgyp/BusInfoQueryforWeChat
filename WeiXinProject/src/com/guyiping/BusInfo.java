package com.guyiping;

import java.util.ArrayList;

public class BusInfo {
	public String BusNo;
	public String price;
	public boolean isDownFlag;
	public String LineDirect;
	public String internalNo;

	public ArrayList<LineInfo> lineInfo;

	public BusInfo(String busno, String price) {
		this.BusNo = busno;
		this.price = price;
		lineInfo = new ArrayList<LineInfo>();
	}
	
	public void setInternalNo(String internalNo)
	{
		this.internalNo = internalNo;
	}

	public void addLineInfo(LineInfo lineInfo) {
		if (this.lineInfo == null) {
			this.lineInfo = new ArrayList<LineInfo>();
			this.lineInfo.add(lineInfo);
		} else
			this.lineInfo.add(lineInfo);
	}

	public String toString() {
		StringBuffer Info = new StringBuffer();
		for (int i = 0; i < lineInfo.size(); i++) {
			LineInfo lineinfo = lineInfo.get(i);
			Info.append(BusNo).append("(").append(lineinfo.lineDirection)
					.append(")\n");
			//Info.append("¼Û¸ñ£º").append(price).append(" \n");
			Info.append(lineinfo.toString());
		}

		return Info.toString();
	}

}
