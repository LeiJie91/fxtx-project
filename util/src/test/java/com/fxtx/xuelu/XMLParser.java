package com.fxtx.xuelu;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: rizenguo
 * Date: 2014/11/1
 * Time: 14:06
 */
public class XMLParser {

    /**
     * 获取返回的xml数据
     * @param text
     * @return
     */
    public static  Map<String,Object> getXML(String text)
    {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(new ByteArrayInputStream(text.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element htmlConfigElement = (Element)document.getRootElement();
        Iterator<Element> iterator = htmlConfigElement.elementIterator();

        Map map=new HashMap();
        while(iterator.hasNext()) {
            Element element = (Element)iterator.next();
            map.put(element.getName(), element.getTextTrim());

        }
        return map;
    }

}
