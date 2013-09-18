package com.guyiping;

public class BackgroundThread implements Runnable {

	public BackgroundThread() {
		// TODO Auto-generated constructor stub
		System.out.println("BackgroundThread default constructor");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			GetBusInfo.getInstance().autoGetBusInfo("B");
			GetBusInfo.getInstance().autoGetBusInfo("Y");
			//GetBusInfo.getInstance().getGPSInfo("B3","ͬЭ·");
		} catch (Exception e) {
			// TODO: handle exception
			GetBusInfo.getInstance().autoGetBusInfo("B");
			GetBusInfo.getInstance().autoGetBusInfo("Y");
		}

		for (int i = 1; i < 999; i++) {
			try {
				GetBusInfo.getInstance().autoGetBusInfo(String.valueOf(i));
				if (i % 10 == 0) {
					Thread.sleep(5000);
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

}
