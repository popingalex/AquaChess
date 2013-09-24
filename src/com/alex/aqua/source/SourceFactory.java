package com.alex.aqua.source;

import java.awt.Image;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;


public class SourceFactory {
	private static HashMap<String, HashMap<String, Object>> sourceTypeMap = new HashMap<String, HashMap<String, Object>>();
	private static HashMap<String, AbstractSourceDao> daoMap = new HashMap<String, AbstractSourceDao>();
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_DEFAULT = "default";
	public static void registerDao(String sourceDaoClassName) {
		try {
			Class<?> sourceDaoClass = Class.forName(sourceDaoClassName);
			AbstractSourceDao sourceDao = sourceDaoClass.asSubclass(AbstractSourceDao.class).newInstance();
			if(null != sourceDao.getDaoName());
			daoMap.put(sourceDao.getDaoName(), sourceDao);
		} catch (ClassNotFoundException e) {//forName fail
			e.printStackTrace();
		} catch (InstantiationException e) {//asSubclass fail
			e.printStackTrace();
		} catch (IllegalAccessException e) {//newInstance fail
			e.printStackTrace();
		}
	}
	public static void loadSource() {
		AbstractSourceDao[] daoArray = new AbstractSourceDao[daoMap.size()];
		daoMap.values().toArray(daoArray);
		for(AbstractSourceDao dao:daoArray) {
			HashMap<String, Object> sourceMap;
			sourceMap = sourceTypeMap.get(dao.getSourceType());
			if(null == sourceMap) {
				sourceMap = new HashMap<String, Object>();
				sourceTypeMap.put(dao.getSourceType(), sourceMap);
			}
			HashMap<String, Object> sourceBrunch = dao.loadSourceMap(new File("src/source/"+dao.getSourceType()));
			if(null != sourceBrunch)
				sourceMap.putAll(sourceBrunch);
		}
	}
	public static Image[] getImageArray(String[] path) {
		Object sourceArray = getSourceObject(TYPE_IMAGE, path);
		if(sourceArray instanceof HashMap<?, ?>) {
			HashMap<?, ?> sourceMap = HashMap.class.cast(sourceArray);
			Object[] keys = sourceMap.keySet().toArray();
			Arrays.sort(keys);
			int i = 0;
			for(Object key:keys) {
				if(sourceMap.get(key) instanceof Image)
					i++;
			}
			Image[] imageArray = new Image[i];
			i = 0;
			for(Object key:keys) {
				if(sourceMap.get(key) instanceof Image)
					imageArray[i++] = Image.class.cast(sourceMap.get(key));
			}
			return imageArray;
		}
		return null;
	}
	public static Image getImage(String[] path) {
		Object source = getSourceObject(TYPE_IMAGE, path);
		if(source instanceof Image)
			return Image.class.cast(source);
		return null;
	}
	public static Object getSourceObject(String sourceType, String[] path) {
		HashMap<?, ?> currentMap = sourceTypeMap.get(sourceType);
		if(null==currentMap) return null;
		Object obj = null;
		for(int i=0; i<path.length;i++) {
			obj = currentMap.get(path[i]);
			if(obj instanceof HashMap)
				currentMap = HashMap.class.cast(obj);
		}
		return obj;
	}
}
