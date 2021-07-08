package com.github.kangmoo.utils.utility;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author kangmoo Heo
 */
public class XmlUtil {
    public static Document parse(String xml) throws DocumentException {
        try(InputStream xmlStream = new ByteArrayInputStream(xml.getBytes())){
            return new SAXReader().read(xmlStream);
        } catch (Exception e){
            return null;
        }
    }
}
