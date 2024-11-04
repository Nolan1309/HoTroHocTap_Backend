//package com.example.hotrohoctapbackend.controller;
//
////import org.docx4j.Docx4J;
////import org.docx4j.convert.out.pdf.PdfConversion;
////import org.docx4j.openxml4j.exceptions.Docx4JException;
////import org.docx4j.openxml4j.packages.WordprocessingMLPackage;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////import org.docx4j.Docx4J;
////import org.docx4j.convert.out.pdf.PdfConversion;
////import org.docx4j.openpackaging.exceptions.Docx4JException;
////import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//
//@RestController
//@RequestMapping("/api")
//public class Controll {
//
//    @PostMapping("/convert-docx-to-pdf")
//    public String convertDocxToPdf(@RequestParam("file") MultipartFile file) {
////        File tempFile = null;
////        try {
////            // Save the uploaded file to a temporary file
////            tempFile = File.createTempFile("temp-", ".docx");
////            file.transferTo(tempFile);
////
////            // Define the path where the PDF will be saved
////            File pdfFile = new File("F:/CardID/output.pdf");
////
////            // Load the DOCX file and convert to PDF
////            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(tempFile);
////            PdfConversion converter = Docx4J.toFO(wordMLPackage, pdfFile);
////            converter.output(new FileOutputStream(pdfFile));
////
////            // Return success message with path
////            return "PDF conversion completed successfully. PDF saved at: " + pdfFile.getAbsolutePath();
////        } catch (IOException | Docx4JException e) {
////            e.printStackTrace();
////            return "Error during PDF conversion: " + e.getMessage();
////        } finally {
////            // Clean up temporary file
////            if (tempFile != null && tempFile.exists()) {
////                tempFile.delete();
////            }
////        }
////    }
//}
