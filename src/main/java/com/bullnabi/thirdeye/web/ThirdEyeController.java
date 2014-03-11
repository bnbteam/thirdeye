package com.bullnabi.thirdeye.web;


import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.bullnabi.thirdeye.domain.ThirdEye;
import com.bullnabi.thirdeye.global.utils.DataMap;
import com.bullnabi.thirdeye.global.utils.XmlUtil;
import com.bullnabi.thirdeye.service.ThirdEyeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.qos.logback.core.joran.spi.XMLUtil;

@Controller
public class ThirdEyeController {
	
	private static final Logger logger = LoggerFactory.getLogger(ThirdEyeController.class);
	
	@Autowired
    private ThirdEyeService thirdEyeService;
	
	@RequestMapping(value="/thirdEye/groupingList.do", method=RequestMethod.POST)
	@ResponseBody
	public List<DataMap> groupingList( 
			@RequestParam String businessCode,  
			@RequestParam String groupingField,  
			@RequestParam String value1Field,  
			@RequestParam String value2Field,
			@RequestParam String historyNames,
			@RequestParam String historyValues) throws Exception {
		
		logger.debug("groupingList start");
		
		System.out.println( "historyNames:"+historyNames.toString() );
		System.out.println( "historyValues:"+historyValues.toString() );
		
		ThirdEye thirdEye = new ThirdEye();
		
		String tbName = ((Element)(getMetaData(businessCode).getElementsByTagName("dataMaster").item(0))).getAttribute("tableViewName");
		thirdEye.setTbName(tbName);
		thirdEye.setGroupingField( groupingField );
		thirdEye.setValue1Field(value1Field);
		thirdEye.setValue2Field(value2Field);
		thirdEye.setWhereSearch(historyNames.split("\\^"), historyValues.split("\\^"));
		
		List<DataMap> list = thirdEyeService.groupingList(thirdEye);

		logger.debug("groupingList result");
		logger.debug(list.toString());
		
		return list;
	}
	
	@RequestMapping(value="/thirdEye/groupingListByXML.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity groupingListByXML( 
			@RequestParam String businessCode,  
			@RequestParam String groupingField,  
			@RequestParam String value1Field,  
			@RequestParam String value2Field,
			@RequestParam String historyNames,
			@RequestParam String historyValues) throws Exception {
		
		logger.debug("groupingList start");
		
		ThirdEye thirdEye = new ThirdEye();
		String tbName = ((Element)(getMetaData(businessCode).getElementsByTagName("dataMaster").item(0))).getAttribute("tableViewName");
		thirdEye.setTbName(tbName);
		thirdEye.setGroupingField( groupingField );
		thirdEye.setValue1Field(value1Field);
		thirdEye.setValue2Field(value2Field);
		thirdEye.setWhereSearch(historyNames.split("\\^"), historyValues.split("\\^"));
		
		List<DataMap> list = thirdEyeService.groupingList(thirdEye);
		Document document = XmlUtil.toXMLDocumentByDataMap(list);
		
		logger.debug("groupingListByXML result");
		//logger.debug(document.toString());
		logger.debug(XmlUtil.getDocumentAsXml(document));
		
		//return XmlUtil.getDocumentAsXml(document);
		
		HttpHeaders responseHeaders = new HttpHeaders();   
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity(XmlUtil.getDocumentAsXml(document), responseHeaders, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value="/thirdEye/groupingListByXMLFromIE.do", method=RequestMethod.POST)
	@ResponseBody
	public String groupingListByXMLFromIE( 
			@RequestParam String businessCode,  
			@RequestParam String groupingField,  
			@RequestParam String value1Field,  
			@RequestParam String value2Field,
			@RequestParam String historyNames,
			@RequestParam String historyValues) throws Exception {
		
		//도저히 못찾고.. IE용으로 별도 만듬
		
		logger.debug("groupingList start");
		
		ThirdEye thirdEye = new ThirdEye();
		String tbName = ((Element)(getMetaData(businessCode).getElementsByTagName("dataMaster").item(0))).getAttribute("tableViewName");
		thirdEye.setTbName(tbName);
		thirdEye.setGroupingField( groupingField );
		thirdEye.setValue1Field(value1Field);
		thirdEye.setValue2Field(value2Field);
		thirdEye.setWhereSearch(historyNames.split("\\^"), historyValues.split("\\^"));
		
		List<DataMap> list = thirdEyeService.groupingList(thirdEye);
		Document document = XmlUtil.toXMLDocumentByDataMap(list);
		
		logger.debug("groupingListByXML result");
		//logger.debug(document.toString());
		logger.debug(XmlUtil.getDocumentAsXml(document));
		
		//return XmlUtil.getDocumentAsXml(document);
		
		return XmlUtil.getDocumentAsXml(document);
		
	}
	
	@RequestMapping(value="/thirdEye/getMetadata.do")
	@ResponseBody
	public Document getMetaData( @RequestParam String businessCode ) throws Exception {
		
		logger.debug("getMetaData start");
		
		
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = servletRequestAttribute.getRequest().getSession(true);
        ServletContext servletContext = httpSession.getServletContext();
        String realPath = servletContext.getRealPath("/");
        
        String filePath = realPath + "WEB-INF/"
				+ "thirdeye/" + businessCode + ".xml";

		System.out.println("getMetaXML.filePath : "+filePath);
		
		File metaFile = new File(filePath);
		
		Document metaXML = XmlUtil.getDocument(metaFile);
		
		// Server 단에서 xml override
		XmlUtil.overrideXmlList(
				metaXML.getElementsByTagName("group"), "fieldId", 
				metaXML.getElementsByTagName("field"), "id");
		XmlUtil.overrideXmlList(
				metaXML.getElementsByTagName("item"), "fieldId", 
				metaXML.getElementsByTagName("field"), "id");
		
		String result = XmlUtil.getDocumentAsXml(metaXML);
		
		logger.debug("getMetaData result");
		logger.debug(result);
		
		return metaXML;
	}
	
	@RequestMapping(value="/thirdEye/getMetaDataByXML.do")
	@ResponseBody
	public ResponseEntity getMetaDataByXML( @RequestParam String businessCode ) throws Exception {
		
		logger.debug("getMetaData start");
		
		
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession httpSession = servletRequestAttribute.getRequest().getSession(true);
		ServletContext servletContext = httpSession.getServletContext();
		String realPath = servletContext.getRealPath("/");
		
		String filePath = realPath + "WEB-INF/"
				+ "thirdeye/" + businessCode + ".xml";
		
		System.out.println("getMetaXML.filePath : "+filePath);
		
		File metaFile = new File(filePath);
		
		Document metaXML = XmlUtil.getDocument(metaFile);
		
		// Server 단에서 xml override
		XmlUtil.overrideXmlList(
				metaXML.getElementsByTagName("group"), "fieldId", 
				metaXML.getElementsByTagName("field"), "id");
		XmlUtil.overrideXmlList(
				metaXML.getElementsByTagName("item"), "fieldId", 
				metaXML.getElementsByTagName("field"), "id");
		
		String result = XmlUtil.getDocumentAsXml(metaXML);
		
		logger.debug("getMetaData result");
		logger.debug(result);
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity(XmlUtil.getDocumentAsXml(metaXML), responseHeaders, HttpStatus.CREATED);
		//return "하하하";
		//return XmlUtil.getDocumentAsXml(metaXML);
		/*
		  	public Document ... 
		  		return metaXML
		 	Flex에서 돌지 않네...? 
		 	(<?xml version="1.0" encoding="utf-8"?>) 이부분이 빠져서 날라가는듯
		 */
	}
	
	@RequestMapping(value="/thirdEye/getMetaDataByXMLFromIE.do")
	@ResponseBody
	public String getMetaDataByXMLFromIE( @RequestParam String businessCode ) throws Exception {
		
		logger.debug("getMetaData start");
		
		
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession httpSession = servletRequestAttribute.getRequest().getSession(true);
		ServletContext servletContext = httpSession.getServletContext();
		String realPath = servletContext.getRealPath("/");
		
		String filePath = realPath + "WEB-INF/"
				+ "thirdeye/" + businessCode + ".xml";
		
		System.out.println("getMetaXML.filePath : "+filePath);
		
		File metaFile = new File(filePath);
		
		Document metaXML = XmlUtil.getDocument(metaFile);
		
		// Server 단에서 xml override
		XmlUtil.overrideXmlList(
				metaXML.getElementsByTagName("group"), "fieldId", 
				metaXML.getElementsByTagName("field"), "id");
		XmlUtil.overrideXmlList(
				metaXML.getElementsByTagName("item"), "fieldId", 
				metaXML.getElementsByTagName("field"), "id");
		
		String result = XmlUtil.getDocumentAsXml(metaXML);
		
		logger.debug("getMetaData result");
		logger.debug(result);
		
		return XmlUtil.getDocumentAsXml(metaXML);
		/*
		  	public Document ... 
		  		return metaXML
		 	Flex에서 돌지 않네...? 
		 	(<?xml version="1.0" encoding="utf-8"?>) 이부분이 빠져서 날라가는듯
		 */
	}
	
	@RequestMapping(value="/thirdEye/genSampleDataSells.do", method=RequestMethod.GET)
	@ResponseBody
	public int genSampleDataSells( @RequestParam int dataCount ) throws Exception {
		
		logger.debug("genSampleDataSells start");
		
		int result = thirdEyeService.genSampleDataSells(dataCount);
		
		logger.debug("genSampleDataSells result");
		
		return result;
	}
	
	@RequestMapping(value="/thirdEye/genSampleDataReleaseCar.do", method=RequestMethod.GET)
	@ResponseBody
	public int genSampleDataReleaseCar( @RequestParam int dataCount ) throws Exception {
		
		logger.debug("genSampleDataReleaseCar start");
		
		int result = thirdEyeService.genSampleDataReleaseCar(dataCount);
		
		logger.debug("genSampleDataReleaseCar result");
		
		return result;
	}
	
}
