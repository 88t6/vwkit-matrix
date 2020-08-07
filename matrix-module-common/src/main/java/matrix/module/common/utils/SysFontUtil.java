package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import sun.font.PhysicalFont;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 系统字体工具
 * @author wangcheng
 */
public class SysFontUtil {

    private static final String[] DEFAULT_FONTS_NAME = new String[]{
            "SimSun", "Microsoft Yahei", "Arial", "Tahoma", "Verdana"
    };

    /**
     * 获取当前系统所有支持的字体名称
     */
    public static Map<String, String> getSystemFontsDict() {
        return SingletonUtil.get("systemFontNameList", () -> {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font[] fonts = graphicsEnvironment.getAllFonts();
            if (fonts != null && fonts.length > 0) {
                Map<String, String> result = new LinkedHashMap<>();
                try {
                    Method method = Font.class.getDeclaredMethod("getFont2D");
                    method.setAccessible(true);
                    Field field = PhysicalFont.class.getDeclaredField("platName");
                    field.setAccessible(true);
                    for (Font font : fonts) {
                        try {
                            result.put(font.getFamily(), String.valueOf(field.get(method.invoke(font))));
                        } catch (Exception ignored) {
                        }
                    }
                } catch (Exception e) {
                    throw new ServiceException(e);
                }
                return result;
            }
            return null;
        });
    }

    /**
     * 匹配字体文件名称
     */
    public static String getFontPath(String fontName) {
        Map<String, String> fontDict = getSystemFontsDict();
        Assert.state(!fontDict.isEmpty(), "system fonts is null");
        if (fontDict.containsKey(fontName)) {
            return fontDict.get(fontName);
        }
        for (String defaultFontName : DEFAULT_FONTS_NAME) {
            if (fontDict.containsKey(defaultFontName)) {
                return fontDict.get(defaultFontName);
            }
        }
        Iterator<String> iterator = fontDict.keySet().iterator();
        return fontDict.get(iterator.next());
    }
}
