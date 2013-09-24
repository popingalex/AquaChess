package com.alex.aqua.bundle;

public class EchoBundle extends Bundle{
	public Object bundle;
	public int param;
	public static EchoBundle extract() {
		EchoBundle echoBundle = BundleFactory.extract(EchoBundle.class);
		if(null != echoBundle) {}
		return echoBundle;
	}
	public static EchoBundle extract(int param, Object bundle) {
		EchoBundle echoBundle = BundleFactory.extract(EchoBundle.class);
		if(null != echoBundle) {
			echoBundle.param = param;
			echoBundle.bundle = bundle;
		}
		return echoBundle;
	}
}
