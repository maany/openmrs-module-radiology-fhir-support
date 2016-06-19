package org.openmrs.module.radiologyfhirsupport.api.util;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.RadiologyFHIRSupportActivator;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateServiceTest;
import org.openmrs.module.radiologyfhirsupport.api.MRRTToFHIRService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTToFHIRServiceTest;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import javax.sql.rowset.serial.SerialClob;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by devmaany on 14/6/16.
 */
public class RadLexUtilTest extends BaseModuleContextSensitiveTest{
    private static Logger logger = Logger.getLogger(MRRTToFHIRServiceTest.class.getName());
    protected static final String MRRT_INITIAL_DATA_XML = "MRRTTemplateDemoData.xml";
    protected static final String ENCOUNTER_INITIAL_DATA_XML= "org/openmrs/api/include/EncounterServiceTest-initialData.xml";
    private String chestXRayEncounterUUID;
    private String cardiacMRIEncounterUUID;
    @Before
    public void loadTestData(){

        try {
            executeDataSet(MRRT_INITIAL_DATA_XML);
            executeDataSet(ENCOUNTER_INITIAL_DATA_XML);
            loadInstallationEntries();
            loadMRRTTemplates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldSetupContext() {
        Assert.assertNotNull(Context.getService(MRRTToFHIRService.class));
    }

    /**
     * Test if the demo data is loaded correctly for the other tests to run correctly
     */
    @Test
    public void testDemoData(){
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        List<MRRTTemplate> templates = mrrtTemplateService.getAll();
        Assert.assertNotNull(templates);
        for(MRRTTemplate template: templates){
            logger.log(Level.INFO,"Template ID {0}", new Object[]{template.getId()});
            try {
                logger.log(Level.INFO,"Template XML : \n {0}", new Object[]{mrrtTemplateService.clobToString(template.getXml())});
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.log(Level.CONFIG,"Test Data Configured Properly");
    }
    @Test
    public void getRadLexCodes_shouldReturnRadLexCodes(){
        Document document = loadDOM();
        RadLexUtil radLexUtil = new RadLexUtil(document);
        assertNotNull(radLexUtil);
        Map<String, String> radLexCodes = radLexUtil.getRadLexCodes();
        assertNotEquals(radLexCodes.size(),0);
        Map<String,String> codedContent = radLexUtil.getCodedContent(true);
        String radLexCode = "RID10376";
        String content = radLexUtil.getBodyCodedContent(radLexCode);
        assertEquals("Normal.",content);

    }
    void loadMRRTTemplates() throws SQLException {
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        MRRTTemplate cardiacMRI = new MRRTTemplate();
        String cardiacMRIString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'/>\n" +
                "<meta content='Cardiac MRI: Adenosine Stress Protocol' name='dcterms.title'/>\n" +
                "<meta content='Cardiac MRI: Adenosine Stress Protocol template :: Authored by Kazerooni EA, et al.' name='dcterms.description'/>\n" +
                "<meta content='http://www.radreport.org/template/0000048' name='dcterms.identifier'/>\n" +
                "<meta content='en' name='dcterms.language'/>\n" +
                "<meta content='IMAGE_REPORT_TEMPLATE' name='dcterms.type'/>\n" +
                "<meta content='Radiological Society of North America (RSNA)' name='dcterms.publisher'/>\n" +
                "<meta content='May be used gratis, subject to license agreement' name='dcterms.rights'/>\n" +
                "<meta content='http://www.radreport.org/license.pdf' name='dcterms.license'/>\n" +
                "<meta content='2012-07-19' name='dcterms.date'/>\n" +
                "<meta content='Kazerooni EA, et al.' name='dcterms.creator'/>\n" +
                "<meta content='Kahn CE Jr [editor]' name='dcterms.contributor'/>\n" +
                "<link rel='stylesheet' type='text/css' href='IHE_Template_Style.css'/>\n" +
                "<!-- The absolute link to the CSS file is http://www.radreport.org/html/IHE_Template_Style.css -->\n" +
                "<script type='text/xml'>\n" +
                "<!--\n" +
                "<template_attributes>\n" +
                "<top-level-flag>true</top-level-flag>\n" +
                "<status>ACTIVE</status>\n" +
                "<coding_schemes>\n" +
                " <coding_scheme name='RadLex' designator='2.16.840.1.113883.6.256' />\n" +
                " <coding_scheme name='SNOMEDCT' designator='2.16.840.1.113883.6.96' />\n" +
                " <coding_scheme name='LOINC' designator='2.16.840.1.113883.6.1' />\n" +
                "</coding_schemes>\n" +
                "<term>\n" +
                " <code meaning='adenosine stress' value='RID10490' scheme='RadLex'/>\n" +
                "</term>\n" +
                "<term>\n" +
                " <code meaning='heart' value='RID1385' scheme='RadLex'/>\n" +
                "</term>\n" +
                "<term>\n" +
                " <code meaning='magnetic resonance imaging' value='RID10312' scheme='RadLex'/>\n" +
                "</term>\n" +
                "<coded_content>\n" +
                "<entry ORIGTXT='T48_5'>\n" +
                " <term>\n" +
                "  <code meaning='adenosine stress' value='RID10490' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_6'>\n" +
                " <term>\n" +
                "  <code meaning='clinical information' value='RID13166' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_7'>\n" +
                " <term>\n" +
                "  <code meaning='comparison' value='RID28483' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_10'>\n" +
                " <term>\n" +
                "  <code meaning='perfusion' value='RID10376' scheme='RadLex'/>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='imaging procedure' value='RID13060' scheme='RadLex'/>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='under stress' value='RID28658' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_12'>\n" +
                " <term>\n" +
                "  <code meaning='perfusion' value='RID10376' scheme='RadLex'/>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='at rest' value='RID12535' scheme='RadLex'/>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='imaging procedure' value='RID13060' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_14'>\n" +
                " <term>\n" +
                "  <code meaning='cine loop' value='RID10928' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_67'>\n" +
                " <term>\n" +
                "  <code meaning='volume' value='RID28668' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_68'>\n" +
                " <term>\n" +
                "  <code meaning='volume' value='RID28668' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_69'>\n" +
                " <term>\n" +
                "  <code meaning='stroke' value='RID5178' scheme='RadLex'/>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='volume' value='RID28668' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_70'>\n" +
                "  <term>\n" +
                "  <code meaning='Right ventricular Ejection fraction by MRI' value='8818-7' scheme='LOINC'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_71'>\n" +
                " <term>\n" +
                "  <code meaning='mass' value='RID3874' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_73'>\n" +
                " <term>\n" +
                "  <code meaning='cardiac chamber' value='RID1386' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_74'>\n" +
                " <term>\n" +
                "  <code meaning='wall of lateral ventricle' value='RID13822' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_75'>\n" +
                " <term>\n" +
                "  <code meaning='left ventricle' value='RID1392' scheme='RadLex'/>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='diameter' value='RID13432' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_78'>\n" +
                " <term>\n" +
                "  <code meaning='(unspecified)' value='RID38772' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_80'>\n" +
                " <term>\n" +
                "  <code meaning='impression section' value='RID13170' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "</coded_content>\n" +
                "</template_attributes>\n" +
                "-->\n" +
                "</script>\n" +
                "<title>Cardiac MRI: Adenosine Stress Protocol</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<section data-section-name='Procedure'>\n" +
                "<header class='level1'>Cardiac MRI: Adenosine Stress Protocol</header>\n" +
                "<p>\n" +
                "<label for='T48_3'>Field strength:</label>\n" +
                "<input type='number' id='T48_3' name='Field strength' data-field-units='T' min='' max='' value='1.5'/> <label for='T48_3'>T</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_5'>Adenosine stress protocol:</label>\n" +
                "<textarea id='T48_5' name='Adenosine stress protocol'>0.14 mg/kg/min adenosine infused over 4 minutes with stress perfusion imaging performed during the last minute of the adenosine infusion using an FGRE-ET sequence and a gadolinium dose of 0.1 mmol/kg. Rest perfusion imaging was performed was then performed after appropriate delay using additional gadolinium dose of 0.1 mmol/kg. Cine white blood imaging including short axis and two, three and four chamber long axis views was performed using a FIESTA sequence to evaluate cardiac morphology and function. Delayed enhanced imaging was performed using an Inversion recovery prepared gradient echo sequence. Images were postprocessed on a computer workstation to assess cardiac anatomy and ventricular function. </textarea>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section data-section-name='Clinical information'>\n" +
                "<header class='level1'>Clinical information</header>\n" +
                "<p>\n" +
                "<textarea id='T48_6' name='Clinical information'> </textarea>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section data-section-name='Comparison'>\n" +
                "<header class='level1'>Comparison</header>\n" +
                "<p>\n" +
                "<textarea id='T48_7' name='Comparison'>None. </textarea>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section data-section-name='Findings'>\n" +
                "<header class='level1'>Findings</header>\n" +
                "<p>\n" +
                "<label for='T48_10'>Stress perfusion imaging:</label>\n" +
                "<input type='text' id='T48_10' name='Stress perfusion imaging' value='Normal.'/>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_12'>Rest perfusion imaging:</label>\n" +
                "<input type='text' id='T48_12' name='Rest perfusion imaging' value='Normal.'/>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_14'>Cine images:</label>\n" +
                "<textarea id='T48_14' name='Cine images'>Normal cardiac anatomy and normal cardiac chamber size. Normal left ventricular wall motion and function. </textarea>\n" +
                "</p>\n" +
                "<section data-section-name='Left ventricle'>\n" +
                "<header class='level2'>Left ventricle</header>\n" +
                "<p>\n" +
                "<label for='T48_67'>End-diastole volume:</label>\n" +
                "<input type='number' id='T48_67' name='End-diastole volume' data-field-units='mL' min='0' max=''/> <label for='T48_67'>mL</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_68'>End-systolic volume:</label>\n" +
                "<input type='number' id='T48_68' name='End-systolic volume' data-field-units='mL' min='0' max=''/> <label for='T48_68'>mL</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_69'>Stroke volume:</label>\n" +
                "<input type='number' id='T48_69' name='Stroke volume' data-field-units='mL' min='0' max=''/> <label for='T48_69'>mL</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_70'>Ejection fraction:</label>\n" +
                "<input type='number' id='T48_70' name='Ejection fraction' value='' data-field-units='%' /> <label for='T48_70'>%</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_71'>Mass:</label>\n" +
                "<input type='number' id='T48_71' name='Mass' data-field-units='g' min='0' max=''/> <label for='T48_71'>g</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_73'>Antero-septal wall (end-diastole):</label>\n" +
                "<input type='number' id='T48_73' name='Antero-septal wall' data-field-units='cm' min='0' max=''/> <label for='T48_73'>cm</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_74'>Infero-lateral wall (end-diastole):</label>\n" +
                "<input type='number' id='T48_74' name='Infero-lateral wall' data-field-units='cm' min='0' max=''/> <label for='T48_74'>cm</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_75'>Left ventricular diameter measured at the base (end-diastole):</label>\n" +
                "<input type='number' id='T48_75' name='Left ventricular diameter end-diastole' value=''/> <label for='T48_75'>cm</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_999'>Left ventricular diameter measured at the base (end-systole):</label>\n" +
                "<input type='number' id='T48_999' name='Left ventricular diameter end-systole' value=''/> <label for='T48_999'>cm</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T48_78'>Delayed enhancement:</label>\n" +
                "<input type='text' id='T48_78' name='Delayed enhancement' value='There is no delayed enhancement of the myocardium or pericardium'/>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section data-section-name='Reference'>\n" +
                "<header class='level2'>Reference</header>\n" +
                "<p>\n" +
                "Reference values from: Maceira et al., Normalized left ventricular systolic and diastolic function by steady state free precession cardiovascular magnetic resonance. J Cardiovascular Magn Reson 2006; 8:417-426.\n" +
                "</p>\n" +
                "</section>\n" +
                "</section>\n" +
                "<section data-section-name='Impression'>\n" +
                "<header class='level1'>Impression</header>\n" +
                "<p>\n" +
                "<textarea id='T48_80' name='Impression'> </textarea>\n" +
                "</p>\n" +
                "</section>\n" +
                "</body>\n" +
                "</html>";
        Clob xml = new SerialClob(cardiacMRIString.toCharArray());
        cardiacMRI.setXml(xml);
        cardiacMRIEncounterUUID = mrrtTemplateService.create(cardiacMRI);

        MRRTTemplate chestXRay = new MRRTTemplate();
        String chestXRayString = " <!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'/>\n" +
                "<meta content='Chest Xray' name='dcterms.title'/>\n" +
                "<meta content='Chest Xray template :: Authored by Kahn CE Jr' name='dcterms.description'/>\n" +
                "<meta content='http://www.radreport.org/template/0000102' name='dcterms.identifier'/>\n" +
                "<meta content='en' name='dcterms.language'/>\n" +
                "<meta content='IMAGE_REPORT_TEMPLATE' name='dcterms.type'/>\n" +
                "<meta content='Radiological Society of North America (RSNA)' name='dcterms.publisher'/>\n" +
                "<meta content='May be used gratis, subject to license agreement' name='dcterms.rights'/>\n" +
                "<meta content='http://www.radreport.org/license.pdf' name='dcterms.license'/>\n" +
                "<meta content='2012-03-28' name='dcterms.date'/>\n" +
                "<meta content='Kahn CE Jr' name='dcterms.creator'/>\n" +
                "<meta content='Kahn CE Jr [editor]' name='dcterms.contributor'/>\n" +
                "<meta content='Medical College of Wisconsin (MCW)' name='dcterms.contributor'/>\n" +
                "<link rel='stylesheet' type='text/css' href='IHE_Template_Style.css'/>\n" +
                "<!-- The absolute link to the CSS file is http://www.radreport.org/html/IHE_Template_Style.css -->\n" +
                "<script type='text/xml'>\n" +
                "<!--\n" +
                "<template_attributes>\n" +
                "<top-level-flag>true</top-level-flag>\n" +
                "<status>ACTIVE</status>\n" +
                "<coding_schemes>\n" +
                " <coding_scheme name='RadLex' designator='2.16.840.1.113883.6.256' />\n" +
                " <coding_scheme name='SNOMEDCT' designator='2.16.840.1.113883.6.96' />\n" +
                " <coding_scheme name='LOINC' designator='2.16.840.1.113883.6.1' />\n" +
                "</coding_schemes>\n" +
                "<term>\n" +
                " <code meaning='projection radiography' value='RID10345' scheme='RadLex'/>\n" +
                "</term>\n" +
                "<term>\n" +
                " <code meaning='thorax' value='RID1243' scheme='RadLex'/>\n" +
                "</term>\n" +
                "<coded_content>\n" +
                "<entry ORIGTXT='T102_2'>\n" +
                " <term>\n" +
                "  <code meaning='procedure' value='RID1559' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_7'>\n" +
                " <term>\n" +
                "  <code meaning='clinical information' value='RID13166' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_12'>\n" +
                " <term>\n" +
                "  <code meaning='comparison' value='RID28483' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_15'>\n" +
                " <term>\n" +
                "  <code meaning='heart' value='RID1385' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_17'>\n" +
                " <term>\n" +
                "  <code meaning='lungs' value='RID13437' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_20'>\n" +
                " <term>\n" +
                "  <code meaning='set of bones' value='RID28569' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_23'>\n" +
                " <term>\n" +
                "  <code meaning='impression section' value='RID13170' scheme='RadLex'/>\n" +
                " </term>\n" +
                "</entry>\n" +
                "</coded_content>\n" +
                "</template_attributes>\n" +
                "-->\n" +
                "</script>\n" +
                "<title>Chest Xray</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<section data-section-name='Procedure'>\n" +
                "<header class='level1'>Chest Xray</header>\n" +
                "<p>\n" +
                "<textarea id='T102_2' name='Procedure'>PA and lateral views. </textarea>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section data-section-name='Clinical information'>\n" +
                "<header class='level1'>Clinical information</header>\n" +
                "<p>\n" +
                "<textarea id='T102_7' name='Clinical information'> </textarea>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section data-section-name='Comparison'>\n" +
                "<header class='level1'>Comparison</header>\n" +
                "<p>\n" +
                "<textarea id='T102_12' name='Comparison'>None. </textarea>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section data-section-name='Findings'>\n" +
                "<header class='level1'>Findings</header>\n" +
                "<p>\n" +
                "<label for='T102_15'>Heart:</label>\n" +
                "<input type='text' id='T102_15' name='Heart' value='Normal.'/>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T102_17'>Lungs:</label>\n" +
                "<input type='text' id='T102_17' name='Lungs' value='Normal.'/>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for='T102_20'>Bones:</label>\n" +
                "<input type='text' id='T102_20' name='Bones' value='Normal.'/>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section data-section-name='Impression'>\n" +
                "<header class='level1'>Impression</header>\n" +
                "<p>\n" +
                "<textarea id='T102_23' name='Impression'>No acute disease. </textarea>\n" +
                "</p>\n" +
                "</section>\n" +
                "</body>\n" +
                "</html>";
        xml = new SerialClob(chestXRayString.toCharArray());
        chestXRay.setXml(xml);
        chestXRayEncounterUUID = mrrtTemplateService.create(chestXRay);
    }
    private void loadInstallationEntries() {
        RadiologyFHIRSupportActivator radiologyFHIRSupportActivator = new RadiologyFHIRSupportActivator();
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        radiologyFHIRSupportActivator.registerFHIRDiagnosticReportHandler(messageSourceService);
        radiologyFHIRSupportActivator.registerEncounterType(messageSourceService);
        radiologyFHIRSupportActivator.addDefaultEncounterRole(messageSourceService);
        radiologyFHIRSupportActivator.addDefaultLocation(messageSourceService);
        radiologyFHIRSupportActivator.addDefaultProvider(messageSourceService);
        radiologyFHIRSupportActivator.addDemoPatient(messageSourceService);

    }
    private Document loadDOM(){
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        MRRTTemplate template = mrrtTemplateService.getAll().get(0);
        String xml = null;
        try {
            xml = mrrtTemplateService.clobToString(template.getXml());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MRRTToFHIRServiceTest mrrtToFHIRServiceTest = new MRRTToFHIRServiceTest();
        XPathMapper xPathMapper = new XPathMapper(template.getEncounterUuid(),xml,mrrtToFHIRServiceTest.getXPathMappings());
        return xPathMapper.loadDomFromXml(xml);
    }


}
