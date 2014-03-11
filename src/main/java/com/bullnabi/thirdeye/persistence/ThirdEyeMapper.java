package com.bullnabi.thirdeye.persistence;

import java.util.List;

import com.bullnabi.thirdeye.domain.ThirdEye;
import com.bullnabi.thirdeye.global.utils.DataMap;

public interface ThirdEyeMapper {
	
	List<DataMap> groupingList(ThirdEye thirdEye);
	
	void genSampleDataSells(DataMap dataMap);
	void genSampleDataReleaseCar(DataMap dataMap);
	
}
