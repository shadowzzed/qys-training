package com.qys.training.biz.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.qys.training.biz.pdf.entity.PDFTextConfig;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Zed, shadowl91@163.com
 * @date 12:41 2020/8/6
 */
public class PDFUtils {

    public static void addText(PdfReader pdfReader, PdfStamper stamper, PDFTextConfig config, String fontPath) throws IOException, DocumentException {
        PdfContentByte canvas = stamper.getOverContent(1);
        BaseFont baseFont = BaseFont.createFont(fontPath + config.getFont().getDescription(), BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 50);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(config.getText(), font), config.getX(), config.getY(), 0);
        stamper.close();
    }

//    public static void addText(PdfReader pdfReader, PdfStamper pdfStamper, PDFTextConfig config, String fontPath)
//            throws Exception {
//        String text = config.getText();
//        BaseColor baseColor = config.getBaseColor();
//        float fontSize = config.getFontSize();
//        fontSize = 50;
//        int pageNum = config.getPageNum();
//        String styleConfig = config.getStyle() == null ? "" : config.getStyle();
//        String alignment = config.getAlign() == null ? "" : config.getAlign();
//        PdfContentByte pdfContentByte = pdfStamper.getOverContent(pageNum);
//        Rectangle pageSize = pdfReader.getPageSizeWithRotation(pageNum);
//        float pageWidth = pageSize.getWidth();
//        float pageHeight = pageSize.getHeight();
//        float x = config.getX() * pageWidth;
//        float y = config.getY() * pageHeight;
//        float width = config.getWidth() * pageWidth;
//        float height = config.getHeight() * pageHeight;
//
//        BaseFont baseFont = BaseFont.createFont(fontPath + config.getFont().getDescription(), BaseFont.IDENTITY_H,
//                BaseFont.EMBEDDED);
//        Font font = new Font(baseFont, fontSize);
//        font.setColor(baseColor);
//        font.setStyle(Font.getStyleValue(styleConfig.toLowerCase()));
//        Rectangle rect = new Rectangle(x, y, x + width, y + height);// 文本框位置
//        if (config.isBorder()) {
//            rect.setBorder(Rectangle.BOX);// 显示边框，默认不显示，常量值：LEFT, RIGHT, TOP, BOTTOM，BOX,
//            rect.setBorderWidth(1f);// 边框线条粗细
//            rect.setBorderColor(BaseColor.BLACK);// 边框颜色
//        }
//        pdfContentByte.rectangle(rect);
//
//        ColumnText ct = new ColumnText(pdfContentByte);
//        if (alignment.indexOf("ALIGN_CENTER") != -1) {
//            ct.setAlignment(Element.ALIGN_CENTER);
//        } else if (alignment.indexOf("ALIGN_RIGHT") != -1) {
//            ct.setAlignment(Element.ALIGN_RIGHT);
//        } else {
//            ct.setAlignment(Element.ALIGN_LEFT);
//        }
//        ct.addText(new Phrase(text, font));
//        ct.setSimpleColumn(rect);
//        ct.setUseAscender(false);
//        ct.go();
//        pdfStamper.close();
//    }
    public static void addImage(PdfReader pdfReader, PdfStamper pdfStamper, Image image, int pageNo, float absoluteX,
                                float absoluteY, float newWidth, float newHeight) throws Exception {
        PdfContentByte overContent = pdfStamper.getOverContent(pageNo);
        image.scaleAbsolute(newWidth, newHeight);
        image.setAbsolutePosition(absoluteX, absoluteY);
        overContent.addImage(image);
        pdfStamper.close();
    }

    public static void addUnderImage(PdfReader pdfReader, PdfStamper pdfStamper, Image image, int pageNo, float absoluteX,
                                     float absoluteY, float newWidth, float newHeight) throws Exception {
        PdfContentByte overContent = pdfStamper.getUnderContent(pageNo);
        image.scaleAbsolute(newWidth, newHeight);
        image.setAbsolutePosition(absoluteX, absoluteY);
        overContent.addImage(image);
        pdfStamper.close();
    }


}
