package matrix.module.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import matrix.module.common.helper.Assert;
import org.apache.poi.ooxml.util.DocumentHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author wangcheng
 */
public class XmlUtil {

    private static final Integer INDENT = 4;

    /**
     * parseXml
     */
    public static JSONObject parseXml(String xml) {
        Assert.isNotNull(xml, "xml");
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            DocumentBuilder documentBuilder = DocumentHelper.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();
            Element element = document.getDocumentElement();
            JSONObject result = new JSONObject();
            XmlUtil.parseXmlForString(element, result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            StreamUtil.closeStream(inputStream);
        }
    }

    private static void parseXmlForString(Element element, JSONObject result) {
        Set<String> keys = new HashSet<>();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element ele = (Element) node;
                keys.add(ele.getNodeName());
            }
        }
        for (String key : keys) {
            NodeList list = element.getElementsByTagName(key);
            if (list.getLength() == 1) {
                Node node = list.item(0);
                if (list.item(0).getChildNodes().getLength() == 1) {
                    result.put(node.getNodeName(), node.getTextContent());
                } else {
                    JSONObject obj = new JSONObject();
                    XmlUtil.parseXmlForString((Element) node, obj);
                    result.put(key, obj);
                }
            } else if (list.getLength() > 1) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);
                    if (node.getChildNodes().getLength() == 1) {
                        jsonArray.add(node.getTextContent());
                    } else {
                        JSONObject obj = new JSONObject();
                        XmlUtil.parseXmlForString((Element) node, obj);
                        jsonArray.add(obj);
                    }
                }
                result.put(key, jsonArray);
            }
        }

    }

    /**
     * xml to string
     *
     * @param rootName 参数
     * @param object 参数
     * @return String
     */
    public static String toXmlString(String rootName, Object object) {
        Assert.isNotNull(object, "object");
        StringBuilder result = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\r");
        result.append("<").append(rootName).append(">\n\r");
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(object), JSONObject.class);
        XmlUtil.processXmlToString(null, jsonObject, result, 1);
        result.append("</").append(rootName).append(">");
        return result.toString();
    }


    private static void processXml(String key, Object value, StringBuilder result, Integer level) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < INDENT * level; i++) {
            space.append(" ");
        }
        if (!(value instanceof JSONObject) && !(value instanceof JSONArray)) {
            result.append(space.toString()).append("<").append(key).append(">");
            result.append(value);
            result.append("</").append(key).append(">\n\r");
        } else if (value instanceof JSONObject) {
            result.append(space.toString()).append("<").append(key).append(">\n\r");
            XmlUtil.processXmlToString(null, value, result, level + 1);
            result.append(space.toString()).append("</").append(key).append(">\n\r");
        } else {
            XmlUtil.processXmlToString(key, value, result, level);
        }
    }

    private static void processXmlToString(String arrayKey, Object object, StringBuilder result, Integer level) {
        if (object == null) {
            return;
        }
        if (object instanceof JSONObject) {
            JSONObject json = (JSONObject) object;
            for (Map.Entry<String, Object> stringObjectEntry : json.entrySet()) {
                String key = String.valueOf(stringObjectEntry.getKey());
                Object value = stringObjectEntry.getValue();
                XmlUtil.processXml(key, value, result, level);
            }
        } else if (object instanceof JSONArray) {
            JSONArray json = (JSONArray) object;
            for (Object obj : json) {
                XmlUtil.processXml(arrayKey, obj, result, level);
            }
        }
    }
}
