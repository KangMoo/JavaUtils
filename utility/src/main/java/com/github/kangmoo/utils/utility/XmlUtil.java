package com.github.kangmoo.utils.utility;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author kangmoo Heo
 */
public class XmlUtil {
    public static Optional<Document> parse(String xml) {
        try (InputStream xmlStream = new ByteArrayInputStream(xml.getBytes())) {
            return Optional.of(new SAXReader().read(xmlStream));
        } catch (Exception e) {
            return Optional.empty();
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
    public static Node selectSingleNode(String xml, String xpathExpression) {
        return parse(xml).map(o -> o.selectSingleNode(xpathExpression)).orElse(null);
    }

    public static List<Node> selectNodes(String xml, String xpathExpression) {
        return parse(xml).map(o -> o.selectNodes(xpathExpression)).orElse(null);
    }

    public static String getStrParam(org.w3c.dom.Node node) {
        return node == null ? null : node.getTextContent();
    }

    public static Boolean getBoolParam(org.w3c.dom.Node node) {
        return node == null ? null : node.getTextContent().equalsIgnoreCase("true");
    }

    public static Integer getIntParam(org.w3c.dom.Node node) {
        return node == null ? null : Integer.parseInt(node.getTextContent());
    }

    public static Float getFloatParam(org.w3c.dom.Node node) {
        return node == null ? null : Float.parseFloat(node.getTextContent());
    }

    public static String getStrParamWithDefault(org.w3c.dom.Node node, String defaultVal) {
        return node == null ? defaultVal : node.getTextContent();
    }

    public static Boolean getBoolParamWithDefault(org.w3c.dom.Node node, boolean defaultVal) {
        return node == null ? defaultVal : node.getTextContent().equalsIgnoreCase("true");
    }

    public static Integer getIntParamWithDefault(org.w3c.dom.Node node, int defaultVal) {
        return node == null ? defaultVal : Integer.parseInt(node.getTextContent());
    }

    public static Float getFloatParamWithDefault(org.w3c.dom.Node node, float defaultVal) {
        return node == null ? defaultVal : Float.parseFloat(node.getTextContent());
    }
}
