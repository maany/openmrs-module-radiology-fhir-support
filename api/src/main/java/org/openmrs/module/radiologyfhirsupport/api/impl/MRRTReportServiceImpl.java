package org.openmrs.module.radiologyfhirsupport.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTReportService;
import org.openmrs.module.radiologyfhirsupport.api.db.MRRTReportDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by devmaany on 27/10/16.
 */
public class MRRTReportServiceImpl extends BaseOpenmrsService implements MRRTReportService{

    protected final Log log = LogFactory.getLog(this.getClass());

    private MRRTReportDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(MRRTReportDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public MRRTReportDAO getDao() {
        return dao;
    }


    @Override
    @Transactional(readOnly = true)
    public MRRTReport getById(int id) {
        return dao.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MRRTReport> getAll() {
        return dao.getAll();
    }

    @Override
    public List<MRRTReport> getByTemplate(MRRTTemplate template) {
        return dao.getByTemplate(template);
    }

    @Override
    @Transactional(readOnly = true)
    public MRRTReport getByEncounterUUID(String encounterUUID) {
        return dao.getByEncounterUUID(encounterUUID);
    }

    @Override
    public MRRTReport getByEncounter(Encounter encounter) {
        return dao.getByEncounter(encounter);
    }

    @Override
    public List<MRRTReport> getByPatientId(Integer patientId) {
        return dao.getByPatientId(patientId);
    }

    @Override
    @Transactional
    public int saveOrUpdate(MRRTReport report) {
        return dao.saveOrUpdate(report);
    }

    @Override
    @Transactional
    public String create(MRRTReport report) {
        dao.saveOrUpdate(report);
        return report.getEncounter().getUuid();
    }

    @Override
    @Transactional
    public MRRTReport delete(int id) {
        return dao.deleteById(id);
    }

    @Override
    @Transactional
    public MRRTReport delete(MRRTReport report) {
        return dao.delete(report);
    }

    private Encounter createOpenMRSEncounter(MRRTTemplate mrrtTemplate, Patient patient, Location location, Date date ){
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        Encounter encounter = new Encounter();
        encounter.setPatient(patient);
        // EncounterType
        String encounterType = messageSourceService.getMessage("radiologyfhirsupport.encounterType");
        EncounterType radMRRTEncounterType = Context.getEncounterService().getEncounterType(encounterType);
        encounter.setEncounterType(radMRRTEncounterType);
        // Location
        encounter.setLocation(location);
        // Date Created
        encounter.setDateCreated(new Date());
        // DateTime
        encounter.setEncounterDatetime(date);
        // EncounterRole
        String useDefaultEncounterRole = messageSourceService.getMessage("radiologyfhirsupport.useDefaultEncounterRole");
        EncounterRole encounterRole = null;
        if(useDefaultEncounterRole.equals("true") || useDefaultEncounterRole.equals("radiologyfhirsupport.useDefaultEncounterRole")){
            String encounterRoleName = messageSourceService.getMessage("radiologyfhirsupport.encounterRoleName");
            encounterRole= Context.getEncounterService().getEncounterRoleByName(encounterRoleName);
        }else {
            //TODO obtain EncounterRole from XML here
        }
        // Provider
        Provider provider = null;
        String useDefaultProvider = Context.getMessageSourceService().getMessage("radiologyfhirsupport.useDefaultProvider");
        if(useDefaultProvider.equals("true") || useDefaultProvider.equals("radiologyfhirsupport.useDefaultProvider")) {
            String providerIdentifier = Context.getMessageSourceService().getMessage("radiologyfhirsupport.providerIdentifier");
            provider = Context.getProviderService().getProviderByIdentifier(providerIdentifier);
        }else {
            //TODO obtain Provider from XML here
        }
        encounter.setProvider(encounterRole,provider);
        // Form
        Form form = new Form();
        form.setName(mrrtTemplate.getName());
        form.setDescription("");
        //encounter.setForm(form);
        //  Creator
        encounter.setCreator(Context.getAuthenticatedUser());
        //  Patient
        encounter.setPatient(patient);
        return encounter;

    }
}
