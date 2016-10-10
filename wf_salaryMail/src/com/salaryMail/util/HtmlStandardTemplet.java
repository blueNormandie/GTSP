package com.salaryMail.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HtmlStandardTemplet {
	/*
	 * 生成html字符串
	 */
	public static String createHtmlContent(List<Map<String,String>> list){
		String content=null;
		
		content=""+
		"<html>"+
		"<head>"+
		""+
		""+
		"<style type='text/css'>"+
		"<!-- CSS-->"+
		".BillDate{"+
		"border-collapse:collapse;"+
		"border:none;"+
		"border: 0px solid #000;"+ 
		"}"+
		".BillDate td{"+
		"border:solid #000 0px;}"+
		".BillInfo{"+
		"border-collapse:collapse;"+
		"}"+
		".BillMark{"+
			"border-top-width: 1px solid black;"+
			"border-right-width: 1px solid black;"+
			"border-bottom-width: 1px solid black;"+
			"border-left-width: 3px solid black;"+
			"font-family: serif;"+
		    "border:2px solid black;"+
		    
		"}"+
		".STYLE8 {font-family: '新宋体'; color: #FFFFFF; font-weight: bold; }"+
		".STYLE10 {color: #FFFFFF; font-weight: bold; }"+
		".STYLE17 {font-family: '宋体'; font-size: 13px; }"+
		".STYLE20 {font-family: 'Times New Roman', Times, serif; font-size: 13px; }"+
		".STYLE21 {font-size: 14px; font-family: '宋体'; }"+
		".STYLE25 {font-family: '宋体'}"+
		"</style>"+
		"</head>"+
		"<body>"+
		    "<div id='1' style='position: absolute;width: 700px;height: 57px;top: 27px;left: 45px;'>"+
		    "<table class='BillDate' width='700' style='table-layout: fixed;'>"+
		       " <tr>"+
		            "<td rowspan='2' width='200'><img src='图标.PNG' alt='' width='179' height='59' align='absmiddle' /></td>"+
		            "<td width='300' height='28' style='text-align: center; font-size: 15px;' ><div align='center' class='STYLE17'>"+
		              "<div align='right'><span class='BillDate'>发放日期</span>：</div>"+
		         "</div></td>"+
		            "<td width='200' height='28' style='text-align: right; font-size: 15px;'><span class='STYLE21'>2015-01-25</span></td>"+
		        "</tr>"+
		        "<tr style='font-size: 15px;'>"+
		            "<td width='300' height='28' style='text-align: center; font-size: 15px;'><div align='center' class='STYLE17'>"+ 
		              "<div align='right'>薪资期间：</div>"+
		         "</div></td>"+
		            "<td width='200' height='28' style='text-align: right; font-size: 15px;'><span class='STYLE21'>2015-01</span></td>"+
		        "</tr>"+
		    "</table>"+
		"</div>"+
		    "<div id='2' style='position: absolute;width: 700px;top: 101px;left: 45px;'>"+
		    "<table class='BillInfo' width='700' >"+
		        "<tr>"+
		            "<td width='25%'><span class='STYLE17'>姓名：</span></td>"+
		            "<td width='25%'><div align='justify' class='STYLE20'>James Bond</div></td>"+
		            "<td width='25%'><span class='STYLE17'>部门：</span></td>"+
		            "<td width='25%'><span class='STYLE20'>Operations</span></td>"+
		        "</tr>"+
		        "<tr>"+
		            "<td height='26'><span class='STYLE17'>公司工号：</span></td>"+
		            "<td><span class='STYLE20'>A007</span></td>"+
		            "<td><span class='STYLE17'>成本中心：</span></td>"+  
		            "<td><span class='STYLE20'>962002</span></td>"+
		       "</tr>"+
		        "<tr>"+
		            "<td><span class='STYLE17'>外服工号：</span></td>"+
		            "<td><span class='STYLE20'>1000007</span></td>"+
		            "<td><span class='STYLE17'>职位：</span></td>"+
		            "<td><span class='STYLE20'>Special Agent</span></td>"+
		      "</tr>"+
		        "<tr>"+
		            "<td><span class='STYLE17'>银行卡号：</span></td>"+
		            "<td><span class='STYLE20'>456351080003406****</span></td>"+
		            "<td><span class='STYLE17'>工作地：</span></td>"+
		            "<td><span class='STYLE20'>Shanghai</span></td>"+
		        "</tr>"+
		    "</table>"+
		"</div>"+
		    "<div id='3' style='position: absolute;width: 700px;top: 205px;left: 45px;'>"+
		    "<table width='700' border='0' rules=none class='BillHead'>"+
		        "<tr bgcolor='#63CFA3'>"+
		            "<td colspan='3'><span class='STYLE8'>A.税前应发</span></td>"+
		            "<td width='170' dir='rtl'><div align='right' class='STYLE8'>合计:13550.95</div></td>"+
		        "</tr>"+
		            openList(list)+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3'><span class='STYLE8'>B.税前应扣</span></td>"+
		                "<td dir='rtl'><div align='right' class='STYLE8'>合计:441.30</div></td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>病假扣款</td>"+
		                "<td>个人养老</td>"+
		                "<td>个人医疗</td>"+
		                "<td>个人失业</td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>100.15</td>"+
		                "<td>200.00</td>"+
		                "<td>150.00</td>"+
		                "<td>50.00</td>"+
		            "</tr>"+
		            "<tr>"+
		                "<td colspan='4' dir='rtl'><hr size='1' width='100%' color='#95DD9E' noshade='noshade' /></td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>个人公积金</td>"+
		                "<td>个人年金</td>"+
		                "<td>调整</td>"+
		                "<td>&nbsp;</td>"+
		           "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>100.15</td>"+
		                "<td>1.00</td>"+
		                "<td>-160.00</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3'><span class='STYLE8'>C.仅计税不发工资</span></td>"+
		                "<td dir='rtl'><div align='right' class='STYLE8'>合计:995.56</div></td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>商业保险</td>"+
		                "<td>已发工资合并计税</td>"+
		                "<td>计税调整项</td>"+
		                "<td>年金超标部分</td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>500.00</td>"+
		                "<td>300.00</td>"+
		                "<td>75.56</td>"+
		                "<td>120.00</td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>D.仅抵扣计税项</span></td>"+
		                 "<td  dir='rtl'><div align='right' class='STYLE8'>合计:5500.00</div></td>"+
		            "</tr>"+ 
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>捐款抵扣</td>"+
		                "<td>外籍人租房抵</td>"+
		                "<td>&nbsp;</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>500.00</td>"+
		                "<td>5000.00</td>"+
		                "<td>&nbsp;</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>E.计税总额(A-B+C-D)</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:8605.21</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3'><span class='STYLE8'>F.个人所得税</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:275.52</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3'><span class='STYLE8'>G.税后应发</span></td>"+
		                "<td   dir='rtl'><div align='right' class='STYLE8'>合计:4830.00</div></td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>独生子女费</td>"+
		                "<td>外籍人房租补贴</td>"+
		                "<td>税后津贴</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>30.00</td>"+
		                "<td>5000.00</td>"+
		                "<td>-200.00</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td  colspan='3' ><span class='STYLE8'>H.税后应扣</span></td>"+
		                "<td dir='rtl'><div align='right' class='STYLE8'>合计:1080.00</div></td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>已发现金工资</td>"+
		                "<td>税后扣款</td>"+
		                "<td>&nbsp;</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		               "<td>1000.00</td>"+
		                "<td>80.00</td>"+
		                "<td>&nbsp;</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		      "</table >"+
		"</div>"+
		    "<div id='4' style='position: absolute;width: 700px;top: 694px;left: 45px;'>"+
		         "<table class='BillBody_1'  rules=none width='700'>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td height='21' colspan='3' ><span class='STYLE8'>I.实发工资(A-B-F+G-H)</span></td>"+
		              "<td width='170'  dir='rtl'><div align='right' class='STYLE8'>合计:16584.13</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>A.税前应发年终奖</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:50000.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3'><span class='STYLE8'>B.税前应扣年终奖</span></td>"+
		                "<td   dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td  colspan='3' ><span class='STYLE10'>C.仅计税不发放年终奖</span></td>"+
		                "<td dir='rtl'><div align='right' class='STYLE8'>合计:10000.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>D.仅抵扣计税项</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>E.计年终奖总额(A-B+C-D) </span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>F.年终奖个人所得税</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:11445.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>G.税后应发年终奖</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>H.税后应扣年终奖</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:5000.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td  colspan='3' ><span class='STYLE8'>I.实发年终奖(A-B-F+G-H)</span></td>"+
		                "<td dir='rtl'><div align='right' class='STYLE8'>合计:33555.00</div></td>"+
		            "</tr>"+
		      "</table>"+
		"</div>"+
		         "<div id='5' style='position: absolute;width: 700px;top: 912px;left: 45px;'>"+
		            "<table class='BillBody_2' rules=none width='700'>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>A.税前应发股票期权</span></td>"+
		              "<td width='170'  dir='rtl'><div align='right' class='STYLE8'>合计:50000.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>B.税前应扣股票期权</span></td>"+
		              "<td width='170' dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3'><span class='STYLE8'>C.仅计税不发放股票期权</span></td>"+
		                "<td dir='rtl'><div align='right' class='STYLE8'>合计:10000.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>D.仅抵扣计税项</span></td>"+
		                "<td dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+ 
		                "<td colspan='3'><span class='STYLE8'>E.计税股票期权总额(A-B+C-D)</span></td>"+
		                "<td   dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		               "<td colspan='3' ><span class='STYLE8'>F.股票期权个人所得税</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:5340.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		               "<td  colspan='3'><span class='STYLE8'>G.税后应发股票期权</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>H.税后应扣股票期权</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:20000.00</div></td>"+
		            "</tr>"+
		            "<tr bgcolor='#63CFA3'>"+
		                "<td colspan='3' ><span class='STYLE8'>I.实发股票期权(A-B-F+G-H)</span></td>"+
		                "<td  dir='rtl'><div align='right' class='STYLE8'>合计:24660.00</div></td>"+
		            "</tr>"+
		           "</table>"+
		"</div>"+
		         "<div id='6' style='position: absolute;width: 700px;top: 1105px;left: 45px;'>"+
				 	"<table class='BillBody_3' rules=none width='700'>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td colspan='3'><span class='STYLE10'>A.税前应发经济补偿金</span></td>"+
		                      "<td width='170'   dir='rtl'><div align='right' class='STYLE8'>合计:120000.00</div></td>"+
		                    "</tr>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td  colspan='3'><span class='STYLE10'>B.税前应扣经济补偿金</span></td>"+
		                      "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		                    "</tr>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td colspan='3' ><span class='STYLE10'>C.仅计税不发放经济补偿金</span></td>"+
		                      "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		                    "</tr>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td colspan='3' ><span class='STYLE10'>D.仅抵扣计税项</span></td>"+
		                      "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		                    "</tr>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td colspan='3' ><span class='STYLE10'>E.计税经济补偿金总额(A-B+C-D)</span></td>"+
		                      "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		                    "</tr>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td colspan='3' ><span class='STYLE10'>F.经济补偿金个人所得税</span></td>"+
		                      "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		                    "</tr>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td colspan='3' ><span class='STYLE10'>G.税后应发经济补偿金</span></td>"+
		                      "<td  dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		                    "</tr>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td  colspan='3' ><span class='STYLE10'>H.税后应扣经济补偿金</span></td>"+
		                      "<td dir='rtl'><div align='right' class='STYLE8'>合计:0.00</div></td>"+
		                    "</tr>"+
		                    "<tr bgcolor='#63CFA3'>"+
		                      "<td  colspan='3' ><span class='STYLE10'>I.实发工资经济补偿金(A-B-F+G-H)</span></td>"+
		                      "<td dir='rtl'><div align='right' class='STYLE8'>合计:120000.00</div></td>"+
		                    "</tr>"+
		           "</table>"+
		"</div>"+
				"<div id='7' style='position: absolute;width: 700px;top: 1323px;left: 45px;'>"+
		         "<table class='BillFoot' width='700' >"+
		            "<td colspan='4' bgcolor='#63CFA3'><span class='STYLE8'>J.其他显示信息</span></td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td width='170'>公司年金</td>"+
		                "<td width='170'>销售达成率</td>"+
		                "<td width='170'>薪资级别</td>"+
		                "<td width='170'>奖金考核方案</td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>350.00</td>"+
		                "<td>115%</td>"+
		                "<td>5</td>"+
		                "<td>D方案</td>"+
		            "</tr>"+
		            "<tr>"+
		                "<td colspan='4' dir='rtl'><hr size='1' width='100%' color='#95DD9E' noshade='noshade' /></td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;font-family:黑体'>"+
		                "<td>储蓄计划累计金额</td>"+
		                "<td>调薪比例</td>"+
		                "<td>&nbsp;</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		            "<tr style='text-align: center;font-size: 13px;'>"+
		               "<td>108546.85</td>"+
		                "<td>10%</td>"+
		                "<td>&nbsp;</td>"+
		                "<td>&nbsp;</td>"+
		            "</tr>"+
		    "</table>"+
		"</div>"+
		      "<div id='8' style='position: absolute;width: 700px;top: 1455px;left: 45px;'>"+
		    "<table class='BillMark'width='700'>"+
		        "<tr>"+
		            "<td><span class='STYLE25'>备注信息：</span></td>"+
		        "</tr>"+
		        "<tr>"+
		            "<td><span class='STYLE25'>感谢您为公司的服务，请核对您的工资信息，如您对工资情况有疑问，请向人力资源部张三丰（021-12345678）反聩。</span></td>"+
		        "</tr>"+
		        "<tr>"+
		            "<td><span class='STYLE25'>请遵守员工守则中的规章制度，个人工资信息请注意保密。</span></td>"+
		        "</tr>"+
				"<tr>"+
					"<td><span class='STYLE25'>&nbsp;</span></td>"+
				"</tr>"+
		    "</table>"+
		"</div>"+
		"<div id='9' style=' height:1600px ' >"+
		"</div>"+
		"</body>"+
		"</html>";
		return content;
	}
	/**
	 * 展开项逻辑方法
	 * @param list
	 * @return
	 */
	public static String openList(List<Map<String,String>> list){
		String openContent=null;
		Set keySet = list.get(0).keySet();
		Iterator keyIterator = keySet.iterator();
		List<String> keyList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		while(keyIterator.hasNext()){
			Object object = keyIterator.next();
			keyList.add((String) object);
			valueList.add(list.get(0).get(object));
		}
		int index=list.get(0).size();
		int flag1=0;
		int flag2=0;
		StringBuffer st=new StringBuffer("");
		for(int i=index/4;i>=0;i--){
			if(index-flag1>0){
				st.append( "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td width='170'>"+keyList.get(flag1++)+"</td>");
			}else{
				st.append( "<tr style='text-align: center;font-size: 13px;'>"+
		                "<td width='170'></td>");
			}
			if(index-flag1>0){
				st.append("<td width='170'>"+keyList.get(flag1++)+"</td>");
			}else{
				st.append("<td width='170'></td>");
			}
			if(index-flag1>0){
				st.append("<td width='170'>"+keyList.get(flag1++)+"</td>");
			}else{
				st.append("<td width='170'></td>");
			}
			if(index-flag1>0){
				st.append("<td width='170'>"+keyList.get(flag1++)+"</td>"+
			            "</tr>");
			}else{
				st.append("<td width='170'></td>"+
			            "</tr>");
			}
			if(index-flag2>0){
				st.append("<tr style='text-align: center;font-size: 13px;'>"+
		                "<td>"+valueList.get(flag2++)+"</td>");
			}else{
				st.append("<tr style='text-align: center;font-size: 13px;'>"+
		                "<td></td>");
			}
			if(index-flag2>0){
				st.append("<td>"+valueList.get(flag2++)+"</td>");
			}else{
				st.append("<td></td>");
			}
			if(index-flag2>0){
				st.append("<td>"+valueList.get(flag2++)+"</td>");
			}else{
				st.append("<td></td>");
			}
			if(index-flag2>0){
				st.append( "<td>"+valueList.get(flag2++)+"</td>"+
			            "</tr>");
			}else{
				st.append("<td></td>"+
			            "</tr>");
			}
			if(i>0&&index%4!=0){
				st.append("<tr>"+
			               "<td colspan='4' dir='rtl'><hr size='1' width='100%' color='#95DD9E' noshade='noshade' /></td>"+
				            "</tr>");
			}
			
		}
		openContent=st.toString();
		return openContent;
	}
}
