package org.openmrs.module.radiologyfhirsupport.api.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by devmaany on 14/6/16.
 */
public class RadLexUtil {
    private Document document;

    public RadLexUtil(Document document) {
        this.document = document;
        if(!(isRadLexUsedAsCodingScheme() || isRadLexUsedAsCodingSchemeInCodedContent())) {
            MessageSourceService messageSourceService = Context.getService(MessageSourceService.class);
            String error = messageSourceService.getMessage("radiologyfhirsupport.radlexError");
            throw new RuntimeException(error);
        }
    }

    public boolean isRadLexUsedAsCodingScheme(){
        String xPath = "//html/head/script/template_attributes/coding_schemes/coding_scheme/@name";
        List codingSchemes = document.selectNodes(xPath);
        for(Iterator iter = codingSchemes.iterator(); iter.hasNext();){
            Attribute attr = (Attribute) iter.next();
            String codingScheme = attr.getValue();
            if(codingScheme.toUpperCase().equals("RADLEX"))
                return true;
        }
        return false;
    }
    public boolean isRadLexUsedAsCodingSchemeInCodedContent(){
        String xPath = "//html/head/script/template_attributes/coded_content/coding_schemes/coding_scheme/@name";
        List codingSchemes = document.selectNodes(xPath);
        for(Iterator iter = codingSchemes.iterator(); iter.hasNext();){
            Attribute attr = (Attribute) iter.next();
            String codingScheme = attr.getValue();
            if(codingScheme.toUpperCase().equals("RADLEX"))
                return true;
        }
        return false;
    }

    /**
     *
     * @return Map<RADLEX_CODE, RADLEX_CODE_MEANING>
     */
    public Map<String,String> getRadLexCodes(){
        Map<String,String> radLexCodeMeaning = new HashMap<String, String>();
        List<Node> nodes = (List<Node>) document.selectNodes("//code");
        for (Node node : nodes) {
            String scheme = node.valueOf("@scheme");
            if (!scheme.toUpperCase().equals("RADLEX"))
                continue;
            String codeValue = node.valueOf("@value");
            String codeMeaning = node.valueOf("@meaning");
            radLexCodeMeaning.put(codeValue, codeMeaning);
        }
        return radLexCodeMeaning;
    }

    /**
     *
     * @return Map<RADLEX_CODE,ORIGTXT>
     */
    public Map<String,String> getCodedContent(boolean codeOrigtxt){
        String xml = "//html/head/script/template_attributes/coded_content/entry";
        Map<String,String> codedContent = new HashMap<String, String>();
        List entryNodes = document.selectNodes(xml);
        for (Iterator<Element> it = entryNodes.iterator(); it.hasNext();){
            Element entryNode = it.next();
            String origtxt = entryNode.attributeValue("ORIGTXT");
            for(Iterator<Element> itInner = entryNode.elementIterator();itInner.hasNext();) {
                Element childNode = itInner.next();
                String childNodeName = childNode.getName();
                if(childNodeName.equals("term")){
                    Element code = childNode.element("code");
                    if(code.attributeValue("scheme").toUpperCase().equals("RADLEX")) {
                        String codeValue = code.attributeValue("value");
                        if(codeOrigtxt)
                            codedContent.put(codeValue,origtxt);
                        else
                            codedContent.put(origtxt,codeValue);
                    }
                }
            }
        }
        return codedContent;
    }

    /**
     *
     * @param radLexCode
     * @return if multiple values returned, string should start with 'multiple;;' and the values should be separated with double semicolon ';;
     * @return if elementType = select , returns the selected options
     */
    public String getBodyCodedContent(String radLexCode){
        boolean multiple = false;
        String retVal=null;
        Map<String, String> codedContent = getCodedContent(true);
        if(!codedContent.containsKey(radLexCode)) {
            return null;
        }
        /* find section paragraph containing input/select/textarea element with id=origtxt*/
        String origtxt = codedContent.get(radLexCode);

        String xPath = "//html/body/section/p";
        List<Element> sectionParagraphs = document.selectNodes(xPath);
        for(Element sectionParagraph:sectionParagraphs){
            if(sectionParagraph.elements("input")!=null){
                List<Element> elements = sectionParagraph.elements("input");
                for (Element inputElement:elements) {
                    String id = inputElement.attributeValue("id");
                    if(!id.equals(origtxt))
                        continue;
                    String type = inputElement.attributeValue("type");
                    inputElement.attributeValue("type");
                    if (type.equals("text") || type.equals("date") || type.equals("time")) {
                        return inputElement.attributeValue("value");
                    } else if (type.equals("number") ){
                        String units = inputElement.attributeValue("data-field-units");
                        return inputElement.attributeValue("value") + " " +units;
                    } if (type.equals("checkbox")) {

                    }
                }
            }else if(sectionParagraph.elements("textarea")!=null) {
                List<Element> textAreaElements = sectionParagraph.elements("textarea");
                for(Element textAreaElement: textAreaElements) {
                    String id = textAreaElement.attributeValue("id");
                    if (!id.equals(origtxt))
                        continue;
                    return textAreaElement.getText();
                }


            }else if(sectionParagraph.elements("select")!=null){
                List<Element> selectElements = sectionParagraph.elements("select");
                for(Element selectElement:selectElements) {
                    String id = selectElement.attributeValue("id");
                    if (!id.equals(origtxt))
                        continue;
                    multiple = selectElement.attribute("multiple") != null;
                    retVal="";
                    if(multiple)
                        retVal="multiple;;";
                    for (Element optionElement : (List<Element>) selectElement.elements("options")) {
                        if(optionElement.attribute("selected")!=null){
                            retVal += optionElement.attributeValue("value");
                            if(!multiple)
                                return  retVal;
                            else
                                retVal += ";;";
                        }
                    }
                }
                return retVal;
            }
        }
        return null;
    }
    public String getSingleElementContent(Element sectionParagraph){
        boolean multiple = false;
        String retVal = null;
        if(sectionParagraph.elements("input")!=null && !sectionParagraph.elements("input").isEmpty()){
            List<Element> elements = sectionParagraph.elements("input");
            for (Element inputElement:elements) {
                String id = inputElement.attributeValue("id");
                String type = inputElement.attributeValue("type");
                inputElement.attributeValue("type");
                if (type.equals("text") || type.equals("date") || type.equals("time")) {
                    return inputElement.attributeValue("value");
                } else if (type.equals("number") ){
                    String units = inputElement.attributeValue("data-field-units");
                    return inputElement.attributeValue("value") + " " +units;
                } if (type.equals("checkbox")) {

                }
            }
        }else if(sectionParagraph.elements("textarea")!=null) {
            List<Element> textAreaElements = sectionParagraph.elements("textarea");
            for(Element textAreaElement: textAreaElements) {
                String id = textAreaElement.attributeValue("id");
                return textAreaElement.getText();
            }


        }else if(sectionParagraph.elements("select")!=null){
            List<Element> selectElements = sectionParagraph.elements("select");
            for(Element selectElement:selectElements) {
                String id = selectElement.attributeValue("id");
                multiple = selectElement.attribute("multiple") != null;
                retVal="";
                if(multiple)
                    retVal="multiple;;";
                for (Element optionElement : (List<Element>) selectElement.elements("options")) {
                    if(optionElement.attribute("selected")!=null){
                        retVal += optionElement.attributeValue("value");
                        if(!multiple)
                            return  retVal;
                        else
                            retVal += ";;";
                    }
                }
            }
            return retVal;
        }
        return null;
    }
    public String getCodeForMeaning(String meaning){
        Map<String, String> codedContent = getCodedContent(false);
        Map<String, String> radLexCodes = getRadLexCodes();
        for(Map.Entry<String,String> radLexCodeEntry : radLexCodes.entrySet()){
            if(radLexCodeEntry.getValue().toLowerCase().equals(meaning.toLowerCase())){
                return radLexCodeEntry.getKey();
            }
        }
        return null;
    }


}


