package com.salaryMail.util;

import java.io.FileOutputStream;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class OpenPdfAddPicture {

	public static void realPdf(String filePath, String imagePath,String newFilePath) {
		try {
			//创建一个pdf读入流
			PdfReader reader = new PdfReader(filePath);
			//根据一个pdfreader创建一个pdfStamper.用来生成新的pdf
			PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(newFilePath));
			//获得pdfstamper在当前页的上层打印内容。也就是说 这些内容会覆盖在原先的pdf内容之上。
			PdfContentByte over = stamper.getOverContent(1);
			//用pdfreader获得当前页字典对象。包含了该页的一些数据。比如该页的坐标轴信息。
			PdfDictionary p = reader.getPageN(1);
			//拿到mediaBox 里面放着该页pdf的大小信息。
			PdfObject po =  p.get(new PdfName("MediaBox"));
			//po是一个数组对象。里面包含了该页pdf的坐标轴范围。
			PdfArray pa = (PdfArray) po;
			//创建image对象
			Image image=Image.getInstance(imagePath);
			image.scalePercent(50);
			//设置image对象的输出位置pa.getAsNumber（pa.size（）-1）.floatValue（） 是该页pdf坐标轴的y轴的最大值
			image.setAbsolutePosition(250, 750);//0, 0, 841.92, 595.32
			over.addImage(image);
			stamper.close();
		} catch (Exception e) {
			System.out.println("读取PDF文件失败！" + e);
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {
		realPdf("C:/Users/sunshine/Desktop/650793-201509.pdf", "C:/Users/sunshine/Desktop/coachLogo.jpg", "C:/Users/sunshine/Desktop/newPdf.pdf");
	}
}
