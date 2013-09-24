package com.alex.aqua.bundle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class BundleFactory  {
	private static int min = 5;
	private static int max = 50;
	private static HashMap<Class<?>, LinkedBlockingQueue<Bundle>> spriteBundles = new HashMap<Class<?>, LinkedBlockingQueue<Bundle>>();

	public static <T> T extract(Class<T> bundleClass) {
		if(!Bundle.class.isAssignableFrom(bundleClass)) return null;
		T instance = null;
		LinkedBlockingQueue<Bundle> bundleQueue;
		bundleQueue = spriteBundles.get(bundleClass);
		if(bundleQueue == null) {
			bundleQueue = new LinkedBlockingQueue<Bundle>(min);
			spriteBundles.put(bundleClass, bundleQueue);
		}
		instance = bundleClass.cast(bundleQueue.poll());
		if(null == instance)
			try {
				instance = bundleClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return instance;
	}
	public static void store(Bundle bundle) {
		Class<? extends Bundle> bundleClass = bundle.getClass();
		LinkedBlockingQueue<Bundle> bundleQueue = spriteBundles.get(bundle.getClass());
		for(Field field : bundleClass.getFields()) {
			Object fieldObject = null;
			try {
				fieldObject = field.get(bundle);
				if(null != fieldObject) {
					if (fieldObject instanceof Bundle) {
						store(Bundle.class.cast(fieldObject));
					} else if (fieldObject.getClass().isArray() || fieldObject instanceof String) {
					} else continue;
				}
				field.set(bundle, null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if(bundleQueue.size()<max) {
			bundleQueue.offer(bundleClass.cast(bundle));
			System.out.println(bundleQueue.size());
		}
	}
}
