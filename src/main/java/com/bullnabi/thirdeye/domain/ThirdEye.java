package com.bullnabi.thirdeye.domain;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import com.bullnabi.thirdeye.global.utils.DataMap;
import com.bullnabi.thirdeye.global.utils.XmlUtil;

import org.apache.ibatis.type.Alias;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Value Object 로
 * tire를 옮겨다닐때 마다 사용한다.
 * 이 VO는 확장할 수 없기때문에 모든 가능성을 포함시켜야 한다.
 * 	불나비에서 쓰는 map 외에
 * 	커스터 마이징을 위해 custMap 을 추가함. 
 * @author gstar
 */
@Alias("thirdEye")
public class ThirdEye implements Serializable {

	private static final long serialVersionUID = -3779124151296509269L;

// ▼▼▼▼▼▼▼▼▼▼ 확장성을 위한 동적 클래스 - 메소드도 선언해야 되나?
	
	/**
	 * 실행될 서비스 클래스로 세팅이 안되면 BullnabiService를 사용함.
	 ******** 반드시 BullnabiService를 확장하거나 Interface를 구현해야 하도록 해야겠다.
	 * 파라미터로 강제 세팅된 것 우선
	 */
	private String serviceClass;
	public String getServiceClass() {
		if(this.serviceClass == null){
			if(getDataMaster().hasAttribute("serviceClass")){
				this.serviceClass = getDataMaster().getAttribute("serviceClass");
			}
		}
		return this.serviceClass;
	}
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
	
	/**
	 * 실행될 Mapper 클래스로 세팅이 안되면 BullnabiMapper를 사용함.
	 ******** 반드시 BullnabiMapper를 확장하거나 Interface를 구현해야 하도록 해야겠다.
	 ******** 이게 필요한가. 확장이 필요하면 serviceClass를 새로 만들어 거기서 알아서 하도록 할까?
	 * 파라미터로 강제 세팅된 것 우선
	 */
	private String mapperClass;
	public String getMapperClass() {
		if(this.mapperClass == null){
			if(getDataMaster().hasAttribute("mapperClass")){
				this.mapperClass = getDataMaster().getAttribute("mapperClass");
			}
		}
		return this.mapperClass;
	}
	public void setMapperClass(String mapperClass) {
		this.mapperClass = mapperClass;
	}
	
// ▲▲▲▲▲▲▲▲▲▲ 확장성을 위한 동적 클래스
	

// ▼▼▼▼▼▼▼▼▼▼ 메타데이터 XML 객체 생성 관련
	
	private String company;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	private String language;
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * 메타데이터 XML 객체
	 * 	메타데이터는 아래 3가지로 분류되지만
	 * 		<dataMaster/>
	 * 		<listMaster/>
	 * 		<readMaster/>
	 * 	서버단에서는 쿼리를 만들기 위해 <dataMaster/>만 사용한다.
	 */
	private Document metaXML;
	public Document getMetaXML() {
		return metaXML;
	}
	
	/**
	 * 메타데이터를 XML 객체로 생성하기 위한
	 * 1. context path 의 물리적 주소
	 */
	private String realPath;
	public String getRealPath() {
		return realPath;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
		initMetaXML(this.metaFileName);
	}
	
	/**
	 * 메타데이터를 XML 객체로 생성하기 위한
	 * 2.  순수 파일 명
	 */
	private String metaFileName;
	public String getMetaFileName() {
		return metaFileName;
	}
	public void setMetaFileName(String metaFileName) {
		initMetaXML(metaFileName);
		this.metaFileName = metaFileName;
	}
	
	/**
	 *	메타데이터를 XML 객체로 생성시킨다. 
	 */
	private void initMetaXML(String metaFileName){
		
		if(this.getRealPath() == null || metaFileName == null || company == null || language == null){
			return;
		}
		
		if(		this.metaFileName == null
			||	this.metaXML == null
			||	!this.metaFileName.equals(metaFileName)){
			
			String filePath = this.realPath 
							+ "metadata/"+company+"/"+language+"/business/"	//나중에 동적으로
							+ metaFileName + ".xml";
			
			System.out.println("getMetaXML.filePath : "+filePath);
			
			File metaFile = new File(filePath);
			
			try {
				metaXML = XmlUtil.getDocument(metaFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

// ▲▲▲▲▲▲▲▲▲▲ 메타데이터 XML 객체 생성 관련
	
	
// ▼▼▼▼▼▼▼▼▼▼ runtime data 저장
	
	/**
	 * 불나비 기본 페키지에서 쓰는 파라미터를 담는다.
	 * 	실제 데이터를 저장함.
	 */
	private DataMap map;
	public DataMap getMap() {
		return map;
	}
	public void setMap(DataMap map) {
		this.map = map;
	}
	
	/**
	 * list 호출 시 검색조건문으로
	 * sql 문을 String으로 바로 받는다.
	 * 
	 *******이 부분 좀 바꿔야 될 듯
	 */
	private String whereSearch;
	public String getWhereSearch() {
		if(whereSearch == null){
			whereSearch = "";
		}
		return whereSearch;
	}
	public void setWhereSearch(String whereSearch) {
		this.whereSearch = whereSearch;
	}
	public void setWhereSearch(String[] historyNames, String[] historyValues) {
		String whereSearch = "";
		for(int i=0; i<historyNames.length; i++){
			if( !historyNames[i].equals("") && !historyValues[i].equals("") && !historyNames[i].equals("root_tmp") ){
				String[] historyValue = historyValues[i].split(",");
				whereSearch += " AND " + historyNames[i] + " in (";
				for(int j=0; j<historyValue.length; j++){
					whereSearch += " '"+historyValue[j]+"' " ;
					if( j < historyValue.length-1){
						whereSearch += "," ;
					}
				}
				whereSearch += ") " ;
			}
		}
		this.whereSearch = whereSearch;
	}
	
// ▲▲▲▲▲▲▲▲▲▲ runtime data 저장
	

// ▼▼▼▼▼▼▼▼▼▼ 메타데이터 XML 객체 접근
	
	/**
	 * 	<dataMaster/> 노드를 돌려주는 유틸성 함수
	 */
	public Element getDataMaster(){
		return (Element)(metaXML.getElementsByTagName("dataMaster").item(0));
	}
	
	/**
	 * 	<field/> 노드 리스트를 돌려주는 유틸성 함수
	 */
	public NodeList getFieldList(){
		return metaXML.getElementsByTagName("field");
	}
	
	/**
	 * 	<listMaster/> 노드를 돌려주는 유틸성 함수로 서버단에서 필요하지 않음.
	 */
	public Element getListMaster(){
		return (Element)(metaXML.getElementsByTagName("listMaster").item(0));
	}
	
	/**
	 * 	<readMaster/> 노드를 돌려주는 유틸성 함수로 서버단에서 필요하지 않음.
	 */
	public Element getReadMaster(){
		return (Element)(metaXML.getElementsByTagName("readMaster").item(0));
	}
	
	private String tableName;
	public String getTableName(){
		return getDataMaster().getAttribute("tableName");
	}

// ▲▲▲▲▲▲▲▲▲▲ 메타데이터 XML 객체 접근
	

// ▼▼▼▼▼▼▼▼▼▼ 메타데이터 XML을 근거로 부분 쿼리문을 만듬. -- 이 부분은 확장성을 고려해 Service 단으로 빼야겠다.
	
	// ▼▼▼▼▼▼▼▼▼▼ 그루핑 쿼리 생성에 필요
	
	private String tbName;
	public void setTbName(String tbName){
		this.tbName = tbName;
	}
	public String getTbName(){
		return this.tbName;
	}
	
	private String groupingField;
	public void setGroupingField(String groupingField){
		this.groupingField = groupingField;
	}
	public String getGroupingField(){
		return this.groupingField;
	}
	
	private String value1Field;
	public void setValue1Field(String value1Field){
		this.value1Field = value1Field;
	}
	public String getValue1Field(){
		return this.value1Field;
	}
	
	private String value2Field;
	public void setValue2Field(String value2Field){
		this.value2Field = value2Field;
	}
	public String getValue2Field(){
		return this.value2Field;
	}
	
	// ▲▲▲▲▲▲▲▲▲▲ 그루핑 쿼리 생성에 필요

// ▲▲▲▲▲▲▲▲▲▲ 메타데이터 XML을 근거로 쿼리문을 만듬.
	
}
