package com.bullnabi.thirdeye.service;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TestDao {

	int insert(@Param("str") String str) throws Exception;
	
	void deleteAllData() throws Exception;
	
	Collection<Map<String, String>> getList()throws Exception;
	
}
