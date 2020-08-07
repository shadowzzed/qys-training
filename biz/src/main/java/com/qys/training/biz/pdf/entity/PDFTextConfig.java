package com.qys.training.biz.pdf.entity;

import com.itextpdf.text.BaseColor;
import com.qys.training.base.entity.BaseEntity;

/**
 * @author Zed, shadowl91@163.com
 * @date 12:34 2020/8/6
 */
public class PDFTextConfig extends BaseEntity {
    private String text;
    private Font font;
    private String rgb;
    private float fontSize;
    private int pageNum;
    private float x;
    private float y;
    private float width;
    private float height;
    private float widthSpec;
    private float heightSpec;
    private int pxWidth;
    private int pxHeight;

    private String style;
    private String align;
    private Boolean border;
    private String type;
    private String orientation;

    private BaseColor baseColor;

    public boolean isBorder() {
        return true;
    }


    public enum Font {
        SONG_TI("simsun.ttf"),
        HEI_TI("simkai.ttf"),
        KAI_TI("simhei.ttf"),
        FANG_SONG("simfang.ttf"),
        WEIRUAN_YAHEI("MSMHei.ttf"),
        ARIAL("arial.ttf"),
        GEORGIA("georgia.ttf"),
        IMPACT("impact.ttf"),
        TAHOMA("tahoma.ttf"),
        VERDANA("verdana.ttf"),
        TIMES_NEW_ROMAN("Times_New_Roman.fon");

        private String description;

        private Font(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidthSpec() {
        return widthSpec;
    }

    public void setWidthSpec(float widthSpec) {
        this.widthSpec = widthSpec;
    }

    public float getHeightSpec() {
        return heightSpec;
    }

    public void setHeightSpec(float heightSpec) {
        this.heightSpec = heightSpec;
    }

    public int getPxWidth() {
        return pxWidth;
    }

    public void setPxWidth(int pxWidth) {
        this.pxWidth = pxWidth;
    }

    public int getPxHeight() {
        return pxHeight;
    }

    public void setPxHeight(int pxHeight) {
        this.pxHeight = pxHeight;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public Boolean getBorder() {
        return border;
    }

    public void setBorder(Boolean border) {
        this.border = border;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public BaseColor getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(BaseColor baseColor) {
        this.baseColor = baseColor;
    }
}
