package com.salaryMail.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.salaryMail.entity.FormatInfo;
import com.salaryMail.entity.SalaryTempletData;
import com.salaryMail.entity.TempletBaseInfo;
import com.salaryMail.entity.TempletSalaryItemInfo;

public class Excell07FreeTemplet {
	
	/**
	 * 上海外服工资模板（自定义）
	 */
	@SuppressWarnings("deprecation")
	public static String sfscExcellPay(List<SalaryTempletData> dealSalaryInfo,
			String fileName,String imagePath, int m) {

		// String path = System.getProperty("user.dir");
		// String filePath = path + "\\" + fileName + ".xls";
		FileOutputStream fileOut = null;

		XSSFWorkbook wb = new XSSFWorkbook();// 创建工作表

		XSSFSheet sheet = wb.createSheet();// 创建工作簿
		sheet.setDisplayGridlines(false);
		XSSFFont baseFont=wb.createFont();
		baseFont.setFontHeightInPoints((short)10);
		XSSFFont itemFont=wb.createFont();
		itemFont.setFontHeightInPoints((short)8);
		wb.setSheetName(0, "PayList");

		// 设置各列的宽度
		sheet.setColumnWidth(0, 5500);
		sheet.setColumnWidth(1, 5500);
		sheet.setColumnWidth(2, 0);
		sheet.setColumnWidth(3, 5500);
		sheet.setColumnWidth(4, 5500);

		// 第一部分（公司logo及工资发放日期、薪资期间）
		int irow = 0;
		XSSFRow row = sheet.createRow(irow);
		row.setHeight((short) 500);
		XSSFCellStyle oneStyle = wb.createCellStyle();
		oneStyle.setFont(baseFont);
		oneStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		
		XSSFCellStyle oneStyle2 = wb.createCellStyle();
		XSSFFont companyNameFont=wb.createFont();
		companyNameFont.setFontHeightInPoints((short) 12);
		companyNameFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		companyNameFont.setColor(HSSFColor.SEA_GREEN.index);
		companyNameFont.setFontName("宋体");
		oneStyle2.setFont(companyNameFont);
		oneStyle2.setWrapText(true);
		oneStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		oneStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
		
		XSSFCell cell;
		cell = row.createCell(0);
		cell.setCellStyle(oneStyle);
		cell.setCellValue("");
		cell = row.createCell(1);
		cell.setCellStyle(oneStyle);
		cell.setCellValue("");
		cell = row.createCell(2);
		cell.setCellStyle(oneStyle);
		cell.setCellValue("");
		cell = row.createCell(3);
		cell.setCellStyle(oneStyle);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("");
		cell = row.createCell(4);
		cell.setCellStyle(oneStyle);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("");
		irow++;

		row = sheet.createRow(irow);
		row.setHeight((short) 500);
		cell = row.createCell(0);
		cell.setCellStyle(oneStyle);
		cell.setCellValue("");
		cell = row.createCell(1);
		cell.setCellStyle(oneStyle);
		cell.setCellValue("");
		cell = row.createCell(2);
		cell.setCellStyle(oneStyle);
		cell.setCellValue("");
		cell = row.createCell(3);
		cell.setCellStyle(oneStyle);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("");
		cell = row.createCell(4);
		cell.setCellStyle(oneStyle);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("");
		irow++;
		
		//添加图片下公司名字
//		String compName="中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中";
		String compName="";
		if("1".equals(dealSalaryInfo.get(m).getIsCompnameDisplay())){
			if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
				compName=dealSalaryInfo.get(m).getCompanyNameCh();
			}
			if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
				compName=dealSalaryInfo.get(m).getCompanyNameEn();
			}
			row = sheet.createRow(irow);
			row.setHeight((short) 400);
			cell = row.createCell(0);
			cell.setCellStyle(oneStyle2);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(compName);
			cell = row.createCell(1);
			cell.setCellStyle(oneStyle2);
			cell.setCellValue("");
			cell = row.createCell(2);
			cell.setCellStyle(oneStyle2);
			cell.setCellValue("");
			cell = row.createCell(3);
			cell.setCellStyle(oneStyle2);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("");
			cell = row.createCell(4);
			cell.setCellStyle(oneStyle2);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("");
			sheet.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(irow, irow, 0, 4));
			irow++;
		}
		
		// 加入数据
		FormatInfo formatInfo = dealSalaryInfo.get(m).getFormatInfo();
		//发送日期、薪资期间
		  if ("1".equals(formatInfo.getSendDateIsDisplay())) {
			  if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
				  sheet.getRow(0).getCell(3)
				  .setCellValue(formatInfo.getSendDateDisplayName());
			  }
			  if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
				  sheet.getRow(0).getCell(3)
				  .setCellValue("Pay Day");
			  }
		   sheet.getRow(0).getCell(4)
		     .setCellValue(dealSalaryInfo.get(m).getSendDate());
		   if ("1".equals(formatInfo.getSalaryPeriodIsDisplay())) {
			   if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
				   sheet.getRow(1).getCell(3)
				   .setCellValue(formatInfo.getSalaryPeriodDisplayName());
			   }
			   if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
				   sheet.getRow(1).getCell(3)
				   .setCellValue("Salary Period");
			   }
		    sheet.getRow(1).getCell(4)
		      .setCellValue(dealSalaryInfo.get(m).getDatePeriod());
		   }
		  } else {
		   if ("1".equals(formatInfo.getSalaryPeriodIsDisplay())) {
			   if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
				   sheet.getRow(0).getCell(3)
				   .setCellValue(formatInfo.getSalaryPeriodDisplayName());
			   }
			   if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
				   sheet.getRow(0).getCell(3)
				   .setCellValue("Salary Period");
			   }
		    sheet.getRow(0).getCell(4)
		      .setCellValue(dealSalaryInfo.get(m).getDatePeriod());
		   }
		  }
		// 插入图片
		try {
//			String path = System.getProperty("user.dir");
//			String distName="distName";
//			String distImgPath = path + "\\" + distName + ".jpg";
//			
//			resizeImage(imagePath, distImgPath, 152, 48);
			File picture = new File(imagePath);
			   BufferedImage image=ImageIO.read(new FileInputStream(picture));
			   
			   FileInputStream fis = new FileInputStream(imagePath);
			   byte[] bytes = IOUtils.toByteArray(fis);
			   int pictureIdx = wb.addPicture(bytes, wb.PICTURE_TYPE_JPEG);
			   fis.close();
			   
			   Drawing drawing = sheet.createDrawingPatriarch();
			   CreationHelper helper = wb.getCreationHelper();
			   ClientAnchor anchor = helper.createClientAnchor();
			   if(image.getHeight()>50){
			    anchor.setCol1(0);
			    anchor.setRow1(0);
			   }else{
			    anchor.setCol1(0);
			    anchor.setRow1(1);
			   }
			   Picture pict = drawing.createPicture(anchor, pictureIdx);
			   pict.resize();//该方法只支持JPEG 和 PNG后缀文件
		} catch (FileNotFoundException e1) {
		} catch (IOException e1) {
		}

		// 第二部分（各类人员信息）
		XSSFCellStyle twoStyle = wb.createCellStyle();
		twoStyle.setFont(baseFont);
		twoStyle.setWrapText(true);
		twoStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
		twoStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);

		// 加入数据
		List<TempletBaseInfo> list1 = dealSalaryInfo.get(m).getfirstData();
		List<TempletBaseInfo> list2 = new ArrayList<TempletBaseInfo>();
		TempletBaseInfo tbi = null;
		for (int j = 0; j < list1.size(); j++) {
			if (list1.get(j).getIsShow() == 1) {
				if(list1.get(j).getTitleValue()!= null&&!"null".equals(list1.get(j).getTitleValue())) {
					tbi = list1.get(j);
					list2.add(tbi);
				}
			} else {
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
		// 按格式输出
		int flag = 0;
		if (list2.size() > 0) {
			for (int k = list2.size() / 2; k >= 0; k--) {
				row = sheet.createRow(irow);
				row.setHeight((short) 500);
				if (list2.size() - flag > 0) {
					cell = row.createCell(0);
					cell.setCellStyle(twoStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(list2.get(flag).getTitleValue());
					cell = row.createCell(1);
					cell.setCellStyle(twoStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(list2.get(flag).getItemValue());
					cell = row.createCell(2);
					cell.setCellStyle(twoStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					flag++;
					if (list2.size() - flag > 0) {
						cell = row.createCell(3);
						cell.setCellStyle(twoStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(list2.get(flag).getTitleValue());
						cell = row.createCell(4);
						cell.setCellStyle(twoStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(list2.get(flag).getItemValue());
						flag++;
					} else {
						cell = row.createCell(3);
						cell.setCellStyle(twoStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("");
						cell = row.createCell(4);
						cell.setCellStyle(twoStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("");
						flag++;
					}
					irow++;
				}
			}
		}
		//基本信息和工资部分添加空白线
		row = sheet.createRow(irow);
		row.setHeight((short) 300);
		irow++;
		
		// 第三部分
		XSSFCellStyle threeStyle1 = wb.createCellStyle();
		// 标题样式
		threeStyle1.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
		threeStyle1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		threeStyle1.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		XSSFFont font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		font.setFontHeightInPoints((short) 13);
		threeStyle1.setFont(font);
		//分割线样式
		XSSFCellStyle threeStyle4=wb.createCellStyle();
		threeStyle4.setBorderBottom((short)1);
		threeStyle4.setBottomBorderColor(HSSFColor.GREY_25_PERCENT.index);
		// 项目样式
		XSSFCellStyle threeStyle2 = wb.createCellStyle();
		threeStyle2.setFont(itemFont);
		threeStyle2.setWrapText(true);
		threeStyle2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 合计样式
		XSSFCellStyle threeStyle3 = wb.createCellStyle();
		threeStyle3.setWrapText(true);
		threeStyle3.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		threeStyle3.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
		threeStyle3.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		threeStyle3.setFont(font);

		irow = bigTitileList(sheet, row, cell, threeStyle1, threeStyle2,
				threeStyle3,threeStyle4, dealSalaryInfo, irow, m);

		// 第四部分（epay的备注信息）
		XSSFCellStyle fourStyle1 = wb.createCellStyle();
		fourStyle1.setFont(itemFont);
		fourStyle1.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
		fourStyle1.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		fourStyle1.setWrapText(true);
		fourStyle1.setBorderTop(XSSFCellStyle.BORDER_THIN);
		fourStyle1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		fourStyle1.setBorderRight(XSSFCellStyle.BORDER_THIN);
		fourStyle1.setBorderBottom(XSSFCellStyle.BORDER_THIN);

		String remarks="";
		if("1".equals(dealSalaryInfo.get(m).getItemNameType())){
			remarks = "备注信息：\n"+ dealSalaryInfo.get(m).getRemarks()+ "\n";
		}
		if("2".equals(dealSalaryInfo.get(m).getItemNameType())){
			remarks = "Remarks：\n"+ dealSalaryInfo.get(m).getRemarks()+ "\n";
		}
		String Announcement = dealSalaryInfo.get(m).getFormatInfo().getAnnouncement()==null?"":dealSalaryInfo.get(m).getFormatInfo().getAnnouncement();
		if("0".equals(dealSalaryInfo.get(m).getFormatInfo().getIsEpayRemarksDisplay())){
			remarks="";
		}
		if(!StringUtils.isEmpty(dealSalaryInfo.get(m).getRemarks())||!StringUtils.isEmpty(Announcement)){
            if("0".equals(dealSalaryInfo.get(m).getFormatInfo().getIsEpayRemarksDisplay())
            		&&StringUtils.isEmpty(Announcement)){
			}else{
				row = sheet.createRow(irow);
				cell = row.createCell(0);
				cell.setCellStyle(fourStyle1);
				String str = remarks + Announcement;
				cell.setCellValue(str);
				cell = row.createCell(1);
				cell.setCellStyle(fourStyle1);
				cell.setCellValue("");
				cell = row.createCell(2);
				cell.setCellStyle(fourStyle1);
				cell.setCellValue("");
				cell = row.createCell(3);
				cell.setCellStyle(fourStyle1);
				cell.setCellValue("");
				cell = row.createCell(4);
				cell.setCellStyle(fourStyle1);
				cell.setCellValue("");
				sheet.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(irow, irow, 0, 4));
				sheet.getRow(sheet.getLastRowNum()).setHeightInPoints(
						getExcelCellAutoHeight(str, 40f));
				irow++;
			}
		}

		try {
			fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileName;
	}

	// 设置行高自适应
	public static float getExcelCellAutoHeight(String str, float fontCountInline) {
		float defaultRowHeight = 12.00f;// 每一行的高度指定
		float defaultCount = 0.00f;
		for (int i = 0; i < str.length(); i++) {
			float ff = getregex(str.substring(i, i + 1));
			defaultCount = defaultCount + ff;
		}
		return ((int) (defaultCount / fontCountInline) + 4) * defaultRowHeight;// 计算
	}

	public static float getregex(String charStr) {

		if (charStr == " ") {
			return 0.5f;
		}
		// 判断是否为字母或字符
		if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
			return 0.5f;
		}
		// 判断是否为全角
		if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
			return 1.00f;
		}
		// 全角符号及中文
		if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
			return 1.00f;
		}
		return 0.5f;
	}

	/**
	 * 标题项目循环方法
	 */
	public static int bigTitileList(XSSFSheet sheet, XSSFRow row,
			XSSFCell cell, XSSFCellStyle threeStyle1,
			XSSFCellStyle threeStyle2, XSSFCellStyle threeStyle3,XSSFCellStyle threeStyle4,
			List<SalaryTempletData> list, int irow, int m) {
		for (int i = 0; i < list.get(m).getSecondData().size(); i++) {
			// 大标题
			row = sheet.createRow(irow);
			cell = row.createCell(0);
			cell.setCellStyle(threeStyle1);
			cell.setCellValue(list.get(m).getSecondData().get(i).getBigTitle());
			cell = row.createCell(1);
			cell.setCellStyle(threeStyle1);
			cell.setCellValue("");
			cell = row.createCell(2);
			cell.setCellStyle(threeStyle1);
			cell.setCellValue("");
			cell = row.createCell(3);
			cell.setCellStyle(threeStyle3);
			if("1".equals(list.get(m).getSecondData().get(i).getIsCount())){
				if("1".equals(list.get(m).getItemNameType())){
					cell.setCellValue("合计："+ list.get(m).getSecondData().get(i).getBigSum().toString());
				}
				if("2".equals(list.get(m).getItemNameType())){
					cell.setCellValue("Subtotal："+ list.get(m).getSecondData().get(i).getBigSum().toString());
				}
			}else{
				cell.setCellValue("");
			}
			cell = row.createCell(4);
			cell.setCellStyle(threeStyle3);
			cell.setCellValue("");
			// 合并单元格
			sheet.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(irow, irow, 0, 2));
			sheet.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(irow, irow, 3, 4));
			irow++;
			/*if (list.get(m).getSecondData().get(i).getBigTitlePosition() % 9 == 0) {
				// 添加空白线
				row = sheet.createRow(irow);
				row.setHeight((short) 80);
				cell = row.createCell(0);
				cell.setCellStyle(threeStyle2);
				cell.setCellValue("");
				cell = row.createCell(1);
				cell.setCellStyle(threeStyle2);
				cell.setCellValue("");
				cell = row.createCell(2);
				cell.setCellStyle(threeStyle2);
				cell.setCellValue("");
				cell = row.createCell(3);
				cell.setCellStyle(threeStyle2);
				cell.setCellValue("");
				cell = row.createCell(4);
				cell.setCellStyle(threeStyle2);
				cell.setCellValue("");
				irow++;
			}*/
			irow = openList(sheet, row, cell, threeStyle1, threeStyle2,threeStyle4, list,
					irow, list.get(m).getSecondData().get(i)
							.getBigTitlePosition(), m);
		}
		return irow;
	}

	/**
	 * 展开项循环的方法
	 */
	public static int openList(XSSFSheet sheet, XSSFRow row, XSSFCell cell,
			XSSFCellStyle threeStyle1, XSSFCellStyle threeStyle2,XSSFCellStyle threeStyle4,
			List<SalaryTempletData> list, int irow, int belongTo, int m) {
		List<TempletSalaryItemInfo> newList = new ArrayList<TempletSalaryItemInfo>();
		for (int k = 0; k < list.get(m).getSecondItemData().size(); k++) {
			if (list.get(m).getSecondItemData().get(k).getItemBelongPosition() == belongTo) {
				if (list.get(m).getSecondItemData().get(k).getItemIsShow() == 0) {
					if (list.get(m).getSecondItemData().get(k).getItemTitle() == null
							| "null".equals(list.get(m).getSecondItemData()
									.get(k).getItemTitle())
							| list.get(m).getSecondItemData().get(k)
									.getItemValue() == null
							| "null".equals(list.get(m).getSecondItemData()
									.get(k).getItemValue())
							| "0".equals(list.get(m).getSecondItemData()
									.get(k).getItemValue())
							| "0.0".equals(list.get(m).getSecondItemData()
									.get(k).getItemValue())
							| "0.00".equals(list.get(m).getSecondItemData()
									.get(k).getItemValue())) {
						continue;
					}
					newList.add(list.get(m).getSecondItemData().get(k));
				} else if (list.get(m).getSecondItemData().get(k)
						.getItemTitle() != null
						&& !"null".equals(list.get(m).getSecondItemData()
								.get(k).getItemTitle())) {
					newList.add(list.get(m).getSecondItemData().get(k));
				}
			}
		}
		if (newList.size() > 0) {
			int index = newList.size();
			int multiple = index / 4 + (index % 4 > 0 ? 1 : 0);
			int flag1 = 0;
			int flag2 = 0;
			for (int i = multiple; i > 0; i--) {
				row = sheet.createRow(irow);
				row.setHeight((short) 450);
				if (index - flag1 > 0) {
					cell = row.createCell(0);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue(newList.get(flag1++).getItemTitle());
				} else {
					cell = row.createCell(0);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue("");
				}
				if (index - flag1 > 0) {
					cell = row.createCell(1);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue(newList.get(flag1++).getItemTitle());
				} else {
					cell = row.createCell(1);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue("");
				}
				cell = row.createCell(2);
				cell.setCellStyle(threeStyle2);
				cell.setCellValue("");
				if (index - flag1 > 0) {
					cell = row.createCell(3);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue(newList.get(flag1++).getItemTitle());
				} else {
					cell = row.createCell(3);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue("");
				}
				if (index - flag1 > 0) {
					cell = row.createCell(4);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue(newList.get(flag1++).getItemTitle());
				} else {
					cell = row.createCell(4);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue("");
				}
				sheet.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(irow, irow, 1, 2));
				irow++;
				row = sheet.createRow(irow);
				row.setHeight((short) 450);
				if (index - flag2 > 0) {
					cell = row.createCell(0);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue(newList.get(flag2++).getItemValue());
				} else {
					cell = row.createCell(0);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue("");
				}
				if (index - flag2 > 0) {
					cell = row.createCell(1);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue(newList.get(flag2++).getItemValue());
				} else {
					cell = row.createCell(1);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue("");
				}
				cell = row.createCell(2);
				cell.setCellStyle(threeStyle2);
				cell.setCellValue("");
				if (index - flag2 > 0) {
					cell = row.createCell(3);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue(newList.get(flag2++).getItemValue());
				} else {
					cell = row.createCell(3);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue("");
				}
				if (index - flag2 > 0) {
					cell = row.createCell(4);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue(newList.get(flag2++).getItemValue());
				} else {
					cell = row.createCell(4);
					cell.setCellStyle(threeStyle2);
					cell.setCellValue("");
				}
				sheet.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(irow, irow, 1, 2));
				irow++;
				if (i > 1) {
					// 分割线
					row = sheet.createRow(irow);
					row.setHeight((short) 15);
					cell = row.createCell(0);
					cell.setCellStyle(threeStyle4);
					cell.setCellValue("");
					cell = row.createCell(1);
					cell.setCellStyle(threeStyle4);
					cell.setCellValue("");
					cell = row.createCell(2);
					cell.setCellStyle(threeStyle4);
					cell.setCellValue("");
					cell = row.createCell(3);
					cell.setCellStyle(threeStyle4);
					cell.setCellValue("");
					cell = row.createCell(4);
					cell.setCellStyle(threeStyle4);
					cell.setCellValue("");
					irow++;
				}
			}
		}
		return irow;
	}

	 /**
	  * 调整图片大小
	  * @param srcImgPath
	  * @param distImgPath
	  * @param width
	  * @param height
	  * @throws IOException
	  */
	 public static void resizeImage(String srcImgPath, String distImgPath,  
	            int width, int height) throws IOException {  
	  
	        File srcFile = new File(srcImgPath);  
	        Image srcImg = ImageIO.read(srcFile);  
	        BufferedImage buffImg = null;  
	        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
	        buffImg.getGraphics().drawImage(  
	                srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,  
	                0, null);  
	  
	        ImageIO.write(buffImg, "JPEG", new File(distImgPath));  
	  
	  }
}
