package com.guyiping;

public class GetMessage {
	
	public static String HelpMessage()
	{
		return "欢迎使用杭州公交小助手，目前提供查公交线路和车辆到站信息查询，大家可以直接输入线路名称即可实时查询公交路线或者输入线路<空格>站名查询车辆到站信息。\n 例如 输入 “8”，获取8路车的线路信息，输入“8 市一”即可获得8路车市一医院站的到站信息，站名支持联想，输入一两个关键字就可以哦，如遇到无返回的情况，请继续猛戳，输入h获得试用帮助。"
				+ "";
	}
	
	public static String getMessage(String UserInputMsgType,String UserInputContent)  throws Exception
	{

		if (0 == UserInputMsgType.compareToIgnoreCase("event")) {
			return HelpMessage();	
		}else if(0 != UserInputMsgType.compareToIgnoreCase("text"))
		{
			return HelpMessage();
		}
		else
		{
			if (0 == UserInputContent.trim().compareToIgnoreCase("h")) {
				return HelpMessage();
				
			}
			
			
			String[] UserInput = UserInputContent.split(" ");
			int userInputSize = UserInput.length ;
			
			boolean webStatus = GetBusInfo.getInstance().isQueryNormal();
			if (!webStatus) {
				return " 呜呜呜，公交公司网站不理小助手，真讨厌~，请稍后再试";
			}
			
			String busno = GetBusInfo.getInstance().autoConfirmBusNo(UserInput[0]);
			
			if (busno == null) {
				return "未找到该车次，请重新输入";
			}
			else if(busno.length()> 10)
			{
				return busno;
			}
			else
			{            
				switch (userInputSize) {
				case 1:
					return GetBusInfo.getInstance().getLineInfo(busno);
					
				case 2:
					return GetBusInfo.getInstance().getGPSInfo(busno,UserInput[1]);

			    //默认返回路线
				default:
					return GetBusInfo.getInstance().getLineInfo(busno);
					
				}
			}
			
			
			
		}
			

		
	}
	
	
	
	
	

}
