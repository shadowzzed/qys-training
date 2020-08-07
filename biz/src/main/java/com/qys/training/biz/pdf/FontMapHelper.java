package com.qys.training.biz.pdf;

import com.qys.training.biz.pdf.entity.PDFTextConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Zed, shadowl91@163.com
 * @date 12:41 2020/8/7
 */
@Component
public class FontMapHelper {
    // 字体map K -> 字体名字 V -> 字体路径
    private static final Map<String, PDFTextConfig.Font> map = new HashMap<>();

    static {
        map.put("SONG_TI", PDFTextConfig.Font.SONG_TI);
        map.put("HEI_TI", PDFTextConfig.Font.HEI_TI);
        map.put("KAI_TI", PDFTextConfig.Font.KAI_TI);
    }

    public static PDFTextConfig.Font getFontPath(String font) {
        final PDFTextConfig.Font bFont = map.get(font);
        if (!StringUtils.isEmpty(bFont))
            return bFont;
        else
            return map.get("SONG_TI");
    }
}
