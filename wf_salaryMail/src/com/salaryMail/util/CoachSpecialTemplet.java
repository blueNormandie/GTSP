package com.salaryMail.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.salaryMail.entity.CoachDeductSalary;
import com.salaryMail.entity.CoachEarnSalary;
import com.salaryMail.entity.CoachSalary;

public class CoachSpecialTemplet {
	
	/**
	 * 蔻驰公司个性化工资单模板
	 */
	public static String buildTemplet(CoachSalary coachSalary,String fileName){
		FileOutputStream fileOut=null;
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet();
		wb.setSheetName(0, "coachPayList");
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 2000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 3700);
		sheet.setColumnWidth(4, 3700);
		sheet.setColumnWidth(5, 8000);
		sheet.setColumnWidth(6, 2000);
		sheet.setColumnWidth(7, 5000);
		sheet.setDisplayGridlines(false);
		int irow=0;
		//第一行logo
		HSSFRow row=sheet.createRow(irow);
		row.setHeight((short)1300);
		irow++;
		// 插入图片
		/*try {
			FileInputStream fis = new FileInputStream(imagePath);
			byte[] bytes = IOUtils.toByteArray(fis);
			int pictureIdx = wb.addPicture(bytes, wb.PICTURE_TYPE_JPEG);
			fis.close();
			
			Drawing drawing = sheet.createDrawingPatriarch();
			CreationHelper helper = wb.getCreationHelper();
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setCol1(3);
			anchor.setRow1(0);
		    Picture pict = drawing.createPicture(anchor, pictureIdx);
		    pict.resize();//该方法只支持JPEG 和 PNG后缀文件
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		//样式
		HSSFFont baseFont=wb.createFont();
		baseFont.setFontHeightInPoints((short) 10);
		baseFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFFont itemFont=wb.createFont();
		itemFont.setFontHeightInPoints((short) 10);
		itemFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		
		HSSFCellStyle Style1=wb.createCellStyle();
		Style1.setFont(baseFont);
		Style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFCellStyle Style2=wb.createCellStyle();
		Style2.setFont(baseFont);
		Style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		
		HSSFCellStyle Style3=wb.createCellStyle();
		Style3.setFont(baseFont);
		Style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		Style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFCellStyle Style4=wb.createCellStyle();
		Style4.setFont(baseFont);
		Style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		Style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		HSSFCellStyle Style5=wb.createCellStyle();
		Style5.setFont(itemFont);
		Style5.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		Style5.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFCellStyle Style6=wb.createCellStyle();
		Style6.setFont(itemFont);
		Style6.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		Style6.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		
		HSSFCellStyle Style7=wb.createCellStyle();
		Style7.setFont(itemFont);
		Style7.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		Style7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Style7.setWrapText(true);
		HSSFCellStyle Style8=wb.createCellStyle();
		Style8.setFont(itemFont);
		Style8.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		Style8.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Style8.setWrapText(true);
		HSSFCellStyle Style9=wb.createCellStyle();
		Style9.setFont(itemFont);
		Style9.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		row=sheet.createRow(irow);
		HSSFCell cell=row.createCell(0);
		cell.setCellStyle(Style1);
		cell.setCellValue("Company");
		cell=row.createCell(2);//值
		cell.setCellStyle(Style1);
		cell.setCellValue("Coach Consulting Dongguan Co., Limited");
		cell=row.createCell(5);
		cell.setCellStyle(Style1);
		cell.setCellValue("Payroll Period");
		cell=row.createCell(7);//值
		cell.setCellStyle(Style1);
		cell.setCellValue(coachSalary.getPayrollPeriod());
		irow++;
		
		row=sheet.createRow(irow);
		cell=row.createCell(0);
		cell.setCellStyle(Style1);
		cell.setCellValue("EEID");
		cell=row.createCell(2);//值
		cell.setCellStyle(Style1);
		cell.setCellValue(coachSalary.getEeid());
		cell=row.createCell(5);
		cell.setCellStyle(Style1);
		cell.setCellValue("Employee Name");
		cell=row.createCell(7);//值
		cell.setCellStyle(Style1);
		cell.setCellValue(coachSalary.getEmployeeName());
		irow++;
		
		row=sheet.createRow(irow);
		cell=row.createCell(0);
		cell.setCellStyle(Style1);
		cell.setCellValue("Cost Center");
		cell=row.createCell(2);//值
		cell.setCellStyle(Style1);
		cell.setCellValue(coachSalary.getCostCenter());
		cell=row.createCell(5);
		cell.setCellStyle(Style1);
		cell.setCellValue("Department");
		cell=row.createCell(7);//值
		cell.setCellStyle(Style1);
		cell.setCellValue(coachSalary.getDepartment());
		irow++;
		
		row=sheet.createRow(irow);
		irow++;
		
		row=sheet.createRow(irow);
		cell=row.createCell(0);
		cell.setCellStyle(Style3);
		cell.setCellValue("Earnings:");
		cell=row.createCell(1);
		cell.setCellStyle(Style3);
		cell.setCellValue("");
		cell=row.createCell(2);
		cell.setCellStyle(Style4);
		cell.setCellValue("Amount");
		cell=row.createCell(3);
		cell.setCellStyle(Style3);
		cell.setCellValue("");
		cell=row.createCell(4);
		cell.setCellStyle(Style3);
		cell.setCellValue("");
		cell=row.createCell(5);
		cell.setCellStyle(Style3);
		cell.setCellValue("Deductions:");
		cell=row.createCell(6);
		cell.setCellStyle(Style3);
		cell.setCellValue("");
		cell=row.createCell(7);
		cell.setCellStyle(Style4);
		cell.setCellValue("Amount");
		irow++;
		//循环整理工资数据
		int count=0;
		int earnCount=coachSalary.getCoachEarnSalaryList().size();
		int deductCount=coachSalary.getCoachDeductSalary().size();
		if(earnCount>deductCount){
			count=earnCount;
		}else{
			count=deductCount;
		}
		for(int i=0;i<count;i++){
			row=sheet.createRow(irow);
			cell=row.createCell(0);
			cell.setCellStyle(Style7);
			if(earnCount>i){//加项标题
				cell.setCellValue(coachSalary.getCoachEarnSalaryList().get(i).getEarnTitle());
				cell=row.createCell(1);
				cell.setCellStyle(Style7);
				cell.setCellValue("CNY");
			}else{
				cell.setCellValue("");
			}
			cell=row.createCell(2);
			cell.setCellStyle(Style8);
			if(earnCount>i){//加项值
				cell.setCellValue(coachSalary.getCoachEarnSalaryList().get(i).getEarnValue());
			}else{
				cell.setCellValue("");
			}
			cell=row.createCell(5);
			cell.setCellStyle(Style7);
			if(deductCount>i){//减项标题
				cell.setCellValue(coachSalary.getCoachDeductSalary().get(i).getDeductTitle());
				cell=row.createCell(6);
				cell.setCellStyle(Style7);
				cell.setCellValue("CNY");
			}else{
				cell.setCellValue("");
			}
			cell=row.createCell(7);
			cell.setCellStyle(Style8);
			if(deductCount>i){//减项值
				cell.setCellValue(coachSalary.getCoachDeductSalary().get(i).getDeductValue());
			}else{
				cell.setCellValue("");
			}
			irow++;
		}
		row=sheet.createRow(irow);
		irow++;
		//合计
		row=sheet.createRow(irow);
		cell=row.createCell(5);
		cell.setCellStyle(Style5);
		cell.setCellValue("Net Pay");
		cell=row.createCell(6);
		cell.setCellStyle(Style5);
		cell.setCellValue("CNY");
		cell=row.createCell(7);
		cell.setCellStyle(Style6);
		cell.setCellValue(coachSalary.getNetPay());//合计值
		irow++;
		row=sheet.createRow(irow);
		irow++;
		//备注
		row=sheet.createRow(irow);
		cell=row.createCell(0);
		cell.setCellStyle(Style9);
		cell.setCellValue("Any descripancy should be reported to Human Resources Department within 7 days from the date you receive the report.");
		irow++;
		row=sheet.createRow(irow);
		cell=row.createCell(0);
		cell.setCellStyle(Style9);
		cell.setCellValue("Otherwise, the above will be assumed to be corrected and accepted.");
		irow++;
		
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
	//main方法测试
	public static void main(String[] args) {
		CoachSalary coachSalary=new CoachSalary();
		List<CoachEarnSalary> earnList=new ArrayList<CoachEarnSalary>();
		List<CoachDeductSalary> deductList=new ArrayList<CoachDeductSalary>();
		
		CoachEarnSalary coachEarnSalary1=new CoachEarnSalary();
		coachEarnSalary1.setEarnTitle("Base Salary");
		coachEarnSalary1.setEarnValue("30000.00");
		CoachEarnSalary coachEarnSalary2=new CoachEarnSalary();
		coachEarnSalary2.setEarnTitle("Telecom Allowance");
		coachEarnSalary2.setEarnValue("1234.00");
		CoachEarnSalary coachEarnSalary3=new CoachEarnSalary();
		coachEarnSalary3.setEarnTitle("Home Leave Allowance");
		coachEarnSalary3.setEarnValue("1000.00");
		earnList.add(coachEarnSalary1);
		earnList.add(coachEarnSalary2);
		earnList.add(coachEarnSalary3);
		
		
		CoachDeductSalary coachDeductSalary1=new CoachDeductSalary();
		coachDeductSalary1.setDeductTitle("Housing Allowance Deduction");
		coachDeductSalary1.setDeductValue("-1000.00");
		CoachDeductSalary coachDeductSalary2=new CoachDeductSalary();
		coachDeductSalary2.setDeductTitle("Meal Deduction");
		coachDeductSalary2.setDeductValue("-500.00");
		deductList.add(coachDeductSalary1);
		deductList.add(coachDeductSalary2);
		
		coachSalary.setCoachEarnSalaryList(earnList);
		coachSalary.setCoachDeductSalary(deductList);
		
		buildTemplet(coachSalary, "D://coachExcell.xls");
//		ExcellToPdf.saveAsPDF("D:\\coachExcell.xls", "D:\\coachPdf.pdf");
	}
}
