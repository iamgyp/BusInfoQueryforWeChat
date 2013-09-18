package com.wechat.utils;  
  
import java.io.IOException;  
import java.io.StringReader;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
  
import org.jdom.Attribute;  
import org.jdom.Document;  
import org.jdom.Element;  
import org.jdom.JDOMException;  
import org.jdom.input.SAXBuilder;  
  
public class XmlUtil {  
  
    public static Map<String, String> xml2Map(String xmlStr)  
            throws JDOMException, IOException {  
        Map<String, String> rtnMap = new HashMap<String, String>();  
        SAXBuilder builder = new SAXBuilder();  
        Document doc = builder.build(new StringReader(xmlStr));  
        // �õ����ڵ�  
        Element root = doc.getRootElement();  
        String rootName = root.getName();  
        rtnMap.put("root.name", rootName);  
        // ���õݹ麯�����õ�������ײ�Ԫ�ص����ƺ�ֵ������map��  
        convert(root, rtnMap, rootName);  
        return rtnMap;  
    }  
  
    /** 
     * �ݹ麯�����ҳ����²�Ľڵ㲢���뵽map�У���xml2Map�������á� 
     *  
     * @param e 
     *           xml �ڵ㣬�������ڵ� 
     * @param map 
     *            Ŀ��map 
     * @param lastname 
     *            �Ӹ��ڵ㵽��һ���ڵ��������ӵ��ִ� 
     */  
    public static void convert(Element e, Map<String, String> map,  
            String lastname) {  
        if (e.getAttributes().size() > 0) {  
            Iterator it_attr = e.getAttributes().iterator();  
            while (it_attr.hasNext()) {  
                Attribute attribute = (Attribute) it_attr.next();  
                String attrname = attribute.getName();  
                String attrvalue = e.getAttributeValue(attrname);  
                map.put(lastname + "." + attrname, attrvalue);  
            }  
        }  
        List children = e.getChildren();  
        Iterator it = children.iterator();  
        while (it.hasNext()) {  
            Element child = (Element) it.next();  
            String name = lastname + "." + child.getName();  
            // ������ӽڵ㣬��ݹ����  
            if (child.getChildren().size() > 0) {  
                convert(child, map, name);  
            } else {  
                // ���û���ӽڵ㣬���ֵ����map  
                map.put(name, child.getText());  
                // ����ýڵ������ԣ�������е�����ֵҲ����map  
                if (child.getAttributes().size() > 0) {  
                    Iterator attr = child.getAttributes().iterator();  
                    while (attr.hasNext()) {  
                        Attribute attribute = (Attribute) attr.next();  
                        String attrname = attribute.getName();  
                        String attrvalue = child.getAttributeValue(attrname);  
                        map.put(name + "." + attrname, attrvalue);  
                    }  
                }  
            }  
        }  
    }  
  
}  