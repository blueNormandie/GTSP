package com.salaryMail.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ExcellToPdf {
	public static void saveAsPDF(String xlsfile, String pdffile) {
		ActiveXComponent x1 = new ActiveXComponent("Excel.Application"); // 启动excel(Excel.Application)
		try {
			ComThread.InitSTA(); 
			x1.setProperty("Visible", false);//不显示打开excell
			Dispatch workbooks = x1.getProperty("Workbooks").toDispatch();//工作簿对象
			Dispatch workbook = Dispatch.invoke(//打开具体工作簿
					workbooks,
					"Open",
					Dispatch.Method,
					new Object[] { xlsfile,
							new Variant(false),//可写入
							new Variant(true) },
							new int[1]).toDispatch();//打开具体工作薄
			//设置excell打印格式
			Dispatch currentSheet=Dispatch.get(workbook, "ActiveSheet").toDispatch();//获得当前表 
	        Dispatch PageSetup=Dispatch.get(currentSheet,"PageSetup").toDispatch();  
	        Dispatch.put(PageSetup, "PrintArea", false);
            Dispatch.put(PageSetup, "Orientation", 1);  
            Dispatch.put(PageSetup, "Zoom", false);        
            Dispatch.put(PageSetup, "FitToPagesTall", false);  
            Dispatch.put(PageSetup, "FitToPagesWide", 1);  
			
		    Dispatch.invoke(workbook, "SaveAs", Dispatch.Method, new Object[]
		    { pdffile, new Variant(57), new Variant(false), new Variant(57),
		    new Variant(57), new Variant(false), new Variant(true), new
		    Variant(57), new Variant(true), new Variant(true), new
		    Variant(true) }, new int[1]);
		    
			/*Dispatch.invoke(workbook, "SaveAs", Dispatch.Method, new Object[] {
					pdffile, new Variant(57), new Variant(false) }, new int[1]);*/
		    
			Variant f = new Variant(false);
			Dispatch.call(workbook, "Close", f);//关闭excell

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			x1.invoke("Quit", new Variant[] {});//释放资源
			ComThread.Release();
		}
	}
}
