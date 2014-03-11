package com.bullnabi.thirdeye.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bullnabi.thirdeye.global.utils.DataMap;

public interface TableDetailDao {

	List<DataMap<String, String>> getTableName();
	
	List<DataMap<String, String>> getTableDetails(@Param("tableName") String tableName);
	
}
