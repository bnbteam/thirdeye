package com.bullnabi.thirdeye.service;

import java.util.List;

import com.bullnabi.thirdeye.domain.ThirdEye;
import com.bullnabi.thirdeye.global.utils.DataMap;
import com.bullnabi.thirdeye.persistence.ThirdEyeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdEyeService {

	@Autowired
	public ThirdEyeMapper thirdEyeMapper;

	public List<DataMap> groupingList(ThirdEye thirdEye) {
		
		System.out.println(">>"+thirdEye.getTbName());
		return thirdEyeMapper.groupingList(thirdEye);
	}
	
	public int genSampleDataSells(int dataCount) {
		
		/* 한글 왜 안되노 */
		
		String[] ca_type_gbs = new String[] {"교육상권","시장상권","교통상권","주택상권"};
		String[] loc_type_gbs = new String[] {"대도시","중소도시","농촌","어촌","산촌"};
		String[] ca_inx_lgbs = new String[] {"90이상","80이상","70이상","60이상","50이상","50미만"};
		String[] ca_nms = new String[] {"강서구청상권","명동상권","부평역상권","서현역상권","신촌(이대)상권","화정역상권"};
		String[] bas_years = new String[] {"2013","2012","2011","2010","2009","2008","2007","2006","2005","2004"};
		String[] bas_months = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"};
		String[] gov_bz_lcd_nms = new String[] {"관광/여가/오락","기술서비스","도매/유통/무역","부동산","생활서비스","소매","숙박","스포츠","음식","의료","전자/정보통신","제조","학문/교육"};
		String[] gov_bz_mcd_nms = new String[] {"중분류01","중분류02","중분류03","중분류04","중분류05","중분류06","중분류07","중분류08","중분류09","중분류10"};
		
		/*
		String[] ca_type_gbs = new String[] {"EDU","MARKET","TRAFF","HOUSE"};
		String[] loc_type_gbs = new String[] {"Tera","Mega","Kb","Byte","Bit"};
		String[] ca_inx_lgbs = new String[] {"over90","over80","over70","over60","over50","under50"};
		String[] ca_nms = new String[] {"gangseu-gu","myung-dong","bupyung-station","seuhyun-station","sinchon(ewha)-station","whajung-station"};
		String[] bas_years = new String[] {"2013","2012","2011","2010","2009","2008","2007","2006","2005","2004"};
		String[] bas_months = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"};
		String[] gov_bz_lcd_nms = new String[] {"BZ_LCD_001","BZ_LCD_002","BZ_LCD_003","BZ_LCD_004","BZ_LCD_005","BZ_LCD_006","BZ_LCD_007","BZ_LCD_008","BZ_LCD_009","BZ_LCD_010","BZ_LCD_011","BZ_LCD_012","BZ_LCD_013"};
		String[] gov_bz_mcd_nms = new String[] {"BZ_MCD_0001","BZ_MCD_0002","BZ_MCD_0003","BZ_MCD_0004","BZ_MCD_0005","BZ_MCD_0006","BZ_MCD_0007","BZ_MCD_0008","BZ_MCD_0009","BZ_MCD_0010"};
		*/
		DataMap map = null;
		for(int i=0; i<dataCount; i++){
			
			map = new DataMap();
			
			map.put("ca_type_gb", getRandomData(ca_type_gbs));          	//"상권유형" 			dataType="string"                 
			map.put("loc_type_gb", getRandomData(loc_type_gbs));         	//"입지유형" 			dataType="string"                 
			map.put("ca_inx_lgb", getRandomData(ca_inx_lgbs));          	//"상권지수지역구분" 		dataType="string"                     
			map.put("ca_nm", getRandomData(ca_nms));               			//"상권"	 				dataType="string"                 
			map.put("bas_year", getRandomData(bas_years));            		//"년도" 				dataType="string"             
			map.put("bas_month", getRandomData(bas_months));           		//"월" 					dataType="string"             
			map.put("gov_bz_lcd_nm", getRandomData(gov_bz_lcd_nms));       	//"업종대분류" 			dataType="string"                 
			map.put("gov_bz_mcd_nm", getRandomData(gov_bz_mcd_nms));       	//"업종중분류" 			dataType="string"                 
			map.put("sell_amt", getRandomData(10000));            			//"매출금액" 			dataType="number"	unitDisp="백만" 
			map.put("aprv_cnt", getRandomData(10000));            			//"구매건수" 			dataType="number"	unitDisp="천건" 
			map.put("sell_aprv", getRandomData(10000));           			//"객단가" 				dataType="number"	unitDisp="천원"	
			map.put("sell_mcht", getRandomData(1000));           			//"점포당 매출금액" 		dataType="number"	unitDisp="천원" 
			map.put("open_cnt", getRandomData(100));            			//"창업점포수" 			dataType="number"	unitDisp="개" 
			map.put("close_cnt", getRandomData(100));           			//"폐업점포수" 			dataType="number"	unitDisp="개" 
			
			thirdEyeMapper.genSampleDataSells(map);
		}
		
		return 1;
	}
	
	public int genSampleDataReleaseCar(int dataCount) {
		
		String[] sells_channel = new String[] {"HMC","KMC"};
		String[] brand = new String[] {"Bluelink","Uvo","Mozen"};
		String[] terminal = new String[] {"N400k","N500k","N600k","N700s","N800s","N900s"};
		String[] area1 = new String[] {"서울시","인천시","경기도","부산시","경상남도","전라북도","대전시","충청남도"};
		String[] area2 = new String[] {"지점1","지점2","지점3","지점4","지점5","지점6","지점7","지점8"};
		String[] model = new String[] {"아반떼","쏘나타","i40","그랜져HG","제네시스","에쿠스","K3","K5","K7","K9","스포티지R","쏘울"};
		String[] year = new String[] {"2013","2012","2011","2010","2009","2008","2007","2006","2005","2004"};
		String[] quarter = new String[] {"1분기","2분기","3분기","4분기"};
		String[] month = new String[] {"01월","02월","03월","04월","05월","06월","07월","08월","09월","10월","11월","12월"};
		String[] day = new String[] {"MON","TUS","WED","THU","FRI","SAT","SUN"};
		DataMap map = null;
		for(int i=0; i<dataCount; i++){
			
			map = new DataMap();
			
			map.put("sells_channel", getRandomData(sells_channel));         //"판매채널"               
			map.put("brand", getRandomData(brand));         				//"브랜드"
			map.put("terminal", getRandomData(terminal));          			//"단말기"
			map.put("area1", getRandomData(area1));          				//"사업실"
			map.put("area2", getRandomData(area2));          				//"지점"
			map.put("model", getRandomData(model));               			//"차종"
			map.put("year", getRandomData(year));            				//"년도"
			map.put("quarter", getRandomData(quarter));            			//"분기"
			map.put("month", getRandomData(month));           				//"월"
			map.put("day", getRandomData(day));       						//"요일"
			map.put("sells_cnt", getRandomData(10));       				//"판매대수"
			map.put("release_date", getRandomData(5));            		//"출고소요일"
			map.put("phone_used", getRandomData(2));            		//"스마트폰사용"
			
			thirdEyeMapper.genSampleDataReleaseCar(map);
		}
		
		return 1;
	}
	
	public String getRandomData( String[] anyArray ){
		
		int randomIndex = (int) (Math.random() * anyArray.length);
		return anyArray[randomIndex];
	}
	
	public String getRandomData( int unit ){
		
		int randomValue = (int) (Math.random() * unit);
		return randomValue+"";
	}
	
// ▼▼▼▼▼▼▼▼▼▼ 메타데이터 XML을 근거로 부분 쿼리문을 만듬.
	
// ▲▲▲▲▲▲▲▲▲▲ 메타데이터 XML을 근거로 쿼리문을 만듬.
	
}
