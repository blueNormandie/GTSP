package com.salaryMail.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class HtmlToWord {

	// 8 代表word保存成html
	public static final int WORD_HTML = 8;

	public static void main(String[] args) {

		String content = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"
				+ "<title>员工在线账单</title><style type='text/css'>.BillDate{border-collapse:collapse;border:none;border: 0px solid #000;}.BillDate td{border:solid #000 0px;}.BillInfo{border-collapse:collapse;}.STYLE8 {font-family: '新宋体'; color: #FFFFFF; font-weight: bold; }.STYLE10 {color: #FFFFFF; font-weight: bold; }.STYLE17 {font-family: '宋体'; font-size: 13px; }.STYLE20 {font-family: 'Times New Roman', Times, serif; font-size: 13px; }.STYLE21 {font-size: 14px; font-family: '宋体'; }.STYLE25 {font-family: '宋体'}</style></head><body>"
				+ "<div align='center' style='width:75%' id='3'><table rules='none' class='BillHead' border='0' width=75% ><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据1</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>测试数据1</td><td height='18px' width='170'>测试数据2</td><td height='18px' width='170'>测试数据3</td><td height='18px' width='170'>测试数据5</td></tr><tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>8888888</td><td height='18px' width='170'>8888888</td><td height='18px' width='170'>8888888</td><td height='18px' width='170'>8888888</td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据2</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>测试数据4</td><td height='18px' width='170'>测试数据6</td><td height='18px' width='170'>测试数据7</td><td height='18px' width='170'>测试数据8</td></tr><tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>8888888</td><td height='18px' width='170'>8888888</td><td height='18px' width='170'>8888888</td><td height='18px' width='170'>8888888</td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据3</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>测试数据9</td><td height='18px' width='170'></td><td height='18px' width='170'></td><td height='18px' width='170'></td></tr><tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>8888888</td><td height='18px' width='170'></td><td height='18px' width='170'></td><td height='18px' width='170'></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据4</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据5</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据6</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据7</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据8</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据9</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr><td colspan='4' dir='rtl'></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据10</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据36</span></td><td dir='rtl' width='170' height='23px'><div class='STYLE8' align='right'>合计:88.88</div></td></tr><tr><td colspan='4' dir='rtl'></td></tr><tr bgcolor='#63CFA3' style='text-align: left;'><td colspan='3' height='23px'><span class='STYLE8'>大标题测试数据37</span></td><td></td></tr><tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>其他信息</td>"
				+ "<td height='18px' width='170'></td><td height='18px' width='170'></td><td height='18px' width='170'></td></tr>"
				+ "<tr style='text-align: center;font-size: 13px;'><td height='18px' width='170'>其他信息</td>"
				+ "<td height='18px' width='170'></td><td height='18px' width='170'></td><td height='18px' width='170'></td></tr>"
				+ "<tr><td colspan='4'><div style='border:2px #000000 solid'><div>备注信息：<div><div>小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！小伙帅呆了！</div><br/><div></td></tr>"
				+ "</table>"
				+ " </div><div id='9' style='position: relative'></div></body></html>";
		writeWordFile(content);
	}

	public static void htmlToWord(String htmlFile, String docFile) {
		ActiveXComponent app = new ActiveXComponent("Word.Application"); // 启动word

		try {
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			Dispatch doc = Dispatch.invoke(
					docs,
					"Open",
					Dispatch.Method,
					new Object[] { htmlFile, new Variant(false),
							new Variant(true) }, new int[1]).toDispatch();
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
					docFile, new Variant(1) }, new int[1]);
			Variant f = new Variant(false);
			Dispatch.call(doc, "Close", f);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			app.invoke("Quit", new Variant[] {});
			ComThread.Release();
		}
	}

	// 用poi解析html为word
	public static boolean writeWordFile(String content) {
		boolean w = false;
		String path = "d:/";
		try {
			if (!"".equals(path)) {
				// 检查目录是否存在
				File fileDir = new File(path);
				if (fileDir.exists()) {
					// 生成临时文件名称
					String fileName = "wordModel.doc";
					byte b[] = content.getBytes();
					ByteArrayInputStream bais = new ByteArrayInputStream(b);
					POIFSFileSystem poifs = new POIFSFileSystem();
					DirectoryEntry directory = poifs.getRoot();
					DocumentEntry documentEntry = directory.createDocument(
							"WordDocument", bais);
					FileOutputStream ostream = new FileOutputStream(path
							+ fileName);
					poifs.writeFilesystem(ostream);
					bais.close();
					ostream.close();

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return w;
	}

}
