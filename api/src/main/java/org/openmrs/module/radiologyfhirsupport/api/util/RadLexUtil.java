package org.openmrs.module.radiologyfhirsupport.api.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
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
     * @param xPath
     * @return Map<RADLEX_CODE, RADLEX_CODE_MEANING>
     */
    public Map<String,String> getRadLexCodes(String xPathCodeParent){
        Map<String,String> radLexCodeMeaning = new HashMap<String, String>();
//        String xPath = "//head/html/script/template_attributes/term/code";
        List<Node> parentNodes = document.selectNodes(xPathCodeParent);
        for(Node parentNode:parentNodes) {
            List<Node> nodes = (List<Node>) parentNode.selectNodes("//code");
            for (Node node : nodes) {
                String scheme = node.valueOf("@scheme");
                if (!scheme.toUpperCase().equals("RADLEX"))
                    continue;
                String codeValue = node.valueOf("@value");
                String codeMeaning = node.valueOf("@meaning");
                radLexCodeMeaning.put(codeValue, codeMeaning);
            }
        }
        return radLexCodeMeaning;
    }

    /**
     *
     * @return Map<RADLEX_CODE,ORIGTXT>
     */
    public Map<String,String> getCodedContent(){
        String xml = "//html/head/script/template_attributes/coded_content/entry";
        List list = document.selectNodes(xml);
    }
    public String getCodedContentBody(String radLexCode){
        Map<String, String> codedContent = getCodedContent();
        if(!codedContent.containsKey(radLexCode))
            return null;
        String origtxt = codedContent.get(radLexCode);
        String xPath = "//html/head/body/section";
        List<Node> sections = document.selectNodes(xPath);
        for(Node section:sections){

        }
    }

}
