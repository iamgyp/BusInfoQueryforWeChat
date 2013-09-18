package com.guyiping;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GetBusInfoListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.err.println("DBConnListener Startup!");
		BackgroundThread bt = new BackgroundThread();
		Thread th1 = new Thread(bt);
		th1.start();
		System.err.println("DBConnListener Startup!");
	}

}
