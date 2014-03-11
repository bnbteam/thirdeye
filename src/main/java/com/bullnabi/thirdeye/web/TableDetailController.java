package com.bullnabi.thirdeye.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bullnabi.thirdeye.global.utils.DataMap;
import com.bullnabi.thirdeye.service.TableDetailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tableInfo")
public class TableDetailController {

	final static Logger logger = LoggerFactory
			.getLogger(TableDetailController.class);

	@Autowired
	private TableDetailService tableDetailService;

	@RequestMapping(value = "/{tableName}", method = RequestMethod.GET)
	@ResponseBody
	public List<DataMap<String, String>> getTableDetails(
			@PathVariable("tableName") String tableName) {
		return tableDetailService.getTableDetails(tableName);
	} 

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<DataMap<String, String>> getTableName() {
		return tableDetailService.getTableName();
	}

	@RequestMapping(value = "/makeXML.do", method = RequestMethod.POST)
	@ResponseBody
	public DataMap<String, String> makeXML(HttpServletRequest request)
			throws IOException {
		DataMap<String, String> result = new DataMap<String, String>();
		result.put("MESSAGE", "OK");

		// basic value
		String tableName = request.getParameter("tableName");
		String title = request.getParameter("title");
		String fileName = (request.getParameter("fileName").toLowerCase().indexOf(".xml") != -1)?request.getParameter("fileName").toLowerCase():(request.getParameter("fileName").toLowerCase()+".xml");
		List<DataMap<String, String>> fields = new ArrayList<DataMap<String, String>>();
		List<DataMap<String, String>> groups = new ArrayList<DataMap<String, String>>();
		List<DataMap<String, String>> items = new ArrayList<DataMap<String, String>>();

		String[] useRows = request.getParameterValues("useYn");

		for (String useRow : useRows) {
			int rowNum = Integer.parseInt(useRow);
			DataMap<String, String> rowData = new DataMap<String, String>();
			rowData.put("fieldName",
					request.getParameterValues("fieldName")[rowNum]);
			rowData.put("fieldTitle",
					request.getParameterValues("fieldTitle")[rowNum]);
			rowData.put("dataType",
					request.getParameterValues("dataType")[rowNum]);

			rowData.put("unit", request.getParameterValues("unit")[rowNum]);

			rowData.put("yAxis", request.getParameterValues("yAxis")[rowNum]);

			rowData.put("xAxis", request.getParameter("xAxis" + rowNum));
			rowData.put("necessaryField",
					request.getParameterValues("necessaryField")[rowNum]);

			fields.add(rowData);

			if (!"".equals(rowData.getString("xAxis"))) {
				groups.add(rowData);
			} else if (!"".equals(rowData.getString("yAxis"))) {
				items.add(rowData);
			}
		}

		// xml 일단 문자열로 하자
		StringBuilder xmlStr = new StringBuilder();
		xmlStr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
		xmlStr.append("<root>\r\n");
		xmlStr.append("<dataMaster tableViewName=\"");
		xmlStr.append(tableName);
		xmlStr.append("\" mainTitle=\"");
		xmlStr.append(title);
		xmlStr.append("\">\r\n");
		xmlStr.append("<FIELDS>\r\n");
		for (DataMap<String, String> field : fields) {
			// <field id="root_tmp" tagName="root_tmp" headerText="전체"
			// dataType="string" />
			xmlStr.append("<field id=\"");
			xmlStr.append(field.getString("fieldName"));
			xmlStr.append("\" tagName=\"");
			xmlStr.append(field.getString("fieldName"));
			xmlStr.append("\" headerText=\"");
			xmlStr.append(field.getString("fieldTitle"));
			xmlStr.append("\" dataType=\"");
			xmlStr.append(field.getString("dataType"));
			if(!"".equals(field.getString("unit"))){
				xmlStr.append("\" unitDisp=\"");
				xmlStr.append(field.getString("unit"));
			}
			xmlStr.append("\" />\r\n");
		}
		xmlStr.append("</FIELDS>\r\n");
		xmlStr.append("</dataMaster>\r\n");
		xmlStr.append("<listMaster>\r\n");
		xmlStr.append("<GROUPPINGS>\r\n");
		for (DataMap<String, String> group : groups) {
			// <group fieldId="gov_bz_mcd_nm" parentFieldId="gov_bz_lcd_nm"/>
			xmlStr.append("<group fieldId=\"");
			xmlStr.append(group.getString("fieldName"));
			if (!"".equals(group.getString("necessaryField"))) {
				xmlStr.append("\" parentFieldId=\"");
				xmlStr.append(group.getString("necessaryField"));
			}
			xmlStr.append("\" />\r\n");
		}
		xmlStr.append("</GROUPPINGS>\r\n");
		xmlStr.append("<ITEMS>\r\n");
		for (DataMap<String, String> item : items) {
			// <item fieldId="open_cnt" summaryFunc="SUM"/>
			xmlStr.append("<item fieldId=\"");
			xmlStr.append(item.getString("fieldName"));
			xmlStr.append("\" summaryFunc=\"");
			xmlStr.append(item.getString("yAxis"));
			xmlStr.append("\" />\r\n");
		}
		xmlStr.append("</ITEMS>\r\n");

		xmlStr.append("</listMaster>\r\n");
		xmlStr.append("</root>");

		// file SAVE

		;
//		File file = new File(url);
		//"/WEB-INF/thirdeye/"
		String path = request.getSession().getServletContext().getRealPath("WEB-INF/thirdeye/"+ fileName);
		File file = new File(path);
		String fullPathToYourWebappRoot = file.getCanonicalPath();
		

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF-8"));
        
		bw.write(xmlStr.toString());
                                        
		bw.close();
		logger.debug(fullPathToYourWebappRoot);
		logger.debug(xmlStr.toString());

		return result;
	}
}
