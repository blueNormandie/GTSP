package com.salaryMail.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.salaryMail.entity.FormatInfo;
import com.salaryMail.entity.SalaryTempletData;
import com.salaryMail.entity.TempletBaseInfo;
import com.salaryMail.entity.TempletSalaryInfo;
import com.salaryMail.entity.TempletSalaryItemInfo;

public class HtmlSalaryPreview {

	public static String generateHtmlSalaryFile(List<SalaryTempletData> dealSalaryInfo,
			String HTMLFileName) {
		PrintWriter printWriter = null;
		SalaryTempletData std = null;
		StringBuffer HtmlContent = new StringBuffer("");

		// File file=new File("E://efesco_project//centerdb_2_development//工资单//wageTest//meteor.html");
		File file = new File(HTMLFileName);
		// File file=new File(System.getProperty("user.dir")+"\\WebRoot\\html\\"+HTMLFileName+".html");
		System.out.println("html文件绝对路径：" + file.getAbsolutePath());
		if (dealSalaryInfo == null) {
			HtmlContent.append("<!DOCTYPE html><html><head><title></title>"
					+ "<meta http-equiv='keywords' content='keyword1,keyword2,keyword3'>"
					+ "<meta http-equiv='description' content='this is my page'>"
					+ "<meta http-equiv='content-type' content='text/html; charset=UTF-8'>"
					+ "</head>"
					+ "<body>"
					+ "对不起，没有数据！(sorry, no data!) <br>"
					+ "</body></html>");
		} else {
			std = dealSalaryInfo.get(0);
			HtmlContent.append("<!DOCTYPE html>"
					+ "<html>"
					+ "<head lang='en'>"
					+ "<meta http-equiv='content-type' content='text/html; charset=UTF-8'>"
					+ "<title></title>"
					+ "<link rel='stylesheet' type='text/css' href='css/wage_com.css'>");
			if ("1".equals(std.getFormatInfo().getFormatType())) {
				if ("1".equals(std.getItemNameType())) {
					HtmlContent.append("<link rel='stylesheet' type='text/css' href='css/wage_dialog.css'>");
				} else if ("2".equals(std.getItemNameType())) {
					HtmlContent.append("<link rel='stylesheet' type='text/css' href='css/wage_dialog_en.css'>");
				}
			} else {
				if ("1".equals(std.getItemNameType())) {
					HtmlContent.append("<link rel='stylesheet' type='text/css' href='css/wage_dialog_free.css'>");
				} else if ("2".equals(std.getItemNameType())) {
					HtmlContent.append("<link rel='stylesheet' type='text/css' href='css/wage_dialog_en_free.css'>");
				}
			}
			HtmlContent.append("</head>"
					+ "<body style='min-height:700px;position:relative;'>"
					+ "<div class='dialog_wage'>"
					+ "<div class='wage_dialog'>");
			if (!"1".equals(std.getFormatInfo().getSendDateIsDisplay())
					&& !"1".equals(std.getFormatInfo().getSalaryPeriodIsDisplay())) {
			} else {
				HtmlContent.append("<div class='wd_top'>");
				if ("1".equals(std.getFormatInfo().getSalaryPeriodIsDisplay())) {
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='wd_top_left'>工资单详情 <span> - "
								+ std.getFormatInfo().getSalaryPeriodDisplayName()
								+ ":"
								+ std.getDatePeriod()
								+ "</span></div>");
					} else {
						HtmlContent.append("<div class='wd_top_left'>pay list<span> - Salary Period："
								+ std.getDatePeriod()
								+ "</span></div>");
					}
				}
				if ("1".equals(std.getFormatInfo().getSendDateIsDisplay())) {
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='wd_top_right'><span>"
								+ std.getFormatInfo().getSendDateDisplayName()
								+ ":"
								+ std.getSendDate()
								+ "</span></div>");
					} else {
						HtmlContent.append("<div class='wd_top_right'><span>Pay Day："
								+ std.getSendDate()
								+ "</span></div>");
					}
				}

				HtmlContent.append("</div>");
			}
			/* 基本信息数据处理 */
			List<TempletBaseInfo> list1 = std.getFirstData();
			List<TempletBaseInfo> list2 = new ArrayList<TempletBaseInfo>();
			TempletBaseInfo tbi = null;
			for (int j = 0; j < list1.size(); j++) {
				if (list1.get(j).getIsShow() == 1) {
					if (list1.get(j).getTitleValue() != null
							&& !"".equals(list1.get(j).getTitleValue())) {
						tbi = list1.get(j);
						list2.add(tbi);
					}
				} else {
					if ("1903".equals(list1.get(j).getItemCode())) {
						continue;
					} else {
						if (list1.get(j).getItemValue() == null
								| "".equals(list1.get(j).getItemValue())) {
							continue;
						} else if (list1.get(j).getTitleValue() != null
								&& !"".equals(list1.get(j).getTitleValue())) {
							tbi = list1.get(j);
							list2.add(tbi);
						}
					}
				}
			}
			if ("1".equals(std.getFormatInfo().getFormatType())) {
				/* 标准模板开始 */
				// 处理大标题数据
				List<TempletSalaryInfo> newList1 = new ArrayList<TempletSalaryInfo>();
				List<TempletSalaryInfo> newList2 = new ArrayList<TempletSalaryInfo>();
				List<TempletSalaryInfo> newList3 = new ArrayList<TempletSalaryInfo>();
				List<TempletSalaryInfo> newList4 = new ArrayList<TempletSalaryInfo>();
				List<TempletSalaryInfo> newList5 = new ArrayList<TempletSalaryInfo>();
				for (int i = 0; i < std.getSecondData().size(); i++) {
					if (std.getSecondData().get(i).getBigTitlePosition() >= 10
							&& std.getSecondData().get(i).getBigTitlePosition() <= 18) {
						if (Double.valueOf(std.getSecondData().get(i).getBigSum()) == 0.0) {
							continue;
						} else {
							newList2.add(std.getSecondData().get(i));
						}
					}
					if (std.getSecondData().get(i).getBigTitlePosition() >= 19
							&& std.getSecondData().get(i).getBigTitlePosition() <= 27) {
						if (Double.valueOf(std.getSecondData().get(i).getBigSum()) == 0.0) {
							continue;
						} else {
							newList3.add(std.getSecondData().get(i));
						}
					}
					if (std.getSecondData().get(i).getBigTitlePosition() >= 28
							&& std.getSecondData().get(i).getBigTitlePosition() <= 36) {
						if (Double.valueOf(std.getSecondData().get(i).getBigSum()) == 0.0) {
							continue;
						} else {
							newList4.add(std.getSecondData().get(i));
						}
					} else if (std.getSecondData().get(i).getBigTitlePosition() <= 9) {
						newList1.add(std.getSecondData().get(i));
					} else if (std.getSecondData().get(i).getBigTitlePosition() == 37) {
						// 判断：如果其他项目里面没有项目，其他信息不显示
						int flagNum = 0;
						for (int k = 0; k < std.getSecondItemData().size(); k++) {
							if (std.getSecondItemData().get(k).getItemBelongPosition() == 37) {
								if (std.getSecondItemData().get(k).getItemIsShow() == 0) {
									if (std.getSecondItemData().get(k).getItemTitle() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemTitle())
											|| std.getSecondItemData().get(k).getItemValue() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.00".equals(std.getSecondItemData().get(k)
													.getItemValue())) {
										continue;
									}
									flagNum++;
								} else if (std.getSecondItemData().get(k).getItemTitle() != null
										&& !"".equals(std.getSecondItemData().get(k).getItemTitle())) {
									flagNum++;
								}
							}
						}
						if (flagNum > 0) {
							newList5.add(std.getSecondData().get(i));
						}
					}
				}
				// 左边导航
				boolean mark=false;
				HtmlContent.append("<div class='wd_con'>"
						+ "<div class='wd_com_left'>");
				HtmlContent.append("<div class='wd_column' id='number1'>"
						+ "<div class='wd_cln_letter letter_hover'>A");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("A")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_cln_letter'>B");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("B")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_cln_letter'>C");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("C")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_cln_letter'>D");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("D")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_cln_letter'>E");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("E")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_cln_letter'>F");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("F")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_cln_letter'>G");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("G")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_cln_letter'>H");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("H")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_cln_letter'>I");
				for (int i = 0; i < newList1.size(); i++) {
					if (newList1.get(i).getBigTitle().trim().startsWith("I")) {
						mark=true;
						HtmlContent.append("<div class='letter_title'>"
								+ newList1.get(i).getBigTitle().trim().substring(2)
								+ "</div>");
						break;
					}
				}
				if(mark==false){
					if ("1".equals(std.getItemNameType())) {
						HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
					} else {
						HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
					}
				}mark=false;
				HtmlContent.append("</div>");
				HtmlContent.append("</div>");
				if (newList2.size() > 0) {
					HtmlContent.append("<div class='wd_column' id='number2' style='display:none'>"
							+ "<div class='wd_cln_letter letter_hover'>A");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("A")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>B");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("B")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>C");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("C")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>D");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("D")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>E");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("E")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>F");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("F")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>G");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("G")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>H");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("H")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>I");
					for (int i = 0; i < newList2.size(); i++) {
						if (newList2.get(i).getBigTitle().trim().startsWith("I")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList2.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("</div>");

				}
				if (newList3.size() > 0) {
					HtmlContent.append("<div class='wd_column' id='number3' style='display:none'>"
							+ "<div class='wd_cln_letter letter_hover'>A");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("A")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>B");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("B")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>C");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("C")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>D");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("D")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>E");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("E")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>F");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("F")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>G");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("G")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>H");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("H")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>I");
					for (int i = 0; i < newList3.size(); i++) {
						if (newList3.get(i).getBigTitle().trim().startsWith("I")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList3.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("</div>");
				}
				if (newList4.size() > 0) {
					HtmlContent.append("<div class='wd_column' id='number4' style='display:none'>"
							+ "<div class='wd_cln_letter letter_hover'>A");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("A")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>B");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("B")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>C");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("C")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>D");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("D")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>E");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("E")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>F");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("F")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>G");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("G")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>H");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("H")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("<div class='wd_cln_letter'>I");
					for (int i = 0; i < newList4.size(); i++) {
						if (newList4.get(i).getBigTitle().trim().startsWith("I")) {
							mark=true;
							HtmlContent.append("<div class='letter_title'>"
									+ newList4.get(i).getBigTitle().trim().substring(2)
									+ "</div>");
							break;
						}
					}
					if(mark==false){
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='letter_title'>当前模块没有明细项目</div>");
						} else {
							HtmlContent	.append("<div class='letter_title'>The current module has no details</div>");
						}
					}mark=false;
					HtmlContent.append("</div>");
					HtmlContent.append("</div>");
				}
				// 基本信息

				HtmlContent.append("<div class='wd_tables'>" + "<div class='wd_tables_top'>"
						+ "<div class='wdt_top_left'>");
				if (list2.size() > 0) {
					for (int i = 0; i < list2.size(); i++) {
						if ("1901".equals(list2.get(i).getItemCode())) {
							HtmlContent.append("<div class='wdt_tl_word'>"
									+ list2.get(i).getItemValue()
									+ "</div>");
						} else {
							continue;
						}
					}
				}
				if ("1".equals(std.getItemNameType())) {
					HtmlContent.append("<a href='javascript:void(0);' class='wdt_tl_a'>个人详细信息</a>");
				} else if ("2".equals(std.getItemNameType())) {
					HtmlContent.append("<a href='javascript:void(0);' class='wdt_tl_a'>personal details</a>");
				}
				HtmlContent.append("</div>");

				if (list2.size() > 0) {
					int flag = 0;
					HtmlContent.append("<div class='wdt_u_info'>" + "<table>");
					for (int i = list2.size() / 2; i >= 0; i--) {
						if (list2.size() - flag > 0) {
							HtmlContent.append("<tr>" + "<td><span class='td_left'>"
									+ list2.get(flag).getTitleValue()
									+ "：</span><span class='td_right'>"
									+ list2.get(flag).getItemValue() + "</span></td>");
							flag++;
							if (list2.size() - flag > 0) {
								HtmlContent.append("<td><span class='td_left'>"
										+ list2.get(flag).getTitleValue()
										+ "：</span><span class='td_right'>"
										+ list2.get(flag).getItemValue() + "</span></td>");
								flag++;
							} else {
								HtmlContent.append("<td><span class='td_left'></span><span class='td_right'></span></td>");
								flag++;
							}
							HtmlContent.append("</tr>");
						}
					}
					HtmlContent.append("</table>" + "</div>");
				}
				if (std.getLogoPath() != null) {
					HtmlContent.append("<img src='"
							+ std.getLogoPath().replace("/home/httpd/imgehr.efesco.com","http://172.16.221.143")
							+ "' class='wg_logo'>");
				}
				HtmlContent.append("</div>");
				// 大标题循环
				HtmlContent
						.append("<div id='tables_body1' class='tables_body' style='display:block'>");
				for (int i = 0; i < newList1.size(); i++) {
					HtmlContent.append("<div class='table_div'>"
							+ "<div class='table_div_title'>"
							+ newList1.get(i).getBigTitle());
					String bigRemarks = "";
					if ("1".equals(std.getBigTitleRemarksIsDisplay())) {
						if ("1".equals(std.getItemNameType())) {
							bigRemarks = newList1.get(i).getBigRemarks() == null ? "没有补充说明信息" : newList1.get(i).getBigRemarks();
							HtmlContent.append("<img src='icons/qmark.png' title='"
									+ bigRemarks
									+ "'>");
						} else if ("2".equals(std.getItemNameType())) {
							bigRemarks = newList1.get(i).getBigRemarks() == null ? "No comment" : newList1.get(i).getBigRemarks();
							HtmlContent.append("<img src='icons/qmark.png' title='"
									+ bigRemarks
									+ "'>");
						}
					}
					HtmlContent.append("</div>");
					// 处理小标题数据
					List<TempletSalaryItemInfo> itemList = new ArrayList<TempletSalaryItemInfo>();
					for (int k = 0; k < std.getSecondItemData().size(); k++) {
						if (std.getSecondItemData().get(k).getItemBelongPosition().intValue() == newList1
								.get(i).getBigTitlePosition().intValue()) {
							if (std.getSecondItemData().get(k).getItemIsShow() == 0) {
								if (std.getSecondItemData().get(k).getItemTitle() == null
										|| "".equals(std.getSecondItemData().get(k).getItemTitle())
										|| std.getSecondItemData().get(k).getItemValue() == null
										|| "".equals(std.getSecondItemData().get(k).getItemValue())
										|| "0".equals(std.getSecondItemData().get(k).getItemValue())
										|| "0.0".equals(std.getSecondItemData().get(k)
												.getItemValue())
										|| "0.00".equals(std.getSecondItemData().get(k)
												.getItemValue())) {
									continue;
								}
								itemList.add(std.getSecondItemData().get(k));
							} else if (std.getSecondItemData().get(k).getItemTitle() != null
									&& !"".equals(std.getSecondItemData().get(k).getItemTitle())) {
								itemList.add(std.getSecondItemData().get(k));
							}
						}
					}
					// 循环小标题
					if (itemList.size() > 0) {
						HtmlContent.append("<table>");
						int flag = 0;
						for (int m = itemList.size() / 2; m >= 0; m--) {
							if (itemList.size() - flag > 0) {
								HtmlContent.append("<tr>"
										+ "<td><span class='td_left'>"
										+ itemList.get(flag).getItemTitle()
										+ "</span><span class='td_right'>"
										+ itemList.get(flag).getItemValue()
										+ "</span></td>");
								flag++;
								if (itemList.size() - flag > 0) {
									HtmlContent.append("<td><span class='td_left'>"
											+ itemList.get(flag).getItemTitle()
											+ "</span><span class='td_right'>"
											+ itemList.get(flag).getItemValue()
											+ "</span></td>");
									flag++;
								} else {
									HtmlContent.append("<td><span class='td_left'></span><span class='td_right'></span></td>");
									flag++;
								}
								HtmlContent.append("</tr>");
							}
						}
						HtmlContent.append("</table>");
					}
					if ("1".equals(newList1.get(i).getIsCount())) {
						if ("1".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='table_div_result'>合计:"
									+ newList1.get(i).getBigSum()
									+ "</div>");
						} else if ("2".equals(std.getItemNameType())) {
							HtmlContent.append("<div class='table_div_result'>Subtotal:"
									+ newList1.get(i).getBigSum()
									+ "</div>");
						}
					}
					HtmlContent.append("</div>");
				}
				HtmlContent.append("</div>");
				if (newList2.size() > 0) {
					HtmlContent
							.append("<div id='tables_body2' class='tables_body' style='display:none'>");
					for (int i = 0; i < newList2.size(); i++) {
						HtmlContent.append("<div class='table_div'>"
								+ "<div class='table_div_title'>"
								+ newList2.get(i).getBigTitle());
						String bigRemarks = "";
						if ("1".equals(std.getBigTitleRemarksIsDisplay())) {
							if ("1".equals(std.getItemNameType())) {
								bigRemarks = newList1.get(i).getBigRemarks() == null ? "没有补充说明信息" : newList2.get(i).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks
										+ "'>");
							} else if ("2".equals(std.getItemNameType())) {
								bigRemarks = newList1.get(i).getBigRemarks() == null ? "No comment" : newList2.get(i).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks
										+ "'>");
							}
						}
						HtmlContent.append("</div>");
						// 处理小标题数据
						List<TempletSalaryItemInfo> itemList = new ArrayList<TempletSalaryItemInfo>();
						for (int k = 0; k < std.getSecondItemData().size(); k++) {
							if (std.getSecondItemData().get(k).getItemBelongPosition().intValue() == newList2
									.get(i).getBigTitlePosition().intValue()) {
								if (std.getSecondItemData().get(k).getItemIsShow() == 0) {
									if (std.getSecondItemData().get(k).getItemTitle() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemTitle())
											|| std.getSecondItemData().get(k).getItemValue() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.00".equals(std.getSecondItemData().get(k)
													.getItemValue())) {
										continue;
									}
									itemList.add(std.getSecondItemData().get(k));
								} else if (std.getSecondItemData().get(k).getItemTitle() != null
										&& !"".equals(std.getSecondItemData().get(k).getItemTitle())) {
									itemList.add(std.getSecondItemData().get(k));
								}
							}
						}
						// 循环小标题
						if (itemList.size() > 0) {
							HtmlContent.append("<table>");
							int flag = 0;
							for (int m = itemList.size() / 2; m >= 0; m--) {
								if (itemList.size() - flag > 0) {
									HtmlContent.append("<tr>"
											+ "<td><span class='td_left'>"
											+ itemList.get(flag).getItemTitle()
											+ "</span><span class='td_right'>"
											+ itemList.get(flag).getItemValue()
											+ "</span></td>");
									flag++;
									if (itemList.size() - flag > 0) {
										HtmlContent.append("<td><span class='td_left'>"
												+ itemList.get(flag).getItemTitle()
												+ "</span><span class='td_right'>"
												+ itemList.get(flag).getItemValue()
												+ "</span></td>");
										flag++;
									} else {
										HtmlContent.append("<td><span class='td_left'></span><span class='td_right'></span></td>");
										flag++;
									}
									HtmlContent.append("</tr>");
								}
							}
							HtmlContent.append("</table>");
						}
						if ("1".equals(newList2.get(i).getIsCount())) {
							if ("1".equals(std.getItemNameType())) {
								HtmlContent.append("<div class='table_div_result'>合计:"
										+ newList2.get(i).getBigSum()
										+ "</div>");
							} else if ("2".equals(std.getItemNameType())) {
								HtmlContent.append("<div class='table_div_result'>Subtotal:"
										+ newList2.get(i).getBigSum()
										+ "</div>");
							}
						}
						HtmlContent.append("</div>");
					}
					HtmlContent.append("</div>");
				}
				if (newList3.size() > 0) {
					HtmlContent
							.append("<div id='tables_body3' class='tables_body' style='display:none'>");
					for (int i = 0; i < newList3.size(); i++) {
						HtmlContent.append("<div class='table_div'>"
								+ "<div class='table_div_title'>"
								+ newList3.get(i).getBigTitle());
						String bigRemarks = "";
						if ("1".equals(std.getBigTitleRemarksIsDisplay())) {
							if ("1".equals(std.getItemNameType())) {
								bigRemarks = newList1.get(i).getBigRemarks() == null ? "没有补充说明信息" : newList3.get(i).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks
										+ "'>");
							} else if ("2".equals(std.getItemNameType())) {
								bigRemarks = newList1.get(i).getBigRemarks() == null ? "No comment" : newList3.get(i).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks
										+ "'>");
							}
						}
						HtmlContent.append("</div>");
						// 处理小标题数据
						List<TempletSalaryItemInfo> itemList = new ArrayList<TempletSalaryItemInfo>();
						for (int k = 0; k < std.getSecondItemData().size(); k++) {
							if (std.getSecondItemData().get(k).getItemBelongPosition().intValue() == newList3
									.get(i).getBigTitlePosition().intValue()) {
								if (std.getSecondItemData().get(k).getItemIsShow() == 0) {
									if (std.getSecondItemData().get(k).getItemTitle() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemTitle())
											|| std.getSecondItemData().get(k).getItemValue() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.00".equals(std.getSecondItemData().get(k)
													.getItemValue())) {
										continue;
									}
									itemList.add(std.getSecondItemData().get(k));
								} else if (std.getSecondItemData().get(k).getItemTitle() != null
										&& !"".equals(std.getSecondItemData().get(k).getItemTitle())) {
									itemList.add(std.getSecondItemData().get(k));
								}
							}
						}
						// 循环小标题
						if (itemList.size() > 0) {
							HtmlContent.append("<table>");
							int flag = 0;
							for (int m = itemList.size() / 2; m >= 0; m--) {
								if (itemList.size() - flag > 0) {
									HtmlContent.append("<tr>" + "<td><span class='td_left'>"
											+ itemList.get(flag).getItemTitle()
											+ "</span><span class='td_right'>"
											+ itemList.get(flag).getItemValue() + "</span></td>");
									flag++;
									if (itemList.size() - flag > 0) {
										HtmlContent.append("<td><span class='td_left'>"
												+ itemList.get(flag).getItemTitle()
												+ "</span><span class='td_right'>"
												+ itemList.get(flag).getItemValue()
												+ "</span></td>");
										flag++;
									} else {
										HtmlContent
												.append("<td><span class='td_left'></span><span class='td_right'></span></td>");
										flag++;
									}
									HtmlContent.append("</tr>");
								}
							}
							HtmlContent.append("</table>");
						}
						if ("1".equals(newList3.get(i).getIsCount())) {
							if ("1".equals(std.getItemNameType())) {
								HtmlContent.append("<div class='table_div_result'>合计:"
										+ newList3.get(i).getBigSum()
										+ "</div>");
							} else if ("2".equals(std.getItemNameType())) {
								HtmlContent.append("<div class='table_div_result'>Subtotal:"
										+ newList3.get(i).getBigSum()
										+ "</div>");
							}
						}
						HtmlContent.append("</div>");
					}
					HtmlContent.append("</div>");
				}
				if (newList4.size() > 0) {
					HtmlContent
							.append("<div id='tables_body4' class='tables_body' style='display:none'>");
					for (int i = 0; i < newList4.size(); i++) {
						HtmlContent.append("<div class='table_div'>"
								+ "<div class='table_div_title'>"
								+ newList4.get(i).getBigTitle());
						String bigRemarks = "";
						if ("1".equals(std.getBigTitleRemarksIsDisplay())) {
							if ("1".equals(std.getItemNameType())) {
								bigRemarks = newList1.get(i).getBigRemarks() == null ? "没有补充说明信息" : newList4.get(i).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks
										+ "'>");
							} else if ("2".equals(std.getItemNameType())) {
								bigRemarks = newList1.get(i).getBigRemarks() == null ? "No comment" : newList4.get(i).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks
										+ "'>");
							}
						}
						HtmlContent.append("</div>");
						// 处理小标题数据
						List<TempletSalaryItemInfo> itemList = new ArrayList<TempletSalaryItemInfo>();
						for (int k = 0; k < std.getSecondItemData().size(); k++) {
							if (std.getSecondItemData().get(k).getItemBelongPosition().intValue() == newList4
									.get(i).getBigTitlePosition().intValue()) {
								if (std.getSecondItemData().get(k).getItemIsShow() == 0) {
									if (std.getSecondItemData().get(k).getItemTitle() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemTitle())
											|| std.getSecondItemData().get(k).getItemValue() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.00".equals(std.getSecondItemData().get(k)
													.getItemValue())) {
										continue;
									}
									itemList.add(std.getSecondItemData().get(k));
								} else if (std.getSecondItemData().get(k).getItemTitle() != null
										&& !"".equals(std.getSecondItemData().get(k).getItemTitle())) {
									itemList.add(std.getSecondItemData().get(k));
								}
							}
						}
						// 循环小标题
						if (itemList.size() > 0) {
							HtmlContent.append("<table>");
							int flag = 0;
							for (int m = itemList.size() / 2; m >= 0; m--) {
								if (itemList.size() - flag > 0) {
									HtmlContent.append("<tr>"
											+ "<td><span class='td_left'>"
											+ itemList.get(flag).getItemTitle()
											+ "</span><span class='td_right'>"
											+ itemList.get(flag).getItemValue()
											+ "</span></td>");
									flag++;
									if (itemList.size() - flag > 0) {
										HtmlContent.append("<td><span class='td_left'>"
												+ itemList.get(flag).getItemTitle()
												+ "</span><span class='td_right'>"
												+ itemList.get(flag).getItemValue()
												+ "</span></td>");
										flag++;
									} else {
										HtmlContent
												.append("<td><span class='td_left'></span><span class='td_right'></span></td>");
										flag++;
									}
									HtmlContent.append("</tr>");
								}
							}
							HtmlContent.append("</table>");
						}
						if ("1".equals(newList4.get(i).getIsCount())) {
							if ("1".equals(std.getItemNameType())) {
								HtmlContent.append("<div class='table_div_result'>合计:"
										+ newList4.get(i).getBigSum()
										+ "</div>");
							} else if ("2".equals(std.getItemNameType())) {
								HtmlContent.append("<div class='table_div_result'>Subtotal:"
										+ newList4.get(i).getBigSum()
										+ "</div>");
							}
						}
						HtmlContent.append("</div>");
					}
					HtmlContent.append("</div>");
				}
				if (newList5.size() > 0) {
					HtmlContent
							.append("<div id='tables_body5' class='tables_body' style='display:none'>");
					for (int i = 0; i < newList5.size(); i++) {
						HtmlContent.append("<div class='table_div'>"
								+ "<div class='table_div_title'>"
								+ newList5.get(i).getBigTitle());
						String bigRemarks = "";
						if ("1".equals(std.getBigTitleRemarksIsDisplay())) {
							if ("1".equals(std.getItemNameType())) {
								bigRemarks = newList1.get(i).getBigRemarks() == null ? "没有补充说明信息" : newList5.get(i).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks
										+ "'>");
							} else if ("2".equals(std.getItemNameType())) {
								bigRemarks = newList1.get(i).getBigRemarks() == null ? "No comment" : newList5.get(i).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks
										+ "'>");
							}
						}
						HtmlContent.append("</div>");
						// 处理小标题数据
						List<TempletSalaryItemInfo> itemList = new ArrayList<TempletSalaryItemInfo>();
						for (int k = 0; k < std.getSecondItemData().size(); k++) {
							if (std.getSecondItemData().get(k).getItemBelongPosition().intValue() == newList5
									.get(i).getBigTitlePosition().intValue()) {
								if (std.getSecondItemData().get(k).getItemIsShow() == 0) {
									if (std.getSecondItemData().get(k).getItemTitle() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemTitle())
											|| std.getSecondItemData().get(k).getItemValue() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.00".equals(std.getSecondItemData().get(k)
													.getItemValue())) {
										continue;
									}
									itemList.add(std.getSecondItemData().get(k));
								} else if (std.getSecondItemData().get(k).getItemTitle() != null
										&& !"".equals(std.getSecondItemData().get(k).getItemTitle())) {
									itemList.add(std.getSecondItemData().get(k));
								}
							}
						}
						// 循环小标题
						if (itemList.size() > 0) {
							HtmlContent.append("<table>");
							int flag = 0;
							for (int m = itemList.size() / 2; m >= 0; m--) {
								if (itemList.size() - flag > 0) {
									HtmlContent.append("<tr>"
											+ "<td><span class='td_left'>"
											+ itemList.get(flag).getItemTitle()
											+ "</span><span class='td_right'>"
											+ itemList.get(flag).getItemValue()
											+ "</span></td>");
									flag++;
									if (itemList.size() - flag > 0) {
										HtmlContent.append("<td><span class='td_left'>"
												+ itemList.get(flag).getItemTitle()
												+ "</span><span class='td_right'>"
												+ itemList.get(flag).getItemValue()
												+ "</span></td>");
										flag++;
									} else {
										HtmlContent
												.append("<td><span class='td_left'></span><span class='td_right'></span></td>");
										flag++;
									}
									HtmlContent.append("</tr>");
								}
							}
							HtmlContent.append("</table>");
						}
//						if ("1".equals(newList5.get(i).getIsCount())) {
//							if ("1".equals(std.getItemNameType())) {
//								HtmlContent.append("<div class='table_div_result'>合计:"
//										+ newList5.get(i).getBigSum()
//										+ "</div>");
//							} else if ("2".equals(std.getItemNameType())) {
//								HtmlContent.append("<div class='table_div_result'>Subtotal:"
//										+ newList5.get(i).getBigSum()
//										+ "</div>");
//							}
//						}
						HtmlContent.append("</div>");
					}
					HtmlContent.append("</div>");
				}

				HtmlContent.append("</div>" + "</div>");
				HtmlContent.append("<div class='wd_com_right'>"
						+ "<img class='wd_img' src='icons/wd_img.png'>");
				if ("1".equals(std.getItemNameType())) {
					HtmlContent
							.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation1'>基本工资</a>");
					if (newList2.size() > 0) {
						HtmlContent
								.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation2'>年终奖</a>");
					}
					if (newList3.size() > 0) {
						HtmlContent
								.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation3'>股权激励</a>");
					}
					if (newList4.size() > 0) {
						HtmlContent
								.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation4'>经济补偿金</a>");
					}
					if (newList5.size() > 0) {
						HtmlContent
								.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation5'>其他信息</a>");
					}
				} else if ("2".equals(std.getItemNameType())) {
					HtmlContent
							.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation1'>BASIC SALARY</a>");
					if (newList2.size() > 0) {
						HtmlContent
								.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation2'>BONUS</a>");
					}
					if (newList3.size() > 0) {
						HtmlContent
								.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation3'>STOCK</a>");
					}
					if (newList4.size() > 0) {
						HtmlContent
								.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation4'>SUBSIDY</a>");
					}
					if (newList5.size() > 0) {
						HtmlContent
								.append("<a href='javascript:void(0);' class='wdr_tl_a' id='navigation5'>OTHER</a>");
					}
				}
			} else {
				/* 自定义模板开始 */
				HtmlContent.append("<div class='wd_con'>"
						+ "<div class='wd_com_left'>");
				if (std.getSecondData().size() < 23) {
					HtmlContent.append("<div class='wd_column'>");
					if (std.getSecondData().size() > 0) {
						HtmlContent.append("<div class='wd_cln_letter letter_hover'>1"
								+ "<div class='letter_title'>"
								+ std.getSecondData().get(0).getBigTitle()
								+ "</div>"
								+ "</div>");
						if (std.getSecondData().size() > 1) {
							for (int i = 1; i < std.getSecondData().size(); i++) {
								HtmlContent.append("<div class='wd_cln_letter'>"
										+ (i + 1)
										+ "<div class='letter_title'>"
										+ std.getSecondData().get(i).getBigTitle()
										+ "</div>"
										+ "</div>");
							}
						}
					}
					HtmlContent.append("</div>");
				} else {
					HtmlContent.append("<div class='wd_column'>");
					for (int i = 0; i < 24; i++) {
						HtmlContent.append("<div class='wd_cln_letter'>"
								+ (i + 1)
								+ "<div class='letter_title'>"
								+ std.getSecondData().get(i).getBigTitle()
								+ "</div>"
								+ "</div>");
					}
					HtmlContent.append("</div>"
							+ "<div class='wd_column'>");
					for (int i = 24; i < std.getSecondData().size(); i++) {
						HtmlContent.append("<div class='wd_cln_letter1'>"
								+ (i + 1)
								+ "<div class='letter_title'>"
								+ std.getSecondData().get(i).getBigTitle()
								+ "</div>"
								+ "</div>");
					}
					HtmlContent.append("</div>");
				}

				// 基本信息
				HtmlContent.append("<div class='wd_tables'>"
						+ "<div class='wd_tables_top'>"
						+ "<div class='wdt_top_left'>");
				for (int i = 0; i < list2.size(); i++) {
					if ("1901".equals(list2.get(i).getItemCode())) {
						HtmlContent.append("<div class='wdt_tl_word'>"
								+ list2.get(i).getItemValue()
								+ "</div>");
					} else {
						continue;
					}
				}
				if ("1".equals(std.getItemNameType())) {
					HtmlContent.append("<a href='javascript:void(0);' class='wdt_tl_a'>个人详细信息</a>");
				} else if ("2".equals(std.getItemNameType())) {
					HtmlContent
							.append("<a href='javascript:void(0);' class='wdt_tl_a'>personal details</a>");
				}
				HtmlContent.append("</div>");
				if (list2.size() > 0) {
					int flag = 0;

					HtmlContent.append("<div class='wdt_u_info'> <table>");

					for (int j = list2.size() / 2; j >= 0; j--) {
						if (list2.size() - flag > 0) {

							HtmlContent.append("<tr>"
									+ "<td><span class='td_left'>"
									+ list2.get(flag).getTitleValue()
									+ "：</span><span class='td_right'>"
									+ list2.get(flag).getItemValue()
									+ "</span></td>");
							flag++;
							if (list2.size() - flag > 0) {
								HtmlContent.append("<td><span class='td_left'>"
										+ list2.get(flag).getTitleValue()
										+ "：</span><span class='td_right'>"
										+ list2.get(flag).getItemValue()
										+ "</span></td>");
								flag++;
							} else {
								HtmlContent
										.append("<td><span class='td_left'></span><span class='td_right'></span></td>");
								flag++;
							}
							HtmlContent.append("</tr>");
						}
					}
					HtmlContent.append("</table> </div>");
				}
				if (std.getLogoPath() != null) {
					HtmlContent.append("<img src='"
							+ std.getLogoPath().replace("/home/httpd/imgehr.efesco.com", "http://172.16.221.143")
							+ "' class='wg_logo'>");
				}
				HtmlContent.append("</div>");
				// --------------------------------------------------------
				// 开始工资数据-------------------------------------------------- -->

				// -- 大标题循环 -->
				HtmlContent
						.append("<div id='tables_body1' class='tables_body' style='display:block'>");

				if (std.getSecondData().size() > 0) {
					for (int n = 0; n < std.getSecondData().size(); n++) {
						HtmlContent.append("<div class='table_div'>"
								+ "<div class='table_div_title'>" + (n + 1) + "."
								+ std.getSecondData().get(n).getBigTitle());
						String bigRemarks = "";
						if ("1".equals(std.getBigTitleRemarksIsDisplay())) {
							if ("1".equals(std.getItemNameType())) {
								bigRemarks = std.getSecondData().get(n).getBigRemarks() == null ? "没有补充说明信息" : std.getSecondData().get(n).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks + "'>");
							} else if ("2".equals(std.getItemNameType())) {
								bigRemarks = std.getSecondData().get(n).getBigRemarks() == null ? "No comment" : std.getSecondData().get(n).getBigRemarks();
								HtmlContent.append("<img src='icons/qmark.png' title='"
										+ bigRemarks + "'>");
							}
						}
						HtmlContent.append("</div>");
						// -- 处理小标题数据-->

						List<TempletSalaryItemInfo> itemList = new ArrayList<TempletSalaryItemInfo>();
						for (int k = 0; k < std.getSecondItemData().size(); k++) {
							if (std.getSecondItemData().get(k).getItemBelongPosition().intValue() == std
									.getSecondData().get(n).getBigTitlePosition().intValue()) {
								if (std.getSecondItemData().get(k).getItemIsShow() == 0) {
									if (std.getSecondItemData().get(k).getItemTitle() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemTitle())
											|| std.getSecondItemData().get(k).getItemValue() == null
											|| "".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.0".equals(std.getSecondItemData().get(k)
													.getItemValue())
											|| "0.00".equals(std.getSecondItemData().get(k)
													.getItemValue())) {
										continue;
									}
									itemList.add(std.getSecondItemData().get(k));
								} else if (std.getSecondItemData().get(k).getItemTitle() != null
										&& !"".equals(std.getSecondItemData().get(k).getItemTitle())) {
									itemList.add(std.getSecondItemData().get(k));
								}
							}
						}

						// -- 循环小标题 -->
						if (itemList.size() > 0) {
							HtmlContent.append("<table>");

							int flag = 0;
							for (int m = itemList.size() / 2; m >= 0; m--) {
								if (itemList.size() - flag > 0) {
									HtmlContent.append("<tr>" + "<td><span class='td_left'>"
											+ itemList.get(flag).getItemTitle()
											+ "</span><span class='td_right'>"
											+ itemList.get(flag).getItemValue() + "</span></td>");
									flag++;
									if (itemList.size() - flag > 0) {
										HtmlContent.append("<td><span class='td_left'>"
												+ itemList.get(flag).getItemTitle()
												+ "</span><span class='td_right'>"
												+ itemList.get(flag).getItemValue()
												+ "</span></td>");
										flag++;
									} else {
										HtmlContent.append("<td><span class='td_left'></span><span class='td_right'></span></td>");
										flag++;
									}
									HtmlContent.append("</tr>");
								}
							}
							HtmlContent.append("</table>");
						}

						if ("1".equals(std.getSecondData().get(n).getIsCount())) {
							if ("1".equals(std.getItemNameType())) {

								HtmlContent.append("<div class='table_div_result'>合计:"
										+ std.getSecondData().get(n).getBigSum()
										+ "</div>");

							} else if ("2".equals(std.getItemNameType())) {

								HtmlContent.append("<div class='table_div_result'>Subtotal:"
										+ std.getSecondData().get(n).getBigSum()
										+ "</div>");

							}
						}

						HtmlContent.append("</div>");

					}
				}

				HtmlContent.append("</div>");
				// --------------------------------------------------------
				// 结束工资数据-------------------------------------------------- -->
				HtmlContent.append("</div>");
				HtmlContent.append("</div>");
				HtmlContent.append("<div class='wd_com_right'>"
						+ "<img class='wd_img' src='icons/wd_img.png'>");
			}

			String remarks = "";
			if ("1".equals(std.getItemNameType())) {
				remarks = "备注信息";
			}
			if ("2".equals(std.getItemNameType())) {
				remarks = "Remarks";
			}
			String Announcement = std.getFormatInfo().getAnnouncement() == null ?
					"" : std.getFormatInfo().getAnnouncement();
			if ("0".equals(std.getFormatInfo().getIsEpayRemarksDisplay())) {
				remarks = "";
			}
			if (!"".equals(std.getRemarks()) || !"".equals(Announcement)) {
				if ("0".equals(std.getFormatInfo().getIsEpayRemarksDisplay())
						&& "".equals(Announcement)) {
				} else {
					HtmlContent.append("<div class='wdr_bei'>"
							+ remarks
							+ "</div>"
							+ "<div class='wdr_bei_word'>"
							+ "<span>"
							+ std.getRemarks()
							+ "</span><br/>"
							+ "<span>"
							+ Announcement
							+ "</span></div>");
				}
			}

			if ("1".equals(std.getItemNameType())) {
				HtmlContent.append("<div class='wdr_explain'>请遵守员工守则中的规章制度，个人工资信息请注意保密</div>");
			} else if ("2".equals(std.getItemNameType())) {
				HtmlContent
						.append("<div class='wdr_explain'>Please comply with the rules and regulations in the code of staff,"
								+ "personal information, please pay attention to the confidentiality of information</div>");
			}
		    HtmlContent.append("</div>"+
		        "</div>"+
		    "</div>"+
		"</div>"+
		"</body>"+
		/*---------------------------javascript-------------------------------*/
		"<script type='text/javascript' src='jquery-1.8.3.min.js'></script>"+
		"<script type='text/javascript'>"+
		"$(function(){"+
			"var td_left=$('.table_div .td_left');"+
			"td_left.each(function(){"+
				"$(this).next().css('height',$(this).height());"+
				"$(this).next().css('line-height',$(this).height()+'px');"+
			"});"+
		"});"+
		"$('#navigation1').click(function(){"+
			"$('#number1').show();"+
			"$('#number2').hide();"+
			"$('#number3').hide();"+
			"$('#number4').hide();"+
			
			"$('#tables_body1').show();"+
			"$('#tables_body2').hide();"+
			"$('#tables_body3').hide();"+
			"$('#tables_body4').hide();"+
			"$('#tables_body5').hide();"+
			"var td_left=$('.table_div .td_left');"+
			"td_left.each(function(){"+
				"$(this).next().css('height',$(this).height());"+
				"$(this).next().css('line-height',$(this).height()+'px');"+
			"});"+
		"});"+
		
		"$('#navigation2').click(function(){"+
			"$('#number1').hide();"+
			"$('#number2').show();"+
			"$('#number3').hide();"+
			"$('#number4').hide();"+
			
			"$('#tables_body1').hide();"+
			"$('#tables_body2').show();"+
			"$('#tables_body3').hide();"+
			"$('#tables_body4').hide();"+
			"$('#tables_body5').hide();"+
			"var td_left=$('.table_div .td_left');"+
			"td_left.each(function(){"+
				"$(this).next().css('height',$(this).height());"+
				"$(this).next().css('line-height',$(this).height()+'px');"+
			"});"+
		"});"+
		
		"$('#navigation3').click(function(){"+
			"$('#number1').hide();"+
			"$('#number2').hide();"+
			"$('#number3').show();"+
			"$('#number4').hide();"+
			
			"$('#tables_body1').hide();"+
			"$('#tables_body2').hide();"+
			"$('#tables_body3').show();"+
			"$('#tables_body4').hide();"+
			"$('#tables_body5').hide();"+
			"var td_left=$('.table_div .td_left');"+
			"td_left.each(function(){"+
				"$(this).next().css('height',$(this).height());"+
				"$(this).next().css('line-height',$(this).height()+'px');"+
			"});"+
		"});"+
		
		"$('#navigation4').click(function(){"+
			"$('#number1').hide();"+
			"$('#number2').hide();"+
			"$('#number3').hide();"+
			"$('#number4').show();"+
			
			"$('#tables_body1').hide();"+
			"$('#tables_body2').hide();"+
			"$('#tables_body3').hide();"+
			"$('#tables_body4').show();"+
			"$('#tables_body5').hide();"+
			"var td_left=$('.table_div .td_left');"+
			"td_left.each(function(){"+
				"$(this).next().css('height',$(this).height());"+
				"$(this).next().css('line-height',$(this).height()+'px');"+
			"});"+
		"});"+
		
		"$('#navigation5').click(function(){"+
			"$('#number1').hide();"+
			"$('#number2').hide();"+
			"$('#number3').hide();"+
			"$('#number4').hide();"+
			
			"$('#tables_body1').hide();"+
			"$('#tables_body2').hide();"+
			"$('#tables_body3').hide();"+
			"$('#tables_body4').hide();"+
			"$('#tables_body5').show();"+
			"$('#tables_body5').css('width','550px');"+
			"var td_left=$('.table_div .td_left');"+
			"td_left.each(function(){"+
				"$(this).next().css('height',$(this).height());"+
				"$(this).next().css('line-height',$(this).height()+'px');"+
			"});"+
		"});");
		if("1".equals(std.getItemNameType())){
			HtmlContent.append("$('.wdt_tl_a').click(function(){"+
	        "if($(this).attr('data-vl')==1){"+
	           "$('.wdt_u_info').slideUp();"+
	            "$(this).removeAttr('data-vl').html('个人详细信息');"+
	        "}else{"+
	            "$(this).attr('data-vl',1).html('收起');"+
	            "$('.wdt_u_info').slideDown();"+
	        "}"+
	    "});");
		}else if("2".equals(std.getItemNameType())){
			HtmlContent.append("$('.wdt_tl_a').click(function(){"+
	        "if($(this).attr('data-vl')==1){"+
	           "$('.wdt_u_info').slideUp();"+
	            "$(this).removeAttr('data-vl').html('personal details');"+
	        "}else{"+
	            "$(this).attr('data-vl',1).html('give up');"+
	            "$('.wdt_u_info').slideDown();"+
	        "}"+
		"});");
		}
		HtmlContent.append("$('#d_close').click(function(){"+
	       "$('.dialog_wage').hide();"+
	    "});"+

	    "$('.wd_cln_letter').hover(function () {"+
	        "$(this).find('.letter_title').show();"+
	    "}, function () {"+
	        "$(this).find('.letter_title').hide();"+
	    "});"+
	    "$('.wd_cln_letter1').hover(function () {"+
	        "$(this).find('.letter_title').show();"+
	    "}, function () {"+
	        "$(this).find('.letter_title').hide();"+
	    "});"+
	    "$('.wd_cln_letter').click(function () {"+
	        "if (!$(this).hasClass('letter_hover')) {"+
	            "$('.letter_hover').removeClass('letter_hover');"+
	            "$(this).addClass('letter_hover');"+
	            "var index = $(this).index();"+
				"if($('#tables_body1').is(':visible')){"+
					"var tables = $('#tables_body1 .table_div');"+
	            	"if (index <= tables.length) {"+
	                "var top = 0;"+
	                "tables.each(function (n, o) {"+
	                    "if (index > n) {"+
	                        "top += $(o).height();"+
	                    "}"+
	                "});"+
	                "$('#tables_body1').animate({"+
	                    "scrollTop: top"+
	                "}, function () {"+
	                    "console.log('滚动了')"+
	                "});"+
	            	"}"+
				"}"+
				
				"if($('#tables_body2').is(':visible')){"+
					"var tables = $('#tables_body2 .table_div');"+
	            	"if (index <= tables.length) {"+
	                "var top = 0;"+
	                "tables.each(function (n, o) {"+
	                    "if (index > n) {"+
	                        "top += $(o).height();"+
	                    "}"+
	                "});"+
	                "$('#tables_body2').animate({"+
	                    "scrollTop: top"+
	                "}, function () {"+
	                   "console.log('滚动了')"+
	                "});"+
	            	"}"+
				"}"+
				
				"if($('#tables_body3').is(':visible')){"+
					"var tables = $('#tables_body3 .table_div');"+
	            	"if (index <= tables.length) {"+
	                "var top = 0;"+
	               " tables.each(function (n, o) {"+
	                    "if (index > n) {"+
	                        "top += $(o).height();"+
	                    "}"+
	                "});"+
	                "$('#tables_body3').animate({"+
	                    "scrollTop: top"+
	                "}, function () {"+
	                    "console.log('滚动了')"+
	                "});"+
	            	"}"+
				"}"+
				
				"if($('#tables_body4').is(':visible')){"+
					"var tables = $('#tables_body4 .table_div');"+
	            	"if (index <= tables.length) {"+
	                "var top = 0;"+
	                "tables.each(function (n, o) {"+
	                    "if (index > n) {"+
	                        "top += $(o).height();"+
	                    "}"+
	                "});"+
	                "$('#tables_body4').animate({"+
	                    "scrollTop: top"+
	                "}, function () {"+
	                    "console.log('滚动了')"+
	                "});"+
	            	"}"+
				"}"+
				
				"if($('#tables_body5').is(':visible')){"+
					"var tables = $('#tables_body5 .table_div');"+
	            	"if (index <= tables.length) {"+
	                "var top = 0;"+
	                "tables.each(function (n, o) {"+
	                    "if (index > n) {"+
	                        "top += $(o).height();"+
	                    "}"+
	                "});"+
	                "$('#tables_body5').animate({"+
	                    "scrollTop: top"+
	                "}, function () {"+
	                    "console.log('滚动了')"+
	                "});"+
	            	"}"+
				"}"+
				
	        "}"+
	    "});"+
	    "$('.wd_cln_letter1').click(function () {"+
	        "if (!$(this).hasClass('letter_hover')) {"+
	            "$('.letter_hover').removeClass('letter_hover');"+
	            "$(this).addClass('letter_hover');"+
	            "var index = $(this).index()+25;"+
				"if($('#tables_body1').is(':visible')){"+
					"var tables = $('#tables_body1 .table_div');"+
	            	"if (index <= tables.length) {"+
	                "var top = 0;"+
	                "tables.each(function (n, o) {"+
	                    "if (index > n) {"+
	                        "top += $(o).height();"+
	                    "}"+
	                "});"+
	                "$('#tables_body1').animate({"+
	                    "scrollTop: top"+
	                "}, function () {"+
	                    "console.log('滚动了');"+
	                "});"+
	            	"}"+
				"}"+
				
	        "}"+
	    "});"+
	    "</script>"+
	/*---------------------------javascript-------------------------------*/
		"</html>");
		}
		try {
			printWriter = new PrintWriter(file, "utf-8");
			printWriter.print(HtmlContent.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}

		return HTMLFileName;
	}

	//测试
	public static void main(String[] args) {
		List<SalaryTempletData> dealSalaryInfo=new ArrayList<SalaryTempletData>();
		SalaryTempletData std=new SalaryTempletData();
		std.setItemNameType("1");
		//模板
		FormatInfo formatInfo=new FormatInfo();
		formatInfo.setAnnouncement("HTML公告文字！");
		formatInfo.setFormatType("2");
		formatInfo.setIsEpayRemarksDisplay("1");
		formatInfo.setRemarks("导航备注");
		formatInfo.setSalaryPeriodDisplayName("薪资期间");
		formatInfo.setSalaryPeriodIsDisplay("1");
		formatInfo.setSendDateDisplayName("发送日期");
		formatInfo.setSendDateIsDisplay("1");
		std.setFormatInfo(formatInfo);
		//基本信息
		List<TempletBaseInfo> firstData = new ArrayList<TempletBaseInfo>();
		firstData.add(new TempletBaseInfo("基本信息1", "基本信息1", 1, 1, "12"));
		firstData.add(new TempletBaseInfo("基本信息2", "基本信息2", 1, 2, "12"));
		firstData.add(new TempletBaseInfo("基本信息3", "基本信息3", 0, 2, "12"));
		firstData.add(new TempletBaseInfo("基本信息4", "基本信息4", 1, 2, "12"));
		firstData.add(new TempletBaseInfo("基本信息5", "基本信息5", 1, 2, "12"));
		firstData.add(new TempletBaseInfo("基本信息6", "基本信息6", 0, 6, "12"));
		firstData.add(new TempletBaseInfo("基本信息7", "基本信息7", 0, 6, "12"));
		firstData.add(new TempletBaseInfo("基本信息8", "基本信息8", 0, 6, "12"));
		firstData.add(new TempletBaseInfo("基本信息9", "基本信息9", 1, 6, "12"));
		firstData.add(new TempletBaseInfo("基本信息10", "基本信息10", 1, 6, "12"));
		std.setfirstData(firstData);
		//标题信息
		List<TempletSalaryInfo> secondData = new ArrayList<TempletSalaryInfo>();
		secondData.add(new TempletSalaryInfo(1, "大标题测试数据1", "大标题备注信息1", "88.88","1"));
		secondData.add(new TempletSalaryInfo(2, "大标题测试数据2", "大标题备注信息2", "88.88","1"));
		secondData.add(new TempletSalaryInfo(3, "大标题测试数据3", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(4, "大标题测试数据4", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(5, "大标题测试数据5", "大标题备注信息3", "88.88","0"));
		secondData.add(new TempletSalaryInfo(6, "大标题测试数据6", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(7, "大标题测试数据7", "大标题备注信息3", "88.88","0"));
		secondData.add(new TempletSalaryInfo(8, "大标题测试数据8", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(9, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(10, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(11, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(12, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(13, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(14, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(15, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(16, "大标题测试数据9", "大标题备注信息3", "0.00","1"));
		secondData.add(new TempletSalaryInfo(17, "大标题测试数据9", "大标题备注信息3", "0.00","1"));
		secondData.add(new TempletSalaryInfo(18, "大标题测试数据18", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(19, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(20, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(21, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(22, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(23, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(24, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(25, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(26, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(27, "大标题测试数据27", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(28, "大标题测试数据9", "大标题备注信息3", "88.88","1"));
		secondData
				.add(new TempletSalaryInfo(29, "大标题测试数据10", "大标题备注信息3", "88.88","1"));
		secondData.add(new TempletSalaryInfo(36, "大标题测试数据36", "大标题备注信息36",
				"88.88","1"));
		secondData
				.add(new TempletSalaryInfo(37, "大标题测试数据37", "大标题备注信息37", "88.88","1"));
		std.setSecondData(secondData);
		//工资信息
		List<TempletSalaryItemInfo> secondItemData = new ArrayList<TempletSalaryItemInfo>();
		secondItemData.add(new TempletSalaryItemInfo("测试数据1", "8888888", 1, 1,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据2", "8888888", 1, 1,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据3", "8888888", 1, 1,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据4", "8888888", 1, 1,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据5", "8888888", 1, 1,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据6", "8888888", 1, 1,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据7", "8888888", 1, 1,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据8", "8888888", 1, 1,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据8", "8888888", 1, 2,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据8", "8888888", 1, 2,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据8", "8888888", 1, 2,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据8", "8888888", 1, 2,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据8", "8888888", 1, 2,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据8", "8888888", 1, 2,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("测试数据9", "8888888", 1, 3,
				"1", 1));
		secondItemData.add(new TempletSalaryItemInfo("其他信息", "其他信息", 1, 37,
				"1", 1));
		std.setSecondItemData(secondItemData);
		//发送日期、薪资期间
		std.setDatePeriod("2018/08/08-2018/08/08");
		std.setSendDate("2018/08/08");
		//备注、中英文、标题备注是否显示、图片地址
		std.setRemarks("只因为在人群中多看了你一眼");
		std.setItemNameType("1");
		std.setBigTitleRemarksIsDisplay("1");
		std.setLogoPath("/home/httpd/imgehr.efesco.com/wf_salaryMail/CompanyLogo/HK4472_logo_.jpg");
		dealSalaryInfo.add(std);
		generateHtmlSalaryFile(dealSalaryInfo, "test");
	}
}
