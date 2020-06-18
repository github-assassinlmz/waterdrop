package com.sgcc.dlsc.utils;

import java.io.InputStream;
import java.util.HashMap;

import com.sgcc.dlsc.entity.DBEntity;

/**
 * 
 * @title ReadFileUtil
 * @description 配置文件读取工具类
 * @author niemingming
 */
public class ReadFileUtil {
	/**数据库配置对象*/
	private DBEntity dbEntity;
	/**单例实体*/
	private static ReadFileUtil single;
	
	private static String lock = "readFileUtil";
	
	private ReadFileUtil(){
	}
	
	/**
	 * 
	 * @description 获取数据库配置信息
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	public  DBEntity getDBConfigEntity(){
		if(dbEntity == null) {
			HashMap<String, String> prop = loadFile("config.ini");
			dbEntity = new DBEntity(prop.get("driver"), 
					prop.get("url"), prop.get("userid"), prop.get("password"));
		}
		return dbEntity;
	}
	/**
	 * 
	 * @description 加载指定的配置文件
	 * @param string
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	private HashMap<String, String> loadFile(String filePath) {
		HashMap<String, String> prop = new HashMap<String, String>();
		try {
			InputStream in = ReadFileUtil.class.getClassLoader().getResourceAsStream(filePath);
			byte[] buff = new byte[in.available()];
			in.read(buff);
			String configs = new String(buff).replace("\n", "");
			int start = 0;
			int end = 0;
			while((end = configs.indexOf(";", start)) != -1) {
				String sub = configs.substring(start, end);
				String pks[] = sub.split("=");
				int fhindex = sub.indexOf("=") + 1;
				prop.put(pks[0].trim(), sub.substring(fhindex));
				start = end + 1;
			}
			in.close();
		} catch (Exception e) {//加载配置文件失败的异常
//			e.printStackTrace();
			//加载配置文件失败！
			System.exit(46);
		}
		return prop;
	}

	/**
	 * 
	 * @description 获取工具类实例
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	public static ReadFileUtil getInstance(){
		synchronized (lock) {
			if(single == null) {
				single = new ReadFileUtil();
			}
		}
		return single;
	}
	/**
	 * 
	 * @description 读取调用公共参数
	 * @param args
	 * @return
	 * @author niemingming
	 * @date 2015-5-11
	 */
	public static HashMap<String, String> readCommonParams(String args[]){
		HashMap<String, String> map = new HashMap<String, String>();
		String tradeseqId = getArgs(args,0);
		String interval = getArgs(args, 1);
		String timeparts = getArgs(args, 2);
		String marketId = getArgs(args, 3);
		String ruleId = getArgs(args, 4);
		
		map.put("tradeseqId", tradeseqId);
		map.put("interval", interval);
		map.put("periodCfg", timeparts);
		map.put("marketId", marketId);
		map.put("ruleId", ruleId);
		map.put("jsjd", "4");
		return map;
		
	}
	/**
	 * 
	 * @description 获取指定位置的参数
	 * @param args
	 * @param i
	 * @return
	 * @author niemingming
	 * @date 2014-2-12
	 */
	public static String getArgs(String[] args, int index) {
		if(args.length >= index)
			return args[index];
		return "";
	}

}
