package org.openmrs.module.radiologyfhirsupport.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.RadiologyFHIRSupportActivator;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import javax.sql.rowset.serial.SerialClob;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Created by devmaany on 27/10/16.
 */
public class MRRTReportServiceTest extends BaseModuleContextSensitiveTest {
    private static Logger logger = Logger.getLogger(MRRTTemplateServiceTest.class.getName());
    protected static final String MRRT_INITIAL_DATA_XML = "MRRTTemplateDemoData.xml";
    protected static final String ENCOUNTER_INITIAL_DATA_XML= "org/openmrs/api/include/EncounterServiceTest-initialData.xml";
    private String chestXRayEncounterUUID;
    private String cardiacMRIEncounterUUID;
    private Integer patientId;
    @Before
    public void loadTestData(){

        try {
            executeDataSet(ENCOUNTER_INITIAL_DATA_XML);
            executeDataSet(MRRT_INITIAL_DATA_XML);
            loadInstallationEntries();
            loadMRRTTemplates();
            loadMRRTReports();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Test if the demo data is loaded correctly for the other tests to run correctly.
     * By default, this means, {@link MRRTTemplateService#create(MRRTTemplate)}, {@link MRRTTemplateService#saveOrUpdate(MRRTTemplate)}, {@link MRRTTemplateService#getAll()} work fine and need not to tested separately
     */
    @Test
    public void testDemoData(){
        MRRTReportService mrrtReportService = getService();
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        List<MRRTReport> reports = mrrtReportService.getAll();
        assertNotNull(reports);
        for(MRRTReport report: reports){
            logger.log(Level.INFO,"Report ID {0}", new Object[]{report.getId()});
            try {
                logger.log(Level.INFO,"Report XML : \n {0}", new Object[]{mrrtTemplateService.clobToString(report.getXml())});
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals(2,reports.size());
        logger.log(Level.CONFIG,"Test Data Configured Properly");
    }
    @Test
    public void getById_shouldReturnMRRTReportById(){
        Assert.assertNotNull(getService().getById(getService().getAll().get(0).getId()));
    }
    @Test
    public void getByEncounterUuid_ShouldReturnMRRTReportByEncounterUuid(){
        MRRTReport report = getService().getByEncounterUUID(cardiacMRIEncounterUUID);
        Assert.assertNotNull(report);
    }
    @Test
    public void getByEncounter_ShouldReturnMRRTReportByEncounter(){
        Encounter encounter = Context.getService(EncounterService.class).getEncounterByUuid(cardiacMRIEncounterUUID);
        MRRTReport report = getService().getByEncounter(encounter);
        Assert.assertNotNull(report);
    }
    @Test
    public void getbyPatientId_ShouldReturnAllReportsForPatient(){
        List<MRRTReport> reports = getService().getByPatientId(patientId);
        Assert.assertNotNull(reports);
        Assert.assertNotEquals(reports.size(),0);
        Assert.assertEquals(reports.size(),2);
    }
    @Test
    public void getByTemplate_ShouldReturnAllReportsForAGivenTemplate(){
        List<MRRTReport> reports = getService().getAll();
        MRRTTemplate baseTemplate = reports.get(0).getMrrtTemplate();
        reports = getService().getByTemplate(baseTemplate);
        Assert.assertNotNull(reports);
        Assert.assertNotEquals(reports.size(),0);

    }
    @Test
    public void deleteById_ShouldDeleteMRRTReport(){
        List<MRRTReport> reports = getService().getAll();
        int sizeInitial= reports.size();
        MRRTReport report = reports.get(0);
        Encounter encounter = report.getEncounter();
        int encounterId = encounter.getEncounterId();
        getService().delete(reports.get(0));
        reports = getService().getAll();
        int sizeFinal = reports.size();
        Assert.assertEquals(sizeInitial,sizeFinal+1);
        Assert.assertNull("Encounter not deleted when deleting report. Wrong behaviour",Context.getEncounterService().getEncounter(encounterId));
    }
    /*
     * Utility Methods for this Test class
     */

    MRRTReportService getService(){
        return Context.getService(MRRTReportService.class);
    }
    void loadMRRTReports(){
        Patient patient = Context.getPatientService().getAllPatients().get(0);
        patientId = patient.getId();

        MRRTReport cardiacMRIReport = new MRRTReport();
        MRRTTemplate cardiacMRITemplate = Context.getService(MRRTTemplateService.class).getByEncounterUUID(cardiacMRIEncounterUUID);
        Encounter cardiacMRIEncounter = Context.getService(org.openmrs.api.EncounterService.class).getEncounterByUuid(cardiacMRIEncounterUUID);
        cardiacMRIEncounter.setPatient(patient);
        cardiacMRIReport.setMrrtTemplate(cardiacMRITemplate);
        cardiacMRIReport.setEncounter(cardiacMRIEncounter);
        cardiacMRIReport.setXml(cardiacMRITemplate.getXml());
        getService().create(cardiacMRIReport);

        MRRTReport chestXRayReport = new MRRTReport();
        MRRTTemplate chestXRayTemplate = Context.getService(MRRTTemplateService.class).getByEncounterUUID(chestXRayEncounterUUID);
        Encounter chestXRayEncounter = Context.getService(org.openmrs.api.EncounterService.class).getEncounterByUuid(chestXRayEncounterUUID);
        chestXRayEncounter.setPatient(patient);
        chestXRayReport.setMrrtTemplate(chestXRayTemplate);
        chestXRayReport.setEncounter(chestXRayEncounter);
        chestXRayReport.setXml(chestXRayTemplate.getXml());
        getService().saveOrUpdate(chestXRayReport);
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
                " <code meaning='adenosine stress' value='RID10490' scheme='RadLex'>\n" +
                "</term>\n" +
                "<term>\n" +
                " <code meaning='heart' value='RID1385' scheme='RadLex'>\n" +
                "</term>\n" +
                "<term>\n" +
                " <code meaning='magnetic resonance imaging' value='RID10312' scheme='RadLex'>\n" +
                "</term>\n" +
                "<coded_content>\n" +
                "<entry ORIGTXT='T48_5'>\n" +
                " <term>\n" +
                "  <code meaning='adenosine stress' value='RID10490' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_6'>\n" +
                " <term>\n" +
                "  <code meaning='clinical information' value='RID13166' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_7'>\n" +
                " <term>\n" +
                "  <code meaning='comparison' value='RID28483' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_10'>\n" +
                " <term>\n" +
                "  <code meaning='perfusion' value='RID10376' scheme='RadLex'>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='imaging procedure' value='RID13060' scheme='RadLex'>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='under stress' value='RID28658' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_12'>\n" +
                " <term>\n" +
                "  <code meaning='perfusion' value='RID10376' scheme='RadLex'>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='at rest' value='RID12535' scheme='RadLex'>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='imaging procedure' value='RID13060' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_14'>\n" +
                " <term>\n" +
                "  <code meaning='cine loop' value='RID10928' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_67'>\n" +
                " <term>\n" +
                "  <code meaning='volume' value='RID28668' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_68'>\n" +
                " <term>\n" +
                "  <code meaning='volume' value='RID28668' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_69'>\n" +
                " <term>\n" +
                "  <code meaning='stroke' value='RID5178' scheme='RadLex'>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='volume' value='RID28668' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_70'>\n" +
                "  <term>\n" +
                "  <code meaning='Right ventricular Ejection fraction by MRI' value='8818-7' scheme='LOINC'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_71'>\n" +
                " <term>\n" +
                "  <code meaning='mass' value='RID3874' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_73'>\n" +
                " <term>\n" +
                "  <code meaning='cardiac chamber' value='RID1386' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_74'>\n" +
                " <term>\n" +
                "  <code meaning='wall of lateral ventricle' value='RID13822' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_75'>\n" +
                " <term>\n" +
                "  <code meaning='left ventricle' value='RID1392' scheme='RadLex'>\n" +
                " </term>\n" +
                " <term>\n" +
                "  <code meaning='diameter' value='RID13432' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_78'>\n" +
                " <term>\n" +
                "  <code meaning='(unspecified)' value='RID38772' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T48_80'>\n" +
                " <term>\n" +
                "  <code meaning='impression section' value='RID13170' scheme='RadLex'>\n" +
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
                " <code meaning='projection radiography' value='RID10345' scheme='RadLex'>\n" +
                "</term>\n" +
                "<term>\n" +
                " <code meaning='thorax' value='RID1243' scheme='RadLex'>\n" +
                "</term>\n" +
                "<coded_content>\n" +
                "<entry ORIGTXT='T102_2'>\n" +
                " <term>\n" +
                "  <code meaning='procedure' value='RID1559' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_7'>\n" +
                " <term>\n" +
                "  <code meaning='clinical information' value='RID13166' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_12'>\n" +
                " <term>\n" +
                "  <code meaning='comparison' value='RID28483' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_15'>\n" +
                " <term>\n" +
                "  <code meaning='heart' value='RID1385' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_17'>\n" +
                " <term>\n" +
                "  <code meaning='lungs' value='RID13437' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_20'>\n" +
                " <term>\n" +
                "  <code meaning='set of bones' value='RID28569' scheme='RadLex'>\n" +
                " </term>\n" +
                "</entry>\n" +
                "<entry ORIGTXT='T102_23'>\n" +
                " <term>\n" +
                "  <code meaning='impression section' value='RID13170' scheme='RadLex'>\n" +
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
}
