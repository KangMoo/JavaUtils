package test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.print.DocFlavor.INPUT_STREAM;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import com.github.kangmoo.utils.utility.XmlUtil;
import org.junit.Test;

/**
 * @author kangmoo Heo
 */
public class XmlUtilTest {
    @Test
    public void test() throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read( new ByteArrayInputStream(xmlSample.getBytes()));
        System.out.println("Root element :" + document.getRootElement().getName());
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes("company/employee");
        System.out.println("----------------------------");
        for (Node node : nodes) {
            System.out.println("\nCurrent Element :"
                    + node.getName());
            System.out.println("First Name : "
                    + node.selectSingleNode("firstname").getText());
            System.out.println("Last Name : "
                    + node.selectSingleNode("lastname").getText());
            System.out.println("Salary : "
                    + node.selectSingleNode("salary").getText());


            // System.out.println(XmlUtil.selectNodes(xmlSample, "company/employee"));
        }
    }

    String xmlSample = "<?xml version=\"1.0\"?>\n" +
            "<company>\n" +
            "    <employee id=\"1001\">\n" +
            "        <firstname>Tony</firstname>\n" +
            "        <lastname>Black</lastname>\n" +
            "        <salary>100000</salary>\n" +
            "    </employee>\n" +
            "    <employee id=\"2001\">\n" +
            "        <firstname>Amy</firstname>\n" +
            "        <lastname>Green</lastname>\n" +
            "        <salary>200000</salary>\n" +
            "    </employee>\n" +
            "</company>";
}
