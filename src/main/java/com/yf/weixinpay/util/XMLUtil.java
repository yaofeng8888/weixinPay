package com.yf.weixinpay.util;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XMLUtil {

    public static Map doXMLParse(String strxml) throws JDOMException,IOException{
        strxml.replaceFirst("encoding = \".*\"","encoding=\"UTF-8\"");
        if(null==strxml||"".equals(strxml)){
            return null;
        }
        Map map = new HashMap();
        InputStream inputStream = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(inputStream);
        Element root = document.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()){
            Element e = (Element) it.next();
            String key = e.getName();
            String value = "";
            List childreList = e.getChildren();
            if(childreList.isEmpty()){
                value = e.getTextNormalize();
            }else {
                value = XMLUtil.getChildrenText(childreList);
            }
            map.put(key,value);
        }
        inputStream.close();
        return map;
    }

    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(XMLUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }
}
