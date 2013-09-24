package com.alex.aqua.source;

import java.io.File;
import java.util.HashMap;

public abstract class AbstractSourceDao {

	public abstract String getDaoName();
	protected abstract Object loadSource(File file);
	public String getSourceType() {
		return SourceFactory.TYPE_DEFAULT;
	}
	public final HashMap<String, Object> loadSourceMap(File file) {
		if(!file.exists()) return null;
		HashMap<String, Object> sourceMap = new HashMap<String, Object>();
		File[] childList;
		childList = file.listFiles();
		for(File childFile:childList) {
			if(isFileFiltered(childFile)) {
				if(childFile.isDirectory()) {
					Object childMap = loadSourceMap(childFile);
					if(null != sourceMap)
						sourceMap.put(childFile.getName(), childMap);
				} else {
					Object source = loadSource(childFile);
					if(null != source) {
						sourceMap.put(childFile.getName(), source);
					}
				}
			}
		}
		if(sourceMap.size()==0)
			return null;
		else
			return sourceMap;
	}
	protected boolean isFileFiltered(File file) {
		return true;
	}
}
