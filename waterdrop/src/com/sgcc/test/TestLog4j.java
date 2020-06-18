package com.sgcc.test;

import org.apache.log4j.Logger;

public class TestLog4j {
	private static Logger logger = Logger.getLogger(TestLog4j.class);	
	public static void main(String[] args){
		logger.debug("debug");
		logger.info("info");
		logger.error("erro");
	}
}
