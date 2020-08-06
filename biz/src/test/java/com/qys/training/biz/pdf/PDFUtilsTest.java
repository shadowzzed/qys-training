package com.qys.training.biz.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:40 2020/8/6
 */
public class PDFUtilsTest {
    @Test
    public void testItext() throws FileNotFoundException, DocumentException {
        final Document document = new Document();
        writeIn(document, "D:\\result.pdf");
    }

    @Test
    public void testItext2() throws DocumentException, FileNotFoundException {
        Rectangle pagesize = new Rectangle(216f, 720f);
        final Document document = new Document(pagesize);

        writeIn(document, "D:\\result1.pdf");
    }

    @Test
    public void testItext3() throws FileNotFoundException, DocumentException {
        Document document = new Document(new Rectangle(14400, 14400));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:\\result2.pdf"));
        writer.setUserunit(75000f);
        document.open();
        document.add(new Paragraph("2222"));
        document.close();
    }

    @Test
    public void testItext4() throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:\\result.pdf"));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        document.open();
//        PdfReader reader = new PdfReader("D:\\result.pdf");
//        System.out.println(reader.getPdfVersion());
//        System.out.println(document.getPageNumber());
        PdfContentByte canvas = writer.getDirectContentUnder();
        writer.setCompressionLevel(0);
        canvas.saveState();
        canvas.beginText();
        canvas.moveText(36,820);
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        canvas.showText("hhhhh");
        canvas.endText();
        canvas.restoreState();
        document.close();
    }

    @Test
    public void testItext5() throws IOException {
        PdfReader reader = new PdfReader("D:\\result2.pdf");
        System.out.println(reader.getNumberOfPages());
//        System.out.println(Arrays.toString(reader.getPageContent(1)));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(reader.getPageContent(1))));
        String content = null;
        while (!StringUtils.isEmpty(content = bufferedReader.readLine()))
            System.out.println(content);
        final Rectangle pageSize = reader.getPageSize(1);
        System.out.println("****************************************");
        System.out.println(pageSize.getLeft());
        System.out.println(pageSize.getRight());
        System.out.println(pageSize.getBottom());
        System.out.println(pageSize.getTop());
        System.out.println("****************************************");
        System.out.println(reader.getPageRotation(1));
        System.out.println(reader.getPageSizeWithRotation(1));
        System.out.println("****************************************");
        System.out.println(reader.getFileLength());
        System.out.println(reader.isRebuilt());
        System.out.println(reader.isEncrypted());

    }

    @Test
    public void test6() throws IOException, DocumentException {
        PdfReader reader = new PdfReader("D:\\result2.pdf");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("D:\\result.pdf"));
        PdfContentByte canvas = stamper.getOverContent(1);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("hello!!!!"), 36, 200, 0);
        stamper.close();
    }

    @Test
    public void test7() throws Exception {
        final PdfReader reader = new PdfReader("D:\\result.pdf");
        final PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("D:\\result7.pdf"));
        Image image = Image.getInstance("D:\\11.jpg");

//        Image image = Image.getInstance("");
        PDFUtils.addImage(reader, stamper, image, 1, 36, 540, 939, 220);
    }

    private void writeIn(Document document, String path) throws DocumentException, FileNotFoundException {
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        document.add(new Paragraph("hello itext"));
        document.add(new Paragraph("22222222222222222222222"));
        document.close();
    }

}