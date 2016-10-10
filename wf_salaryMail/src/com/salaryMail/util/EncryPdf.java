package com.salaryMail.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.PDStandardEncryption;

public class EncryPdf {

	public static void encryptPdf(String filepath, String password)
    { 
        try
        {
        PDDocument pdfdocument = PDDocument.load(filepath);   
        PDStandardEncryption encryptionOptions =new PDStandardEncryption();
        encryptionOptions.setCanPrint(false);
        pdfdocument.setEncryptionDictionary(encryptionOptions );
        pdfdocument.encrypt(password,password );
        pdfdocument.save(filepath);
        pdfdocument.close();
     }
     catch (Exception e) {
    throw new RuntimeException(  
               "encrypt error  " + e);
     }
     
    }
	public static void main(String[] args) {
		encryptPdf("C:/Users/sunshine/Desktop/650793-201509.pdf", "123");
	}
}
