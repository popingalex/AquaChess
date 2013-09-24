package com.alex.aqua.bundle;

public class InputBundle extends Bundle {
	public long timestamp;
	public int type;
	public int key;
	public int param1;
	public int param2;
	public static InputBundle extract(long timestamp, int type, int key, int param1, int param2) {
		InputBundle inputBundle = BundleFactory.extract(InputBundle.class);
		if(null != inputBundle) {
			inputBundle.timestamp = timestamp;
			inputBundle.type = type;
			inputBundle.key = key;
			inputBundle.param1 = param1;
			inputBundle.param2 = param2;
		}
		return inputBundle;
	}
}
