package com.bullnabi.thirdeye.service;

import java.util.List;

import com.bullnabi.thirdeye.global.utils.DataMap;
import com.bullnabi.thirdeye.persistence.TableDetailDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableDetailService {

	@Autowired
	private TableDetailDao tableDetailDao;
	
	public  List<DataMap<String, String>> getTableName(){
		return tableDetailDao.getTableName();
	}
			
	
	public List<DataMap<String, String>> getTableDetails(String tableName){
		return tableDetailDao.getTableDetails(tableName);
	}
	
}
