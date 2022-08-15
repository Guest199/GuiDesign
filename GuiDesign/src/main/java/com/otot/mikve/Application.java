package com.otot.mikve;

public class Application extends android.app.Application  {

	@Override
	public void onCreate() {

//		Security.insertProviderAt(Conscrypt.newProvider(), 1); // needed for direct credit charges

		super.onCreate();

//		handleUSBDevices(); // check if needed
	}

	//
	public static final String APP_NAME = "Add Value Station", APP_VER = "1.0";



}
