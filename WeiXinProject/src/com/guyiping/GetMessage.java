package com.guyiping;

public class GetMessage {
	
	public static String HelpMessage()
	{
		return "��ӭʹ�ú��ݹ���С���֣�Ŀǰ�ṩ�鹫����·�ͳ�����վ��Ϣ��ѯ����ҿ���ֱ��������·���Ƽ���ʵʱ��ѯ����·�߻���������·<�ո�>վ����ѯ������վ��Ϣ��\n ���� ���� ��8������ȡ8·������·��Ϣ�����롰8 ��һ�����ɻ��8·����һҽԺվ�ĵ�վ��Ϣ��վ��֧�����룬����һ�����ؼ��־Ϳ���Ŷ���������޷��ص������������ʹ�������h������ð�����"
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
				return " �����أ�������˾��վ����С���֣�������~�����Ժ�����";
			}
			
			String busno = GetBusInfo.getInstance().autoConfirmBusNo(UserInput[0]);
			
			if (busno == null) {
				return "δ�ҵ��ó��Σ�����������";
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

			    //Ĭ�Ϸ���·��
				default:
					return GetBusInfo.getInstance().getLineInfo(busno);
					
				}
			}
			
			
			
		}
			

		
	}
	
	
	
	
	

}
