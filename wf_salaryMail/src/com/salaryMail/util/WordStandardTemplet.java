package com.salaryMail.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.salaryMail.entity.FormatInfo;
import com.salaryMail.entity.SalaryTempletData;
import com.salaryMail.entity.TempletBaseInfo;
import com.salaryMail.entity.TempletSalaryInfo;
import com.salaryMail.entity.TempletSalaryItemInfo;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class WordStandardTemplet {

	public static boolean writeWordFile(List<SalaryTempletData> dealSalaryInfo,
			String filePath, String imagePath, int m) {

		boolean w = false;
		// String fileName = "d:/PayBill.doc";
		try {

			if (!"".equals(filePath)) {
				// Html开始
				String Html = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>"
						+ "<html xmlns='http://www.w3.org/1999/xhtml'>"
						+ "<head>"
						+ "<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"
						+ "<title>员工在线账单</title>"
						+ "<style type='text/css'>"
						+ ".BillInfo{border-collapse:collapse;font-size:14px;}"
						+ ".Title {font-family: '新宋体'; color: #FFFFFF; font-weight: bold; font-size: 16px;}"
						+ ".Info {font-family: 'Times New Roman', Times, serif; font-size: 14px; }"
						+ "</style></head><body>";
				// Html结束
				String HtmlEnd = "<div id='9' style='position: relative'></div></body></html>";
				String content = Html + getDate(dealSalaryInfo, imagePath, m)
						+ getItem(dealSalaryInfo, m)
						+ getItemA(dealSalaryInfo, m)
						+ getMark(dealSalaryInfo, m) + HtmlEnd;
				// 生成临时文件名称
				byte b[] = content.getBytes("utf-8");
				// 获取Html
				ByteArrayInputStream bais = new ByteArrayInputStream(b);
				POIFSFileSystem poifs = new POIFSFileSystem();
				DirectoryEntry directory = poifs.getRoot();
				DocumentEntry documentEntry = directory.createDocument(
						"WordDocument", bais);
				FileOutputStream ostream = new FileOutputStream(filePath);
				poifs.writeFilesystem(ostream);
				// 生成Doc文件
				File file = new File(filePath);
				boolean fileStyle = file.canRead();
				bais.close();
				ostream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return w;
	}

	// 工资单第一部分信息(日期+Logo)
	public static String getDate(List<SalaryTempletData> dealSalaryInfo,
			String imagePath, int m) {

		// 第一部分数据（
		FormatInfo formatInfo = dealSalaryInfo.get(m).getFormatInfo();
		StringBuffer str1 = new StringBuffer("");
		// 公司Logo
		String date = "<div id='1' align='center' style='position: relative'>"
				+ "<table class='BillInfo' class='BillInfo' border='0px' cellpadding='0px' cellspacing='0px' width='95%'><tbody>"
				+ "<tr> <td rowspan='2' width='400' align='left'><img src='" + imagePath
				+ "' alt='找不到Logo'/></td>";
		// 发送日期、薪资期间
		String compName="";
		if ("1".equals(formatInfo.getSendDateIsDisplay())) {
			if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
				str1.append("<td style='text-align: right;' width='400' height='28' >"
						+ formatInfo.getSendDateDisplayName()
						+ "</td>");
			}
			if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
				str1.append("<td style='text-align: right;' width='400' height='28' >"
						+ "Pay Day"
						+ "</td>");
			}
			str1.append("<td style='text-align: right;'width='400' height='28' >"
					+ dealSalaryInfo.get(m).getSendDate() + "</td></tr>");
			if ("1".equals(formatInfo.getSalaryPeriodIsDisplay())) {
				if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
					str1.append("<tr><td style='text-align: right;'  width='400' height='28' >"
							+ formatInfo.getSalaryPeriodDisplayName()
							+ "</td>");
				}
				if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
					str1.append("<tr><td style='text-align: right;'  width='400' height='28' >"
							+ "Salary Period"
							+ "</td>");
				}
				if("1".equals(dealSalaryInfo.get(m).getIsCompnameDisplay())){
					if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
						compName=dealSalaryInfo.get(m).getCompanyNameCh();
					}
					if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
						compName=dealSalaryInfo.get(m).getCompanyNameEn();
					}
					str1.append("<td  style='text-align: right;' width='400' height='28'>"
							+ dealSalaryInfo.get(m).getDatePeriod()
							+ "</td></tr><tr><td colspan='3' style='text-align: left;font-size: 16px;' width='400' height='28'>"+compName+"</td></tr></tbody></table></div>");
				}else{
					str1.append("<td  style='text-align: right;' width='400' height='28'>"
							+ dealSalaryInfo.get(m).getDatePeriod()
							+ "</td></tr></tbody></table></div>");
				}
			} else {
					str1.append("<td style='text-align: right;' width='400' height='28'  >"
							+ "</td>");
					if("1".equals(dealSalaryInfo.get(m).getIsCompnameDisplay())){
						if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
							compName=dealSalaryInfo.get(m).getCompanyNameCh();
						}
						if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
							compName=dealSalaryInfo.get(m).getCompanyNameEn();
						}
						str1.append("<td  style='text-align: right;' width='400' height='28'>"
								+ "</td></tr><tr><td colspan='3' style='text-align: left;font-size: 16px;' width='400' height='28'>"+compName+"</td></tr></tbody></table></div>");
					}else{
						str1.append("<td style='text-align: right;' width='400' height='28' >"
								+ "</td></tr></tbody></table></div>");
					}
			}
		} else {
			if ("1".equals(formatInfo.getSalaryPeriodIsDisplay())) {
				if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
					str1.append("<td style='text-align: right;' width='400' height='28' >"
							+ formatInfo.getSalaryPeriodDisplayName()
							+ "</td>");
				}
				if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
					str1.append("<td style='text-align: right;' width='400' height='28' >"
							+ "Salary Period"
							+ "</td>");
				}
				str1.append("<td style='text-align: right;;'width='400' height='28' >"
						+ dealSalaryInfo.get(m).getDatePeriod() + "</td></tr>");
			} else {
				str1.append("<td style='text-align: right;' width='400' height='28' >"
						+ "</td>");
				str1.append("<td style='text-align: right;' width='400' height='28' >"
						+ "</td></tr>");
			}
			    str1.append("<td style='text-align: right;' width='400' height='28' >"
					    + "</td>");
			    if("1".equals(dealSalaryInfo.get(m).getIsCompnameDisplay())){
					if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
						compName=dealSalaryInfo.get(m).getCompanyNameCh();
					}
					if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
						compName=dealSalaryInfo.get(m).getCompanyNameEn();
					}
					str1.append("<td  style='text-align: right;' width='400' height='28'>"
							+ "</td></tr><tr><td colspan='3' style='text-align: left;font-size: 16px;' width='400' height='28'>"+compName+"</td></tr></tbody></table></div>");
				}else{
					str1.append("<td style='text-align: right;' width='400' height='28' >"
							+ "</td></tr></tbody></table></div>");
				}
		}
		return date + str1.toString();

	}

	// 工资单第二部分
	// 设置基本信息
	public static String getItem(List<SalaryTempletData> dealSalaryInfo, int m) {
		List<TempletBaseInfo> list1 = dealSalaryInfo.get(m).getFirstData();
		List<TempletBaseInfo> list2 = new ArrayList<TempletBaseInfo>();
		TempletBaseInfo tbi = null;
		StringBuffer str2 = new StringBuffer("");
		String baseInfo = "<div id='2' align='center' style='position: relative'>"
				+ "<table class='Info' width='95%'>";
		// 获取基本信息List
		for (int j = 0; j < list1.size(); j++) {
				// item设置为显示时
				if (list1.get(j).getIsShow() == 1) {
					if(list1.get(j).getTitleValue()!= null&&!"null".equals(list1.get(j).getTitleValue())) {
						tbi = list1.get(j);
						list2.add(tbi);
					}
				} else {
					// item值为空时
					if("1903".equals(list1.get(j).getItemCode())){
						continue;
					}else{
						if (list1.get(j).getItemValue() == null
								| "null".equals(list1.get(j).getItemValue())
								| "".equals(list1.get(j).getItemValue())) {
							continue;
						} else if(list1.get(j).getTitleValue()!= null&&!"null".equals(list1.get(j).getTitleValue())) {
							tbi = list1.get(j);
							list2.add(tbi);
						}
					}
				}
		}
		// 显示循环控制
		int flag = 0;
		if (list2.size() > 0) {
			for (int k = list2.size() / 2; k >= 0; k--) {
				if (list2.size() - flag > 0) {
					str2.append("<tr><td height='18px' width='170'><span>"
							+ list2.get(flag).getTitleValue()
							+ "</span></td>"
							+ "<td height='18px' width='170'><span >"
							+ list2.get(flag).getItemValue() + "</span></td>");
					flag++;
					if (list2.size() - flag > 0) {
						str2.append("<td height='18px' width='170'><span >"
								+ list2.get(flag).getTitleValue()
								+ "</span></td>"
								+ "<td height='18px' width='170'><span >"
								+ list2.get(flag).getItemValue()
								+ "</span></td></tr>");
						flag++;
					} else {
						str2.append("");
						str2.append("</tr>");
						flag++;
					}

				}
			}
		}
		return baseInfo + str2.toString() + "</table></div><br/>";

	}

	// 工资单第三部分-Title
	public static String getItemA(List<SalaryTempletData> dealSalaryInfo, int m) {
		List<TempletSalaryInfo> newList = new ArrayList<TempletSalaryInfo>();
		StringBuffer strA = new StringBuffer("");
		String Title = "<div id='3' align='center'>"
				+ "<table rules='none' class='BillHead' border='0' width=95% >";
		for (int i = 0; i < dealSalaryInfo.get(m).getSecondData().size(); i++) {
			// 大标题序号
			int position = dealSalaryInfo.get(m).getSecondData().get(i)
					.getBigTitlePosition();
			String TitleName = dealSalaryInfo.get(m).getSecondData().get(i)
					.getBigTitle();
			Double BigSum = Double.valueOf(dealSalaryInfo.get(m).getSecondData().get(i)
					.getBigSum());
			// 如果Title合计为零项等于9时，整块不显示
			if (position >= 10 && position <= 18) {
				if (BigSum == 0.0){
					continue;
				}
				else{
					newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
				}
			}
			if (position >= 19 && position <= 27) {
				if (BigSum == 0.0){
					continue;
				}
				else{
					newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
				}
			}
			if (position >= 28 && position <= 36) {
				if (BigSum == 0.0){
					continue;
				}
				else{
					newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
				}
			}
			else if(position<=9){
				newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
			}
			else if(position==37){
				//判断：如果其他项目里面没有项目，其他信息不显示
				int flagNum=0;
				for (int k = 0; k < dealSalaryInfo.get(m).getSecondItemData().size(); k++) {
					if (dealSalaryInfo.get(m).getSecondItemData().get(k).getItemBelongPosition() == 37) {
						if (dealSalaryInfo.get(m).getSecondItemData().get(k).getItemIsShow() == 0) {
							if (dealSalaryInfo.get(m).getSecondItemData().get(k).getItemTitle() == null
									| "null".equals(dealSalaryInfo.get(m).getSecondItemData()
											.get(k).getItemTitle())
									| dealSalaryInfo.get(m).getSecondItemData().get(k)
											.getItemValue() == null
									| "null".equals(dealSalaryInfo.get(m).getSecondItemData()
											.get(k).getItemValue())
									| "0".equals(dealSalaryInfo.get(m).getSecondItemData()
											.get(k).getItemValue())
									| "0.0".equals(dealSalaryInfo.get(m).getSecondItemData()
											.get(k).getItemValue())
									| "0.00".equals(dealSalaryInfo.get(m).getSecondItemData()
											.get(k).getItemValue())
									| "".equals(dealSalaryInfo.get(m).getSecondItemData()
													.get(k).getItemValue())) {
								continue;
							}
							flagNum++;
						} else if (dealSalaryInfo.get(m).getSecondItemData().get(k)
								.getItemTitle() != null
								&& !"null".equals(dealSalaryInfo.get(m).getSecondItemData()
										.get(k).getItemTitle())) {
							flagNum++;
						}
					}
				}
				if(flagNum>0){
					newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
				}
			}
		}
		/*for (int i = 0; i < dealSalaryInfo.get(m).getSecondData().size(); i++) {
			// Title1-9模块
			if (dealSalaryInfo.get(m).getSecondData().get(i)
					.getBigTitlePosition() <= 9) {
				newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
				// Title10-18模块
			} else if (dealSalaryInfo.get(m).getSecondData().get(i)
					.getBigTitlePosition() >= 10
					&& dealSalaryInfo.get(m).getSecondData().get(i)
							.getBigTitlePosition() <= 18) {
				if (bonusIndex != 9) {
					newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
				}
				// Title19-27模块
			} else if (dealSalaryInfo.get(m).getSecondData().get(i)
					.getBigTitlePosition() >= 19
					&& dealSalaryInfo.get(m).getSecondData().get(i)
							.getBigTitlePosition() <= 27) {
				if (stockIndex != 9) {
					newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
				}
				// Title28-36模块
			} else if (dealSalaryInfo.get(m).getSecondData().get(i)
					.getBigTitlePosition() >= 28
					&& dealSalaryInfo.get(m).getSecondData().get(i)
							.getBigTitlePosition() <= 36) {
				if (suppleIndex != 9) {
					newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
				}
				// Title37
			} else {
				newList.add(dealSalaryInfo.get(m).getSecondData().get(i));
			}
		}*/
		// 循环控制Title的显示
		for (int i = 0; i < newList.size(); i++) {

			if (newList.get(i).getBigTitlePosition() != 37) {
				if("1".equals(newList.get(i).getIsCount())){
					if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
						strA.append("<tr bgcolor='#63CFA3' style='text-align: left;'>"
								+ "<td colspan='3' height='23px'><span class='Title'>"
								+ newList.get(i).getBigTitle()
								+ "</span></td>"
								+ "<td dir='rtl' width='170' height='23px'><div class='Title' align='right'>合计:"
								+ newList.get(i).getBigSum() + "</div></td></tr>");
					}
					if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
						strA.append("<tr bgcolor='#63CFA3' style='text-align: left;'>"
								+ "<td colspan='3' height='23px'><span class='Title'>"
								+ newList.get(i).getBigTitle()
								+ "</span></td>"
								+ "<td dir='rtl' width='170' height='23px'><div class='Title' align='right'>Subtotal:"
								+ newList.get(i).getBigSum() + "</div></td></tr>");
					}
				}else{
					strA.append("<tr bgcolor='#63CFA3' style='text-align: left;'>"
							+ "<td colspan='3' height='23px'><span class='Title'>"
							+ newList.get(i).getBigTitle()
							+ "</span></td><td></td></tr>");
				}
			}
			if (newList.get(i).getBigTitlePosition() == 37) {

				strA.append("<tr bgcolor='#63CFA3' style='text-align: left;'>"
						+ "<td colspan='3' height='23px'><span class='Title'>"
						+ newList.get(i).getBigTitle()
						+ "</span></td><td></td></tr>");
			}
			// 每个模块下面添加一条白线
			if (newList.get(i).getBigTitlePosition() == 9||newList.get(i).getBigTitlePosition() == 18||newList.get(i).getBigTitlePosition() == 27
					||newList.get(i).getBigTitlePosition() == 36||newList.get(i).getBigTitlePosition() == 37) {
				strA.append("<tr><td colspan='4' dir='rtl'></td></tr>");
			}
			// 添加item
			strA.append(getItemB(dealSalaryInfo, newList.get(i)
					.getBigTitlePosition(), m));
		}
		return Title + strA.toString();

	}

	// 工资单第三部分--item
	public static String getItemB(List<SalaryTempletData> dealSalaryInfo,
			int position, int m) {

		List<TempletSalaryItemInfo> newList = new ArrayList<TempletSalaryItemInfo>();
		StringBuffer strB = new StringBuffer("");
		for (int n = 0; n < dealSalaryInfo.get(m).getSecondItemData().size(); n++) {
			// 判断item所属位置
			if (dealSalaryInfo.get(m).getSecondItemData().get(n)
					.getItemBelongPosition() == position) {
				// 判断item是否显示
				if (dealSalaryInfo.get(m).getSecondItemData().get(n)
						.getItemIsShow() == 0) {
					// 判断item是否为空(为空不显示)
					if (dealSalaryInfo.get(m).getSecondItemData().get(n)
							.getItemTitle() == null
							| "null".equals(dealSalaryInfo.get(m)
									.getSecondItemData().get(n).getItemTitle())
							| dealSalaryInfo.get(m).getSecondItemData().get(n)
									.getItemValue() == null
							| "null".equals(dealSalaryInfo.get(m)
									.getSecondItemData().get(n).getItemValue())
							| "0.00".equals(dealSalaryInfo.get(m)
									.getSecondItemData().get(n).getItemValue())
							| "0.0".equals(dealSalaryInfo.get(m)
									.getSecondItemData().get(n).getItemValue())
							| "0".equals(dealSalaryInfo.get(m)
									.getSecondItemData().get(n).getItemValue())
							| "".equals(dealSalaryInfo.get(m).
									getSecondItemData().get(n).getItemValue())) {
						continue;
					}
					// item不为空时
					newList.add(dealSalaryInfo.get(m).getSecondItemData()
							.get(n));
				} else if (dealSalaryInfo.get(m).getSecondItemData().get(n)
						.getItemTitle() != null
						&& !"null".equals(dealSalaryInfo.get(m)
								.getSecondItemData().get(n).getItemTitle())) {
					newList.add(dealSalaryInfo.get(m).getSecondItemData()
							.get(n));
				}
			}
		}
		if (newList.size() > 0) {
			int index = newList.size();
			int row = index / 4 + (index % 4 > 0 ? 1 : 0);
			int itemFlag1 = 0;
			int itemFlag2 = 0;
			for (int x = row; x > 0; x--) {// 出错点
				// 小标题名
				// 1
				if (index - itemFlag1 > 0) {
					strB.append("<tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>"
							+ newList.get(itemFlag1++).getItemTitle() + "</td>");
				} else {
					strB.append("<tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'></td>");
				}
				// 2
				if (index - itemFlag1 > 0) {
					strB.append("<td height='18px' width='170'>"
							+ newList.get(itemFlag1++).getItemTitle() + "</td>");
				} else {
					strB.append("<td height='18px' width='170'></td>");
				}
				// 3
				if (index - itemFlag1 > 0) {
					strB.append("<td height='18px' width='170'>"
							+ newList.get(itemFlag1++).getItemTitle() + "</td>");
				} else {
					strB.append("<td height='18px' width='170'></td>");
				}
				// 4
				if (index - itemFlag1 > 0) {
					strB.append("<td height='18px' width='170'>"
							+ newList.get(itemFlag1++).getItemTitle()
							+ "</td></tr>");
				} else {
					strB.append("<td height='18px' width='170'></td></tr>");
				}

				// 取值
				// 1
				if (index - itemFlag2 > 0) {

					strB.append("<tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>"
							+ newList.get(itemFlag2++).getItemValue() + "</td>");
				} else {
					strB.append("<tr  style='text-align: center;font-size: 13px;'><td height='18px' width='170'></td>");
				}
				// 2
				if (index - itemFlag2 > 0) {

					strB.append("<td height='18px' width='170'>"
							+ newList.get(itemFlag2++).getItemValue() + "</td>");
				} else {
					strB.append("<td height='18px' width='170'></td>");
				}
				// 3
				if (index - itemFlag2 > 0) {

					strB.append("<td height='18px' width='170'>"
							+ newList.get(itemFlag2++).getItemValue() + "</td>");
				} else {
					strB.append("<td height='18px' width='170'></td>");
				}
				// 4
				if (index - itemFlag2 > 0) {

					strB.append("<td height='18px' width='170'>"
							+ newList.get(itemFlag2++).getItemValue()
							+ "</td></tr>");
				} else {
					strB.append("<td height='18px' width='170'></td></tr>");
				}
				if (x > 1) {
					strB.append(" <tr><td colspan='4' dir='rtl'><hr color='#999999' noshade='noshade' size='1' width='100%' /></td></tr>");
				}
			}

		}
		return strB.toString();
	}

	public static String getMark(List<SalaryTempletData> dealSalaryInfo, int m) {
		StringBuffer str3 = new StringBuffer("");
		String remarks="";
		if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
			remarks = "备注信息：<br/>" + dealSalaryInfo.get(m).getRemarks();
		}
		if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
			remarks = "Remarks：<br/>" + dealSalaryInfo.get(m).getRemarks();
		}
		String Announcement = dealSalaryInfo.get(m).getFormatInfo().getAnnouncement()==null?"":dealSalaryInfo.get(m).getFormatInfo().getAnnouncement();
		if ("0".equals(dealSalaryInfo.get(m).getFormatInfo()
				.getIsEpayRemarksDisplay())) {
			remarks = "";
		}
		if (!StringUtils.isEmpty(dealSalaryInfo.get(m).getRemarks())
				|| !StringUtils.isEmpty(Announcement)) {
            if("0".equals(dealSalaryInfo.get(m).getFormatInfo().getIsEpayRemarksDisplay())&&StringUtils.isEmpty(Announcement)){
				
			}else{
			str3.append("<tr> <td colspan='4'><table style='border:2px #000000 solid;width:100%;height:100%' frame='void'><tr style='font-size:13px;'><td>"
					+ "<div>"
					+ remarks
					+ " </div>"
					+ "<div>"
					+ Announcement
					+ "</div><br/></td></tr></table></td></tr></table></div>");
			}
		} else {
			str3.append("</table></div>");
		}
		return str3.toString();
	}

}
