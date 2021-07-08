package com.github.kangmoo.utils.utility;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author kangmoo Heo
 */
public class XmlUtil {
    public static Document parse(String xml) {
        try(InputStream xmlStream = new ByteArrayInputStream(xml.getBytes())){
            return new SAXReader().read(xmlStream);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * item : <item>요소를 모두 선택함
     * /item : "/" 루트 노드의 자식 노드중에서 <item>엘리먼트를 선택함 (앞에 "/"가 들어가면 절대 경로)
     * item/jeongpro : <item>엘리먼트의 자식 노드중에서 <jeongpro>엘리먼트를 선택 (상대 경로)
     * // : 현재 노드의 위치와 상관없이 지정된 노드부터 탐색
     * //item : 위치와 상관없이 엘리먼트 이름이 <item>인 모든 엘리먼트
     * item/@id : 모든 <item>엘리먼트의 id속성 노드를 모두 선택함
     * item[k] : <item>엘리먼트 중에서 k번 째 <item>엘리먼트
     * item[@attr = val] : attr이라는 속성이 val값을 가지는 모든 <item>엘리먼트
     */
    public static Node selectSingleNode(String xml, String xpathExpression){
        return parse(xml).selectSingleNode(xpathExpression);
    }

    public static List<Node> selectNodes(String xml, String xpathExpression){
        return parse(xml).selectNodes(xpathExpression);
    }
}
