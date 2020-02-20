package matrix.module.pay.converter;

/**
 * @author wangcheng
 * @date 2019/5/6
 */
public class HtmlConvert {

    public static final StringBuilder HTML = new StringBuilder();

    static {
        HTML.append("<html>");
        HTML.append("<head>");
        HTML.append("<style rel='stylesheet' type='text/css'>");
        HTML.append("   *{margin: 0;padding:0}");
        HTML.append("   body{margin:0;padding:0}");
        HTML.append("</style>");
        HTML.append("</head>");
        HTML.append("<body>%s</body>");
        HTML.append("</html>");
    }

    public static String bodyConvert(String body) {
        return String.format(HTML.toString(), body);
    }

    public static String imageConvert(String image) {
        return String.format(HTML.toString(), "<img src='" + image + "' />");
    }
}
